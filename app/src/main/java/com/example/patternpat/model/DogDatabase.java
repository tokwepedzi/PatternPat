package com.example.patternpat.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DogBreed.class}, version = 1)
public abstract class DogDatabase extends RoomDatabase {

    private static DogDatabase dogDatabaseInstance;

    public static DogDatabase getInstance(Context context){
        if(dogDatabaseInstance == null){
            dogDatabaseInstance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    DogDatabase.class,
                    "dogdatabase")
                    .build();
        }
        return dogDatabaseInstance;
    }

    public abstract DogDao dogDao();
}
