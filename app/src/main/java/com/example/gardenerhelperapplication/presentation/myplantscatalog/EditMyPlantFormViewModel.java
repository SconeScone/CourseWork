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
import com.example.gardenerhelperapplication.utils.PlantNameValidator;
import com.example.gardenerhelperapplication.utils.PlantSortValidator;
import com.example.gardenerhelperapplication.utils.StringFormatter;
import com.example.gardenerhelperapplication.utils.ValidationResult;

public class EditMyPlantFormViewModel extends AndroidViewModel {
    private MyPlantsCatalogManager myPlantsCatalogManager;
    private MutableLiveData<MyPlant> myPlant;
    private MutableLiveData<EditableMyPlantFormState> formState;
    private PlantNameValidator plantNameValidator;
    private PlantSortValidator plantSortValidator;
    private StringFormatter formatter;
    private final ImageManager imageManager;

    public EditMyPlantFormViewModel(@NonNull Application application) {
        super(application);

        myPlantsCatalogManager = new MyPlantsCatalogManagerImpl(MyPlantsDatabase.getMyPlantsDatabase(application).getMyPlantDao());
        formState = new MutableLiveData<>(null);
        plantNameValidator = new PlantNameValidator();
        plantSortValidator = new PlantSortValidator();
        formatter = new StringFormatter();
        imageManager = new ImageManager();
    }

    public LiveData<MyPlant> getMyPlantById(int id) {
        if (myPlant == null) {
            myPlant = new MutableLiveData<>(null);
            loadMyPlantById(id);
        }
        return myPlant;
    }

    private void loadMyPlantById(int id) {
        myPlantsCatalogManager.getMyPlantById(id, new GetMyPlantCallback() {
            @Override
            public void onComplete(MyPlant result) {
                if (result != null) {
                    myPlant.postValue(result);
                }
            }
        });
    }

    public LiveData<EditableMyPlantFormState> getEditMyPlantFormState() {
        return formState;
    }

    public void updateMyPlantInfo(EditableMyPlantFormState formState, boolean isPlantImageEdit) {
        ValidationResult plantNameValidResult = plantNameValidator.validate(formState.getPlantName());
        ValidationResult plantSortValidResult = plantSortValidator.validate(formState.getPlantSort());

        boolean isValid = plantNameValidResult.getIsValid() && plantSortValidResult.getIsValid();

        formState.setPlantNameAttentionMessage(plantNameValidResult.getAttentionMessage());
        formState.setPlantSortAttentionMessage(plantSortValidResult.getAttentionMessage());

        if (!isValid) {
            formState.setValid(false);
            this.formState.setValue(formState);
        } else {
            MyPlant myPlantToUpdate = myPlant.getValue();

            if (myPlantToUpdate != null) {
                MyPlant myPlantToUpdateCopy = new MyPlant(myPlantToUpdate);
                boolean canUpdatePlant = false;

                myPlantToUpdate.setPlantName(formatter.getFormattedString(formState.getPlantName()));
                myPlantToUpdate.setPlantSort(formatter.getFormattedString(formState.getPlantSort()));

                if (isPlantImageEdit) { // Если картинка растения была отредактирована (сброшена или установлена новая)
                    if (formState.getPlantImage() != null) {
                        if (myPlantToUpdate.getPlantImage() != null) {  // Если у растения раньше была картинка
                            imageManager.savePlantImage(this.getApplication().getApplicationContext(), formState.getPlantImage(), myPlantToUpdate.getPlantImage());
                            canUpdatePlant = true;
                        } else { // Если у растения не было картинки
                            String plantImageName = imageManager.generatePlantImageName();
                            myPlantToUpdate.setPlantImage(plantImageName);

                            imageManager.savePlantImage(this.getApplication().getApplicationContext(), formState.getPlantImage(), plantImageName);
                            canUpdatePlant = true;
                        }
                    } else { // Удаление картинки
                        if (myPlantToUpdate.getPlantImage() != null) { // Если у растения была картинка, но она сброшена
                            imageManager.deletePlantImage(this.getApplication().getApplicationContext(), myPlantToUpdate.getPlantImage());
                            myPlantToUpdate.setPlantImage(null);
                            canUpdatePlant = true;
                        }
                    }
                }

                // Если у растения не было картинки и новой не установлено (была установлена и сброшена)
                // У растения есть картинка (пустая или зполненная), и новой не установлено
                // Тогда canUpdatePlant = false

                myPlantToUpdate.setDateOnSeedlings(formatter.getFormattedString(formState.getDateOnSeedlings()));
                myPlantToUpdate.setDatePlantedInGround(formatter.getFormattedString(formState.getDatePlantedInGround()));
                myPlantToUpdate.setDateHarvesting(formatter.getFormattedString(formState.getDateHarvesting()));
                myPlantToUpdate.setDescription(formatter.getFormattedString(formState.getDescription()));
                myPlantToUpdate.setCare(formatter.getFormattedString(formState.getCare()));
                myPlantToUpdate.setOtherInfo(formatter.getFormattedString(formState.getOtherInfo()));

                if (myPlantToUpdate.equals(myPlantToUpdateCopy)) {
                    if (canUpdatePlant) {
                        updateMyPlant(myPlantToUpdate);
                    }
                } else {
                    updateMyPlant(myPlantToUpdate);
                }

                formState.setValid(true);
                this.formState.setValue(formState);
            }
        }
    }

    private void updateMyPlant(MyPlant myPlantToUpdate) {
        myPlantsCatalogManager.updateMyPlant(myPlantToUpdate);
    }
}


