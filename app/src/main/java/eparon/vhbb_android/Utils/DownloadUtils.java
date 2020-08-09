package eparon.vhbb_android.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

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

        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle(filename)
                .setDescription(context.getString(R.string.downloading))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setVisibleInDownloadsUi(true)
                .addRequestHeader(VitaDB.UA_REQUEST_HEADER, VitaDB.UA_REQUEST_VALUE) // Set a valid user-agent for the requests.
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

        assert downloadmanager != null;
        downloadmanager.enqueue(request);
    }

}
