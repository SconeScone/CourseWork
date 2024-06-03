package com.example.gardenerhelperapplication.presentation.plantlists.plantsingroundlist;

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
import com.example.gardenerhelperapplication.databinding.PlantInGroundItemBinding;
import com.example.gardenerhelperapplication.entities.PlantInGround;
import com.example.gardenerhelperapplication.presentation.plantlists.OnPlantItemButtonsClickListener;
import com.example.gardenerhelperapplication.utils.DateDiffCalculator;
import com.example.gardenerhelperapplication.utils.ImageManager;

import java.io.File;
import java.util.List;

public class PlantsInGroundListAdapter extends RecyclerView.Adapter<PlantsInGroundListAdapter.PlantInGroundViewHolder> {
    private SortedList<PlantInGround> plantsInGroundList;
    private OnPlantItemButtonsClickListener listener;
    private ImageManager imageManager;
    private Context context;

    public PlantsInGroundListAdapter(OnPlantItemButtonsClickListener listener, ImageManager imageManager, Context context) {
        this.listener = listener;
        this.imageManager = imageManager;
        this.context = context;

        plantsInGroundList = new SortedList<>(PlantInGround.class, new SortedList.Callback<PlantInGround>() {
            @Override
            public int compare(PlantInGround o1, PlantInGround o2) {
                return Integer.compare(o2.getId(), o1.getId());
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(PlantInGround oldItem, PlantInGround newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(PlantInGround item1, PlantInGround item2) {
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
    public PlantInGroundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlantInGroundItemBinding binding = PlantInGroundItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new PlantInGroundViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantInGroundViewHolder holder, int position) {
        PlantInGround plantInGround = plantsInGroundList.get(position);
        holder.bind(plantInGround, listener, imageManager, context);
    }

    @Override
    public int getItemCount() {
        return plantsInGroundList.size();
    }

    public void setPlantsInGroundList(List<PlantInGround> plantsInGroundList) {
        this.plantsInGroundList.replaceAll(plantsInGroundList);
    }

    public static class PlantInGroundViewHolder extends RecyclerView.ViewHolder {
        private PlantInGroundItemBinding binding;

        public PlantInGroundViewHolder(PlantInGroundItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PlantInGround plantInGround, OnPlantItemButtonsClickListener listener, ImageManager imageManager, Context context) {

            if (plantInGround.getPlantImage() != null) { // Изображение растения НЕ null
                File dir = imageManager.getPlantImagesDir(context);
                File plantImageFile = new File(dir, plantInGround.getPlantImage());

                Glide.with(binding.getRoot().getRootView()).load(plantImageFile)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(binding.plantInGroundImageItem);
            } else { // Изображение растения null
                binding.plantInGroundImageItem.setImageBitmap(null);
            }

            binding.plantInGroundNameItem.setText(plantInGround.getPlantName());
            binding.plantInGroundSortItem.setText(plantInGround.getPlantSort());
            binding.plantInGroundDateItem.setText(plantInGround.getDatePlantedInGround());

            DateDiffCalculator.DateDiffResult dateDiffWateringResult = DateDiffCalculator.calcRemDaysBeforePlantCare(
                    plantInGround.getNextWaterDate(),
                    context.getResources().getString(R.string.it_is_time_to_water),
                    context.getResources().getString(R.string.water_in_some_days));
            binding.buttonWaterPlantInGround.setEnabled(dateDiffWateringResult.isItTimeToPlantCare());
            binding.waterInPlantInGroundItem.setText(dateDiffWateringResult.getResultMessage());

            DateDiffCalculator.DateDiffResult dateDiffFertilizingResult = DateDiffCalculator.calcRemDaysBeforePlantCare(
                    plantInGround.getNextFertilizeDate(),
                    context.getResources().getString(R.string.it_is_time_to_fertilize),
                    context.getResources().getString(R.string.fertilize_in_some_days));
            binding.buttonFertilizePlantInGround.setEnabled(dateDiffFertilizingResult.isItTimeToPlantCare());
            binding.fertilizeInPlantInGroundItem.setText(dateDiffFertilizingResult.getResultMessage());

            binding.buttonWaterPlantInGround.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onWaterButtonClick(plantInGround.getId(), plantInGround.getWaterFreq());
                }
            });

            binding.buttonFertilizePlantInGround.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFertilizeButtonClick(plantInGround.getId(), plantInGround.getFertilizeFreq());
                }
            });

            binding.menuEditDeletePlantInGround.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMenu(v, plantInGround, listener);
                }
            });
        }

        private void showMenu(View v, PlantInGround plantInGround, OnPlantItemButtonsClickListener listener) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.menu_delete_edit);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.menuEdit) {
                        listener.onEditButtonClick(plantInGround.getId(), plantInGround.getPlantName(), plantInGround.getPlantSort());
                        return true;
                    }
                    if (id == R.id.menuDelete) {
                        listener.onDeleteButtonClick(plantInGround.getId(), plantInGround.getPlantId());
                        return true;
                    }
                    return false;
                }
            });

            popupMenu.show();
        }
    }
}
