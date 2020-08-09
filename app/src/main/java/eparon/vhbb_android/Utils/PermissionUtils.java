package eparon.vhbb_android.Utils;

import android.Manifest;
import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import eparon.vhbb_android.R;

/**
 * Permission Utilities
 */
public class PermissionUtils {

    private static final int STORAGE_PERMISSION_CODE = 42069; // App's private request storage permission code.

    /**
     * Requests WRITE_EXTERNAL_STORAGE permission on runtime.
     *
     * @param activity the given activity
     */
    public static void requestStoragePermission (Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            new AlertDialog.Builder(activity)
                    .setTitle(activity.getString(R.string.permutils_title))
                    .setMessage(activity.getString(R.string.permutils_message))
                    .setPositiveButton(activity.getString(R.string.ok), (dialog, which) -> ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE))
                    .setNegativeButton(activity.getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                    .create().show();
        else
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

}