package com.example.gardenerhelperapplication.presentation.plantlists.plantsonseedlingslist;

public class EditPlantOnSeedlingsFormState {
    private String dateOnSeedlings;
    private String waterFreq;
    private String fertilizeFreq;

    public EditPlantOnSeedlingsFormState(String dateOnSeedlings, String waterFreq, String fertilizeFreq) {
        this.dateOnSeedlings = dateOnSeedlings;
        this.waterFreq = waterFreq;
        this.fertilizeFreq = fertilizeFreq;
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
}
