package com.example.gardenerhelperapplication.presentation.plantlists.plantsingroundlist;

public class AddPlantInGroundFormState {
    private String datePlantedInGround;
    private String waterFreq;
    private String fertilizeFreq;
    private int plantId;

    public AddPlantInGroundFormState(String datePlantedInGround, String waterFreq, String fertilizeFreq, int plantId) {
        this.datePlantedInGround = datePlantedInGround;
        this.waterFreq = waterFreq;
        this.fertilizeFreq = fertilizeFreq;
        this.plantId = plantId;
    }

    public String getDatePlantedInGround() {
        return datePlantedInGround;
    }

    public String getWaterFreq() {
        return waterFreq;
    }

    public String getFertilizeFreq() {
        return fertilizeFreq;
    }

    public int getPlantId() {
        return plantId;
    }
}
