package com.example.gardenerhelperapplication.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.gardenerhelperapplication.entities.PlantInGround;
import com.example.gardenerhelperapplication.entities.PlantInGroundInfo;

import java.time.LocalDate;
import java.util.List;

/**
 * Абстрактный класс, с абстрактными методами запросов к БД растений (работа с таблицами plants_in_ground_info и my_plants)
 */
@Dao
public abstract class PlantInGroundDao {
    //-------------------------------------------------------
    @Query("UPDATE my_plants SET is_plant_in_ground=:isPlantInGround WHERE id=:plantId")
    public abstract void updateMyPlantIsPlantInGround(int plantId, boolean isPlantInGround);

    @Insert
    public abstract void insertPlantInGroundInfo(PlantInGroundInfo plantInGroundInfo);

    @Transaction
    public void insertPlantInGround(PlantInGroundInfo plantInGroundInfo, int plantId) {
        insertPlantInGroundInfo(plantInGroundInfo);
        updateMyPlantIsPlantInGround(plantId, true);
    }

    //-------------------------------------------------------
    @Query("DELETE FROM plants_in_ground_info WHERE id=:id")
    public abstract void deletePlantInGroundInfo(int id);

    @Transaction
    public void deletePlantInGround(int id, int plantId) {
        deletePlantInGroundInfo(id);
        updateMyPlantIsPlantInGround(plantId, false);
    }

    //-------------------------------------------------------
    @Update
    public abstract void updatePlantInGroundInfo(PlantInGroundInfo plantInGroundInfo);

    @Query("UPDATE plants_in_ground_info SET current_water_date=:curWaterDate, next_water_date=:nextWaterDate WHERE id=:id")
    public abstract void updatePlantInGroundWaterDate(int id, LocalDate curWaterDate, LocalDate nextWaterDate);

    @Query("UPDATE plants_in_ground_info SET current_fertilize_date=:curFertilizeDate, next_fertilize_date=:nextFertilizeDate WHERE id=:id")
    public abstract void updatePlantInGroundFertilizeDate(int id, LocalDate curFertilizeDate, LocalDate nextFertilizeDate);
    //-------------------------------------------------------

    @Query("SELECT * FROM plants_in_ground_info WHERE id=:id")
    public abstract PlantInGroundInfo getPlantInGroundInfoById(int id);

    @Query("SELECT my_plants.plant_name as plantName, my_plants.plant_sort as plantSort, my_plants.plant_image as plantImage," +
            " plants_in_ground_info.id as id, plants_in_ground_info.date_planted_in_ground as datePlantedInGround, " +
            "plants_in_ground_info.watering_frequency as waterFreq, plants_in_ground_info.fertilization_frequency as fertilizeFreq, " +
            "plants_in_ground_info.next_water_date as nextWaterDate, plants_in_ground_info.current_water_date as curWaterDate, " +
            "plants_in_ground_info.current_fertilize_date as curFertilizeDate, plants_in_ground_info.next_fertilize_date as nextFertilizeDate, " +
            "plants_in_ground_info.plant_id as plantId FROM my_plants, plants_in_ground_info WHERE my_plants.id == plants_in_ground_info.plant_id")
    public abstract LiveData<List<PlantInGround>> getAllPlantsInGround();
}
