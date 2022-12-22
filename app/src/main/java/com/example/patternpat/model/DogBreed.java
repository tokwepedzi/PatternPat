package com.example.patternpat.model;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

public class DogBreed {
    @ColumnInfo(name = "bree_id")
    @SerializedName("id")
    public String breedId;

    @ColumnInfo(name = "dog_name")
    @SerializedName("name")
    public String dogBreed;

    @ColumnInfo(name = "life_span")
    @SerializedName("life_span")
    public String lifeSpan;


    //Gson and Room annotations
    @ColumnInfo(name = "breed_group")
    @SerializedName("breed_group")
    public String breedGroup;

    @ColumnInfo(name = "bred_for")
    @SerializedName("bred_for")
    public String bredFor;

    public String temperament;

    @ColumnInfo(name = "dog_image_url")
    @SerializedName("url")
    public String imageUrl;

    @PrimaryKey(autoGenerate = true)
    public int uuid;

    public DogBreed() {
    }

    public DogBreed(String breedId, String dogBreed, String lifeSpan, String breedGroup,
                    String bredFor, String temperament, String imageUrl) {
        this.breedId = breedId;
        this.dogBreed = dogBreed;
        this.lifeSpan = lifeSpan;
        this.breedGroup = breedGroup;
        this.bredFor = bredFor;
        this.temperament = temperament;
        this.imageUrl = imageUrl;
    }
}
