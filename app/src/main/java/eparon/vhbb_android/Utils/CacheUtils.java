package eparon.vhbb_android.Utils;

import android.content.Context;

import java.io.File;

/**
 * Cache Utilities
 */
public class CacheUtils {

    public static void deleteCache (Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir (File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            assert children != null;

            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success)
                    return false;
            }

            return dir.delete();

        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

}
