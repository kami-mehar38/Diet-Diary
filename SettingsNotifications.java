package a098.ramzan.kamran.paleodietdiary;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.andexert.library.RippleView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SettingsNotifications extends AppCompatActivity implements RippleView.OnRippleCompleteListener, CompoundButton.OnCheckedChangeListener {

    private SwitchCompat switchRemind;
    public static String REMIND_TIME = "REMIND_TIME";
    public static String REMIND_ME = "REMIND_ME";
    public static String ALARM_TIME = "ALARM_TIME";
    public static int ALARM_INTENT_ID = 64;
    private static SharedPreferences sharedPreferences;
    private static TextView remindTime;
    private TextView remind;
    private RippleView reminderSettings;
    private static DateUtills dateUtills;
    private static Context context;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsNotifications.context = getApplicationContext();
        setContentView(R.layout.activity_settings_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_KEY, MODE_PRIVATE);
        NetworkState networkState = new NetworkState(this);
        adView = (AdView) findViewById(R.id.adView);
        if (networkState.haveConnection()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            adView.loadAd(adRequest);
        } else {
            adView.setVisibility(View.GONE);
        }
        dateUtills = new DateUtills(this);
        setAlarm();
        String[] time = sharedPreferences.getString(REMIND_TIME, dateUtills.getRemindTime(7, 30)).split(":");
        remindTime = (TextView) findViewById(R.id.remindTime);
        remind = (TextView) findViewById(R.id.remind);
        remindTime.setText(dateUtills.getRemindTime(Integer.parseInt(time[0]), Integer.parseInt(time[1].split(" ")[0])));

        RippleView remindSettings = (RippleView) findViewById(R.id.remindSettings);
        reminderSettings = (RippleView) findViewById(R.id.reminderSettings);

        remindSettings.setOnRippleCompleteListener(this);
        reminderSettings.setOnRippleCompleteListener(this);

        switchRemind = (SwitchCompat) findViewById(R.id.switchRemind);
        switchRemind.setOnCheckedChangeListener(this);

        if (sharedPreferences.getBoolean(REMIND_ME, false)) {
            switchRemind.setChecked(true);
        } else {
            switchRemind.setChecked(false);
            remind.setTextColor(Color.parseColor("#c9c9c9"));
            remindTime.setTextColor(Color.parseColor("#c9c9c9"));
            reminderSettings.setOnRippleCompleteListener(null);
        }
    }

    public static Context getAppContext() {
        return SettingsNotifications.context;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        switch (rippleView.getId()) {
            case R.id.remindSettings: {
                if (switchRemind.isChecked()) {
                    switchRemind.setChecked(false);
                } else switchRemind.setChecked(true);
                break;
            }
            case R.id.reminderSettings: {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
                break;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.switchRemind: {
                if (b) {
                    remind.setTextColor(Color.parseColor("#727272"));
                    remindTime.setTextColor(Color.parseColor("#727272"));
                    reminderSettings.setOnRippleCompleteListener(this);
                    setAlarm();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(REMIND_ME, true);
                    editor.apply();
                } else {
                    remind.setTextColor(Color.parseColor("#c9c9c9"));
                    remindTime.setTextColor(Color.parseColor("#c9c9c9"));
                    reminderSettings.setOnRippleCompleteListener(null);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(REMIND_ME, false);
                    editor.apply();
                    cancelAlarm();
                }
                break;
            }
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {


        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            int clockMode = sharedPreferences.getInt(SettingsGeneral.CLOCK_MODE, 0);
            // Create a new instance of TimePickerDialog and return it
            if (clockMode == 1) {
                return new TimePickerDialog(getActivity(), this, hour, minute,
                        true);
            } else if (clockMode == 2) {
                return new TimePickerDialog(getActivity(), this, hour, minute,
                        false);
            } else
                return new TimePickerDialog(getActivity(), this, hour, minute,
                        DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(ALARM_TIME, String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
            editor.apply();

            int clockMode = sharedPreferences.getInt(SettingsGeneral.CLOCK_MODE, 0);
            if (clockMode == 0) {
                if (android.text.format.DateFormat.is24HourFormat(getActivity())) {
                    // 24 hour format
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:mm", Locale.getDefault());
                    remindTime.setText(simpleDateFormat.format(calendar.getTime()));
                    editor.putString(REMIND_TIME,simpleDateFormat.format(calendar.getTime()));
                    editor.apply();
                } else {
                    // 12 hour format
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    remindTime.setText(simpleDateFormat.format(calendar.getTime()));
                    editor.putString(REMIND_TIME,simpleDateFormat.format(calendar.getTime()));
                    editor.apply();
                }
            } else if (clockMode == 1) {
                // 24 hour format
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:mm", Locale.getDefault());
                remindTime.setText(simpleDateFormat.format(calendar.getTime()));
                editor.putString(REMIND_TIME,simpleDateFormat.format(calendar.getTime()));
                editor.apply();
            } else if (clockMode == 2) {
                // 12 hour format
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                remindTime.setText(simpleDateFormat.format(calendar.getTime()));
                editor.putString(REMIND_TIME,simpleDateFormat.format(calendar.getTime()));
                editor.apply();
            }
            setAlarm();
        }
    }

    private static void setAlarm() {
        String[] time = sharedPreferences.getString(ALARM_TIME, "7:30").split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);
        AlarmManager alarmMgr = (AlarmManager) getAppContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getAppContext(), AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getAppContext(), ALARM_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        if (calendar.before(now) || calendar.equals(now)) {
            calendar.add(Calendar.DATE, 1);
        }

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        ComponentName receiver = new ComponentName(getAppContext(), BootReceiver.class);
        PackageManager pm = getAppContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void cancelAlarm() {
        Intent alarmIntent = new Intent(getAppContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getAppContext(), ALARM_INTENT_ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getAppContext().getSystemService(Context.ALARM_SERVICE);
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
        ComponentName receiver = new ComponentName(getAppContext(), BootReceiver.class);
        PackageManager pm = getAppContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }
}
