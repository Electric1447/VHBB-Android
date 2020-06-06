package eparon.vhbb_android.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

/**
 * Network Utilities
 */
public class NetworkUtils {

    /**
     * Check if network is available (API 29 Compatible)
     *
     * @param context app's context
     * @return if network is available
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isNetworkAvailable (Context context) {
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        Network[] networks = cm.getAllNetworks();

        if (networks.length > 0) for (Network network : networks) {
            NetworkCapabilities nc = cm.getNetworkCapabilities(network);
            assert nc != null;
            if (nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                result = true;
        }

        return result;
    }

}
