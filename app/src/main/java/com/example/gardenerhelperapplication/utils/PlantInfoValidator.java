package com.example.gardenerhelperapplication.utils;

public class PlantInfoValidator {
    public ValidationResult validate(String info) {
        if (info == null || info.isEmpty()) {
            ValidationResult result = new ValidationResult(false,
                    "Обязательно для заполнения");
            return result;
        }
        return new ValidationResult(true, "");
    }
}
