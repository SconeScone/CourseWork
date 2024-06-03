package com.example.gardenerhelperapplication.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gardenerhelperapplication.entities.MyPlant;
import com.example.gardenerhelperapplication.entities.Plant;

import java.util.List;

/**
 * Абстрактный класс, с абстрактными методами запросов к БД растений (работа с таблицей my_plants)
 */
@Dao
public abstract class MyPlantDao {
    @Query("SELECT my_plants.id as id, my_plants.plant_name as plantName, my_plants.plant_sort as plantSort," +
            " my_plants.plant_image as plantImage, my_plants.is_on_seedlings as isOnSeedlings," +
            "my_plants.is_plant_in_ground as isPlantInGround FROM my_plants")
    public abstract LiveData<List<Plant>> getAllMyPlants();

    @Query("SELECT * FROM my_plants WHERE id=:id")
    public abstract MyPlant getMyPlantById(int id);

    @Query("DELETE FROM my_plants WHERE id=:id")
    public abstract void deleteMyPlantById(int id);

    @Insert
    public abstract void insertMyPlant(MyPlant myPlant);

    @Update
    public abstract void updateMyPlant(MyPlant myPlant);
}
