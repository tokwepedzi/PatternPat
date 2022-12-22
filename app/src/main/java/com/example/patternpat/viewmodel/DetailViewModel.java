package com.example.patternpat.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.patternpat.model.DogBreed;

public class DetailViewModel extends ViewModel {

    public MutableLiveData<DogBreed> dogBreedMutableLiveData = new MutableLiveData<DogBreed>();

    public void fetch() {
        DogBreed dogBreed = new DogBreed("1", "corgi", "15 years", "wasu", "hunting", "middle", "");


        //Set LiveData
        dogBreedMutableLiveData.setValue(dogBreed);
    }
}
