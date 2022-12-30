package com.example.patternpat.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.patternpat.model.DogBreed;
import com.example.patternpat.model.DogDao;
import com.example.patternpat.model.DogDatabase;
import com.example.patternpat.model.DogsApiService;
import com.example.patternpat.util.NotificationsHelper;
import com.example.patternpat.util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel {

    public MutableLiveData<List<DogBreed>> dogs = new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> dogLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();

    private DogsApiService dogsApiService = new DogsApiService();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private AsyncTask<List<DogBreed>, Void, List<DogBreed>> insertTask;
    private AsyncTask<Void, Void, List<DogBreed>> retrieveTask;
    private SharedPreferencesHelper preferencesHelper = SharedPreferencesHelper.getInstance(getApplication());
    private long refreshTime = 5 * 60 * 1000 * 1000 * 1000L;

    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh() {
        checkCacheDuration();

        long updateTime = preferencesHelper.getUpdateTime();
        long currentTime = System.nanoTime();

        if (updateTime != 0 && currentTime - updateTime < refreshTime) {
            fetchFromDatabase();
        } else {

            fetchFromRemote();
        }

    }

    public void refreshByPassCache() {
        fetchFromRemote();

    }

    private void checkCacheDuration(){
        String cachePreference = preferencesHelper.getCacheDuration();

        if(!cachePreference.equals("")){
            try {
                int prefInt = Integer.parseInt(cachePreference);
                refreshTime = prefInt * 1000 * 1000 * 1000L;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void fetchFromRemote() {
        loading.setValue(true);
        compositeDisposable.add(
                dogsApiService.getDogs()
                        //call the information on new background thread
                        .subscribeOn(Schedulers.newThread())
                        //observe the results on the main thread
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                            @Override
                            public void onSuccess(List<DogBreed> dogBreeds) {
                                insertTask = new InsertDogsTask();
                                insertTask.execute(dogBreeds);

                                Toast.makeText(getApplication(), "Dogs retrieved from endpoint", Toast.LENGTH_SHORT).show();
                                NotificationsHelper.getInstance(getApplication()).createNotification();
                            }

                            @Override
                            public void onError(Throwable e) {
                                dogLoadError.setValue(true);
                                loading.setValue(false);
                                e.printStackTrace();

                            }
                        })
        );
    }

    private void executeUsingExecutorService(List<DogBreed> dogBreeds) {


    }

    private void fetchFromDatabase() {
        loading.setValue(true);
        retrieveTask = new RetrieveDogTask();
        retrieveTask.execute();
    }

    private void dogsRetrieved(List<DogBreed> dogBreedList) {
        dogs.setValue(dogBreedList);
        dogLoadError.setValue(false);
        loading.setValue(false);
    }

    @Override
    protected void onCleared() {
        //Create disposables and clear to avoid memory leaks  VERY IMPORTANT
        super.onCleared();
        compositeDisposable.clear();
        if (insertTask != null) {
            insertTask.cancel(true);
            insertTask = null;
        }

        if (retrieveTask != null) {
            retrieveTask.cancel(true);
            retrieveTask = null;
        }
    }


    //todo AsyncTask is deprecated, replace with ExecutorService
    private class InsertDogsTask extends AsyncTask<List<DogBreed>, Void, List<DogBreed>> {
        @Override
        protected List<DogBreed> doInBackground(List<DogBreed>... lists) {
            List<DogBreed> list = lists[0];
            DogDao dao = DogDatabase.getInstance(getApplication()).dogDao();
            dao.deleteAllDogs();

            ArrayList<DogBreed> newList = new ArrayList<>(list);
            List<Long> result = dao.insertAll(newList.toArray(new DogBreed[0]));

            int i = 0;
            while (i < list.size()) {
                list.get(i).uuid = result.get(i).intValue();
                ++i;
            }

            return list;

        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreedList) {
            dogsRetrieved(dogBreedList);
            preferencesHelper.saveUpdateTime(System.nanoTime());
        }
    }

    private class RetrieveDogTask extends AsyncTask<Void, Void, List<DogBreed>> {

        @Override
        protected List<DogBreed> doInBackground(Void... voids) {
            return DogDatabase.getInstance(getApplication()).dogDao().getAllDogs();
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreedList) {
            dogsRetrieved(dogBreedList);
            Toast.makeText(getApplication(), "Dogs retrieved from database", Toast.LENGTH_SHORT).show();

        }
    }
}
