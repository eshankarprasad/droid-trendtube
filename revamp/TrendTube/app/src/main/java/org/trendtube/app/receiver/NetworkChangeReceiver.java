package org.trendtube.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.trendtube.app.interfaces.NetworkChangeListener;
import org.trendtube.app.utils.ServiceManager;

/**
 * Created by shankar on 17/12/15.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private NetworkChangeListener listener;

    public NetworkChangeReceiver(NetworkChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (checkInternet(context)) {
            //Toast.makeText(context, "Network Available Do operations", Toast.LENGTH_SHORT).show();
            listener.onNetworkConnected();
        } else {
            //Toast.makeText(context, "Network unavailable Do operations", Toast.LENGTH_SHORT).show();
            listener.onNetworkDisconnected();
        }
    }

    boolean checkInternet(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        if (serviceManager.isNetworkAvailable()) {
            return true;
        } else {
            return false;
        }
    }

}