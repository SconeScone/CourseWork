package com.example.gardenerhelperapplication.managers;

import com.example.gardenerhelperapplication.entities.PlantInGroundInfo;
import com.example.gardenerhelperapplication.presentation.plantlists.plantsingroundlist.GetPlantInGroundInfoCallback;

import java.time.LocalDate;

public interface PlantsInGroundListManager {
    public abstract void getPlantInGroundInfoById(int id, GetPlantInGroundInfoCallback callback);
    public abstract void insertPlantInGround(PlantInGroundInfo plantInGroundInfo);
    public abstract void deletePlantInGroundById(int id, int plantId);
    public abstract void updatePlantInGroundInfo(PlantInGroundInfo plantInGroundInfo);
    public abstract void updatePlantInGroundWaterDate(int id, LocalDate curWaterDate, LocalDate nextWaterDate);
    public abstract void updatePlantInGroundFertilizeDate(int id, LocalDate curFertilizeDate, LocalDate nextFertilizeDate);
}
