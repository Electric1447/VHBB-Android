package eparon.vhbb_android.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.File;

import eparon.vhbb_android.Constants.VitaDB;
import eparon.vhbb_android.R;

public class DownloadUtils {

    public static void VHBBDownloadManager (Activity activity, Context context, Uri uri, String filename) {
        if ((ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) && (Build.VERSION.SDK_INT < 29)) {
            PermissionUtils.requestStoragePermission(activity);
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                return;
        }

        if (!NetworkUtils.isNetworkAvailable(context)) {
            Toast.makeText(context, context.getString(R.string.err_network_not_available), Toast.LENGTH_SHORT).show();
            return;
        }

        DownloadManager downloadmanager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);

        SharedPreferences sharedPref = activity.getSharedPreferences(activity.getString(R.string.prefs_string), Context.MODE_PRIVATE);
        String downloadLocation = sharedPref.getString(activity.getString(R.string.prefs_custom_download_location_key), Environment.DIRECTORY_DOWNLOADS);

        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle(filename)
                .setDescription(context.getString(R.string.downloading))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setVisibleInDownloadsUi(true)
                .addRequestHeader(VitaDB.UA_REQUEST_HEADER, VitaDB.UA_REQUEST_VALUE); // Set a valid user-agent for the requests.

        assert downloadLocation != null;
        if(downloadLocation.equals(Environment.DIRECTORY_DOWNLOADS)){
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        } else {
            File path = new File(downloadLocation);
            if(path.exists()) {
                request.setDestinationUri(Uri.fromFile(new File(downloadLocation + "/" + filename)));
            } else {
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
            }
        }

        assert downloadmanager != null;
        downloadmanager.enqueue(request);
    }

}
