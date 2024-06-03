package com.example.gardenerhelperapplication.utils;

public class ValidationResult {
    private boolean isValid;
    private String attentionMessage;

    public ValidationResult(boolean isValid, String attentionMessage) {
        this.isValid = isValid;
        this.attentionMessage = attentionMessage;
    }

    public boolean getIsValid() {
        return isValid;
    }

    public String getAttentionMessage() {
        return attentionMessage;
    }
}
