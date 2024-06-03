package com.example.gardenerhelperapplication.presentation.myplantscatalog;

import com.example.gardenerhelperapplication.entities.MyPlant;

public interface GetMyPlantCallback {
    public abstract void onComplete(MyPlant result);
}
