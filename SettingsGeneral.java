package a098.ramzan.kamran.paleodietdiary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.andexert.library.RippleView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SettingsGeneral extends AppCompatActivity implements RippleView.OnRippleCompleteListener {

    private AlertDialog alertDialog;
    private SharedPreferences sharedPreferences;
    public static String CLOCK_MODE = "CLOCK_MODE";
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_general);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_KEY, Context.MODE_PRIVATE);
        NetworkState networkState = new NetworkState(this);
        adView = (AdView) findViewById(R.id.adView);
        if (networkState.haveConnection()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            adView.loadAd(adRequest);
        } else {
            adView.setVisibility(View.GONE);
        }
        RippleView clockModeSettings = (RippleView) findViewById(R.id.clockModeSettings);
        clockModeSettings.setOnRippleCompleteListener(this);
    }

    private void showClockModeAlert() {
        String[] choices = new String[]{"System Default", "24 Hour", "AM/PM"};
        int defaultMode = sharedPreferences.getInt(CLOCK_MODE, 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsGeneral.this);
        builder.setTitle("Clock Mode");
        builder.setSingleChoiceItems(choices, defaultMode, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(CLOCK_MODE, i);
                editor.apply();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.cancel();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
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
            case R.id.clockModeSettings: {
                showClockModeAlert();
                break;
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
