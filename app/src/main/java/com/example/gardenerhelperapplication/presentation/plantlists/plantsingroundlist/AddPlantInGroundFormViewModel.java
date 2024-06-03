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

public class AddPlantInGroundFormViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> closeFormResult;
    private PlantsInGroundListManager plantsInGroundListManager;

    public AddPlantInGroundFormViewModel(@NonNull Application application) {
        super(application);

        closeFormResult = new MutableLiveData<>(false);
        plantsInGroundListManager = new PlantsInGroundListManagerImpl(MyPlantsDatabase.getMyPlantsDatabase(application).getPlantInGroundDao());
    }

    public LiveData<Boolean> getCloseFormResult() {
        return closeFormResult;
    }

    public void addPlantInGround(AddPlantInGroundFormState formState) {
        PlantInGroundInfo plantInGroundInfo = getPlantInGroundInfoInstance(formState);
        insertPlantInGround(plantInGroundInfo);

        closeFormResult.setValue(true);
    }

    private PlantInGroundInfo getPlantInGroundInfoInstance(AddPlantInGroundFormState formState) {
        FrequencyConverter waterFrequencyConverter = new WaterFrequencyConverter();
        FrequencyConverter fertilizeFrequencyConverter = new FertilizeFrequencyConverter();

        String datePlantedInGround = formState.getDatePlantedInGround();
        int waterFreq = waterFrequencyConverter.frequencyFromString(formState.getWaterFreq());
        int fertilizeFreq = fertilizeFrequencyConverter.frequencyFromString(formState.getFertilizeFreq());
        int plantId = formState.getPlantId();

        return new PlantInGroundInfo(datePlantedInGround, waterFreq, fertilizeFreq, null, null, null, null, plantId);
    }

    private void insertPlantInGround(PlantInGroundInfo plantInGroundInfo) {
        plantsInGroundListManager.insertPlantInGround(plantInGroundInfo);
    }
}
