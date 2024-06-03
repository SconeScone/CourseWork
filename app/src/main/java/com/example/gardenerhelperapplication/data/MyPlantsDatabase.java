package com.example.gardenerhelperapplication.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.gardenerhelperapplication.entities.MyPlant;
import com.example.gardenerhelperapplication.entities.PlantInGroundInfo;
import com.example.gardenerhelperapplication.entities.PlantOnSeedlingsInfo;

/**
 * БД растений
 */
@Database(entities = {MyPlant.class, PlantOnSeedlingsInfo.class, PlantInGroundInfo.class}, version = 10, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class MyPlantsDatabase extends RoomDatabase {
    public abstract MyPlantDao getMyPlantDao();
    public abstract PlantOnSeedlingsDao getPlantOnSeedlingsDao();
    public abstract PlantInGroundDao getPlantInGroundDao();
    private static volatile MyPlantsDatabase instance;

    public static MyPlantsDatabase getMyPlantsDatabase(Context context) {
        if (instance == null) {
            synchronized (MyPlantsDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), MyPlantsDatabase.class, "my_plants_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
