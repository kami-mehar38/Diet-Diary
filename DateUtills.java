package a098.ramzan.kamran.paleodietdiary;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * This project PaleoDietDiary is created by Kamran Ramzan on 04-Mar-17.
 */

class DateUtills {

    private Context context;
    private SharedPreferences sharedPreferences;

    DateUtills(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(MainActivity.PREFERENCE_KEY, Context.MODE_PRIVATE);
    }

    String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE/ dd MMM yyyy", Locale.getDefault());
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    String getPaleoDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String date = simpleDateFormat.format(Calendar.getInstance().getTime());
        return "Paleo Day: " + date.split(" ")[0] + "(" + date.split(" ")[1] + " " + date.split(" ")[2] + ")";
    }

    String getCurrentTime() {
        int clockMode = sharedPreferences.getInt(SettingsGeneral.CLOCK_MODE, 0);
        if (clockMode == 0) {
            if (android.text.format.DateFormat.is24HourFormat(context)) {
                // 24 hour format
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:mm", Locale.getDefault());
                return simpleDateFormat.format(Calendar.getInstance().getTime());
            } else {
                // 12 hour format
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                return simpleDateFormat.format(Calendar.getInstance().getTime());
            }
        } else if (clockMode == 1) {
            // 24 hour format
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:mm", Locale.getDefault());
            return simpleDateFormat.format(Calendar.getInstance().getTime());
        } else if (clockMode == 2) {
            // 12 hour format
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return simpleDateFormat.format(Calendar.getInstance().getTime());
        }
        return null;
    }

    String getRemindTime(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        int clockMode = sharedPreferences.getInt(SettingsGeneral.CLOCK_MODE, 0);
        if (clockMode == 0) {
            if (android.text.format.DateFormat.is24HourFormat(context)) {
                // 24 hour format
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:mm", Locale.getDefault());
               return simpleDateFormat.format(calendar.getTime());
            } else {
                // 12 hour format
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                return simpleDateFormat.format(calendar.getTime());
            }
        } else if (clockMode == 1) {
            // 24 hour format
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:mm", Locale.getDefault());
            return simpleDateFormat.format(calendar.getTime());
        } else if (clockMode == 2) {
            // 12 hour format
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return simpleDateFormat.format(calendar.getTime());
        }
        return null;
    }

    String incrementDay(String currentDate) {
        String date = currentDate;  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
            c.add(Calendar.DATE, 1);  // number of days to add
            date = sdf.format(c.getTime());  // dt is now the new date
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    String decrementDay(String currentDate) {
        String date = currentDate;  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
            c.add(Calendar.DATE, -1);  // number of days to add
            date = sdf.format(c.getTime());  // dt is now the new date
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    int currentDayInYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_YEAR);
    }
}
