package com.example.gardenerhelperapplication.presentation.plantlists;

public interface OnPlantItemButtonsClickListener {
    public void onDeleteButtonClick(int id, int plantId);
    public void onEditButtonClick(int id, String plantName, String plantSort);
    public void onWaterButtonClick(int id, int waterFreq);
    public void onFertilizeButtonClick(int id, int fertilizeFreq);
}
