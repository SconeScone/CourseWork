package com.example.gardenerhelperapplication.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "my_plants")
public class MyPlant {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "plant_name")
    private String plantName;
    @ColumnInfo(name = "plant_sort")
    private String plantSort;
    @ColumnInfo(name = "plant_image")
    private String plantImage;
    @ColumnInfo(name = "date_on_seedlings")
    private String dateOnSeedlings;
    @ColumnInfo(name = "date_planted_in_ground")
    private String datePlantedInGround;
    @ColumnInfo(name = "date_harvesting")
    private String dateHarvesting;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "care")
    private String care;
    @ColumnInfo(name = "other_information")
    private String otherInfo;
    @ColumnInfo(name = "is_on_seedlings")
    private boolean isOnSeedlings;
    @ColumnInfo(name = "is_plant_in_ground")
    private boolean isPlantInGround;

    public MyPlant() {
    }

    @Ignore
    public MyPlant(String plantName, String plantSort, String plantImage, String dateOnSeedlings,
                   String datePlantedInGround, String dateHarvesting, String description, String care, String otherInfo,
                   boolean isOnSeedlings, boolean isPlantInGround) {
        this.plantName = plantName;
        this.plantSort = plantSort;
        this.plantImage = plantImage;
        this.dateOnSeedlings = dateOnSeedlings;
        this.datePlantedInGround = datePlantedInGround;
        this.dateHarvesting = dateHarvesting;
        this.description = description;
        this.care = care;
        this.otherInfo = otherInfo;
        this.isOnSeedlings = isOnSeedlings;
        this.isPlantInGround = isPlantInGround;
    }

    @Ignore
    public MyPlant(int id, String plantName, String plantSort, String plantImage,
                   String dateOnSeedlings, String datePlantedInGround, String dateHarvesting, String description,
                   String care, String otherInfo, boolean isOnSeedlings, boolean isPlantInGround) {
        this(plantName, plantSort, plantImage, dateOnSeedlings, datePlantedInGround, dateHarvesting,
                description, care, otherInfo, isOnSeedlings, isPlantInGround);
        this.id = id;
    }

    @Ignore
    public MyPlant(MyPlant myPlantOrig) {
        id = myPlantOrig.getId();
        plantName = myPlantOrig.getPlantName();
        plantSort = myPlantOrig.getPlantSort();
        plantImage = myPlantOrig.getPlantImage();
        dateOnSeedlings = myPlantOrig.getDateOnSeedlings();
        datePlantedInGround = myPlantOrig.getDatePlantedInGround();
        dateHarvesting = myPlantOrig.getDateHarvesting();
        description = myPlantOrig.getDescription();
        care = myPlantOrig.getCare();
        otherInfo = myPlantOrig.getOtherInfo();
        isOnSeedlings = myPlantOrig.isOnSeedlings();
        isPlantInGround = myPlantOrig.isPlantInGround();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPlantSort() {
        return plantSort;
    }

    public void setPlantSort(String plantSort) {
        this.plantSort = plantSort;
    }

    public String getPlantImage() {
        return plantImage;
    }

    public void setPlantImage(String plantImage) {
        this.plantImage = plantImage;
    }

    public String getDateOnSeedlings() {
        return dateOnSeedlings;
    }

    public void setDateOnSeedlings(String dateOnSeedlings) {
        this.dateOnSeedlings = dateOnSeedlings;
    }

    public String getDatePlantedInGround() {
        return datePlantedInGround;
    }

    public void setDatePlantedInGround(String datePlantedInGround) {
        this.datePlantedInGround = datePlantedInGround;
    }

    public String getDateHarvesting() {
        return dateHarvesting;
    }

    public void setDateHarvesting(String dateHarvesting) {
        this.dateHarvesting = dateHarvesting;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCare() {
        return care;
    }

    public void setCare(String care) {
        this.care = care;
    }


    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public boolean isOnSeedlings() {
        return isOnSeedlings;
    }

    public void setOnSeedlings(boolean onSeedlings) {
        isOnSeedlings = onSeedlings;
    }

    public boolean isPlantInGround() {
        return isPlantInGround;
    }

    public void setPlantInGround(boolean plantInGround) {
        isPlantInGround = plantInGround;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyPlant myPlant = (MyPlant) o;
        return Objects.equals(getPlantName(), myPlant.getPlantName())
                && Objects.equals(getPlantSort(), myPlant.getPlantSort())
                && Objects.equals(getDateOnSeedlings(), myPlant.getDateOnSeedlings())
                && Objects.equals(getDatePlantedInGround(), myPlant.getDatePlantedInGround())
                && Objects.equals(getDateHarvesting(), myPlant.getDateHarvesting())
                && Objects.equals(getDescription(), myPlant.getDescription())
                && Objects.equals(getCare(), myPlant.getCare())
                && Objects.equals(getOtherInfo(), myPlant.getOtherInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlantName(), getPlantSort(), getDateOnSeedlings(), getDatePlantedInGround(), getDateHarvesting(), getDescription(), getCare(), getOtherInfo());
    }
}
