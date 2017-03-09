package a098.ramzan.kamran.paleodietdiary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final String PREFERENCE_KEY = "a098.ramzan.kamran.paleodietdiary";
    private static TextView title;
    private DateUtills dateUtills;
    private FloatingActionButton fab, fabOther, fabDrinking, fabDinner;
    private Animation fabOpen, fabClose, rotateClockwise, rotateAntiClockwise;
    private boolean isOpen = false;
    private static RecyclerView recyclerView;
    private static EventsAdapter eventsAdapter;
    private static Modal modal;
    private AdView adView;
    private AlertDialog alertDialog;
    public static String CURRENT_DATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NetworkState networkState = new NetworkState(this);
        adView = (AdView) findViewById(R.id.adView);
        if (networkState.haveConnection()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            adView.loadAd(adRequest);
        } else {
            adView.setVisibility(View.GONE);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabOther = (FloatingActionButton) findViewById(R.id.fabOther);
        fabDrinking = (FloatingActionButton) findViewById(R.id.fabDrinking);
        fabDinner = (FloatingActionButton) findViewById(R.id.fabDinner);
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabOther.setOnClickListener(this);
        fabDrinking.setOnClickListener(this);
        fabDinner.setOnClickListener(this);
        fabOther.setClickable(false);
        fabDrinking.setClickable(false);
        fabDinner.setClickable(false);
        rotateClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        rotateAntiClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anti_clockwise);

        fab.setOnClickListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dateUtills = new DateUtills(this);
        title = (TextView) findViewById(R.id.title);
        title.setText(dateUtills.getPaleoDate());
        title.setOnClickListener(this);
        CURRENT_DATE = dateUtills.getCurrentDate();

        SelectDate.setDate(dateUtills.getCurrentDate());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsAdapter = new EventsAdapter(this);
        modal = new Modal(this);
        getAllEvents(dateUtills.getCurrentDate());
    }

    void copyEventsToClipBoard(){
        List<EventInfo> eventInfoList = eventsAdapter.getEventInfoList();
        String copyText = "Paleo Diet Diary - " + dateUtills.getCurrentDate();
        if (eventInfoList.size() > 0) {
            for (int i = 0; i < eventInfoList.size(); i++) {
                copyText += "\n";
                copyText += eventInfoList.get(i).getEventName() + "(" + eventInfoList.get(i).getEventTime() + "): " + eventInfoList.get(i).getEventDescription();
            }
            Log.i("TAG", "copyEventsToClipBoard: " + copyText);
            copyText += "\n\n";
            copyText += "This diet diary maintained by \"Paleo Diet Diary\" Available in Google Play Store (https://play.google.com/store/apps/developer?id=TAMIL+PALEO+CART)";
            ShareCompat.IntentBuilder
                    .from(this) // getActivity() or activity field if within Fragment
                    .setText(copyText)
                    .setType("text/plain") // most general text sharing MIME type
                    .setChooserTitle("Paleo Diet Diary")
                    .startChooser();
        } else Toast.makeText(MainActivity.this, "No event", Toast.LENGTH_SHORT).show();
    }

    static void getAllEvents(String date) {

        eventsAdapter.removeAllItems();
        List<EventInfo> list = modal.getEvents(date);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                eventsAdapter.addItem(list.get(i));
            }
        }
        recyclerView.setAdapter(eventsAdapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isOpen) {
            closeFabs();
        } else {
            startActivity(new Intent(MainActivity.this, FullScreenAd.class));
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_today) {
            SelectDate.setDate(dateUtills.getCurrentDate());
            title.setText(dateUtills.getPaleoDate());
            getAllEvents(dateUtills.getCurrentDate());
            return true;
        } else if (id == R.id.action_share) {
            copyEventsToClipBoard();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_backup) {
            startActivity(new Intent(MainActivity.this, BackupRestore.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_about) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.about_us_view, null);
            ImageView link = (ImageView) view.findViewById(R.id.click);
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=TAMIL+PALEO+CART")));
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Paleo Diet Diary 1.0");
            builder.setIcon(R.drawable.ic_launcher);
            builder.setView(view);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.cancel();
                }
            });
            alertDialog = builder.create();
            alertDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());
            String date = simpleDateFormat.format(calendar.getTime());
            CURRENT_DATE = date;
            SimpleDateFormat paleoDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String paleoDate = paleoDateFormat.format(calendar.getTime());
            title.setText("Paleo Day: " + paleoDate.split(" ")[0] + "(" + paleoDate.split(" ")[1] + " " + paleoDate.split(" ")[2] + ")");
            getAllEvents(date);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab: {
                if (isOpen) {
                    closeFabs();
                } else {
                    openFabs();
                }
                break;
            }
            case R.id.fabOther: {
                Intent intent = new Intent(MainActivity.this, NewEvent.class);
                intent.putExtra(NewEvent.CATEGORY_ARG, "other");
                startActivity(intent);
                break;
            }
            case R.id.fabDrinking: {
                Intent intent = new Intent(MainActivity.this, NewEvent.class);
                intent.putExtra(NewEvent.CATEGORY_ARG, "drink");
                startActivity(intent);
                break;
            }
            case R.id.fabDinner: {
                Intent intent = new Intent(MainActivity.this, NewEvent.class);
                intent.putExtra(NewEvent.CATEGORY_ARG, "dinner");
                startActivity(intent);
                break;
            }
            case R.id.title: {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            }
        }
    }

    private void openFabs() {
        fab.startAnimation(rotateClockwise);
        fabOther.startAnimation(fabOpen);
        fabDrinking.startAnimation(fabOpen);
        fabDinner.startAnimation(fabOpen);
        fabOther.setClickable(true);
        fabDrinking.setClickable(true);
        fabDinner.setClickable(true);
        isOpen = true;
    }

    private void closeFabs() {
        fab.startAnimation(rotateAntiClockwise);
        fabOther.startAnimation(fabClose);
        fabDrinking.startAnimation(fabClose);
        fabDinner.startAnimation(fabClose);
        fabOther.setClickable(false);
        fabDrinking.setClickable(false);
        fabDinner.setClickable(false);
        isOpen = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isOpen)
            closeFabs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllEvents(dateUtills.getCurrentDate());
        if (adView != null) {
            adView.resume();
        }
    }
}
