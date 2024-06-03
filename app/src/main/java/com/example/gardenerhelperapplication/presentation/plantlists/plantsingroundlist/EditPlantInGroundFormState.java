package com.example.gardenerhelperapplication.presentation.plantlists.plantsingroundlist;

public class EditPlantInGroundFormState {
    private String datePlantedInGround;
    private String waterFreq;
    private String fertilizeFreq;

    public EditPlantInGroundFormState(String datePlantedInGround, String waterFreq, String fertilizeFreq) {
        this.datePlantedInGround = datePlantedInGround;
        this.waterFreq = waterFreq;
        this.fertilizeFreq = fertilizeFreq;
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
}
