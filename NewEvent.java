package a098.ramzan.kamran.paleodietdiary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * This project PaleoDietDiary is created by Kamran Ramzan on 04-Mar-17.
 */

public class NewEvent extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static TextView date;
    private static TextView time;
    private static EventInfo eventInfo;
    private EditText description;
    private Modal modal;
    public static String CATEGORY_ARG = "CATEGORY";
    private String category;
    private static SharedPreferences sharedPreferences;
    private AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
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
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        List<OtherEventInfo> otherEventInfoList = new ArrayList<>();

        spinner.setOnItemSelectedListener(this);

        category = getIntent().getStringExtra(CATEGORY_ARG);

        if (category != null) {
            switch (category) {
                case "other":
                    OtherEventAdapter otherEventAdapter;
                {
                    OtherEventInfo medicationInfo = new OtherEventInfo();
                    medicationInfo.setEventIcon(R.drawable.ic_action_medication);
                    medicationInfo.setEventName("Medication");
                    otherEventInfoList.add(medicationInfo);

                    OtherEventInfo supplementInfo = new OtherEventInfo();
                    supplementInfo.setEventIcon(R.drawable.ic_action_supplements);
                    supplementInfo.setEventName("Supplements");
                    otherEventInfoList.add(supplementInfo);

                    OtherEventInfo walkingExercise = new OtherEventInfo();
                    walkingExercise.setEventIcon(R.drawable.ic_action_exercise);
                    walkingExercise.setEventName("Walking/Exercise");
                    otherEventInfoList.add(walkingExercise);

                    OtherEventInfo measureWeight = new OtherEventInfo();
                    measureWeight.setEventIcon(R.drawable.ic_action_measure);
                    measureWeight.setEventName("Measure Weight");
                    otherEventInfoList.add(measureWeight);

                    OtherEventInfo warmWater = new OtherEventInfo();
                    warmWater.setEventIcon(R.drawable.ic_action_warm);
                    warmWater.setEventName("Take warm water with lime");
                    otherEventInfoList.add(warmWater);

                    OtherEventInfo coconutOil = new OtherEventInfo();
                    coconutOil.setEventIcon(R.drawable.ic_action_oil);
                    coconutOil.setEventName("Coconut Oil");
                    otherEventInfoList.add(coconutOil);

                    OtherEventInfo otherInfo = new OtherEventInfo();
                    otherInfo.setEventIcon(R.drawable.ic_action_dot);
                    otherInfo.setEventName("Other");
                    otherEventInfoList.add(otherInfo);
                    otherEventAdapter = new OtherEventAdapter(this, R.layout.others_new_event, R.id.eventName, otherEventInfoList);
                    spinner.setAdapter(otherEventAdapter);
                    break;
                }
                case "drink": {

                    OtherEventInfo blackCoffee = new OtherEventInfo();
                    blackCoffee.setEventIcon(R.drawable.ic_action_subject);
                    blackCoffee.setEventName("Black Coffee");
                    otherEventInfoList.add(blackCoffee);

                    OtherEventInfo blackTea = new OtherEventInfo();
                    blackTea.setEventIcon(R.drawable.ic_action_subject);
                    blackTea.setEventName("Black Tea");
                    otherEventInfoList.add(blackTea);

                    OtherEventInfo lemonJuice = new OtherEventInfo();
                    lemonJuice.setEventIcon(R.drawable.ic_action_subject);
                    lemonJuice.setEventName("Lemon Juice");
                    otherEventInfoList.add(lemonJuice);

                    OtherEventInfo water = new OtherEventInfo();
                    water.setEventIcon(R.drawable.ic_action_subject);
                    water.setEventName("Water");
                    otherEventInfoList.add(water);

                    OtherEventInfo herbalWater = new OtherEventInfo();
                    herbalWater.setEventIcon(R.drawable.ic_action_subject);
                    herbalWater.setEventName("Herbal Water");
                    otherEventInfoList.add(herbalWater);


                    OtherEventInfo fruitJuice = new OtherEventInfo();
                    fruitJuice.setEventIcon(R.drawable.ic_action_subject);
                    fruitJuice.setEventName("Fruit Juice");
                    otherEventInfoList.add(fruitJuice);

                    OtherEventInfo otherInfo = new OtherEventInfo();
                    otherInfo.setEventIcon(R.drawable.ic_action_subject);
                    otherInfo.setEventName("Other");
                    otherEventInfoList.add(otherInfo);

                    otherEventAdapter = new OtherEventAdapter(this, R.layout.others_new_event, R.id.eventName, otherEventInfoList);
                    spinner.setAdapter(otherEventAdapter);
                    break;
                }
                case "dinner": {
                    OtherEventInfo meal1 = new OtherEventInfo();
                    meal1.setEventIcon(R.drawable.ic_action_subject);
                    meal1.setEventName("Meal 1");
                    otherEventInfoList.add(meal1);

                    OtherEventInfo meal2 = new OtherEventInfo();
                    meal2.setEventIcon(R.drawable.ic_action_subject);
                    meal2.setEventName("Meal 2");
                    otherEventInfoList.add(meal2);

                    OtherEventInfo meal3 = new OtherEventInfo();
                    meal3.setEventIcon(R.drawable.ic_action_subject);
                    meal3.setEventName("Meal 3");
                    otherEventInfoList.add(meal3);

                    OtherEventInfo butter = new OtherEventInfo();
                    butter.setEventIcon(R.drawable.ic_action_subject);
                    butter.setEventName("Butter");
                    otherEventInfoList.add(butter);

                    OtherEventInfo cheese = new OtherEventInfo();
                    cheese.setEventIcon(R.drawable.ic_action_subject);
                    cheese.setEventName("Cheese");
                    otherEventInfoList.add(cheese);

                    OtherEventInfo snack = new OtherEventInfo();
                    snack.setEventIcon(R.drawable.ic_action_subject);
                    snack.setEventName("Snacks");
                    otherEventInfoList.add(snack);

                    OtherEventInfo otherInfo = new OtherEventInfo();
                    otherInfo.setEventIcon(R.drawable.ic_action_subject);
                    otherInfo.setEventName("Other");
                    otherEventInfoList.add(otherInfo);

                    otherEventAdapter = new OtherEventAdapter(this, R.layout.others_new_event, R.id.eventName, otherEventInfoList);
                    spinner.setAdapter(otherEventAdapter);
                    break;
                }
            }
        }

        description = (EditText) findViewById(R.id.description);

        DateUtills dateUtills = new DateUtills(this);
        date = (TextView) findViewById(R.id.date);
        date.setText(dateUtills.getCurrentDate());
        date.setOnClickListener(this);
        time = (TextView) findViewById(R.id.time);
        time.setText(dateUtills.getCurrentTime());
        time.setOnClickListener(this);

        eventInfo = new EventInfo();
        eventInfo.setEventDate(dateUtills.getCurrentDate());
        eventInfo.setEventTime(dateUtills.getCurrentTime());
        eventInfo.setEventClass(category);
        modal = new Modal(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
            case R.id.action_save: {
                String eventDescription = description.getText().toString().trim();
                if (!eventDescription.isEmpty()) {
                    eventInfo.setEventDescription(eventDescription);
                    modal.addEvent(eventInfo);
                    Toast.makeText(NewEvent.this, "Event added", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else
                    Toast.makeText(NewEvent.this, "Please enter description first", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date: {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            }
            case R.id.time: {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
                break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView evenName = (TextView) view.findViewById(R.id.eventName);
        eventInfo.setEventName(evenName.getText().toString());
        if (category != null) {
            switch (category) {
                case "other": {
                    if (i == 0) {
                        eventInfo.setEventIcon(R.drawable.ic_action_medication);
                    } else if (i == 1) {
                        eventInfo.setEventIcon(R.drawable.ic_action_supplements);
                    } else if (i == 2) {
                        eventInfo.setEventIcon(R.drawable.ic_action_exercise);
                    } else if (i == 3) {
                        eventInfo.setEventIcon(R.drawable.ic_action_measure);
                    } else if (i == 4) {
                        eventInfo.setEventIcon(R.drawable.ic_action_warm);
                    } else if (i == 5) {
                        eventInfo.setEventIcon(R.drawable.ic_action_oil);
                    } else if (i == 6) {
                        eventInfo.setEventIcon(R.drawable.ic_action_dot);
                    }
                    break;
                }
                default:
                    eventInfo.setEventIcon(R.drawable.ic_action_subject);
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE- dd MMM yyyy", Locale.getDefault());
            date.setText(simpleDateFormat.format(calendar.getTime()));
            eventInfo.setEventDate(simpleDateFormat.format(calendar.getTime()));
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
            int clockMode = sharedPreferences.getInt(SettingsGeneral.CLOCK_MODE, 0);
            if (clockMode == 0) {
                if (android.text.format.DateFormat.is24HourFormat(getActivity())) {
                    // 24 hour format
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    time.setText(simpleDateFormat.format(calendar.getTime()));
                    eventInfo.setEventTime(simpleDateFormat.format(calendar.getTime()));
                } else {
                    // 12 hour format
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    time.setText(simpleDateFormat.format(calendar.getTime()));
                    eventInfo.setEventTime(simpleDateFormat.format(calendar.getTime()));
                }
            } else if (clockMode == 1) {
                // 24 hour format
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                time.setText(simpleDateFormat.format(calendar.getTime()));
                eventInfo.setEventTime(simpleDateFormat.format(calendar.getTime()));
            } else if (clockMode == 2) {
                // 12 hour format
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                time.setText(simpleDateFormat.format(calendar.getTime()));
                eventInfo.setEventTime(simpleDateFormat.format(calendar.getTime()));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }
}
