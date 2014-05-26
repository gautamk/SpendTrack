package com.gautamk.spendtrack.app;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import com.gautamk.spendtrack.app.adapters.SpendToCsvAdapter;
import com.gautamk.spendtrack.app.async_tasks.ClearSpendsAsyncTask;

import java.io.File;
import java.io.IOException;


public class SettingsFragment extends PreferenceFragment {
    public static final int PICKFILE_RESULT_CODE = 220;

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
                                        new ClearSpendsAsyncTask(getActivity()).execute();
                                    }
                                })
                                .show();
                        return true;
                    }
                }
        );

        Preference import_csv = findPreference("import_csv");
        if (import_csv != null) import_csv.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                fileintent.setType("text/csv");
                Activity actvity = getActivity();
                if (actvity != null) {
                    actvity.startActivityForResult(fileintent, PICKFILE_RESULT_CODE);
                }
                return true;
            }
        });

        Preference export_csv = findPreference("export_csv");
        if (export_csv != null) export_csv.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Activity activity = getActivity();
                if (activity != null) {
                    File outputDir = Environment.getExternalStorageDirectory();
                    File csv = null;
                    try {
                        // Make sure the Pictures directory exists.
                        csv = new File(outputDir, "spend-track-export.csv");
                        SpendToCsvAdapter.adapt(csv.getAbsolutePath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/csv");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(csv));
                    startActivity(Intent.createChooser(intent, "Export CSV"));
                }
                return true;
            }
        });
    }

}