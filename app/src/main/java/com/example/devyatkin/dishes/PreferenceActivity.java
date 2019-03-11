package com.example.devyatkin.dishes;

import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.widget.Toast;

public class PreferenceActivity extends android.preference.PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);


        Preference myPref = findPreference(getResources().getString(R.string.key_pref_storage));
        myPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Toast.makeText(PreferenceActivity.this, "You chose " + newValue.toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
