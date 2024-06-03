package com.example.gardenerhelperapplication.utils;

public class PlantSortValidator {
    public ValidationResult validate(String plantSort) {
        if (plantSort.matches("^\\s*$")) {
            ValidationResult result = new ValidationResult(false,
                    "Введите сорт растения");
            return result;
        }
        if (!plantSort.matches("^[а-яА-Яa-zA-Z0-9\\s]+$")) {
            return new ValidationResult(false,
                    "Сорт растения может содержать только буквы, цифры и пробелы");
        }
        return new ValidationResult(true,
                "");
    }
}
