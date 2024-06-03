package com.example.gardenerhelperapplication.presentation.plantlists.plantsonseedlingslist;

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
import com.example.gardenerhelperapplication.databinding.PlantOnSeedlingsAddEditFormBinding;
import com.example.gardenerhelperapplication.presentation.DialogTitle;
import com.example.gardenerhelperapplication.presentation.MainActivity;
import com.example.gardenerhelperapplication.presentation.plantlists.dialogs.ChoosePeriodDialogFragment;
import com.example.gardenerhelperapplication.utils.DateCreator;

import java.time.LocalDate;

public class AddPlantOnSeedlingsForm extends Fragment {
    public static final String CHOOSE_PERIOD_REQUEST_KEY = "AddPlantOnSeedlingsForm.CHOOSE_PERIOD_REQUEST_KEY";
    public static final String MY_PLANT_ID_KEY = "AddPlantOnSeedlingsForm.MY_PLANT_ID_KEY";
    public static final String MY_PLANT_NAME_KEY = "AddPlantOnSeedlingsForm.MY_PLANT_NAME_KEY";
    public static final String MY_PLANT_SORT_KEY = "AddPlantOnSeedlingsForm.MY_PLANT_SORT_KEY";
    public static final String IS_ALL_INPUTS_SET_KEY = "AddPlantOnSeedlingsForm.IS_ALL_INPUTS_SET_KEY";
    private PlantOnSeedlingsAddEditFormBinding binding;
    private AddPlantOnSeedlingsFormViewModel viewModel;
    private int plantId;
    private String plantName;
    private String plantSort;
    private boolean isDateSet = false;
    private boolean isWaterFreqSet = false;
    private boolean isFertilizeFreqSet = false;
    private boolean isAllInputsSet = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            plantId = getArguments().getInt("MyPlantsCatalog.MY_PLANT_ID_KEY");
            plantName = getArguments().getString("MyPlantsCatalog.MY_PLANT_NAME_KEY");
            plantSort = getArguments().getString("MyPlantsCatalog.MY_PLANT_SORT_KEY");
        }

        if (savedInstanceState != null) {
            plantId = savedInstanceState.getInt(MY_PLANT_ID_KEY);
            plantName = savedInstanceState.getString(MY_PLANT_NAME_KEY);
            plantSort = savedInstanceState.getString(MY_PLANT_SORT_KEY);
            isAllInputsSet = savedInstanceState.getBoolean(IS_ALL_INPUTS_SET_KEY);
        }

        getParentFragmentManager().setFragmentResultListener(CHOOSE_PERIOD_REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int typeOfDialog = result.getInt("TYPE_OF_DIALOG_KEY", -1);
                String frequency = result.getString("PERIOD_DIALOG_RESULT_KEY");

                switch (typeOfDialog) {
                    case 4:
                        binding.editablePlantOnSeedlingsWaterFreq.setText(frequency);
                        isWaterFreqSet = true;
                        if (isDateSet && isWaterFreqSet && isFertilizeFreqSet) {
                            binding.buttonSavePlantOnSeedlings.setEnabled(true);
                            isAllInputsSet = true;
                        }
                        break;
                    case 5:
                        binding.editablePlantOnSeedlingsFertilizeFreq.setText(frequency);
                        isFertilizeFreqSet = true;
                        if (isDateSet && isWaterFreqSet && isFertilizeFreqSet) {
                            binding.buttonSavePlantOnSeedlings.setEnabled(true);
                            isAllInputsSet = true;
                        }
                        break;
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PlantOnSeedlingsAddEditFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSavePlantOnSeedlings.setEnabled(isAllInputsSet);
        binding.toolbarEditablePlantOnSeedlingsForm.setTitle(R.string.adding);
        binding.toolbarEditablePlantOnSeedlingsForm.setSubtitle(R.string.adding_plant_on_seedlings);
        binding.buttonSavePlantOnSeedlings.setText(R.string.add);
        binding.titleEditablePlantOnSeedlingsName.setText(plantName);
        binding.titleEditablePlantOnSeedlingsSort.setText(plantSort);

        viewModel = new ViewModelProvider(this).get(AddPlantOnSeedlingsFormViewModel.class);
        viewModel.getCloseFormResult().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean canCloseForm) {
                if (canCloseForm) {
                    closeAddPlantOnSeedlingsForm();
                }
            }
        });

        binding.editablePlantOnSeedlingsDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });

        binding.editablePlantOnSeedlingsWaterFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePeriodDialogFragment(DialogTitle.WATERING_FREQUENCY.getTypeOfDialog(), DialogTitle.WATERING_FREQUENCY.getDialogTitle());
            }
        });

        binding.editablePlantOnSeedlingsFertilizeFreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePeriodDialogFragment(DialogTitle.FERTILIZATION_FREQUENCY.getTypeOfDialog(), DialogTitle.FERTILIZATION_FREQUENCY.getDialogTitle());
            }
        });

        binding.toolbarEditablePlantOnSeedlingsForm.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAddPlantOnSeedlingsForm();
            }
        });

        binding.buttonSavePlantOnSeedlings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPlantOnSeedlingsFormState formState = getFormState();
                viewModel.addPlantOnSeedlings(formState);
            }
        });
    }

    private AddPlantOnSeedlingsFormState getFormState() {
        String dateOnSeedlings = String.valueOf(binding.editablePlantOnSeedlingsDate.getText());
        String waterFreq = String.valueOf(binding.editablePlantOnSeedlingsWaterFreq.getText());
        String fertilizeFreq = String.valueOf(binding.editablePlantOnSeedlingsFertilizeFreq.getText());

        return new AddPlantOnSeedlingsFormState(dateOnSeedlings, waterFreq, fertilizeFreq, plantId);
    }

    private void openCalendar() {
        LocalDate curDate = LocalDate.now();

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = DateCreator.create(year, month, dayOfMonth);
                binding.editablePlantOnSeedlingsDate.setText(date);

                isDateSet = true;
                if (isDateSet && isWaterFreqSet && isFertilizeFreqSet) {
                    binding.buttonSavePlantOnSeedlings.setEnabled(true);
                    isAllInputsSet = true;
                }

            }
        }, curDate.getYear(), curDate.getMonthValue() - 1, curDate.getDayOfMonth());
        datePickerDialog.show();
    }

    /**
     * Открывает диалоговое окно для выбора периодичности полива или подкормки
     */
    private void showChoosePeriodDialogFragment(int typeOfDialog, String dialogTitle) {
        ChoosePeriodDialogFragment dialogFragment = ChoosePeriodDialogFragment.getInstance(typeOfDialog, dialogTitle, CHOOSE_PERIOD_REQUEST_KEY);
        dialogFragment.show(getParentFragmentManager(), "ChoosePeriodDialogFragment");
    }

    private void closeAddPlantOnSeedlingsForm() {
        ((MainActivity) requireActivity()).unlockDrawer();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(MY_PLANT_ID_KEY, plantId);
        outState.putString(MY_PLANT_NAME_KEY, plantName);
        outState.putString(MY_PLANT_SORT_KEY, plantSort);
        outState.putBoolean(IS_ALL_INPUTS_SET_KEY, isAllInputsSet);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
