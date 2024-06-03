package com.example.gardenerhelperapplication.entities;

import java.time.LocalDate;
import java.util.Objects;

public class PlantInGround {
    private int id;
    private String plantName;
    private String plantSort;
    private String plantImage;
    private String datePlantedInGround;
    private int waterFreq;
    private int fertilizeFreq;
    private LocalDate curWaterDate;
    private LocalDate nextWaterDate;
    private LocalDate curFertilizeDate;
    private LocalDate nextFertilizeDate;
    private int plantId;

    public PlantInGround(int id, String plantName, String plantSort, String plantImage,  String datePlantedInGround, int waterFreq, int fertilizeFreq, LocalDate curWaterDate, LocalDate nextWaterDate, LocalDate curFertilizeDate, LocalDate nextFertilizeDate, int plantId) {
        this.id = id;
        this.plantName = plantName;
        this.plantSort = plantSort;
        this.plantImage = plantImage;
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

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPlantSort() {
        return plantSort;
    }

    public String getPlantImage() {
        return plantImage;
    }

    public void setPlantImage(String plantImage) {
        this.plantImage = plantImage;
    }

    public void setPlantSort(String plantSort) {
        this.plantSort = plantSort;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlantInGround that = (PlantInGround) o;
        return getId() == that.getId()
                && getWaterFreq() == that.getWaterFreq()
                && getFertilizeFreq() == that.getFertilizeFreq()
                && getPlantId() == that.getPlantId()
                && Objects.equals(getDatePlantedInGround(), that.getDatePlantedInGround())
                && Objects.equals(getCurWaterDate(), that.getCurWaterDate())
                && Objects.equals(getNextWaterDate(), that.getNextWaterDate())
                && Objects.equals(getCurFertilizeDate(), that.getCurFertilizeDate())
                && Objects.equals(getNextFertilizeDate(), that.getNextFertilizeDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDatePlantedInGround(), getWaterFreq(), getFertilizeFreq(), getCurWaterDate(), getNextWaterDate(), getCurFertilizeDate(), getNextFertilizeDate(), getPlantId());
    }
}
