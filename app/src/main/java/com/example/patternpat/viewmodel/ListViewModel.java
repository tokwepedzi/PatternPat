package com.example.patternpat.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.patternpat.model.DogBreed;
import com.example.patternpat.model.DogsApiService;

import java.util.ArrayList;
import java.util.List;

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

    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh() {
        //Mock data
       /* DogBreed dogBreed = new DogBreed("1", "corgi", "15 years", "", "", "", "");
        DogBreed dogBreed1 = new DogBreed("2", "horgi", "10 years", "", "", "", "");
        DogBreed dogBreed2 = new DogBreed("3", "morgi", "11 years", "", "", "", "");
        DogBreed dogBreed3 = new DogBreed("4", "porgi", "8 years", "", "", "", "");
        DogBreed dogBreed4 = new DogBreed("5", "torgi", "4 years", "", "", "", "");
        ArrayList<DogBreed> dogBreeds = new ArrayList<>();
        dogBreeds.add(dogBreed);
        dogBreeds.add(dogBreed1);
        dogBreeds.add(dogBreed2);
        dogBreeds.add(dogBreed3);
        dogBreeds.add(dogBreed4);

        dogs.setValue(dogBreeds);
        dogLoadError.setValue(false);
        loading.setValue(false);*/

        fetchFromRemote();

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
                                dogs.setValue(dogBreeds);
                                dogLoadError.setValue(false);
                                loading.setValue(false);
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

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
