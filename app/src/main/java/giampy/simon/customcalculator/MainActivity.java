package giampy.simon.customcalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import giampy.simon.customcalculator.themes.AppleFragment;
import giampy.simon.customcalculator.themes.LgFragment;
import giampy.simon.customcalculator.themes.FlatFragment;
import giampy.simon.customcalculator.themes.MaterialFragment;

public class MainActivity extends AppCompatActivity {

    Fragment fragment;
    int primaryColor;
    int darkColor;
    int textToolbarColor;

    public static final String SQRT = "giampy.simon.customcalculator.mainactivity.SQRT";
    public static final String SIZE = "giampy.simon.customcalculator.mainactivity.SIZE";
    public static final String ENABLETHOUSANDS = "giampy.simon.customcalculator.mainactivity.ENABLE_THOUSANDS";
    public static final String THOUSANDSSTYLE = "giampy.simon.customcalculator.mainactivity.THOUSANDSSTYLE";
    public static final String THEME = "giampy.simon.customcalculator.mainactivity.THEME";
    public static final String THEMECOLOR = "giampy.simon.customcalculator.mainactivity.THEMECOLOR";
    public static final String THEMEACCENT = "giampy.simon.customcalculator.mainactivity.THEMEACCENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View shadow = findViewById(R.id.shadow);
        shadow.setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        Intent intent = new Intent();

        boolean sqrt = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("SQRT", false);
        intent.putExtra(SQRT, sqrt);

        boolean enab = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("ENABLE_THOUSANDS", true);
        intent.putExtra(ENABLETHOUSANDS, enab);

        String a = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("THOUSANDS_STYLE", "DE");
        intent.putExtra(THOUSANDSSTYLE, a);

        DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float textSize;
        if (dpWidth >= 800) {
            textSize = 55;
        } else if (dpWidth >= 720) {
            textSize = 50;
        } else if (dpWidth >= 600) {
            textSize = 45;
        } else if (dpWidth >= 400) {
            textSize = 42;
        } else if (dpWidth >= 360) {
            textSize = 38;
        } else {
            textSize = 35;
        }
        intent.putExtra(SIZE, textSize);
        SharedPreferences preferences = getBaseContext().getSharedPreferences(ThemesActivity.THEMES_KEY, MODE_PRIVATE);
        String theme = preferences.getString(THEME, "Material Dark");
        switch (theme) {
            case "Flat Red": {
                fragment = new FlatFragment();
                primaryColor = R.color.flat_primary_red;
                darkColor = R.color.flat_primarydark_red;
                textToolbarColor = R.color.flat_white;
                intent.putExtra(THEMECOLOR, R.layout.fragment_flat_red);
            }
            break;
            case "Flat Blue": {
                fragment = new FlatFragment();
                primaryColor = R.color.flat_primary_blue;
                darkColor = R.color.flat_primarydark_blue;
                textToolbarColor = R.color.flat_white;
                intent.putExtra(THEMECOLOR, R.layout.fragment_flat_blue);
            }
            break;
            case "Flat Green": {
                fragment = new FlatFragment();
                primaryColor = R.color.flat_primary_green;
                darkColor = R.color.flat_primarydark_green;
                textToolbarColor = R.color.flat_white;
                intent.putExtra(THEMECOLOR, R.layout.fragment_flat_green);
            }
            break;
            case "Circular Orange": {
                fragment = new LgFragment();
                primaryColor = R.color.lg_primary_orange;
                darkColor = R.color.lg_dark_orange;
                textToolbarColor = R.color.lg_number;
                intent.putExtra(THEMECOLOR, R.layout.fragment_lg_orange);
                intent.putExtra(THEMEACCENT, R.style.LgTheme_Orange);
            }
            break;
            case "Circular Violet": {
                fragment = new LgFragment();
                primaryColor = R.color.lg_primary_violet;
                darkColor = R.color.lg_dark_violet;
                textToolbarColor = R.color.lg_number;
                intent.putExtra(THEMECOLOR, R.layout.fragment_lg_violet);
                intent.putExtra(THEMEACCENT, R.style.LgTheme_Violet);
            }
            break;
            case "Circular Green": {
                fragment = new LgFragment();
                primaryColor = R.color.lg_primary_green;
                darkColor = R.color.lg_dark_green;
                textToolbarColor = R.color.lg_number;
                intent.putExtra(THEMECOLOR, R.layout.fragment_lg_green);
                intent.putExtra(THEMEACCENT, R.style.LgTheme_Green);
            }
            break;
            case "iPhone": {
                fragment = new AppleFragment();
                primaryColor = R.color.apple_primary;
                darkColor = R.color.apple_primarydark;
                textToolbarColor = R.color.apple_white;
                shadow.setVisibility(View.GONE);
            }
            break;
            default: {
                fragment = new MaterialFragment();
                primaryColor = R.color.mat_blue;
                darkColor = R.color.mat_blue_darker;
                textToolbarColor = R.color.mat_white;
            }
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(darkColor));
            if (primaryColor != R.color.mat_blue) {
                getWindow().setNavigationBarColor(getResources().getColor(primaryColor));
            }
        }
        toolbar.setBackgroundColor(getResources().getColor(primaryColor));
        toolbar.setTitleTextColor(getResources().getColor(textToolbarColor));

        if (savedInstanceState != null) {
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
        } else {
            fragment.setArguments(intent.getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.relative_main_layout, fragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "fragment", fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                intent.putExtra("primaryColor", primaryColor);
                intent.putExtra("darkColor", darkColor);
                startActivity(intent);
                return true;
            }
            case R.id.theme: {
                startActivity(new Intent(getBaseContext(), ThemesActivity.class));
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
