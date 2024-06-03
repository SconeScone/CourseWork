package com.example.gardenerhelperapplication.data;
import androidx.room.TypeConverter;
import java.time.LocalDate;

/**
 * Класс, с методами конвертации даты в число и обратно
 */
public class DateConverter {
    @TypeConverter
     public long fromDate(LocalDate date) {
        if(date != null) {
            return date.toEpochDay();
        }
        return -1;
     }

     @ TypeConverter
     public LocalDate toDate(long dateInLong) {
        if(dateInLong != -1) {
            return LocalDate.ofEpochDay(dateInLong);
        }
        return null;
     }
}
