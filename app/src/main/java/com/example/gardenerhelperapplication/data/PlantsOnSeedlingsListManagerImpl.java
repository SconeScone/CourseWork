package com.example.gardenerhelperapplication.data;

import com.example.gardenerhelperapplication.GHApp;
import com.example.gardenerhelperapplication.entities.PlantOnSeedlingsInfo;
import com.example.gardenerhelperapplication.managers.PlantsOnSeedlingsListManager;
import com.example.gardenerhelperapplication.presentation.plantlists.plantsonseedlingslist.GetPlantOnSeedlingsInfoCallback;

import java.time.LocalDate;

/**
 * Класс с методами, которые выполняют задачи в отдельном потоке (вызов методов класса PlantOnSeedlingsDao)
 */
public class PlantsOnSeedlingsListManagerImpl implements PlantsOnSeedlingsListManager {
    private final PlantOnSeedlingsDao plantOnSeedlingsDao;
    public PlantsOnSeedlingsListManagerImpl(PlantOnSeedlingsDao plantOnSeedlingsDao) {
        this.plantOnSeedlingsDao = plantOnSeedlingsDao;
    }
    @Override
    public void getPlantOnSeedlingsInfoById(int id, GetPlantOnSeedlingsInfoCallback callback) {
        GHApp.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    PlantOnSeedlingsInfo result = plantOnSeedlingsDao.getPlantOnSeedlingsInfoById(id);
                    callback.onComplete(result);
                }
                catch (Exception e) {
                    callback.onComplete(null);
                }
            }
        });
    }

    @Override
    public void insertPlantOnSeedlings(PlantOnSeedlingsInfo plantOnSeedlingsInfo) {
        GHApp.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                plantOnSeedlingsDao.insertPlantOnSeedlings(plantOnSeedlingsInfo, plantOnSeedlingsInfo.getPlantId());
            }
        });
    }

    @Override
    public void deletePlantOnSeedlingsById(int id, int plantId) {
        GHApp.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                plantOnSeedlingsDao.deletePlantOnSeedlings(id, plantId);
            }
        });
    }

    @Override
    public void updatePlantOnSeedlingsInfo(PlantOnSeedlingsInfo plantOnSeedlingsInfo) {
        GHApp.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                plantOnSeedlingsDao.updatePlantOnSeedlingsInfo(plantOnSeedlingsInfo);
            }
        });
    }

    @Override
    public void updatePlantOnSeedlingsWaterDate(int id, LocalDate curWaterDate, LocalDate nextWaterDate) {
        GHApp.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                plantOnSeedlingsDao.updatePlantOnSeedlingsWaterDate(id, curWaterDate, nextWaterDate);
            }
        });
    }

    @Override
    public void updatePlantOnSeedlingsFertilizeDate(int id, LocalDate curFertilizeDate, LocalDate nextFertilizeDate) {
        GHApp.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                plantOnSeedlingsDao.updatePlantOnSeedlingsFertilizeDate(id, curFertilizeDate, nextFertilizeDate);
            }
        });
    }
}
