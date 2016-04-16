package com.richdroid.masterextempore.utils;

import android.util.Log;

import java.util.Calendar;
import java.util.Random;

public class CalendarUtil {

    public static String generateUniqueImageName(String prefix) {
        Calendar c = Calendar.getInstance();
        String uniqueDate = String.valueOf(c.get(Calendar.YEAR))
                + (c.get(Calendar.MONTH) + 1) + c.get(Calendar.DAY_OF_MONTH)
                + "_" + c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE)
                + c.get(Calendar.SECOND) + c.get(Calendar.MILLISECOND);

        Log.d("CalendarUtil", "Today's unique date info: " + uniqueDate);

        Random generator = new Random();
        int range = 1000000000;
        int generatedNumber = generator.nextInt(range);
        Log.d("Generated Number is: ", String.valueOf(generatedNumber));

        // Now, use these values to create a unique name:
        String uniqueImageName = prefix + "_" + uniqueDate + "_"
                + generatedNumber;
        Log.d("Unique Image Nmae: ", uniqueImageName);
        return uniqueImageName;
    }

}
