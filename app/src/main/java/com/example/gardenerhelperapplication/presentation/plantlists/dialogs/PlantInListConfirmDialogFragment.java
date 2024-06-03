package com.example.gardenerhelperapplication.presentation.plantlists.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.gardenerhelperapplication.R;

public class PlantInListConfirmDialogFragment extends DialogFragment {

    public static PlantInListConfirmDialogFragment getInstance(String requestKey, int id, int plantId) {
        PlantInListConfirmDialogFragment dialogFragment = new PlantInListConfirmDialogFragment();

        Bundle args = new Bundle();
        args.putString("REQUEST_KEY", requestKey);
        args.putInt("PLANT_IN_LIST_ID", id);
        args.putInt("MY_PLANT_ID", plantId);
        dialogFragment.setArguments(args);

        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String requestKey = getArguments().getString("REQUEST_KEY");
        int id = getArguments().getInt("PLANT_IN_LIST_ID");
        int plantId = getArguments().getInt("MY_PLANT_ID");

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.plant_in_list_deletion_title)
                .setMessage(R.string.plant_in_list_deletion_message)
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle result = new Bundle();
                        result.putBoolean("PLANT_IN_LIST_CONFIRM_DIALOG_RESULT_KEY", true);
                        result.putInt("PLANT_IN_LIST_ID", id);
                        result.putInt("MY_PLANT_ID", plantId);
                        getParentFragmentManager().setFragmentResult(requestKey, result);

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}
