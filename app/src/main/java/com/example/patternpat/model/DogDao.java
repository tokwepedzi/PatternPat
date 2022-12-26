package com.example.patternpat.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DogDao {
    //This defines all the methods that we need in order to access the database

    //Insert
    @Insert
    List<Long> insertAll(DogBreed... dogBreeds);

    //query to get all objects from the database
    @Query("SELECT * FROM dogbreed")
    List<DogBreed> getAllDogs();

    //query to select a dog with a specified uid
    @Query("SELECT * FROM dogbreed WHERE uuid = :dogId")
    DogBreed getDog(int dogId);

    //query to delete all dogs from the database
    @Query("DELETE FROM dogbreed")
    void deleteAllDogs();
}
