package com.example.gardenerhelperapplication.utils;

import android.net.Uri;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.LifecycleOwner;

public class ImagePicker {
    private static final String IMAGE_PICKER_FROM_GALLERY_KEY = "ImagePickerFromGallery.IMAGE_PICKER_FROM_GALLERY_KEY";
    private ActivityResultLauncher<String> getPlantImage;
    public ImagePicker(ActivityResultRegistry registry, LifecycleOwner lifecycleOwner, ActivityResultCallback<Uri> callback) {
        getPlantImage = registry.register(IMAGE_PICKER_FROM_GALLERY_KEY, lifecycleOwner, new ActivityResultContracts.GetContent(), callback);
    }

    public void pickImageFromGallery() {
        getPlantImage.launch("image/*");
    }
}
