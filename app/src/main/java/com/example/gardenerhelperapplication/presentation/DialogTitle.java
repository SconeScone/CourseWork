package com.example.gardenerhelperapplication.presentation;

public enum DialogTitle {
    DATE_ON_SEEDLINGS ("Выберите дату посева на рассаду", 1),
    DATE_PLANTED_IN_GROUND ("Выберите дату высадки в грунт", 2),
    DATE_HARVESTING ("Выберите дату сборки урожая", 3),
    WATERING_FREQUENCY ("Выберите периодичность полива растения", 4),
    FERTILIZATION_FREQUENCY ("Выберите периодичность подкормки растения", 5);
    private String dialogTitle;
    private int typeOfDialog;
    DialogTitle(String dialogTitle, int typeOfDialog) {
        this.dialogTitle = dialogTitle;
        this.typeOfDialog = typeOfDialog;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public int getTypeOfDialog() {
        return typeOfDialog;
    }
}
