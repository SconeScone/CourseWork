package com.example.gardenerhelperapplication.presentation.myplantscatalog.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.gardenerhelperapplication.R;

public class MyPlantConfirmDialogFragment extends DialogFragment {

    public static MyPlantConfirmDialogFragment getInstance(String requestKey) {
        MyPlantConfirmDialogFragment dialogFragment = new MyPlantConfirmDialogFragment();

        Bundle args = new Bundle();
        args.putString("REQUEST_KEY", requestKey);
        dialogFragment.setArguments(args);

        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String requestKey = getArguments().getString("REQUEST_KEY");

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setTitle(R.string.my_plant_deletion_title)
                .setMessage(R.string.my_plant_deletion_message)
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle result = new Bundle();
                        result.putBoolean("MY_PLANT_CONFIRM_DIALOG_RESULT_KEY", true);

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
