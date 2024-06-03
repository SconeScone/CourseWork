package com.example.gardenerhelperapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImageManager {
    private static final String plantImagesDir = "plantImagesDir";

    public String generatePlantImageName () {
        LocalDateTime curDateAndTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        String plantImageName = "plantImg_" + curDateAndTime.format(formatter) + ".png";
        return plantImageName;
    }

    public File getPlantImagesDir(Context context) {
        File dir = new File(context.getFilesDir(), plantImagesDir);
        Path dirPath = Paths.get(dir.getAbsolutePath());

        try {
            if(Files.notExists(dirPath)) { // создаем plantImagesDir, т.к. её еще не существует
                Files.createDirectory(dirPath);
                return dir;
            }
            else { // plantImagesDir существует, возвращаем директорию
                return dir;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void savePlantImage(Context context, Bitmap plantImage, String plantImageName) { // сохранение картинки в директорию plantImagesDir
        File dir = getPlantImagesDir(context);
        File plantImageFile = new File(dir, plantImageName);

        try (FileOutputStream fos = new FileOutputStream(plantImageFile)) {
            plantImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePlantImage(Context context, String plantImageName) { // удаление картинки из папки
        File dir = new File(context.getFilesDir(), plantImagesDir);
        File plantImageFile = new File(dir, plantImageName);

        Path filePathToDelete =Paths.get(plantImageFile.getAbsolutePath());

        try {
            Files.deleteIfExists(filePathToDelete);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
