package giampy.simon.customcalculator;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(getIntent().getIntExtra("primaryColor", R.color.lg_primary_orange))));
        }
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(getIntent().getIntExtra("darkColor", R.color.lg_dark_orange)));
            getWindow().setNavigationBarColor(getResources().getColor(getIntent().getIntExtra("primaryColor", R.color.lg_primary_orange)));
        }

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().add(R.id.settings_layout, new SettingsFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPause() {
        super.onPause();
        NavUtils.navigateUpFromSameTask(this);
    }
}
