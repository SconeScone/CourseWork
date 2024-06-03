package com.example.gardenerhelperapplication.presentation.myplantscatalog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gardenerhelperapplication.data.MyPlantDao;
import com.example.gardenerhelperapplication.data.MyPlantsDatabase;
import com.example.gardenerhelperapplication.entities.Plant;

import java.util.List;

public class MyPlantsCatalogViewModel extends AndroidViewModel {
    private final LiveData<List<Plant>> myPlantsCatalog;

    public MyPlantsCatalogViewModel(@NonNull Application application) {
        super(application);

        MyPlantDao myPlantDao = MyPlantsDatabase.getMyPlantsDatabase(application).getMyPlantDao();
        myPlantsCatalog = myPlantDao.getAllMyPlants();
    }

    public LiveData<List<Plant>> getAllMyPlants() {
        return myPlantsCatalog;
    }
}