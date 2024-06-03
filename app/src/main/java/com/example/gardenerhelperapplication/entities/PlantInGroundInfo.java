package com.example.gardenerhelperapplication.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "plants_in_ground_info",
        foreignKeys = {@ForeignKey(entity = MyPlant.class, parentColumns = "id", childColumns = "plant_id", onDelete = ForeignKey.CASCADE)})
public class PlantInGroundInfo {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "date_planted_in_ground")
    private String datePlantedInGround;
    @ColumnInfo(name = "watering_frequency")
    private int waterFreq;
    @ColumnInfo(name = "fertilization_frequency")
    private int fertilizeFreq;
    @ColumnInfo(name = "current_water_date")
    private LocalDate curWaterDate;
    @ColumnInfo(name = "next_water_date")
    private LocalDate nextWaterDate;
    @ColumnInfo(name = "current_fertilize_date")
    private LocalDate curFertilizeDate;
    @ColumnInfo(name = "next_fertilize_date")
    private LocalDate nextFertilizeDate;
    @ColumnInfo(name = "plant_id", index = true)
    private int plantId;

    public PlantInGroundInfo() {}

    @Ignore
    public PlantInGroundInfo(String datePlantedInGround, int waterFreq, int fertilizeFreq, LocalDate curWaterDate, LocalDate nextWaterDate, LocalDate curFertilizeDate, LocalDate nextFertilizeDate, int plantId) {
        this.datePlantedInGround = datePlantedInGround;
        this.waterFreq = waterFreq;
        this.fertilizeFreq = fertilizeFreq;
        this.curWaterDate = curWaterDate;
        this.nextWaterDate = nextWaterDate;
        this.curFertilizeDate = curFertilizeDate;
        this.nextFertilizeDate = nextFertilizeDate;
        this.plantId = plantId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatePlantedInGround() {
        return datePlantedInGround;
    }

    public void setDatePlantedInGround(String datePlantedInGround) {
        this.datePlantedInGround = datePlantedInGround;
    }

    public int getWaterFreq() {
        return waterFreq;
    }

    public void setWaterFreq(int waterFreq) {
        this.waterFreq = waterFreq;
    }

    public int getFertilizeFreq() {
        return fertilizeFreq;
    }

    public void setFertilizeFreq(int fertilizeFreq) {
        this.fertilizeFreq = fertilizeFreq;
    }

    public LocalDate getCurWaterDate() {
        return curWaterDate;
    }

    public void setCurWaterDate(LocalDate curWaterDate) {
        this.curWaterDate = curWaterDate;
    }

    public LocalDate getNextWaterDate() {
        return nextWaterDate;
    }

    public void setNextWaterDate(LocalDate nextWaterDate) {
        this.nextWaterDate = nextWaterDate;
    }

    public LocalDate getCurFertilizeDate() {
        return curFertilizeDate;
    }

    public void setCurFertilizeDate(LocalDate curFertilizeDate) {
        this.curFertilizeDate = curFertilizeDate;
    }

    public LocalDate getNextFertilizeDate() {
        return nextFertilizeDate;
    }

    public void setNextFertilizeDate(LocalDate nextFertilizeDate) {
        this.nextFertilizeDate = nextFertilizeDate;
    }

    public int getPlantId() {
        return plantId;
    }

    public void setPlantId(int plantId) {
        this.plantId = plantId;
    }
}
