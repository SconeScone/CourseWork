package com.example.gardenerhelperapplication.presentation.myplantscatalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.gardenerhelperapplication.databinding.MyPlantItemBinding;
import com.example.gardenerhelperapplication.entities.Plant;
import com.example.gardenerhelperapplication.utils.ImageManager;

import java.io.File;
import java.util.List;

public class MyPlantsCatalogAdapter extends RecyclerView.Adapter<MyPlantsCatalogAdapter.MyPlantViewHolder> {
    private SortedList<Plant> myPlantsCatalog;
    private OnMyPlantItemClickListener listener;
    private ImageManager imageManager;
    private Context context;

    public interface OnMyPlantItemClickListener {
        public void onMyPlantItemClick(int plantId);

        public void onAddPlantOnSeedlingsButtonClick(int plantId, String plantName, String plantSort);

        public void onAddPlantInGroundButtonClick(int plantId, String plantName, String plantSort);
    }

    public MyPlantsCatalogAdapter(OnMyPlantItemClickListener listener, ImageManager imageManager, Context context) {
        this.listener = listener;
        this.imageManager = imageManager;
        this.context = context;

        myPlantsCatalog = new SortedList<>(Plant.class, new SortedList.Callback<Plant>() {
            @Override
            public int compare(Plant o1, Plant o2) {
                return Integer.compare(o2.getId(), o1.getId());
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Plant oldItem, Plant newItem) {
                boolean result = oldItem.equals(newItem);
                if (!result) {
                    return result;
                }
                result = (oldItem.getPlantImage() == null) && (newItem.getPlantImage() == null);
                return result;
            }

            @Override
            public boolean areItemsTheSame(Plant item1, Plant item2) {
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
    public MyPlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyPlantItemBinding binding = MyPlantItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyPlantViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPlantViewHolder holder, int position) {
        Plant myPlant = myPlantsCatalog.get(position);
        holder.bind(myPlant, listener, imageManager, context);
    }

    @Override
    public int getItemCount() {
        return myPlantsCatalog.size();
    }

    public void setMyPlantsCatalog(List<Plant> myPlantsCatalog) {
        this.myPlantsCatalog.replaceAll(myPlantsCatalog);
    }

    public static class MyPlantViewHolder extends RecyclerView.ViewHolder {
        private MyPlantItemBinding binding;

        public MyPlantViewHolder(MyPlantItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Plant myPlant, OnMyPlantItemClickListener listener, ImageManager imageManager, Context context) {
            if (myPlant.getPlantImage() != null) { // Изображение растения НЕ null
                File dir = imageManager.getPlantImagesDir(context);
                File plantImageFile = new File(dir, myPlant.getPlantImage());

                Glide.with(binding.getRoot().getRootView()).load(plantImageFile)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(binding.myPlantImageItem);
            } else { // Изображение растения null
                binding.myPlantImageItem.setImageBitmap(null);
            }

            binding.myPlantIdItem.setText(String.valueOf(myPlant.getId()));
            binding.myPlantNameItem.setText(myPlant.getPlantName());
            binding.myPlantSortItem.setText(myPlant.getPlantSort());

            binding.myPlantAddOnSeedlingsButtonItem.setEnabled(!myPlant.isOnSeedlings());
            binding.myPlantAddPlantInGroundButtonItem.setEnabled(!myPlant.isPlantInGround());

            binding.myPlantAddOnSeedlingsButtonItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAddPlantOnSeedlingsButtonClick(myPlant.getId(), myPlant.getPlantName(), myPlant.getPlantSort());
                }
            });
            binding.myPlantAddPlantInGroundButtonItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAddPlantInGroundButtonClick(myPlant.getId(), myPlant.getPlantName(), myPlant.getPlantSort());
                }
            });
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMyPlantItemClick(myPlant.getId());
                }
            });
        }
    }
}
