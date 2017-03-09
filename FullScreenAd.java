package a098.ramzan.kamran.paleodietdiary;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * This project PaleoDietDiary is created by Kamran Ramzan on 19-Feb-17.
 */

public class FullScreenAd extends Activity {

    InterstitialAd mInterstitialAd;
    private boolean isAddOpen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreenad);

        NetworkState networkState = new NetworkState(this);
        if (!networkState.haveConnection()) {
            finish();
        }

        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
                isAddOpen = true;
            }

            @Override
            public void onAdClosed() {
                finish();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                finish();
                isAddOpen = true;
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (isAddOpen)
        super.onBackPressed();
    }
}