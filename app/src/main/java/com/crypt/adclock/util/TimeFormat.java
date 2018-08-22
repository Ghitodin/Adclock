package com.crypt.adclock.util;


import org.jetbrains.annotations.Contract;

public class TimeFormat {

    @Contract(pure = true)
    public static boolean isAm(int base24hours) {
        return base24hours < 12;
    }

    @Contract(pure = true)
    public static int base12toBase24Hours(int base12hours, boolean isAm) {
        if (isAm) {
            return (base12hours == 12 ? 0 : base12hours);
        } else {
            return (base12hours == 12 ? 12 : base12hours + 12);
        }
    }

    @Contract(pure = true)
    public static int base24toBase12hours(int base24hours) {
        if (base24hours == 0) {
            return 12;
        }
        if (base24hours > 12) {
            return base24hours - 12;
        } else {
            return base24hours;
        }
    }
}
