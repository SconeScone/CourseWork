package com.example.gardenerhelperapplication.utils;

public class StringFormatter {
    public String getFormattedString(String stringToFormat) {
        if(stringToFormat == null || stringToFormat.isEmpty() || stringToFormat.matches("^\\s*$")) {
            return null;
        }
        return stringToFormat.trim();
    }
}
