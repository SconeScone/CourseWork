package com.example.gardenerhelperapplication;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GHApp extends Application {
    private static final ExecutorService queryExecutor = Executors.newFixedThreadPool(4);

    public static ExecutorService getQueryExecutor() {
        return queryExecutor;
    }
}
