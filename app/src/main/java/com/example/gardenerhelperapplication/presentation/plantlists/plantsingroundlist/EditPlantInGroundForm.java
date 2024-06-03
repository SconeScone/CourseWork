package com.example.gardenerhelperapplication.presentation.plantlists.plantsingroundlist;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gardenerhelperapplication.R;
import com.example.gardenerhelperapplication.databinding.PlantInGroundAddEditFormBinding;
import com.example.gardenerhelperapplication.entities.PlantInGroundInfo;
import com.example.gardenerhelperapplication.presentation.DialogTitle;
import com.example.gardenerhelperapplication.presentation.MainActivity;
import com.example.gardenerhelperapplication.presentation.plantlists.dialogs.ChoosePeriodDialogFragment;
import com.example.gardenerhelperapplication.utils.DateCreator;
import com.example.gardenerhelperapplication.utils.FertilizeFrequencyConverter;
import com.example.gardenerhelperapplication.utils.FrequencyConverter;
import com.example.gardenerhelperapplication.utils.WaterFrequencyConverter;

import java.time.LocalDate;

public class EditPlantInGroundForm extends Fragment {
    public static final String CHOOSE_PERIOD_REQUEST_KEY = "EditPlantInGroundForm.CHOOSE_PERIOD_REQUEST_KEY";
    public static final String MY_PLANT_ID_KEY = "EditPlantInGroundForm.MY_PLANT_ID_KEY";
    public static final String MY_PLANT_NAME_KEY = "EditPlantInGroundForm.MY_PLANT_NAME_KEY";
    public static final String MY_PLANT_SORT_KEY = "EditPlantInGroundForm.MY_PLANT_SORT_KEY";
    private PlantInGroundAddEditFormBinding binding;
    private EditPlantInGroundFormViewModel viewModel;
    private FrequencyConverter waterFrequencyConverter = new WaterFrequencyConverter();
    private FrequencyConverter fertilizeFrequencyConverter = new FertilizeFrequencyConverter();
    private int id;
    private String plantName;
    private String plantSort;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getInt("PlantsInGroundList.PLANT_IN_GROUND_ID_KEY");
            plantName = getArguments().getString("PlantsInGroundList.PLANT_IN_GROUND_NAME_KEY");
            plantSort = getArguments().getString("PlantsInGroundList.PLANT_IN_GROUND_SORT_KEY");

        }
        if (savedInstanceState != null) {
            id = savedInstanceState.getInt(MY_PLANT_ID_KEY);
            plantName = savedInstanceState.getString(MY_PLANT_NAME_KEY);
            plantSort = savedInstanceState.getString(MY_PLANT_SORT_KEY);
        }

        getParentFragmentManager().setFragmentResultListener(CHOOSE_PERIOD_REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int typeOfDialog = result.getInt("TYPE_OF_DIALOG_KEY");
                String frequency = result.getString("PERIOD_DIALOG_RESULT_KEY");

                switch (typeOfDialog) {
                    case 4:
                        binding.editablePlantInGroundWaterFreq.setText(frequency);
                        break;
                    case 5:
                        binding.editablePlantInGroundFertilizeFreq.setText(frequency);
                        break;
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PlantInGroundAddEditFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.toolbarEditablePlantInGroundForm.setTitle(R.string.editing);
        binding.buttonSavePlantInGround.setText(R.string.save);
        binding.titleEditablePlantInGroundName.setText(plantName);
        binding.titleEditablePlantInGroundSort.setText(plantSort);

        viewModel = new ViewModelProvider(this).get(EditPlantInGroundFormViewModel.class);

        if (savedInstanceState == null) {
            viewModel.getPlantInGroundInfoById(id).observe(getViewLifecycleOwner(), new Observer<PlantInGroundInfo>() {
                @Override
                public void onChanged(PlantInGroundInfo plantInGroundInfo) {
                    if (plantInGroundInfo != null) {
                        setEditPlantInGroundsForm(plantInGroundInfo);
                    }
                }
            });
        }

        viewModel.getCloseFormResult().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean canCloseForm) {
                if (canCloseForm) {
                    closeEditPlantInGroundForm();
                }
            }
        });

        binding.editablePlantInGroundDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });

        binding.editablePlantInGroundWaterFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePeriodDialogFragment(DialogTitle.WATERING_FREQUENCY.getTypeOfDialog(), DialogTitle.WATERING_FREQUENCY.getDialogTitle());
            }
        });

        binding.editablePlantInGroundFertilizeFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePeriodDialogFragment(DialogTitle.FERTILIZATION_FREQUENCY.getTypeOfDialog(), DialogTitle.FERTILIZATION_FREQUENCY.getDialogTitle());
            }
        });

        binding.toolbarEditablePlantInGroundForm.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeEditPlantInGroundForm();
            }
        });

        binding.buttonSavePlantInGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPlantInGroundFormState formState = getFormState();
                viewModel.saveUpdatedPlantInGroundInfo(formState);
            }
        });
    }

    private void setEditPlantInGroundsForm(PlantInGroundInfo plantInGroundInfo) {
        String waterFreq = waterFrequencyConverter.frequencyToString(plantInGroundInfo.getWaterFreq());
        String fertilizeFreq = fertilizeFrequencyConverter.frequencyToString(plantInGroundInfo.getFertilizeFreq());
        binding.editablePlantInGroundDate.setText(plantInGroundInfo.getDatePlantedInGround());
        binding.editablePlantInGroundWaterFreq.setText(waterFreq);
        binding.editablePlantInGroundFertilizeFreq.setText(fertilizeFreq);
    }

    public EditPlantInGroundFormState getFormState() {
        String datePlantedInGround = String.valueOf(binding.editablePlantInGroundDate.getText());
        String waterFreq = String.valueOf(binding.editablePlantInGroundWaterFreq.getText());
        String fertilizeFreq = String.valueOf(binding.editablePlantInGroundFertilizeFreq.getText());

        return new EditPlantInGroundFormState(datePlantedInGround, waterFreq, fertilizeFreq);
    }

    private void openCalendar() {
        LocalDate curDate = LocalDate.now();

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = DateCreator.create(year, month, dayOfMonth);
                binding.editablePlantInGroundDate.setText(date);
            }
        }, curDate.getYear(), curDate.getMonthValue() - 1, curDate.getDayOfMonth());

        datePickerDialog.show();
    }

    /**
     * Открывает диалоговое окно для выбора периодичности полива или подкормки растения
     */
    private void showChoosePeriodDialogFragment(int typeOfDialog, String dialogTitle) {
        ChoosePeriodDialogFragment dialogFragment = ChoosePeriodDialogFragment.getInstance(typeOfDialog, dialogTitle, CHOOSE_PERIOD_REQUEST_KEY);
        dialogFragment.show(getParentFragmentManager(), "ChoosePeriodDialogFragment");
    }

    private void closeEditPlantInGroundForm() {
        ((MainActivity) requireActivity()).unlockDrawer();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(MY_PLANT_ID_KEY, id);
        outState.putString(MY_PLANT_NAME_KEY, plantName);
        outState.putString(MY_PLANT_SORT_KEY, plantSort);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
