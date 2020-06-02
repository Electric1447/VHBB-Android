package eparon.vhbb_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import eparon.vhbb_android.Constants.VitaDB;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences (Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference mAboutPreference = getPreferenceManager().findPreference("about");
        assert mAboutPreference != null;
        mAboutPreference.setTitle(String.format("%s %s", getString(R.string.app_name), BuildConfig.VERSION_NAME));
        mAboutPreference.setSummary(R.string.nav_header_subtitle);

        Preference mGithubPreference = getPreferenceManager().findPreference("github");
        assert mGithubPreference != null;
        mGithubPreference.setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(VitaDB.PARENT_URL)));
            return true;
        });
    }

}