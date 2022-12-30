package com.example.patternpat.view;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.patternpat.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment(){

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }
}