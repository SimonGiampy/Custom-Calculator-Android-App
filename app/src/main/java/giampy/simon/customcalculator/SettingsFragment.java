package giampy.simon.customcalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

    String appVersion = "1.0";
    ListPreference listp;
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        prefs = getPreferenceManager().getSharedPreferences();
        Boolean en_th = prefs.getBoolean("ENABLE_THOUSANDS", true);

        CheckBoxPreference chbp = (CheckBoxPreference) findPreference("ENABLE_THOUSANDS");
        listp = (ListPreference) findPreference("THOUSANDS_STYLE");

        if (!en_th) {
            listp.setEnabled(false);
            listp.setSummary(R.string.pref_disabled);
        } else {
            listpSummary();
        }
        chbp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (!((boolean) newValue)) {
                    listp.setEnabled(false);
                    listp.setSummary(R.string.pref_disabled);
                } else {
                    listp.setEnabled(true);
                    listpSummary();
                }
                return true;
            }
        });


        try {
            appVersion = getActivity().getBaseContext().getPackageManager().getPackageInfo(getActivity().getBaseContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //premendo la voce "Info" viene mostrato un Dialog
        Preference info = getPreferenceManager().findPreference("INFO");
        info.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle(R.string.about);
                dialog.setMessage(String.format(getResources().getString(R.string.version), appVersion));
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.create().show();
                return true;
            }
        });
    }

    public void listpSummary() {
        String summary;
        if (prefs.getString("THOUSANDS_STYLE", "DE").equals("DE")) {
            summary = getResources().getString(R.string.thousands_style_de);
        } else if (prefs.getString("THOUSANDS_STYLE", "DE").equals("US")) {
            summary = getResources().getString(R.string.thousands_style_us);
        } else {
            summary = getResources().getString(R.string.thousands_style_fr);
        }
        listp.setSummary(summary);
    }
}
