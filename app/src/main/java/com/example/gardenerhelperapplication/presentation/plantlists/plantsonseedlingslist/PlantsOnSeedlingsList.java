package com.example.gardenerhelperapplication.presentation.plantlists.plantsonseedlingslist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gardenerhelperapplication.databinding.PlantsOnSeedlingsListBinding;
import com.example.gardenerhelperapplication.entities.PlantOnSeedlings;
import com.example.gardenerhelperapplication.presentation.MainActivity;
import com.example.gardenerhelperapplication.presentation.myplantscatalog.MyPlantsCatalog;
import com.example.gardenerhelperapplication.presentation.plantlists.OnPlantItemButtonsClickListener;
import com.example.gardenerhelperapplication.presentation.plantlists.dialogs.PlantInListConfirmDialogFragment;
import com.example.gardenerhelperapplication.utils.ImageManager;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.List;

public class PlantsOnSeedlingsList extends Fragment {
    public static final String CONFIRM_DIALOG_REQUEST_KEY = "PlantsOnSeedlingsList.CONFIRM_DIALOG_REQUEST_KEY";
    private PlantsOnSeedlingsListBinding binding;
    private PlantsOnSeedlingsListViewModel viewModel;
    private OnPlantItemButtonsClickListener listener;
    private final ImageManager imageManager = new ImageManager();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener(CONFIRM_DIALOG_REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (result.getBoolean("PLANT_IN_LIST_CONFIRM_DIALOG_RESULT_KEY")) {
                    int id = result.getInt("PLANT_IN_LIST_ID");
                    int plantId = result.getInt("MY_PLANT_ID");

                    viewModel.deletePlantOnSeedlings(id, plantId);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PlantsOnSeedlingsListBinding.inflate(inflater, container, false);

        listener = new OnPlantItemButtonsClickListener() {
            @Override
            public void onDeleteButtonClick(int id, int plantId) {
                showPlantInListConfirmDialogFragment(id, plantId); // Открыть модальное окно для подтверждения или отмены удаления растения на рассаде
            }

            @Override
            public void onEditButtonClick(int id, String plantName, String plantSort) {
                Bundle args = new Bundle();
                args.putInt("PlantsOnSeedlingsList.PLANT_ON_SEEDLINGS_ID_KEY", id);
                args.putString("PlantsOnSeedlingsList.PLANT_ON_SEEDLINGS_NAME_KEY", plantName);
                args.putString("PlantsOnSeedlingsList.PLANT_ON_SEEDLINGS_SORT_KEY", plantSort);

                ((MainActivity) requireActivity()).lockDrawer();
                replaceFragmentWithBackStack(container.getId(), EditPlantOnSeedlingsForm.class, args);
            }

            @Override
            public void onWaterButtonClick(int id, int waterFreq) {
                LocalDate curWaterDate = LocalDate.now();
                LocalDate nextWaterDate = curWaterDate.plusDays(waterFreq);

                viewModel.updatePlantOnSeedlingsWaterDate(id, curWaterDate, nextWaterDate);
            }

            @Override
            public void onFertilizeButtonClick(int id, int fertilizeFreq) {
                LocalDate curFertilizeDate = LocalDate.now();
                LocalDate nextFertilizeDate = curFertilizeDate.plusDays(fertilizeFreq);

                viewModel.updatePlantOnSeedlingsFertilizeDate(id, curFertilizeDate, nextFertilizeDate);
            }
        };

        binding.fabAddPlantOnSeedlings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(container.getId(), MyPlantsCatalog.class);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) requireActivity()).unlockDrawer();
        binding.toolbarPlantsOnSeedlingsList.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).openDrawer();
            }
        });

        binding.recyclerViewPlantsOnSeedlings.setLayoutManager(new LinearLayoutManager(requireActivity()));
        PlantsOnSeedlingsListAdapter adapter = new PlantsOnSeedlingsListAdapter(listener, imageManager, requireActivity().getApplicationContext());
        binding.recyclerViewPlantsOnSeedlings.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(PlantsOnSeedlingsListViewModel.class);

        viewModel.getAllPlantsOnSeedlings().observe(getViewLifecycleOwner(), new Observer<List<PlantOnSeedlings>>() {
            @Override
            public void onChanged(List<PlantOnSeedlings> plantsOnSeedlingsList) {
                adapter.setPlantsOnSeedlings(plantsOnSeedlingsList);
                if (adapter.getItemCount() == 0) {
                    binding.recyclerViewPlantsOnSeedlings.setVisibility(View.GONE);
                    binding.emptyPlantsOnSeedlings.setVisibility(View.VISIBLE);
                } else {
                    if (binding.recyclerViewPlantsOnSeedlings.getVisibility() == View.GONE) {
                        binding.emptyPlantsOnSeedlings.setVisibility(View.GONE);
                        binding.recyclerViewPlantsOnSeedlings.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    /**
     * Заменяет фрагмент со списком растений на рассаде на другой с добавлением текущего в стэк
     */
    private <T extends Fragment> void replaceFragmentWithBackStack(int containerId, Class<T> fragmentClass, Bundle args) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(containerId, fragmentClass, args);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Заменяет фрагмент со списком растений на рассаде на другой (без добавления в стек)
     */
    private <T extends Fragment> void replaceFragment(int containerId, Class<T> fragmentClass) {
        ((MainActivity) requireActivity()).setNavigationMenuItem();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(containerId, fragmentClass, null);
        transaction.commit();
    }

    /**
     * Открывает диалоговое окно для выбора периодичности полива или подкормки
     */
    private void showPlantInListConfirmDialogFragment(int id, int plantId) {
        PlantInListConfirmDialogFragment dialogFragment = PlantInListConfirmDialogFragment.getInstance(CONFIRM_DIALOG_REQUEST_KEY, id, plantId);
        dialogFragment.show(getParentFragmentManager(), "PlantsOnSeedlingsListConfirmDialogFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listener = null;
        binding = null;
    }
}
