package com.example.gardenerhelperapplication.presentation.plantlists.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.gardenerhelperapplication.R;
import com.example.gardenerhelperapplication.databinding.DialogPeriodPickerBinding;

public class ChoosePeriodDialogFragment extends DialogFragment {
    private DialogPeriodPickerBinding binding;
    private String periods[];

    public static ChoosePeriodDialogFragment getInstance(int typeOfDialog, String dialogTitle, String requestKey) {
        ChoosePeriodDialogFragment dialogFragment = new ChoosePeriodDialogFragment();

        Bundle args = new Bundle();
        args.putInt("TYPE_OF_DIALOG_KEY", typeOfDialog);
        args.putString("DIALOG_TITLE_KEY", dialogTitle);
        args.putString("REQUEST_KEY", requestKey);
        dialogFragment.setArguments(args);

        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int typeOfDialog = getArguments().getInt("TYPE_OF_DIALOG_KEY", -1);
        String dialogTitle = getArguments().getString("DIALOG_TITLE_KEY", null);
        String requestKey = getArguments().getString("REQUEST_KEY", null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        binding = DialogPeriodPickerBinding.inflate(requireActivity().getLayoutInflater());

        switch (typeOfDialog) {
            case 4:
                periods = getResources().getStringArray(R.array.water_freq_array);
                break;
            case 5:
                periods = getResources().getStringArray(R.array.fertilize_freq_array);
                break;
        }

        binding.period.setMinValue(0);
        binding.period.setMaxValue(periods.length - 1);
        binding.period.setDisplayedValues(periods);

        builder.setView(binding.getRoot())
                .setTitle(dialogTitle)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String period = periods[binding.period.getValue()];

                        Bundle result = new Bundle();
                        result.putInt("TYPE_OF_DIALOG_KEY", typeOfDialog);
                        result.putString("PERIOD_DIALOG_RESULT_KEY", period);

                        getParentFragmentManager().setFragmentResult(requestKey, result);

                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
