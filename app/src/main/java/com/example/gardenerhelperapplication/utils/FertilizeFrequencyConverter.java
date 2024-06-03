package com.example.gardenerhelperapplication.utils;

import java.time.LocalDate;

public class FertilizeFrequencyConverter implements FrequencyConverter{
    @Override
    public String frequencyToString(int frequency) {
        String fertilizeFreq;
        switch (frequency) {
            case 7:
                fertilizeFreq = "Каждую неделю";
                break;
            case 14:
                fertilizeFreq = "Каждые 2 недели";
                break;
            case 21:
                fertilizeFreq = "Каждые 3 недели";
                break;
            default:
                fertilizeFreq = "Каждый месяц";
                break;
        }
        return fertilizeFreq;
    }

    @Override
    public int frequencyFromString(String frequency) {
        int fertilizeFreq;
        switch (frequency) {
            case "Каждую неделю":
                fertilizeFreq = 7;
                break;
            case "Каждые 2 недели":
                fertilizeFreq = 14;
                break;
            case "Каждые 3 недели":
                fertilizeFreq = 21;
                break;
            default:
                fertilizeFreq = LocalDate.now().lengthOfMonth(); // Каждый месяц
                break;
        }
        return fertilizeFreq;
    }
}