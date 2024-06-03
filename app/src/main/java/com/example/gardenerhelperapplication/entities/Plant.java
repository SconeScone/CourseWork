package com.example.gardenerhelperapplication.entities;

import java.util.Objects;

public class Plant {
    private int id;
    private String plantName;
    private String plantSort;
    private String plantImage;
    private boolean isOnSeedlings;
    private boolean isPlantInGround;

    public Plant(int id, String plantName, String plantSort, String plantImage, boolean isOnSeedlings, boolean isPlantInGround) {
        this.id = id;
        this.plantName = plantName;
        this.plantSort = plantSort;
        this.plantImage = plantImage;
        this.isOnSeedlings = isOnSeedlings;
        this.isPlantInGround = isPlantInGround;
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
        Plant plant = (Plant) o;
        return getId() == plant.getId()
                && isOnSeedlings() == plant.isOnSeedlings()
                && isPlantInGround() == plant.isPlantInGround()
                && Objects.equals(getPlantName(), plant.getPlantName())
                && Objects.equals(getPlantSort(), plant.getPlantSort());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPlantName(), getPlantSort(), isOnSeedlings(), isPlantInGround());
    }
}
