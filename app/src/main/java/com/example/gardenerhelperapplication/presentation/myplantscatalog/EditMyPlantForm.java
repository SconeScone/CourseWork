package com.example.gardenerhelperapplication.presentation.myplantscatalog;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.gardenerhelperapplication.databinding.MyPlantAddEditFormBinding;
import com.example.gardenerhelperapplication.entities.MyPlant;
import com.example.gardenerhelperapplication.presentation.DialogTitle;
import com.example.gardenerhelperapplication.presentation.MonthDecade;
import com.example.gardenerhelperapplication.presentation.myplantscatalog.dialogs.ChooseDateDialogFragment;
import com.example.gardenerhelperapplication.utils.ImageManager;
import com.example.gardenerhelperapplication.utils.ImagePicker;

import java.io.File;

public class EditMyPlantForm extends Fragment {
    public static final String EDIT_FORM_MY_PLANT_ID_KEY = "EditMyPlantForm.MY_PLANT_ID_KEY";
    public static final String EDIT_FORM_MY_PLANT_IMAGE_FILE_NAME_KEY = "EditMyPlantForm.MY_PLANT_IMAGE_FILE_NAME_KEY";
    public static final String CHOOSE_DATE_REQUEST_KEY = "EditMyPlantForm.CHOOSE_DATE_REQUEST_KEY";
    private MyPlantAddEditFormBinding binding;
    private EditMyPlantFormViewModel viewModel;
    private ImagePicker plantImagePicker;
    private final ImageManager imageManager = new ImageManager();
    private Bitmap plantImage = null; // Изображение растения
    private boolean isPlantImageEdit = false;
    private String myPlantImageFileName = null; // Название изображения растения
    private int plantId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            plantId = getArguments().getInt("MyPlantInfoFragment.MY_PLANT_ID_KEY");
            myPlantImageFileName = getArguments().getString("MyPlantInfoFragment.MY_PLANT_IMAGE_FILE_NAME_KEY");
        }
        if (savedInstanceState != null) {
            plantId = savedInstanceState.getInt(EDIT_FORM_MY_PLANT_ID_KEY);
            myPlantImageFileName = savedInstanceState.getString(EDIT_FORM_MY_PLANT_IMAGE_FILE_NAME_KEY);
        }

        plantImagePicker = new ImagePicker(requireActivity().getActivityResultRegistry(), this, new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri plantImageUri) {
                if (plantImageUri != null) {
                    binding.myPlantProgressBar.setVisibility(View.VISIBLE); // Делаем индикатор выполнения (progress bar) видимым, пока изображение загружается
                    binding.editableMyPlantImage.setImageBitmap(null);
                    binding.saveEditableMyPlantForm.setEnabled(false); // Пока изображение загружается, кнопка сохранения отредактированной информации недоступна

                    Glide.with(EditMyPlantForm.this).asBitmap().load(plantImageUri)
                            .skipMemoryCache(true)
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Bitmap> target, boolean isFirstResource) {
                                    binding.myPlantProgressBar.setVisibility(View.GONE);
                                    binding.saveEditableMyPlantForm.setEnabled(true);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(@NonNull Bitmap resource, @NonNull Object model, Target<Bitmap> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                                    binding.myPlantProgressBar.setVisibility(View.GONE); // Делаем индикатор выполнения (progress bar) невидимым
                                    plantImage = resource;
                                    isPlantImageEdit = true;
                                    binding.resetMyPlantImage.setEnabled(true);
                                    binding.downloadMyPlantImage.setEnabled(false);
                                    binding.saveEditableMyPlantForm.setEnabled(true); // Изображение загружено, кнопка сохранения сохранения отредактированной информации доступна

                                    return false;
                                }
                            }).into(binding.editableMyPlantImage);
                }
            }
        });

        getParentFragmentManager().setFragmentResultListener(CHOOSE_DATE_REQUEST_KEY, this, (requestKey, result) -> {
            int typeOfDialog = result.getInt("TYPE_OF_DIALOG_KEY", -1);
            MonthDecade date = (MonthDecade) result.getSerializable("DATE_DIALOG_RESULT_KEY");
            String dateToDisplay = date.getMonth() + " - " + date.getDecade() + " декада";

            switch (typeOfDialog) {
                case 1:
                    binding.editableMyPlantDateOnSeedlings.setText(dateToDisplay);
                    break;
                case 2:
                    binding.editableMyPlantDatePlantedInGround.setText(dateToDisplay);
                    break;
                case 3:
                    binding.editableMyPlanDateHarvesting.setText(dateToDisplay);
                    break;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MyPlantAddEditFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (myPlantImageFileName != null) {
            binding.resetMyPlantImage.setEnabled(true); // Кнопка сброса изображения доступна для нажатия
            binding.downloadMyPlantImage.setEnabled(false); // Кнопка установки изображения недоступна для нажатия

            File dir = imageManager.getPlantImagesDir(requireActivity().getApplicationContext());
            File plantImageFile = new File(dir, myPlantImageFileName);

            Glide.with(this)
                    .load(plantImageFile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.editableMyPlantImage);
        } else {
            binding.resetMyPlantImage.setEnabled(false); // Кнопка сброса изображения недоступна для нажатия
            binding.downloadMyPlantImage.setEnabled(true); // Кнопка установки изображения доступна для нажатия
        }

        viewModel = new ViewModelProvider(this).get(EditMyPlantFormViewModel.class);
        if (savedInstanceState == null) {
            viewModel.getMyPlantById(plantId).observe(getViewLifecycleOwner(), new Observer<MyPlant>() {
                @Override
                public void onChanged(MyPlant myPlant) {
                    if (myPlant != null) {
                        setMyPlantInfo(myPlant);
                    }
                }
            });
        }

        viewModel.getEditMyPlantFormState().observe(getViewLifecycleOwner(), new Observer<EditableMyPlantFormState>() {
            @Override
            public void onChanged(EditableMyPlantFormState formState) {
                if (formState != null) {
                    setAttentionMessage(formState);

                    if (formState.isValid()) {
                        Bundle result = new Bundle();
                        result.putBoolean("IS_EDITED_RESULT_KEY", true);
                        getParentFragmentManager().setFragmentResult("MyPlantInfoFragment.IS_EDITED_REQUEST_KEY", result);

                        closeEditMyPlantForm();
                    }
                }
            }
        });

        binding.editableMyPlantDateOnSeedlings.setOnClickListener(v -> showChooseDateDialogFragment(
                DialogTitle.DATE_ON_SEEDLINGS.getTypeOfDialog(),
                DialogTitle.DATE_ON_SEEDLINGS.getDialogTitle()));

        binding.editableMyPlantDatePlantedInGround.setOnClickListener(v -> showChooseDateDialogFragment(
                DialogTitle.DATE_PLANTED_IN_GROUND.getTypeOfDialog(),
                DialogTitle.DATE_PLANTED_IN_GROUND.getDialogTitle()));

        binding.editableMyPlanDateHarvesting.setOnClickListener(v -> showChooseDateDialogFragment(
                DialogTitle.DATE_HARVESTING.getTypeOfDialog(),
                DialogTitle.DATE_HARVESTING.getDialogTitle()));

        binding.saveEditableMyPlantForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditableMyPlantFormState formState = getEditMyPlantFormState();
                viewModel.updateMyPlantInfo(formState, isPlantImageEdit);
            }
        });

        binding.downloadMyPlantImage.setOnClickListener(new View.OnClickListener() { // Кнопка загрузки изображения
            @Override
            public void onClick(View v) {
                plantImagePicker.pickImageFromGallery(); // Загружаем изображение из галереи
            }
        });

        binding.resetMyPlantImage.setOnClickListener(new View.OnClickListener() { // Кнопка сброса изображения
            @Override
            public void onClick(View v) {
                plantImage = null; // При сбросе изображения пользователем, загруженное изображение == null
                isPlantImageEdit = true; // Устанавливаем флаг, что изображение было отредактировано
                binding.editableMyPlantImage.setImageBitmap(null);
                binding.downloadMyPlantImage.setEnabled(true);
                binding.resetMyPlantImage.setEnabled(false);
            }
        });

        // Установка фильтра на поле с кратким описанием (ввод не более 8 строк)
        binding.editableMyPlantDescription.addTextChangedListener(new CustomEditTextWatcher(binding.editableMyPlantDescription, 8));
        // Установка фильтра на поле с уходом (ввод не более 12 строк)
        binding.editableMyPlantCare.addTextChangedListener(new CustomEditTextWatcher(binding.editableMyPlantCare, 12));
        // Установка фильтра на поле с прочей информацией (ввод не более 10 строк)
        binding.editableMyPlantOtherInfo.addTextChangedListener(new CustomEditTextWatcher(binding.editableMyPlantOtherInfo, 10));

        binding.cancelEditableMyPlantForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    /**
     * Устанавливает сообщение для пользователя о правильности введенного им названия растения и сорта
     */
    private void setAttentionMessage(EditableMyPlantFormState formState) {
        binding.textInputLayoutEditableMyPlantName.setHelperText(formState.getPlantNameAttentionMessage());
        binding.textInputLayoutEditableMyPlantSort.setHelperText(formState.getPlantSortAttentionMessage());
    }

    /**
     * Устанавиливает информацию о растении, полученную из базы данных
     */
    private void setMyPlantInfo(MyPlant myPlant) {
        binding.editableMyPlantName.setText(myPlant.getPlantName());
        binding.editableMyPlantSort.setText(myPlant.getPlantSort());
        binding.editableMyPlantDateOnSeedlings.setText(getEmptyOrFillString(myPlant.getDateOnSeedlings()));
        binding.editableMyPlantDatePlantedInGround.setText(getEmptyOrFillString(myPlant.getDatePlantedInGround()));
        binding.editableMyPlanDateHarvesting.setText(getEmptyOrFillString(myPlant.getDateHarvesting()));
        binding.editableMyPlantDescription.setText(getEmptyOrFillString(myPlant.getDescription()));
        binding.editableMyPlantCare.setText(getEmptyOrFillString(myPlant.getCare()));
        binding.editableMyPlantOtherInfo.setText(getEmptyOrFillString(myPlant.getOtherInfo()));
    }

    private String getEmptyOrFillString(String string) {
        return (string == null) ? "" : string;
    }

    public EditableMyPlantFormState getEditMyPlantFormState() {
        String plantName = String.valueOf(binding.editableMyPlantName.getText());
        String plantSort = String.valueOf(binding.editableMyPlantSort.getText());
        Bitmap plantImage = this.plantImage; // Изображение растения
        String dateOnSeedlings = String.valueOf(binding.editableMyPlantDateOnSeedlings.getText());
        String datePlantedInGround = String.valueOf(binding.editableMyPlantDatePlantedInGround.getText());
        String dateHarvesting = String.valueOf(binding.editableMyPlanDateHarvesting.getText());
        String description = String.valueOf(binding.editableMyPlantDescription.getText());
        String care = String.valueOf(binding.editableMyPlantCare.getText());
        String otherInfo = String.valueOf(binding.editableMyPlantOtherInfo.getText());

        return new EditableMyPlantFormState(plantName, plantSort, plantImage, dateOnSeedlings, datePlantedInGround,
                dateHarvesting, description, care, otherInfo);
    }

    /**
     * Закрывает форму для редактирования информации о растении.
     */
    private void closeEditMyPlantForm() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void showChooseDateDialogFragment(int typeOfDialog, String dialogTitle) {
        ChooseDateDialogFragment dialogFragment = ChooseDateDialogFragment.getInstance(typeOfDialog, dialogTitle, CHOOSE_DATE_REQUEST_KEY);
        dialogFragment.show(getParentFragmentManager(), "ChooseDateDialogFragment");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(EDIT_FORM_MY_PLANT_ID_KEY, plantId);
        outState.putString(EDIT_FORM_MY_PLANT_IMAGE_FILE_NAME_KEY, myPlantImageFileName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
