package eparon.vhbb_android.Utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import eparon.vhbb_android.Constants.VitaDB;

public class DownloadUtils {

    public static void VHBBDownloadManager (Context context, Uri uri, String filename) {
        DownloadManager downloadmanager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle(filename)
                .setDescription("Downloading...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setVisibleInDownloadsUi(true)
                .addRequestHeader(VitaDB.UA_REQUEST_HEADER, VitaDB.UA_REQUEST_VALUE) // Set a valid user-agent for the requests.
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

        assert downloadmanager != null;
        downloadmanager.enqueue(request);
    }

}
