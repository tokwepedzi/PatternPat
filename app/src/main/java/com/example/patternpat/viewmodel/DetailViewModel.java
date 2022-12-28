package com.example.patternpat.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.patternpat.model.DogBreed;
import com.example.patternpat.model.DogDatabase;

public class DetailViewModel extends AndroidViewModel {

    public MutableLiveData<DogBreed> dogBreedMutableLiveData = new MutableLiveData<DogBreed>();

    private RetrieveDogTask retrieveDogTask;

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetch(int uuid) {

        //Fetch the selected dog from database in asyncTask
retrieveDogTask = new RetrieveDogTask();
retrieveDogTask.execute(uuid);

    }

    private class RetrieveDogTask extends AsyncTask<Integer,Void,DogBreed>{

        @Override
        protected DogBreed doInBackground(Integer... integers) {
            int uuid = integers[0];
            return DogDatabase.getInstance(getApplication()).dogDao().getDog(uuid);
        }

        @Override
        protected void onPostExecute(DogBreed dogBreed) {
            dogBreedMutableLiveData.setValue(dogBreed);
        }
    }

    @Override
    protected void onCleared() {
        if(retrieveDogTask != null){
            retrieveDogTask.cancel(true);
            retrieveDogTask = null;
        }
    }
}
