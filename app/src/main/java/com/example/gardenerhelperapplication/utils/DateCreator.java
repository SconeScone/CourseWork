package com.example.gardenerhelperapplication.utils;

public class DateCreator {
    public static String create(int year, int month, int dayOfMonth) {
        String date = "";
        if ((dayOfMonth / 10) == 0) {
            date += "0" + dayOfMonth + ".";
        } else {
            date += dayOfMonth + ".";
        }
        if (((month + 1) / 10) == 0) {
            date += "0" + (month + 1) + ".";
        } else {
            date += (month + 1) + ".";
        }
        date += String.valueOf(year);
        return date;
    }
}
