package com.example.gardenerhelperapplication.presentation.plantlists.plantsonseedlingslist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.gardenerhelperapplication.data.MyPlantsDatabase;
import com.example.gardenerhelperapplication.data.PlantsOnSeedlingsListManagerImpl;
import com.example.gardenerhelperapplication.entities.PlantOnSeedlingsInfo;
import com.example.gardenerhelperapplication.managers.PlantsOnSeedlingsListManager;
import com.example.gardenerhelperapplication.utils.FertilizeFrequencyConverter;
import com.example.gardenerhelperapplication.utils.FrequencyConverter;
import com.example.gardenerhelperapplication.utils.WaterFrequencyConverter;

import java.time.LocalDate;

public class EditPlantOnSeedlingsFormViewModel extends AndroidViewModel {
    private PlantsOnSeedlingsListManager plantsOnSeedlingsListManager;
    private MutableLiveData<PlantOnSeedlingsInfo> plantOnSeedlingsInfo;
    private MutableLiveData<Boolean> closeFormResult;

    public EditPlantOnSeedlingsFormViewModel(@NonNull Application application) {
        super(application);

        plantsOnSeedlingsListManager = new PlantsOnSeedlingsListManagerImpl(MyPlantsDatabase.getMyPlantsDatabase(application).getPlantOnSeedlingsDao());
        closeFormResult = new MutableLiveData<>(false);
    }

    public LiveData<PlantOnSeedlingsInfo> getPlantOnSeedlingsById(int id) {
        if (plantOnSeedlingsInfo == null) {
            plantOnSeedlingsInfo = new MutableLiveData<>(null);
            loadPlantOnSeedlingsInfoById(id);
        }
        return plantOnSeedlingsInfo;
    }

    private void loadPlantOnSeedlingsInfoById(int id) {
        plantsOnSeedlingsListManager.getPlantOnSeedlingsInfoById(id, new GetPlantOnSeedlingsInfoCallback() {
            @Override
            public void onComplete(PlantOnSeedlingsInfo result) {  // Получение растения по id
                if (result != null) {
                    plantOnSeedlingsInfo.postValue(result);
                }
            }
        });
    }

    public LiveData<Boolean> getCloseFormResult() {
        return closeFormResult;
    }

    public void saveUpdatedPlantOnSeedlingsInfo(EditPlantOnSeedlingsFormState formState) {
        PlantOnSeedlingsInfo plantOnSeedlingsInfoToUpdate = plantOnSeedlingsInfo.getValue();

        FrequencyConverter waterFrequencyConverter = new WaterFrequencyConverter();
        FrequencyConverter fertilizeFrequencyConverter = new FertilizeFrequencyConverter();

        String newDateOnSeedlings = formState.getDateOnSeedlings();
        int newWaterFreq = waterFrequencyConverter.frequencyFromString(formState.getWaterFreq());
        int newFertilizeFreq = fertilizeFrequencyConverter.frequencyFromString(formState.getFertilizeFreq());

        boolean hasInfoChanges = false;

        if (plantOnSeedlingsInfoToUpdate != null) {
            if (!newDateOnSeedlings.equals(plantOnSeedlingsInfoToUpdate.getDateOnSeedlings())) {
                plantOnSeedlingsInfoToUpdate.setDateOnSeedlings(newDateOnSeedlings);
                hasInfoChanges = true;
            }

            if (newWaterFreq != plantOnSeedlingsInfoToUpdate.getWaterFreq()) {
                if (plantOnSeedlingsInfoToUpdate.getNextWaterDate() != null) {
                    LocalDate newNextWaterDate = plantOnSeedlingsInfoToUpdate.getCurWaterDate().plusDays(newWaterFreq);
                    plantOnSeedlingsInfoToUpdate.setNextWaterDate(newNextWaterDate);
                }
                plantOnSeedlingsInfoToUpdate.setWaterFreq(newWaterFreq);
                hasInfoChanges = true;
            }

            if (newFertilizeFreq != plantOnSeedlingsInfoToUpdate.getFertilizeFreq()) {
                if (plantOnSeedlingsInfoToUpdate.getNextFertilizeDate() != null) {
                    LocalDate newNextFertilizeDate = plantOnSeedlingsInfoToUpdate.getCurFertilizeDate().plusDays(newFertilizeFreq);
                    plantOnSeedlingsInfoToUpdate.setNextFertilizeDate(newNextFertilizeDate);
                }
                plantOnSeedlingsInfoToUpdate.setFertilizeFreq(newFertilizeFreq);
                hasInfoChanges = true;
            }

            if (hasInfoChanges) { // Если есть изменения в системе ухода, обновляем её
                updatePlantOnSeedlingsInfo(plantOnSeedlingsInfoToUpdate);
            }

            closeFormResult.setValue(true);
        }
    }

    private void updatePlantOnSeedlingsInfo(PlantOnSeedlingsInfo plantOnSeedlingsInfo) {
        plantsOnSeedlingsListManager.updatePlantOnSeedlingsInfo(plantOnSeedlingsInfo);
    }
}
