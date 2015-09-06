package com.hnttechs.www.theladies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dell on 6/8/15.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi != null && wifi.isConnectedOrConnecting() || mobile != null && mobile.isConnectedOrConnecting()) {
            Intent startIntent = new Intent(context, NotificationAlert.class);
            startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            context.startService(startIntent);
        }
    }
}
