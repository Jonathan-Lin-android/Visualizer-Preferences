package android.example.com.visualizerpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.pref_visualizer);

        PreferenceScreen prefScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = prefScreen.getSharedPreferences();

        int count = prefScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);
            if (!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(p.getKey(), "");
                //listPreference value
                setPreferenceSummary(p, value);
            }
        }

        prefScreen.findPreference(getString(R.string.pref_size_key)).setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(final Preference preference, final Object o) {
        //check if entered size is 0 < 3
        if(preference.getKey().equals(getString(R.string.pref_size_key)))
        {
            Toast error = Toast.makeText(getContext(), "Please select a number between 0.1 and 3", Toast.LENGTH_SHORT);
            String changedValue = (String)o;
            try {
                float size = Float.parseFloat(changedValue);
                if(size <=0 || size > 3) {
                    error.show();
                    return false;
                }
            } catch (NumberFormatException nfe)
            {
                error.show();
                return false;
            }
        }
        return true;
    }

    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            preference.setSummary(value);
        } else if (preference instanceof EditTextPreference) {
            preference.setSummary(value);
        }
    }

    /*
        private void setPreferenceSummary(Preference preference, String value)
        {
            if(preference instanceof ListPreference)
            {
                ListPreference listPreference = (ListPreference) preference;

                int prefIndex = listPreference.findIndexOfValue(value);
                if(prefIndex >= 0) {
                    //get all entry labels
                    listPreference.setSummary(listPreference.getEntries()[prefIndex]);
                }
            }
        }
    */
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        Preference p = findPreference(key);

        if (p instanceof ListPreference) {
            //never going to be empty string
            p.setSummary(sharedPreferences.getString(key, ""));
        } else if (p instanceof EditTextPreference) {
            p.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
