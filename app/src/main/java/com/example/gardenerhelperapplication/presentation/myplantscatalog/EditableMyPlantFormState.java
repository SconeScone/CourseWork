package com.example.gardenerhelperapplication.presentation.myplantscatalog;

import android.graphics.Bitmap;

public class EditableMyPlantFormState {
    private String plantName;
    private String plantNameAttentionMessage;
    private String plantSort;
    private String plantSortAttentionMessage;
    private Bitmap plantImage;
    private String dateOnSeedlings;
    private String datePlantedInGround;
    private String dateHarvesting;
    private String description;
    private String care;
    private String otherInfo;
    private boolean isValid = false;

    public EditableMyPlantFormState(String plantName, String plantSort, Bitmap plantImage, String dateOnSeedlings,
                                    String datePlantedInGround, String dateHarvesting, String description, String care, String otherInfo) {
        this.plantName = plantName;
        this.plantSort = plantSort;
        this.plantImage = plantImage;
        this.dateOnSeedlings = dateOnSeedlings;
        this.datePlantedInGround = datePlantedInGround;
        this.dateHarvesting = dateHarvesting;
        this.description = description;
        this.care = care;
        this.otherInfo = otherInfo;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPlantNameAttentionMessage() {
        return plantNameAttentionMessage;
    }

    public void setPlantNameAttentionMessage(String plantNameAttentionMessage) {
        this.plantNameAttentionMessage = plantNameAttentionMessage;
    }

    public String getPlantSort() {
        return plantSort;
    }

    public void setPlantSort(String plantSort) {
        this.plantSort = plantSort;
    }

    public String getPlantSortAttentionMessage() {
        return plantSortAttentionMessage;
    }

    public void setPlantSortAttentionMessage(String plantSortAttentionMessage) {
        this.plantSortAttentionMessage = plantSortAttentionMessage;
    }

    public Bitmap getPlantImage() {
        return plantImage;
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

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
