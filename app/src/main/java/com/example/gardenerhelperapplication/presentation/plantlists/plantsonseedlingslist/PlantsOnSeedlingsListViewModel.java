package com.example.gardenerhelperapplication.presentation.plantlists.plantsonseedlingslist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.gardenerhelperapplication.data.MyPlantsDatabase;
import com.example.gardenerhelperapplication.data.PlantOnSeedlingsDao;
import com.example.gardenerhelperapplication.data.PlantsOnSeedlingsListManagerImpl;
import com.example.gardenerhelperapplication.entities.PlantOnSeedlings;
import com.example.gardenerhelperapplication.managers.PlantsOnSeedlingsListManager;

import java.time.LocalDate;
import java.util.List;

public class PlantsOnSeedlingsListViewModel extends AndroidViewModel {
    private PlantsOnSeedlingsListManager plantsOnSeedlingsListManager;
    private LiveData<List<PlantOnSeedlings>> plantsOnSeedlingsList;

    public PlantsOnSeedlingsListViewModel(@NonNull Application application) {
        super(application);

        PlantOnSeedlingsDao plantOnSeedlingsDao = MyPlantsDatabase.getMyPlantsDatabase(application).getPlantOnSeedlingsDao();
        plantsOnSeedlingsListManager = new PlantsOnSeedlingsListManagerImpl(plantOnSeedlingsDao);
        plantsOnSeedlingsList = plantOnSeedlingsDao.getAllPlantsOnSeedlings();
    }

    public LiveData<List<PlantOnSeedlings>> getAllPlantsOnSeedlings() {
        return plantsOnSeedlingsList;
    }
 
    public void deletePlantOnSeedlings(int id, int plantId) {
        plantsOnSeedlingsListManager.deletePlantOnSeedlingsById(id, plantId);
    }

    public void updatePlantOnSeedlingsWaterDate(int id, LocalDate curWaterDate, LocalDate nextWaterDate) {
        plantsOnSeedlingsListManager.updatePlantOnSeedlingsWaterDate(id, curWaterDate, nextWaterDate);
    }

    public void updatePlantOnSeedlingsFertilizeDate(int id, LocalDate curFertilizeDate, LocalDate nextFertilizeDate) {
        plantsOnSeedlingsListManager.updatePlantOnSeedlingsFertilizeDate(id, curFertilizeDate, nextFertilizeDate);
    }
}
