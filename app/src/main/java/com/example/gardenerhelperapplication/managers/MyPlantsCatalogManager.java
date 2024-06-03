package com.example.gardenerhelperapplication.managers;

import com.example.gardenerhelperapplication.entities.MyPlant;
import com.example.gardenerhelperapplication.presentation.myplantscatalog.GetMyPlantCallback;

public interface MyPlantsCatalogManager {
    public abstract void getMyPlantById(int id, GetMyPlantCallback callback);
    public abstract void insertMyPlant(MyPlant myPlant);
    public abstract void updateMyPlant(MyPlant myPlant);
    public abstract void deleteMyPlantById(int id);
}
