package com.example.patternpat.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.patternpat.model.DogBreed;

import java.util.ArrayList;
import java.util.List;

public class ListViewModel extends AndroidViewModel {

    public MutableLiveData<List<DogBreed>> dogs = new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> dogLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();
    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(){
        //Mock data
        DogBreed dogBreed = new DogBreed("1","corgi","15 years","","","","",1);
        DogBreed dogBreed1 = new DogBreed("2","horgi","10 years","","","","",1);
        DogBreed dogBreed2 = new DogBreed("3","morgi","11 years","","","","",1);
        DogBreed dogBreed3 = new DogBreed("4","porgi","8 years","","","","",1);
        DogBreed dogBreed4 = new DogBreed("5","torgi","4 years","","","","",1);
        ArrayList<DogBreed> dogBreeds = new ArrayList<>();
        dogBreeds.add(dogBreed);
        dogBreeds.add(dogBreed1);
        dogBreeds.add(dogBreed2);
        dogBreeds.add(dogBreed3);
        dogBreeds.add(dogBreed4);

        dogs.setValue(dogBreeds);
        dogLoadError.setValue(false);
        loading.setValue(false);

    }
}
