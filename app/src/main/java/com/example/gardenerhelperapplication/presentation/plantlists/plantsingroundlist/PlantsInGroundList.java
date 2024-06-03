package com.example.gardenerhelperapplication.presentation.plantlists.plantsingroundlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gardenerhelperapplication.databinding.PlantsInGroundListBinding;
import com.example.gardenerhelperapplication.entities.PlantInGround;
import com.example.gardenerhelperapplication.presentation.MainActivity;
import com.example.gardenerhelperapplication.presentation.myplantscatalog.MyPlantsCatalog;
import com.example.gardenerhelperapplication.presentation.plantlists.OnPlantItemButtonsClickListener;
import com.example.gardenerhelperapplication.presentation.plantlists.dialogs.PlantInListConfirmDialogFragment;
import com.example.gardenerhelperapplication.utils.ImageManager;

import java.time.LocalDate;
import java.util.List;

public class PlantsInGroundList extends Fragment {
    public static final String CONFIRM_DIALOG_REQUEST_KEY = "PlantsInGroundList.CONFIRM_DIALOG_REQUEST_KEY";
    private PlantsInGroundListBinding binding;
    private PlantsInGroundListViewModel viewModel;
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

                    viewModel.deletePlantInGround(id, plantId); // удалаяем растение из списка растений, высаженных в грунт
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PlantsInGroundListBinding.inflate(inflater, container, false);

        listener = new OnPlantItemButtonsClickListener() {
            @Override
            public void onDeleteButtonClick(int id, int plantId) {
                showPlantInListConfirmDialogFragment(id, plantId); // Открыть модальное окно для подтверждения или отмены удаления растения, высаженного в грунт
            }

            @Override
            public void onEditButtonClick(int id, String plantName, String plantSort) {
                Bundle args = new Bundle();
                args.putInt("PlantsInGroundList.PLANT_IN_GROUND_ID_KEY", id);
                args.putString("PlantsInGroundList.PLANT_IN_GROUND_NAME_KEY", plantName);
                args.putString("PlantsInGroundList.PLANT_IN_GROUND_SORT_KEY", plantSort);

                ((MainActivity) requireActivity()).lockDrawer();
                replaceFragmentWithBackStack(container.getId(), EditPlantInGroundForm.class, args);
            }

            @Override
            public void onWaterButtonClick(int id, int waterFreq) {
                LocalDate curWaterDate = LocalDate.now();
                LocalDate nextWaterDate = curWaterDate.plusDays(waterFreq);

                viewModel.updatePlantInGroundWaterDate(id, curWaterDate, nextWaterDate);
            }

            @Override
            public void onFertilizeButtonClick(int id, int fertilizeFreq) {
                LocalDate curFertilizeDate = LocalDate.now();
                LocalDate nextFertilizeDate = curFertilizeDate.plusDays(fertilizeFreq);

                viewModel.updatePlantInGroundFertilizeDate(id, curFertilizeDate, nextFertilizeDate);
            }
        };

        binding.fabAddPlantInGround.setOnClickListener(new View.OnClickListener() {
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
        binding.toolbarPlantsInGroundList.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).openDrawer();
            }
        });

        binding.recyclerViewPlantsInGroundList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        PlantsInGroundListAdapter adapter = new PlantsInGroundListAdapter(listener, imageManager, requireActivity().getApplicationContext());
        binding.recyclerViewPlantsInGroundList.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(PlantsInGroundListViewModel.class);

        viewModel.getAllPlantsInGround().observe(getViewLifecycleOwner(), new Observer<List<PlantInGround>>() {
            @Override
            public void onChanged(List<PlantInGround> plantInGroundList) {
                adapter.setPlantsInGroundList(plantInGroundList);
                if (adapter.getItemCount() == 0) {
                    binding.recyclerViewPlantsInGroundList.setVisibility(View.GONE);
                    binding.emptyPlantsInGroundList.setVisibility(View.VISIBLE);
                } else {
                    if (binding.recyclerViewPlantsInGroundList.getVisibility() == View.GONE) {
                        binding.emptyPlantsInGroundList.setVisibility(View.GONE);
                        binding.recyclerViewPlantsInGroundList.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    /**
     * Заменяет фрагмент со списком растений, высаженных в грунт, на другой с добавлением текущего в стэк
     */
    private <T extends Fragment> void replaceFragmentWithBackStack(int containerId, Class<T> fragmentClass, Bundle args) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(containerId, fragmentClass, args);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Заменяет фрагмент со списком растений, высаженных в грунт, на другой (без добавления в стек)
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
        dialogFragment.show(getParentFragmentManager(), "PlantsInGroundListConfirmDialogFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listener = null;
        binding = null;
    }
}
