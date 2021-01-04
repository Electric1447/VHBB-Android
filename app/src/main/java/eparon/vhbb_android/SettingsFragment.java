package eparon.vhbb_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import eparon.vhbb_android.Constants.VHBBAndroid;
import eparon.vhbb_android.Constants.VitaDB;
import eparon.vhbb_android.Utils.CacheUtils;
import eparon.vhbb_android.Utils.StorageUtils;

public class SettingsFragment extends PreferenceFragmentCompat {

    private final int DIRECTORY_CHOOSER = 1011;

    @Override
    public void onCreatePreferences (Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference mDownloadLocationPreference = getPreferenceManager().findPreference("change_download_location");
        assert mDownloadLocationPreference != null;
        mDownloadLocationPreference.setOnPreferenceClickListener(preference -> {
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(Intent.createChooser(i, "Choose download location"), DIRECTORY_CHOOSER);
            return true;
        });

        Preference mCachePreference = getPreferenceManager().findPreference("clear_cache");
        assert mCachePreference != null;
        mCachePreference.setOnPreferenceClickListener(preference -> {
            CacheUtils.deleteCache(requireContext());
            return true;
        });

        Preference mAboutPreference = getPreferenceManager().findPreference("about");
        assert mAboutPreference != null;
        mAboutPreference.setTitle(String.format("%s %s", getString(R.string.app_name), BuildConfig.VERSION_NAME));
        mAboutPreference.setSummary(R.string.nav_header_subtitle);

        Preference mVitaDBPreference = getPreferenceManager().findPreference("vitadb");
        assert mVitaDBPreference != null;
        mVitaDBPreference.setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(VitaDB.PARENT_URL)));
            return true;
        });

        Preference mGithubPreference = getPreferenceManager().findPreference("github");
        assert mGithubPreference != null;
        mGithubPreference.setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(VHBBAndroid.BASE_URL)));
            return true;
        });
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DIRECTORY_CHOOSER) if (data != null) {
            Uri uri = data.getData();
            Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri));
            String path = StorageUtils.getPath(getActivity(), docUri);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getString(R.string.prefs_custom_download_location_key), path);
            editor.apply();
        }
    }

}