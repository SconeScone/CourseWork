package com.example.gardenerhelperapplication.presentation.plantlists.plantsonseedlingslist;

public class AddPlantOnSeedlingsFormState {
    private String dateOnSeedlings;
    private String waterFreq;
    private String fertilizeFreq;
    private int plantId;

    public AddPlantOnSeedlingsFormState(String dateOnSeedlings, String waterFreq, String fertilizeFreq, int plantId) {
        this.dateOnSeedlings = dateOnSeedlings;
        this.waterFreq = waterFreq;
        this.fertilizeFreq = fertilizeFreq;
        this.plantId = plantId;
    }

    public String getDateOnSeedlings() {
        return dateOnSeedlings;
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
