package eparon.vhbb_android.Utils;

import android.Manifest;
import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

public class PermissionUtils {

    private static final int STORAGE_PERMISSION_CODE = 42069;

    public static void requestStoragePermission (Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            new AlertDialog.Builder(activity)
                    .setTitle("Permission needed")
                    .setMessage("Storage permission is needed to download files")
                    .setPositiveButton("Okay", (dialog, which) -> ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE))
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        else
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

}
