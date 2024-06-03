package com.example.gardenerhelperapplication.presentation.myplantscatalog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gardenerhelperapplication.data.MyPlantsCatalogManagerImpl;
import com.example.gardenerhelperapplication.data.MyPlantsDatabase;
import com.example.gardenerhelperapplication.entities.MyPlant;
import com.example.gardenerhelperapplication.managers.MyPlantsCatalogManager;
import com.example.gardenerhelperapplication.utils.ImageManager;

public class MyPlantInfoFragmentViewModel extends AndroidViewModel {
    private MyPlantsCatalogManager myPlantsCatalogManager;
    private MutableLiveData<MyPlant> myPlant;
    private MutableLiveData<Boolean> myPlantIsDeletedResult;
    private final ImageManager imageManager;

    public MyPlantInfoFragmentViewModel(@NonNull Application application) {
        super(application);

        myPlantsCatalogManager = new MyPlantsCatalogManagerImpl(MyPlantsDatabase.getMyPlantsDatabase(application).getMyPlantDao());
        myPlantIsDeletedResult = new MutableLiveData<>(false);
        imageManager = new ImageManager();
    }

    public LiveData<MyPlant> getMyPlantById(int id) {
        if (myPlant == null) {
            myPlant = new MutableLiveData<>(null);
            loadMyPlantById(id);
        }
        return myPlant;
    }

    public void loadMyPlantById(int id) {
        myPlantsCatalogManager.getMyPlantById(id, new GetMyPlantCallback() {
            @Override
            public void onComplete(MyPlant result) {
                if (result != null) {
                    myPlant.postValue(result);
                }
            }
        });
    }

    public LiveData<Boolean> getMyPlantIsDeletedResult() {
        return myPlantIsDeletedResult;
    }

    public void deleteMyPlantById(int id, String myPlantImageFileName) {
        if (myPlantImageFileName != null) { // Название изображения НЕ null: Удаляем изображение из папки plantImagesDir
            imageManager.deletePlantImage(this.getApplication().getApplicationContext(), myPlantImageFileName);
        }

        myPlantsCatalogManager.deleteMyPlantById(id);
        myPlantIsDeletedResult.setValue(true);
    }
}
