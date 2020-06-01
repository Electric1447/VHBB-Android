package eparon.vhbb_android.Utils;

import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    public static boolean doesExists (String URLName) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection)new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
