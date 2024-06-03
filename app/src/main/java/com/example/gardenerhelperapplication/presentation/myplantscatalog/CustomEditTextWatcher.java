package com.example.gardenerhelperapplication.presentation.myplantscatalog;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;

/**
 * Фильтр на многострочное поле ввода (ввод не более заданного количества строк)
 */
public class CustomEditTextWatcher implements TextWatcher {
    String text = "";
    int cursorPosition = 0;
    int maxLines;
    TextInputEditText editText;

    public CustomEditTextWatcher(TextInputEditText editText, int maxLines) {
        this.editText = editText;
        this.maxLines = maxLines;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        cursorPosition = editText.getSelectionStart();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        editText.removeTextChangedListener(this);
        if (editText.getLineCount() > maxLines) {
            editText.setText(text);
            editText.setSelection(cursorPosition);
        } else {
            text  = java.lang.String.valueOf(editText.getText());
        }
        editText.addTextChangedListener(this);
    }
}
