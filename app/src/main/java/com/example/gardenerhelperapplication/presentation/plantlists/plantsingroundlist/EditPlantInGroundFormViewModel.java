package com.example.gardenerhelperapplication.presentation.plantlists.plantsingroundlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gardenerhelperapplication.data.MyPlantsDatabase;
import com.example.gardenerhelperapplication.data.PlantsInGroundListManagerImpl;
import com.example.gardenerhelperapplication.entities.PlantInGroundInfo;
import com.example.gardenerhelperapplication.managers.PlantsInGroundListManager;
import com.example.gardenerhelperapplication.utils.FertilizeFrequencyConverter;
import com.example.gardenerhelperapplication.utils.FrequencyConverter;
import com.example.gardenerhelperapplication.utils.WaterFrequencyConverter;

import java.time.LocalDate;

public class EditPlantInGroundFormViewModel extends AndroidViewModel {
    private PlantsInGroundListManager plantsInGroundListManager;
    private MutableLiveData<PlantInGroundInfo> plantInGroundInfo;
    private MutableLiveData<Boolean> closeFormResult;

    public EditPlantInGroundFormViewModel(@NonNull Application application) {
        super(application);

        plantsInGroundListManager = new PlantsInGroundListManagerImpl(MyPlantsDatabase.getMyPlantsDatabase(application).getPlantInGroundDao());
        closeFormResult = new MutableLiveData<>(false);
    }

    public LiveData<PlantInGroundInfo> getPlantInGroundInfoById(int id) {
        if (plantInGroundInfo == null) {
            plantInGroundInfo = new MutableLiveData<>(null);
            loadPlantInGroundInfoById(id);
        }
        return plantInGroundInfo;
    }

    private void loadPlantInGroundInfoById(int id) {
        plantsInGroundListManager.getPlantInGroundInfoById(id, new GetPlantInGroundInfoCallback() {
            @Override
            public void onComplete(PlantInGroundInfo result) { // Получение растения, высаженного в грунт по id
                if (result != null) {
                    plantInGroundInfo.postValue(result);
                }
            }
        });
    }

    public LiveData<Boolean> getCloseFormResult() {
        return closeFormResult;
    }

    public void saveUpdatedPlantInGroundInfo(EditPlantInGroundFormState formState) {
        PlantInGroundInfo plantInGroundInfoToUpdate = plantInGroundInfo.getValue();

        FrequencyConverter waterFrequencyConverter = new WaterFrequencyConverter();
        FrequencyConverter fertilizeFrequencyConverter = new FertilizeFrequencyConverter();

        String newDatePlantedInGround = formState.getDatePlantedInGround();
        int newWaterFreq = waterFrequencyConverter.frequencyFromString(formState.getWaterFreq());
        int newFertilizeFreq = fertilizeFrequencyConverter.frequencyFromString(formState.getFertilizeFreq());

        boolean hasInfoChanges = false;

        if (plantInGroundInfoToUpdate != null) {
            if (!newDatePlantedInGround.equals(plantInGroundInfoToUpdate.getDatePlantedInGround())) {
                plantInGroundInfoToUpdate.setDatePlantedInGround(newDatePlantedInGround);
                hasInfoChanges = true;
            }

            if (newWaterFreq != plantInGroundInfoToUpdate.getWaterFreq()) {
                if (plantInGroundInfoToUpdate.getNextWaterDate() != null) {
                    LocalDate newNextWaterDate = plantInGroundInfoToUpdate.getCurWaterDate().plusDays(newWaterFreq);
                    plantInGroundInfoToUpdate.setNextWaterDate(newNextWaterDate);
                }
                plantInGroundInfoToUpdate.setWaterFreq(newWaterFreq);
                hasInfoChanges = true;
            }

            if (newFertilizeFreq != plantInGroundInfoToUpdate.getFertilizeFreq()) {
                if (plantInGroundInfoToUpdate.getNextFertilizeDate() != null) {
                    LocalDate newNextFertilizeDate = plantInGroundInfoToUpdate.getCurFertilizeDate().plusDays(newFertilizeFreq);
                    plantInGroundInfoToUpdate.setNextFertilizeDate(newNextFertilizeDate);
                }
                plantInGroundInfoToUpdate.setFertilizeFreq(newFertilizeFreq);
                hasInfoChanges = true;
            }

            if (hasInfoChanges) { // Есть изменения в системе ухода
                updatePlantInGroundInfo(plantInGroundInfoToUpdate);
            }

            closeFormResult.setValue(true);
        }
    }

    private void updatePlantInGroundInfo(PlantInGroundInfo plantInGroundInfo) {
        plantsInGroundListManager.updatePlantInGroundInfo(plantInGroundInfo);
    }
}
