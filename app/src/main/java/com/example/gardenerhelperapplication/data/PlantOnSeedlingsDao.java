package com.example.gardenerhelperapplication.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.gardenerhelperapplication.entities.PlantOnSeedlings;
import com.example.gardenerhelperapplication.entities.PlantOnSeedlingsInfo;

import java.time.LocalDate;
import java.util.List;

/**
 * Абстрактный класс, с абстрактными методами запросов к БД растений (работа с таблицами plants_on_seedlings_info и my_plants)
 */
@Dao
public abstract class PlantOnSeedlingsDao {
    //-------------------------------------------------
    @Query("UPDATE my_plants SET is_on_seedlings=:isOnSeedlings WHERE id=:plantId")
    public abstract void updateMyPlantIsOnSeedlings(int plantId, boolean isOnSeedlings);

    @Insert
    public abstract void insertPlantOnSeedlingsInfo(PlantOnSeedlingsInfo plantOnSeedlingsInfo);

    @Transaction
    public void insertPlantOnSeedlings(PlantOnSeedlingsInfo plantOnSeedlingsInfo, int plantId) {
        insertPlantOnSeedlingsInfo(plantOnSeedlingsInfo);
        updateMyPlantIsOnSeedlings(plantId, true);
    }

    //-------------------------------------------------
    @Query("DELETE FROM plants_on_seedlings_info WHERE id=:id")
    public abstract void deletePlantOnSeedlingsInfo(int id);

    @Transaction
    public void deletePlantOnSeedlings(int id, int plantId) {
        deletePlantOnSeedlingsInfo(id);
        updateMyPlantIsOnSeedlings(plantId, false);
    }

    //-------------------------------------------------
    @Update
    public abstract void updatePlantOnSeedlingsInfo(PlantOnSeedlingsInfo plantOnSeedlingsInfo);

    @Query("UPDATE plants_on_seedlings_info SET current_water_date=:curWaterDate, next_water_date=:nextWaterDate WHERE id=:id")
    public abstract void updatePlantOnSeedlingsWaterDate(int id, LocalDate curWaterDate, LocalDate nextWaterDate);

    @Query("UPDATE plants_on_seedlings_info SET current_fertilize_date=:curFertilizeDate, next_fertilize_date=:nextFertilizeDate WHERE id=:id")
    public abstract void updatePlantOnSeedlingsFertilizeDate(int id, LocalDate curFertilizeDate, LocalDate nextFertilizeDate);
    //-------------------------------------------------

    @Query("SELECT * FROM plants_on_seedlings_info WHERE id=:id")
    public abstract PlantOnSeedlingsInfo getPlantOnSeedlingsInfoById(int id);

    @Query("SELECT my_plants.plant_name as plantName, my_plants.plant_sort as plantSort, my_plants.plant_image as plantImage," +
            " plants_on_seedlings_info.id as id, plants_on_seedlings_info.date_on_seedlings as dateOnSeedlings, " +
            "plants_on_seedlings_info.watering_frequency as waterFreq, plants_on_seedlings_info.fertilization_frequency as fertilizeFreq, " +
            "plants_on_seedlings_info.next_water_date as nextWaterDate, plants_on_seedlings_info.current_water_date as curWaterDate, " +
            "plants_on_seedlings_info.current_fertilize_date as curFertilizeDate, plants_on_seedlings_info.next_fertilize_date as nextFertilizeDate, " +
            "plants_on_seedlings_info.plant_id as plantId FROM my_plants, plants_on_seedlings_info WHERE my_plants.id == plants_on_seedlings_info.plant_id")
    public abstract LiveData<List<PlantOnSeedlings>> getAllPlantsOnSeedlings();
}
