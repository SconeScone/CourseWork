package com.example.gardenerhelperapplication.data;

import com.example.gardenerhelperapplication.GHApp;
import com.example.gardenerhelperapplication.entities.MyPlant;
import com.example.gardenerhelperapplication.managers.MyPlantsCatalogManager;
import com.example.gardenerhelperapplication.presentation.myplantscatalog.GetMyPlantCallback;

/**
 * Класс с методами, которые выполняют задачи в отдельном потоке (вызов методов класса MyPlantDao)
 */
public class MyPlantsCatalogManagerImpl implements MyPlantsCatalogManager {
    private final MyPlantDao myPlantDao;
    public MyPlantsCatalogManagerImpl(MyPlantDao myPlantDao) {
        this.myPlantDao = myPlantDao;
    }
    @Override
    public void getMyPlantById(int id, GetMyPlantCallback callback) {
        GHApp.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    MyPlant result = myPlantDao.getMyPlantById(id);
                    callback.onComplete(result);
                }
                catch (Exception e) {
                    callback.onComplete(null);
                }
            }
        });
    }

    @Override
    public void insertMyPlant(MyPlant myPlant) {
        GHApp.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                myPlantDao.insertMyPlant(myPlant);
            }
        });
    }

    @Override
    public void updateMyPlant(MyPlant myPlant) {
        GHApp.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                myPlantDao.updateMyPlant(myPlant);
            }
        });
    }

    @Override
    public void deleteMyPlantById(int id) {
        GHApp.getQueryExecutor().execute(new Runnable() {
            @Override
            public void run() {
                myPlantDao.deleteMyPlantById(id);
            }
        });
    }
}
