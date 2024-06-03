package com.example.gardenerhelperapplication.data;

import com.example.gardenerhelperapplication.GHApp;
import com.example.gardenerhelperapplication.entities.PlantInGroundInfo;
import com.example.gardenerhelperapplication.managers.PlantsInGroundListManager;
import com.example.gardenerhelperapplication.presentation.plantlists.plantsingroundlist.GetPlantInGroundInfoCallback;

import java.time.LocalDate;

/**
 * Класс с методами, которые выполняют задачи в отдельном потоке (вызов методов класса PlantInGroundDao)
 */
public class PlantsInGroundListManagerImpl implements PlantsInGroundListManager {
    private final PlantInGroundDao plantInGroundDao;

    public PlantsInGroundListManagerImpl(PlantInGroundDao plantInGroundDao) {
        this.plantInGroundDao = plantInGroundDao;
    }

    @Override
    public void getPlantInGroundInfoById(int id, GetPlantInGroundInfoCallback callback) {
        GHApp.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    PlantInGroundInfo result = plantInGroundDao.getPlantInGroundInfoById(id);
                    callback.onComplete(result);
                } catch (Exception e) {
                    callback.onComplete(null);
                }
            }
        });
    }

    @Override
    public void insertPlantInGround(PlantInGroundInfo plantInGroundInfo) {
        GHApp.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                plantInGroundDao.insertPlantInGround(plantInGroundInfo, plantInGroundInfo.getPlantId());
            }
        });
    }

    @Override
    public void deletePlantInGroundById(int id, int plantId) {
        GHApp.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                plantInGroundDao.deletePlantInGround(id, plantId);
            }
        });
    }

    @Override
    public void updatePlantInGroundInfo(PlantInGroundInfo plantInGroundInfo) {
        GHApp.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                plantInGroundDao.updatePlantInGroundInfo(plantInGroundInfo);
            }
        });
    }

    @Override
    public void updatePlantInGroundWaterDate(int id, LocalDate curWaterDate, LocalDate nextWaterDate) {
        GHApp.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                plantInGroundDao.updatePlantInGroundWaterDate(id, curWaterDate, nextWaterDate);
            }
        });
    }

    @Override
    public void updatePlantInGroundFertilizeDate(int id, LocalDate curFertilizeDate, LocalDate nextFertilizeDate) {
        GHApp.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                plantInGroundDao.updatePlantInGroundFertilizeDate(id, curFertilizeDate, nextFertilizeDate);
            }
        });
    }
}
