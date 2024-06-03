package com.example.gardenerhelperapplication.utils;

import java.time.LocalDate;

public class WaterFrequencyConverter implements FrequencyConverter{

    // Форматирует частоту полива из числа в строку
    @Override
    public String frequencyToString(int frequency) {
        String waterFreq;
        switch (frequency) {
            case 1:
                waterFreq = "1 раз в день";
                break;
            case 2:
                waterFreq = "1 раз в 2 дня";
                break;
            case 7:
                waterFreq = "1 раз в неделю";
                break;
            case 14:
                waterFreq = "1 раз в 2 недели";
                break;
            default:
                waterFreq = "1 раз в месяц";
                break;
        }
        return waterFreq;
    }

    // Форматирует частоту полива из строки в число
    @Override
    public int frequencyFromString(String frequency) {
        int waterFreq;
        switch (frequency) {
            case "1 раз в день":
                waterFreq = 1;
                break;
            case "1 раз в 2 дня":
                waterFreq = 2;
                break;
            case "1 раз в неделю":
                waterFreq = 7;
                break;
            case "1 раз в 2 недели":
                waterFreq = 14;
                break;
            default:
                waterFreq = LocalDate.now().lengthOfMonth(); // 1 раз в месяц
                break;
        }
        return waterFreq;
    }
}
