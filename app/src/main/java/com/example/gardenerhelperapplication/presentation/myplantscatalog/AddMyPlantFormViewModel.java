package com.example.gardenerhelperapplication.presentation.myplantscatalog;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gardenerhelperapplication.data.MyPlantsCatalogManagerImpl;
import com.example.gardenerhelperapplication.data.MyPlantsDatabase;
import com.example.gardenerhelperapplication.entities.MyPlant;
import com.example.gardenerhelperapplication.managers.MyPlantsCatalogManager;
import com.example.gardenerhelperapplication.utils.ImageManager;
import com.example.gardenerhelperapplication.utils.PlantNameValidator;
import com.example.gardenerhelperapplication.utils.PlantSortValidator;
import com.example.gardenerhelperapplication.utils.StringFormatter;
import com.example.gardenerhelperapplication.utils.ValidationResult;

public class AddMyPlantFormViewModel extends AndroidViewModel {
    private MyPlantsCatalogManager myPlantsCatalogManager;
    private MutableLiveData<EditableMyPlantFormState> formState;
    private PlantNameValidator plantNameValidator;
    private PlantSortValidator plantSortValidator;
    private final ImageManager imageManager;

    public AddMyPlantFormViewModel(@NonNull Application application) {
        super(application);

        myPlantsCatalogManager = new MyPlantsCatalogManagerImpl(MyPlantsDatabase.getMyPlantsDatabase(application).getMyPlantDao());
        formState = new MutableLiveData<>(null);
        plantNameValidator = new PlantNameValidator();
        plantSortValidator = new PlantSortValidator();
        imageManager = new ImageManager();
    }

    public LiveData<EditableMyPlantFormState> getAddMyPlantFormState() {
        return formState;
    }

    public void addMyPlant(EditableMyPlantFormState formState) {
        ValidationResult plantNameValidResult = plantNameValidator.validate(formState.getPlantName());
        ValidationResult plantSortValidResult = plantSortValidator.validate(formState.getPlantSort());

        boolean isValid = plantNameValidResult.getIsValid() && plantSortValidResult.getIsValid();

        formState.setPlantNameAttentionMessage(plantNameValidResult.getAttentionMessage());
        formState.setPlantSortAttentionMessage(plantSortValidResult.getAttentionMessage());

        if (!isValid) {
            formState.setValid(false); // Название или сорт введены неверно => растение не добавляем
            this.formState.setValue(formState);
        } else {
            MyPlant newMyPlant = getMyPlantInstance(formState); // Получение экземпляра класса MyPlant
            insertMyPlant(newMyPlant); // Добавление нового растения в базу данных
            formState.setValid(true);
            this.formState.setValue(formState);
        }
    }

    private MyPlant getMyPlantInstance(EditableMyPlantFormState formState) {
        StringFormatter formatter = new StringFormatter();

        Bitmap plantImage = formState.getPlantImage();
        String plantImageName = null;
        if (plantImage != null) {
            plantImageName = imageManager.generatePlantImageName();
            imageManager.savePlantImage(this.getApplication().getApplicationContext(), plantImage, plantImageName);
        }

        String plantName = formatter.getFormattedString(formState.getPlantName());
        String plantSort = formatter.getFormattedString(formState.getPlantSort());
        String dateOnSeedlings = formatter.getFormattedString(formState.getDateOnSeedlings());
        String datePlantedInGround = formatter.getFormattedString(formState.getDatePlantedInGround());
        String dateHarvesting = formatter.getFormattedString(formState.getDateHarvesting());
        String description = formatter.getFormattedString(formState.getDescription());
        String care = formatter.getFormattedString(formState.getCare());
        String otherInfo = formatter.getFormattedString(formState.getOtherInfo());

        return new MyPlant(plantName, plantSort, plantImageName,
                dateOnSeedlings, datePlantedInGround, dateHarvesting, description,
                care, otherInfo, false, false);
    }

    private void insertMyPlant(MyPlant myPlant) {
        myPlantsCatalogManager.insertMyPlant(myPlant);
    }
}
