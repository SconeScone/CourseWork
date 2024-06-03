package com.example.gardenerhelperapplication.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Класс с методом расчета разницы между двумя датами
 * (расчет оставшегося количества дней до следующего полива или подкормки растения, в зависимости от случая)
 */
public class DateDiffCalculator {
    public static DateDiffResult calcRemDaysBeforePlantCare(LocalDate nextWaterDate, String itIsTimeMessage, String remDaysMessage) {
        if(nextWaterDate != null) {
            LocalDate curDate = LocalDate.now();
            if(!nextWaterDate.isAfter(curDate)) {
                return new DateDiffResult(true, itIsTimeMessage);
            }
            long daysBeforePlantCare = ChronoUnit.DAYS.between(curDate, nextWaterDate);
            String daysBeforePlantCareText = remDaysMessage + "  " + daysBeforePlantCare;
            return new DateDiffResult(false, daysBeforePlantCareText);
        }
        return new DateDiffResult(true, itIsTimeMessage);
    }

    public static class DateDiffResult {
        private boolean isItTimeToPlantCare;
        private String resultMessage;

        public DateDiffResult(boolean isItTimeToPlantCare, String resultMessage) {
            this.isItTimeToPlantCare = isItTimeToPlantCare;
            this.resultMessage = resultMessage;
        }

        public boolean isItTimeToPlantCare() {
            return isItTimeToPlantCare;
        }

        public String getResultMessage() {
            return resultMessage;
        }
    }
}
