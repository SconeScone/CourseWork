package com.example.gardenerhelperapplication.utils;

public class PlantNameValidator {
    public ValidationResult validate(String plantName) {
        if (plantName.matches("^\\s*$")) {
            ValidationResult result = new ValidationResult(false,
                    "Введите название растения");
            return result;
        }
        if (!plantName.matches("^[а-яА-Яa-zA-Z\\s]+$")) {
            ValidationResult result = new ValidationResult(false,
                    "Название растения может содержать только буквы и пробелы");
            return result;
        }
        return new ValidationResult(true, "");
    }
}
