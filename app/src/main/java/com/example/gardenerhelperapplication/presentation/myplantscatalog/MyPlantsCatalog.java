package com.example.gardenerhelperapplication.presentation.myplantscatalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.gardenerhelperapplication.databinding.MyPlantsCatalogBinding;
import com.example.gardenerhelperapplication.entities.Plant;
import com.example.gardenerhelperapplication.presentation.MainActivity;
import com.example.gardenerhelperapplication.presentation.plantlists.plantsingroundlist.AddPlantInGroundForm;
import com.example.gardenerhelperapplication.presentation.plantlists.plantsonseedlingslist.AddPlantOnSeedlingsForm;
import com.example.gardenerhelperapplication.utils.ImageManager;

import java.util.List;

public class MyPlantsCatalog extends Fragment {
    private MyPlantsCatalogBinding binding;
    private MyPlantsCatalogViewModel viewModel;
    private MyPlantsCatalogAdapter.OnMyPlantItemClickListener listener;
    private final ImageManager imageManager = new ImageManager();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MyPlantsCatalogBinding.inflate(inflater, container, false);

        binding.fabAddMyPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragmentWithBackStack(container.getId(), AddMyPlantForm.class, null);
                ((MainActivity) requireActivity()).lockDrawer();
            }
        });

        listener = new MyPlantsCatalogAdapter.OnMyPlantItemClickListener() {
            @Override
            public void onMyPlantItemClick(int plantId) {
                Bundle args = new Bundle();
                args.putInt("MyPlantsCatalog.MY_PLANT_ID_KEY", plantId);

                ((MainActivity) requireActivity()).lockDrawer();
                replaceFragmentWithBackStack(container.getId(), MyPlantInfoFragment.class, args);
            }

            @Override
            public void onAddPlantOnSeedlingsButtonClick(int plantId, String plantName, String plantSort) {
                Bundle args = new Bundle();
                args.putInt("MyPlantsCatalog.MY_PLANT_ID_KEY", plantId);
                args.putString("MyPlantsCatalog.MY_PLANT_NAME_KEY", plantName);
                args.putString("MyPlantsCatalog.MY_PLANT_SORT_KEY", plantSort);

                ((MainActivity) requireActivity()).lockDrawer();
                replaceFragmentWithBackStack(container.getId(), AddPlantOnSeedlingsForm.class, args);
            }

            @Override
            public void onAddPlantInGroundButtonClick(int plantId, String plantName, String plantSort) {
                Bundle args = new Bundle();
                args.putInt("MyPlantsCatalog.MY_PLANT_ID_KEY", plantId);
                args.putString("MyPlantsCatalog.MY_PLANT_NAME_KEY", plantName);
                args.putString("MyPlantsCatalog.MY_PLANT_SORT_KEY", plantSort);

                ((MainActivity) requireActivity()).lockDrawer();
                replaceFragmentWithBackStack(container.getId(), AddPlantInGroundForm.class, args);
            }
        };

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) requireActivity()).unlockDrawer();
        binding.toolbarMyPlantsCatalog.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).openDrawer();
            }
        });

        binding.recyclerViewMyPlants.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        MyPlantsCatalogAdapter adapter = new MyPlantsCatalogAdapter(listener, imageManager, requireActivity().getApplicationContext());
        binding.recyclerViewMyPlants.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MyPlantsCatalogViewModel.class);
        viewModel.getAllMyPlants().observe(getViewLifecycleOwner(), new Observer<List<Plant>>() {
            @Override
            public void onChanged(List<Plant> myPlants) {
                adapter.setMyPlantsCatalog(myPlants);
                if (adapter.getItemCount() == 0) {
                    binding.recyclerViewMyPlants.setVisibility(View.GONE);
                    binding.emptyMyPlantsCatalog.setVisibility(View.VISIBLE);
                } else {
                    if (binding.recyclerViewMyPlants.getVisibility() == View.GONE) {
                        binding.emptyMyPlantsCatalog.setVisibility(View.GONE);
                        binding.recyclerViewMyPlants.setVisibility(View.VISIBLE);
                    }
                    binding.recyclerViewMyPlants.scrollToPosition(0);
                }
            }
        });
    }

    /**
     * Заменяет фрагмент с каталогом растений на фрагмент с информацией о конкретном растении
     * или на форму для добавления нового растения
     */
    private <T extends Fragment> void replaceFragmentWithBackStack(int containerId, Class<T> fragmentClass, Bundle args) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(containerId, fragmentClass, args);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listener = null;
        binding = null;
    }
}