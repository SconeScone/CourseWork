package com.example.gardenerhelperapplication.presentation.plantlists.plantsingroundlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gardenerhelperapplication.data.MyPlantsDatabase;
import com.example.gardenerhelperapplication.data.PlantInGroundDao;
import com.example.gardenerhelperapplication.data.PlantsInGroundListManagerImpl;
import com.example.gardenerhelperapplication.entities.PlantInGround;
import com.example.gardenerhelperapplication.managers.PlantsInGroundListManager;

import java.time.LocalDate;
import java.util.List;

public class PlantsInGroundListViewModel extends AndroidViewModel {
    private PlantsInGroundListManager plantsInGroundListManager;
    private LiveData<List<PlantInGround>> plantsInGroundList;

    public PlantsInGroundListViewModel(@NonNull Application application) {
        super(application);

        PlantInGroundDao plantInGroundDao = MyPlantsDatabase.getMyPlantsDatabase(application).getPlantInGroundDao();
        plantsInGroundListManager = new PlantsInGroundListManagerImpl(plantInGroundDao);
        plantsInGroundList = plantInGroundDao.getAllPlantsInGround();
    }

    public LiveData<List<PlantInGround>> getAllPlantsInGround() {
        return plantsInGroundList;
    }

    public void deletePlantInGround(int id, int plantId) {
        plantsInGroundListManager.deletePlantInGroundById(id, plantId);
    }

    public void updatePlantInGroundWaterDate(int id, LocalDate curWaterDate, LocalDate nextWaterDate) {
        plantsInGroundListManager.updatePlantInGroundWaterDate(id, curWaterDate, nextWaterDate);
    }

    public void updatePlantInGroundFertilizeDate(int id, LocalDate curFertilizeDate, LocalDate nextFertilizeDate) {
        plantsInGroundListManager.updatePlantInGroundFertilizeDate(id, curFertilizeDate, nextFertilizeDate);
    }
}
