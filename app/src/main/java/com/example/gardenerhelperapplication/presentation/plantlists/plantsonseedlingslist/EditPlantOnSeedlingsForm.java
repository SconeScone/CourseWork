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
import com.example.gardenerhelperapplication.entities.PlantOnSeedlingsInfo;
import com.example.gardenerhelperapplication.presentation.DialogTitle;
import com.example.gardenerhelperapplication.presentation.MainActivity;
import com.example.gardenerhelperapplication.presentation.plantlists.dialogs.ChoosePeriodDialogFragment;
import com.example.gardenerhelperapplication.utils.DateCreator;
import com.example.gardenerhelperapplication.utils.FertilizeFrequencyConverter;
import com.example.gardenerhelperapplication.utils.FrequencyConverter;
import com.example.gardenerhelperapplication.utils.WaterFrequencyConverter;

import java.time.LocalDate;

public class EditPlantOnSeedlingsForm extends Fragment {
    public static final String CHOOSE_PERIOD_REQUEST_KEY = "EditPlantOnSeedlingsForm.CHOOSE_PERIOD_REQUEST_KEY";
    public static final String MY_PLANT_ID_KEY = "EditPlantOnSeedlingsForm.MY_PLANT_ID_KEY";
    public static final String MY_PLANT_NAME_KEY = "EditPlantOnSeedlingsForm.MY_PLANT_NAME_KEY";
    public static final String MY_PLANT_SORT_KEY = "EditPlantOnSeedlingsForm.MY_PLANT_SORT_KEY";
    private PlantOnSeedlingsAddEditFormBinding binding;
    private EditPlantOnSeedlingsFormViewModel viewModel;
    private final FrequencyConverter waterFrequencyConverter = new WaterFrequencyConverter();
    private final FrequencyConverter fertilizeFrequencyConverter = new FertilizeFrequencyConverter();
    private int id; // идентификатор растения на рассаде
    private String plantName; // название растения
    private String plantSort; // сорт растения

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getInt("PlantsOnSeedlingsList.PLANT_ON_SEEDLINGS_ID_KEY");
            plantName = getArguments().getString("PlantsOnSeedlingsList.PLANT_ON_SEEDLINGS_NAME_KEY");
            plantSort = getArguments().getString("PlantsOnSeedlingsList.PLANT_ON_SEEDLINGS_SORT_KEY");
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
                        binding.editablePlantOnSeedlingsWaterFreq.setText(frequency);
                        break;
                    case 5:
                        binding.editablePlantOnSeedlingsFertilizeFreq.setText(frequency);
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

        binding.toolbarEditablePlantOnSeedlingsForm.setTitle(R.string.editing);
        binding.buttonSavePlantOnSeedlings.setText(R.string.save);
        binding.titleEditablePlantOnSeedlingsName.setText(plantName);
        binding.titleEditablePlantOnSeedlingsSort.setText(plantSort);

        viewModel = new ViewModelProvider(this).get(EditPlantOnSeedlingsFormViewModel.class);

        if (savedInstanceState == null) {
            viewModel.getPlantOnSeedlingsById(id).observe(getViewLifecycleOwner(), new Observer<PlantOnSeedlingsInfo>() {
                @Override
                public void onChanged(PlantOnSeedlingsInfo plantOnSeedlings) {
                    if (plantOnSeedlings != null) {
                        setEditPlantOnSeedlingsForm(plantOnSeedlings);
                    }
                }
            });
        }

        viewModel.getCloseFormResult().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean canCloseForm) {
                if (canCloseForm) {
                    closeEditPlantOnSeedlingsForm();
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
                closeEditPlantOnSeedlingsForm();
            }
        });

        binding.buttonSavePlantOnSeedlings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPlantOnSeedlingsFormState formState = getFormState();
                viewModel.saveUpdatedPlantOnSeedlingsInfo(formState);
            }
        });
    }

    private void setEditPlantOnSeedlingsForm(PlantOnSeedlingsInfo plantOnSeedlings) {
        String waterFreq = waterFrequencyConverter.frequencyToString(plantOnSeedlings.getWaterFreq());
        String fertilizeFreq = fertilizeFrequencyConverter.frequencyToString(plantOnSeedlings.getFertilizeFreq());
        binding.editablePlantOnSeedlingsDate.setText(plantOnSeedlings.getDateOnSeedlings());
        binding.editablePlantOnSeedlingsWaterFreq.setText(waterFreq);
        binding.editablePlantOnSeedlingsFertilizeFreq.setText(fertilizeFreq);
    }

    public EditPlantOnSeedlingsFormState getFormState() {
        String dateOnSeedlings = String.valueOf(binding.editablePlantOnSeedlingsDate.getText());
        String waterFreq = String.valueOf(binding.editablePlantOnSeedlingsWaterFreq.getText());
        String fertilizeFreq = String.valueOf(binding.editablePlantOnSeedlingsFertilizeFreq.getText());

        return new EditPlantOnSeedlingsFormState(dateOnSeedlings, waterFreq, fertilizeFreq);
    }

    /**
     * Открывает календарь для выбора даты посева на рассаду
     */
    private void openCalendar() {
        LocalDate curDate = LocalDate.now();

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = DateCreator.create(year, month, dayOfMonth);
                binding.editablePlantOnSeedlingsDate.setText(date);
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

    /**
     * Закрывает форму для редактирования системы ухода за растением на рассаде
     */
    private void closeEditPlantOnSeedlingsForm() {
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
