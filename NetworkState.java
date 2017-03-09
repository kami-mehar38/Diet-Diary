package a098.ramzan.kamran.paleodietdiary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This project PaleoDietDiary is created by Kamran Ramzan on 09-Mar-17.
 */

class NetworkState {

    private Context context;

    NetworkState(Context context) {
        this.context = context;
    }

     boolean haveConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
