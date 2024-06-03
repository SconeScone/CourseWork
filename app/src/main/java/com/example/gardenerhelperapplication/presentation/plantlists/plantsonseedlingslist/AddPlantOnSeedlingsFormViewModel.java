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

public class AddPlantOnSeedlingsFormViewModel extends AndroidViewModel {
    private PlantsOnSeedlingsListManager plantsOnSeedlingsListManager;
    private MutableLiveData<Boolean> closeFormResult;

    public AddPlantOnSeedlingsFormViewModel(@NonNull Application application) {
        super(application);

        plantsOnSeedlingsListManager = new PlantsOnSeedlingsListManagerImpl(MyPlantsDatabase.getMyPlantsDatabase(application).getPlantOnSeedlingsDao());
        closeFormResult = new MutableLiveData<>(false);
    }

    public LiveData<Boolean> getCloseFormResult() {
        return closeFormResult;
    }

    public void addPlantOnSeedlings(AddPlantOnSeedlingsFormState formState) {
        PlantOnSeedlingsInfo plantOnSeedlingsInfo = getPlantOnSeedlingsInfoInstance(formState);
        insertPlantOnSeedlings(plantOnSeedlingsInfo);

        closeFormResult.setValue(true);
    }

    private PlantOnSeedlingsInfo getPlantOnSeedlingsInfoInstance(AddPlantOnSeedlingsFormState formState) {
        FrequencyConverter waterFrequencyConverter = new WaterFrequencyConverter();
        FrequencyConverter fertilizeFrequencyConverter = new FertilizeFrequencyConverter();

        String dateOnSeedlings = formState.getDateOnSeedlings();
        int waterFreq = waterFrequencyConverter.frequencyFromString(formState.getWaterFreq());
        int fertilizeFreq = fertilizeFrequencyConverter.frequencyFromString(formState.getFertilizeFreq());

        return new PlantOnSeedlingsInfo(dateOnSeedlings, waterFreq, fertilizeFreq, null,
                null, null, null, formState.getPlantId());
    }

    private void insertPlantOnSeedlings(PlantOnSeedlingsInfo plantOnSeedlingsInfo) {
        plantsOnSeedlingsListManager.insertPlantOnSeedlings(plantOnSeedlingsInfo);
    }
}
