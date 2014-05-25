package com.gautamk.spendtrack.app;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import com.gautamk.spendtrack.app.managers.SpendManager;


public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        Preference clear_spends = findPreference("clear_spends");
        if (clear_spends != null) clear_spends.setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Clear Spends ?")
                                .setMessage("Delete all spends, this cannot be undone.")
                                .setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new SpendManager.ClearSpendsAsyncTask(getActivity()).execute();
                                    }
                                })
                                .show();
                        return true;
                    }
                }
        );
    }

}