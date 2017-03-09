package a098.ramzan.kamran.paleodietdiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.andexert.library.RippleView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SettingsActivity extends AppCompatActivity implements RippleView.OnRippleCompleteListener {

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RippleView general = (RippleView) findViewById(R.id.general);
        RippleView notifications = (RippleView) findViewById(R.id.notifications);
        general.setOnRippleCompleteListener(this);
        notifications.setOnRippleCompleteListener(this);
        NetworkState networkState = new NetworkState(this);
        adView = (AdView) findViewById(R.id.adView);
        if (networkState.haveConnection()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            adView.loadAd(adRequest);
        } else {
            adView.setVisibility(View.GONE);
        }
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
            case R.id.general: {
                startActivity(new Intent(SettingsActivity.this, SettingsGeneral.class));
                break;
            }
            case R.id.notifications: {
                startActivity(new Intent(SettingsActivity.this, SettingsNotifications.class));
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
