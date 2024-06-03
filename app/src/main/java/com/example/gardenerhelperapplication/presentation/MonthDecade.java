package com.example.gardenerhelperapplication.presentation;
import java.io.Serializable;

public class MonthDecade implements Serializable {
    private String month;
    private String decade;

    public MonthDecade(String month, String decade) {
        this.month = month;
        this.decade = decade;
    }

    public String getMonth() {
        return month;
    }

    public String getDecade() {
        return decade;
    }
}
