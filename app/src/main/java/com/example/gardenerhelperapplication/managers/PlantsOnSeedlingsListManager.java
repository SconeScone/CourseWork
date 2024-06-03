package com.example.gardenerhelperapplication.managers;

import com.example.gardenerhelperapplication.entities.PlantOnSeedlingsInfo;
import com.example.gardenerhelperapplication.presentation.plantlists.plantsonseedlingslist.GetPlantOnSeedlingsInfoCallback;

import java.time.LocalDate;

public interface PlantsOnSeedlingsListManager {
    public abstract void getPlantOnSeedlingsInfoById(int id, GetPlantOnSeedlingsInfoCallback callback);
    public abstract void insertPlantOnSeedlings(PlantOnSeedlingsInfo plantOnSeedlingsInfo);
    public abstract void deletePlantOnSeedlingsById(int id, int plantId);
    public abstract void updatePlantOnSeedlingsInfo(PlantOnSeedlingsInfo plantOnSeedlingsInfo);
    public abstract void updatePlantOnSeedlingsWaterDate(int id, LocalDate curWaterDate, LocalDate nextWaterDate);
    public abstract void updatePlantOnSeedlingsFertilizeDate(int id, LocalDate curFertilizeDate, LocalDate nextFertilizeDate);
}
