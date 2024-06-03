package com.example.gardenerhelperapplication.presentation.plantlists.plantsonseedlingslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.gardenerhelperapplication.R;
import com.example.gardenerhelperapplication.databinding.PlantOnSeedlingsItemBinding;
import com.example.gardenerhelperapplication.entities.PlantOnSeedlings;
import com.example.gardenerhelperapplication.presentation.plantlists.OnPlantItemButtonsClickListener;
import com.example.gardenerhelperapplication.utils.DateDiffCalculator;
import com.example.gardenerhelperapplication.utils.ImageManager;

import java.io.File;
import java.util.List;

public class PlantsOnSeedlingsListAdapter extends RecyclerView.Adapter<PlantsOnSeedlingsListAdapter.PlantOnSeedlingsViewHolder> {
    private SortedList<PlantOnSeedlings> plantsOnSeedlings;
    private OnPlantItemButtonsClickListener listener;
    private ImageManager imageManager;
    private Context context;

    public PlantsOnSeedlingsListAdapter(OnPlantItemButtonsClickListener listener, ImageManager imageManager, Context context) {
        this.listener = listener;
        this.imageManager = imageManager;
        this.context = context;

        plantsOnSeedlings = new SortedList<>(PlantOnSeedlings.class, new SortedList.Callback<PlantOnSeedlings>() {
            @Override
            public int compare(PlantOnSeedlings o1, PlantOnSeedlings o2) {
                return Integer.compare(o2.getId(), o1.getId());
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(PlantOnSeedlings oldItem, PlantOnSeedlings newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(PlantOnSeedlings item1, PlantOnSeedlings item2) {
                return item1.getId() == item2.getId();
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    @NonNull
    @Override
    public PlantOnSeedlingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlantOnSeedlingsItemBinding binding = PlantOnSeedlingsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new PlantOnSeedlingsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantOnSeedlingsViewHolder holder, int position) {
        PlantOnSeedlings plantOnSeedlings = plantsOnSeedlings.get(position);
        holder.bind(plantOnSeedlings, listener, imageManager, context);
    }

    @Override
    public int getItemCount() {
        return plantsOnSeedlings.size();
    }

    public void setPlantsOnSeedlings(List<PlantOnSeedlings> plantsOnSeedlingsList) {
        this.plantsOnSeedlings.replaceAll(plantsOnSeedlingsList);
    }

    public static class PlantOnSeedlingsViewHolder extends RecyclerView.ViewHolder {
        private PlantOnSeedlingsItemBinding binding;

        public PlantOnSeedlingsViewHolder(PlantOnSeedlingsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PlantOnSeedlings plantOnSeedlings, OnPlantItemButtonsClickListener listener, ImageManager imageManager, Context context) {

            if (plantOnSeedlings.getPlantImage() != null) { // Изображение растения НЕ null
                File dir = imageManager.getPlantImagesDir(context);
                File plantImageFile = new File(dir, plantOnSeedlings.getPlantImage());

                Glide.with(binding.getRoot().getRootView()).load(plantImageFile)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(binding.plantOnSeedlingsImageItem);
            } else { // Изображение растения null
                binding.plantOnSeedlingsImageItem.setImageBitmap(null);
            }

            binding.plantOnSeedlingsIdItem.setText(String.valueOf(plantOnSeedlings.getId()));
            binding.plantOnSeedlingsNameItem.setText(plantOnSeedlings.getPlantName());
            binding.plantOnSeedlingsSortItem.setText(plantOnSeedlings.getPlantSort());
            binding.plantOnSeedlingsDateItem.setText(plantOnSeedlings.getDateOnSeedlings());

            DateDiffCalculator.DateDiffResult dateDiffWateringResult = DateDiffCalculator.calcRemDaysBeforePlantCare(
                    plantOnSeedlings.getNextWaterDate(),
                    context.getResources().getString(R.string.it_is_time_to_water),
                    context.getResources().getString(R.string.water_in_some_days));
            binding.buttonWaterPlantOnSeedlings.setEnabled(dateDiffWateringResult.isItTimeToPlantCare());
            binding.waterInPlantOnSeedlingsItem.setText(dateDiffWateringResult.getResultMessage());

            DateDiffCalculator.DateDiffResult dateDiffFertilizingResult = DateDiffCalculator.calcRemDaysBeforePlantCare(
                    plantOnSeedlings.getNextFertilizeDate(),
                    context.getResources().getString(R.string.it_is_time_to_fertilize),
                    context.getResources().getString(R.string.fertilize_in_some_days));
            binding.buttonFertilizePlantOnSeedlings.setEnabled(dateDiffFertilizingResult.isItTimeToPlantCare());
            binding.fertilizeInPlantOnSeedlingsItem.setText(dateDiffFertilizingResult.getResultMessage());


            binding.buttonWaterPlantOnSeedlings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onWaterButtonClick(plantOnSeedlings.getId(), plantOnSeedlings.getWaterFreq());
                }
            });

            binding.buttonFertilizePlantOnSeedlings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFertilizeButtonClick(plantOnSeedlings.getId(), plantOnSeedlings.getFertilizeFreq());
                }
            });

            binding.menuEditDeletePlantOnSeedlings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMenu(v, plantOnSeedlings, listener);
                }
            });
        }

        private void showMenu(View v, PlantOnSeedlings plantOnSeedlings, OnPlantItemButtonsClickListener listener) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.menu_delete_edit);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.menuEdit) {
                        listener.onEditButtonClick(plantOnSeedlings.getId(), plantOnSeedlings.getPlantName(), plantOnSeedlings.getPlantSort());
                        return true;
                    }
                    if (id == R.id.menuDelete) {
                        listener.onDeleteButtonClick(plantOnSeedlings.getId(), plantOnSeedlings.getPlantId());
                        return true;
                    }
                    return false;
                }
            });

            popupMenu.show();
        }
    }
}
