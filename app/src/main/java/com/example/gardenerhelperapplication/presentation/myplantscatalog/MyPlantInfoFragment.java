package com.example.gardenerhelperapplication.presentation.myplantscatalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.gardenerhelperapplication.R;
import com.example.gardenerhelperapplication.databinding.MyPlantInfoBinding;
import com.example.gardenerhelperapplication.entities.MyPlant;
import com.example.gardenerhelperapplication.presentation.MainActivity;
import com.example.gardenerhelperapplication.presentation.myplantscatalog.dialogs.MyPlantConfirmDialogFragment;
import com.example.gardenerhelperapplication.utils.ImageManager;

import java.io.File;

public class MyPlantInfoFragment extends Fragment {
    public static final String MY_PLANT_ID_KEY = "MyPlantInfoFragment.MY_PLANT_ID_KEY";
    public static final String MY_PLANT_IMAGE_PATH_KEY = "MyPlantInfoFragment.MY_PLANT_IMAGE_PATH_KEY";
    public static final String IS_EDITED_REQUEST_KEY = "MyPlantInfoFragment.IS_EDITED_REQUEST_KEY";
    public static final String CONFIRM_DIALOG_REQUEST_KEY = "MyPlantInfoFragment.CONFIRM_DIALOG_REQUEST_KEY";
    private MyPlantInfoBinding binding;
    private MyPlantInfoFragmentViewModel viewModel;
    private final ImageManager imageManager = new ImageManager();
    private String myPlantImageFileName = null;
    private int plantId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            plantId = getArguments().getInt("MyPlantsCatalog.MY_PLANT_ID_KEY");
        }

        if (savedInstanceState != null) {
            plantId = savedInstanceState.getInt(MY_PLANT_ID_KEY);
            myPlantImageFileName = savedInstanceState.getString(MY_PLANT_IMAGE_PATH_KEY);
        }

        getParentFragmentManager().setFragmentResultListener(IS_EDITED_REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (result.getBoolean("IS_EDITED_RESULT_KEY")) {
                    viewModel.loadMyPlantById(plantId);
                }
            }
        });

        getParentFragmentManager().setFragmentResultListener(CONFIRM_DIALOG_REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (result.getBoolean("MY_PLANT_CONFIRM_DIALOG_RESULT_KEY")) {
                    viewModel.deleteMyPlantById(plantId, myPlantImageFileName);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MyPlantInfoBinding.inflate(inflater, container, false);

        binding.toolbarMyPlantInfo.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menuEditMyPlantInfo) {
                    Bundle args = new Bundle();
                    args.putInt("MyPlantInfoFragment.MY_PLANT_ID_KEY", plantId);
                    args.putString("MyPlantInfoFragment.MY_PLANT_IMAGE_FILE_NAME_KEY", myPlantImageFileName);

                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.setReorderingAllowed(true);
                    transaction.replace(container.getId(), EditMyPlantForm.class, args);
                    transaction.addToBackStack(null);
                    transaction.commit();

                    return true;
                }
                if (item.getItemId() == R.id.menuDeleteMyPlantInfo) {
                    showMyPlantConfirmDialogFragment();

                    return true;
                }
                return false;
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(MyPlantInfoFragmentViewModel.class);
        viewModel.getMyPlantById(plantId).observe(getViewLifecycleOwner(), new Observer<MyPlant>() {
            @Override
            public void onChanged(MyPlant myPlant) {
                if (myPlant != null) {
                    setMyPlantInfo(myPlant);
                }
            }
        });

        viewModel.getMyPlantIsDeletedResult().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean myPlantDeleteResult) {
                if (myPlantDeleteResult) {
                    closeMyPlantInfoFragment();
                }
            }
        });

        binding.toolbarMyPlantInfo.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMyPlantInfoFragment();
            }
        });
    }

    /**
     * Устанавливает информацию о растении
     */
    private void setMyPlantInfo(MyPlant myPlant) {

        if (myPlant.getPlantImage() != null) {
            File dir = imageManager.getPlantImagesDir(requireActivity().getApplicationContext());
            File plantImageFile = new File(dir, myPlant.getPlantImage());
            myPlantImageFileName = myPlant.getPlantImage();

            Glide.with(this)
                    .load(plantImageFile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.myPlantImageInfo);
        } else {
            myPlantImageFileName = null;
        }

        binding.myPlantNameInfo.setText(myPlant.getPlantName());
        binding.myPlantSortInfo.setText(myPlant.getPlantSort());
        binding.myPlantDateOnSeedlingsInfo.setText(getEmptyOrFillString(myPlant.getDateOnSeedlings()));
        binding.myPlantDatePlantedInGroundInfo.setText(getEmptyOrFillString(myPlant.getDatePlantedInGround()));
        binding.myPlantDateHarvestingInfo.setText(getEmptyOrFillString(myPlant.getDateHarvesting()));
        binding.myPlantDescriptionInfo.setText(getEmptyOrFillString(myPlant.getDescription()));
        binding.myPlantCareInfo.setText(getEmptyOrFillString(myPlant.getCare()));
        binding.myPlantOtherInfInfo.setText(getEmptyOrFillString(myPlant.getOtherInfo()));
    }

    private String getEmptyOrFillString(String string) {
        return (string == null) ? "Отсутствует" : string;
    }

    /**
     * Закрывает фрагмент с информацией о растении
     */
    private void closeMyPlantInfoFragment() {
        ((MainActivity) requireActivity()).unlockDrawer();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    /**
     * Открывает модальное окно для подтверждения или отмены удаления растения из личного каталога
     */
    private void showMyPlantConfirmDialogFragment() {
        MyPlantConfirmDialogFragment dialogFragment = MyPlantConfirmDialogFragment.getInstance(CONFIRM_DIALOG_REQUEST_KEY);
        dialogFragment.show(getParentFragmentManager(), "MyPlantConfirmDialogFragment");
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(MY_PLANT_ID_KEY, plantId);
        outState.putString(MY_PLANT_IMAGE_PATH_KEY, myPlantImageFileName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
