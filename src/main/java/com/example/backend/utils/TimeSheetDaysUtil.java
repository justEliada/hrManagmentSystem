package com.example.backend.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TimeSheetDaysUtil {
    private TimeSheetDaysUtil() {
    }

    public static long calculateDaysBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }
}
