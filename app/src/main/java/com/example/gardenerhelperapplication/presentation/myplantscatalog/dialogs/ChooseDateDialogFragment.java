package com.example.gardenerhelperapplication.presentation.myplantscatalog.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.gardenerhelperapplication.R;
import com.example.gardenerhelperapplication.databinding.DialogDatePickerBinding;
import com.example.gardenerhelperapplication.presentation.MonthDecade;


public class ChooseDateDialogFragment extends DialogFragment {
    private DialogDatePickerBinding binding;
    private String months[];
    private String decades[];

    public static ChooseDateDialogFragment getInstance(int typeOfDialog, String dialogTitle, String requestKey) {
        ChooseDateDialogFragment dialogFragment = new ChooseDateDialogFragment();

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

        binding = DialogDatePickerBinding.inflate(requireActivity().getLayoutInflater());

        decades = getResources().getStringArray(R.array.decades);
        binding.decade.setMinValue(0);
        binding.decade.setMaxValue(decades.length - 1);
        binding.decade.setDisplayedValues(decades);

        months = getResources().getStringArray(R.array.months);
        binding.month.setMinValue(0);
        binding.month.setMaxValue(months.length - 1);
        binding.month.setDisplayedValues(months);

        builder.setView(binding.getRoot())
                .setTitle(dialogTitle)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String month = months[binding.month.getValue()];
                        String decade = decades[binding.decade.getValue()];

                        MonthDecade date = new MonthDecade(month, decade);

                        Bundle result = new Bundle();
                        result.putInt("TYPE_OF_DIALOG_KEY", typeOfDialog);
                        result.putSerializable("DATE_DIALOG_RESULT_KEY", date);

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
