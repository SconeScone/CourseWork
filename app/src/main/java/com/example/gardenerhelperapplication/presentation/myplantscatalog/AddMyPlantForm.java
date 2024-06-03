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
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.gardenerhelperapplication.databinding.MyPlantAddEditFormBinding;
import com.example.gardenerhelperapplication.presentation.DialogTitle;
import com.example.gardenerhelperapplication.presentation.MainActivity;
import com.example.gardenerhelperapplication.presentation.MonthDecade;
import com.example.gardenerhelperapplication.presentation.myplantscatalog.dialogs.ChooseDateDialogFragment;
import com.example.gardenerhelperapplication.utils.ImagePicker;

public class AddMyPlantForm extends Fragment {
    private MyPlantAddEditFormBinding binding;
    private AddMyPlantFormViewModel viewModel;
    public static final String CHOOSE_DATE_REQUEST_KEY = "AddMyPlantForm.CHOOSE_DATE_REQUEST_KEY";
    private ImagePicker plantImagePicker;
    private Bitmap plantImage = null; // Изображение растения

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        plantImagePicker = new ImagePicker(requireActivity().getActivityResultRegistry(), this, new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri plantImageUri) {
                if (plantImageUri != null) {
                    binding.myPlantProgressBar.setVisibility(View.VISIBLE); // Делаем индикатор выполнения (progress bar) видимым, пока изображение загружается
                    binding.editableMyPlantImage.setImageBitmap(null);
                    binding.saveEditableMyPlantForm.setEnabled(false); // Пока изображение загружается, кнопка сохранения растения недоступна

                    Glide.with(AddMyPlantForm.this).asBitmap().load(plantImageUri).skipMemoryCache(true)
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
                                    binding.resetMyPlantImage.setEnabled(true);
                                    binding.downloadMyPlantImage.setEnabled(false);
                                    binding.saveEditableMyPlantForm.setEnabled(true); // Изображение загружено, кнопка сохранения растения доступна

                                    return false;
                                }
                            }).into(binding.editableMyPlantImage);
                }
            }
        });

        getParentFragmentManager().setFragmentResultListener(CHOOSE_DATE_REQUEST_KEY, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int typeOfDialog = result.getInt("TYPE_OF_DIALOG_KEY");
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

        binding.resetMyPlantImage.setEnabled(false); // Кнопка сброса изображения сначала недоступна для нажатия, т.к. изображение еще не загружено
        binding.downloadMyPlantImage.setEnabled(true); // Кнопка установки изображения сначала доступна для нажатия

        viewModel = new ViewModelProvider(this).get(AddMyPlantFormViewModel.class);
        viewModel.getAddMyPlantFormState().observe(getViewLifecycleOwner(), new Observer<EditableMyPlantFormState>() {
            @Override
            public void onChanged(EditableMyPlantFormState state) {
                if (state != null) {
                    setAttentionMessage(state);
                    if (state.isValid()) {
                        closeAddMyPlantForm();
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

        binding.saveEditableMyPlantForm.setOnClickListener(v -> {
            EditableMyPlantFormState state = getAddMyPlantFormState();
            viewModel.addMyPlant(state);
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
                closeAddMyPlantForm();
            }
        });
    }

    /**
     * Устанавливает сообщение для пользователя о правильности введенного им названия растения и сорта
     */
    private void setAttentionMessage(EditableMyPlantFormState state) {
        binding.textInputLayoutEditableMyPlantName.setHelperText(state.getPlantNameAttentionMessage());
        binding.textInputLayoutEditableMyPlantSort.setHelperText(state.getPlantSortAttentionMessage());
    }

    /**
     * Возвращает текущее состояние формы для добавления растения
     */
    public EditableMyPlantFormState getAddMyPlantFormState() {
        String plantName = String.valueOf(binding.editableMyPlantName.getText());
        String plantSort = String.valueOf(binding.editableMyPlantSort.getText());
        Bitmap plantImage = this.plantImage;
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
     * Открывает диалоговое окно для выбора даты (посева на рассаду, высадки в грунт, сбора урожая)
     */
    private void showChooseDateDialogFragment(int typeOfDialog, String dialogTitle) {
        ChooseDateDialogFragment dialogFragment = ChooseDateDialogFragment.getInstance(typeOfDialog, dialogTitle, CHOOSE_DATE_REQUEST_KEY);
        dialogFragment.show(getParentFragmentManager(), "ChooseDateDialogFragment");
    }

    /**
     * Закрывает форму для добаления растения. Разблокирует открывание NavigationDrawer по свайпу
     */
    private void closeAddMyPlantForm() {
        ((MainActivity) requireActivity()).unlockDrawer();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
