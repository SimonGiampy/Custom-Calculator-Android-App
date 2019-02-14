package giampy.simon.customcalculator;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ThemesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    int[] themeImages;
    int[] themeNames;  //this array contains the string resources of the names
    int[] themeNamesDetails;  //this array contains the string resources of the details of the themes
    String[] themeNamesEng;  //this array contains the names of the themes not translated, for the purpose to save it in the preferences file

    public static final String THEMES_KEY = "giampy.simon.customcalculator.THEMES_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //themeImages = new int[]{R.drawable.lg_preview, R.drawable.apple_preview, R.drawable.ml_red_preview, R.drawable.ml_red_preview, R.drawable.ml_red_preview};
        themeNamesDetails = new int[]{R.string.material_details, R.string.co_details, R.string.cv_details, R.string.cg_details, R.string.apple_details, R.string.flatred_details, R.string.flatblue_details, R.string.flatgreen_details};
        themeNames = new int[]{R.string.material, R.string.circular_orange, R.string.circular_violet, R.string.circular_green, R.string.apple, R.string.flat_red, R.string.flat_blue, R.string.flat_green};
        themeNamesEng = new String[]{"Material Dark", "Circular Orange", "Circular Violet", "Circular Green", "iPhone", "Flat Red", "Flat Blue", "Flat Green"};

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new ThemesAdapter());
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

    private class ThemesAdapter extends RecyclerView.Adapter<ThemesHolder> {

        @Override
        public ThemesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_item_themes, parent, false);
            return new ThemesHolder(view);
        }

        @Override
        public void onBindViewHolder(ThemesHolder holder, int position) {
            holder.details.setText(getThemeNameDetails(position));
            holder.name.setText(getThemeName(position));
            holder.position = position;
        }

        @Override
        public int getItemCount() {
            return themeNames.length;
        }
    }

    private class ThemesHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView details;
        RelativeLayout container;
        int position;

        public ThemesHolder(View view) {
            super(view);

            details = (TextView) view.findViewById(R.id.theme_details);
            name = (TextView) view.findViewById(R.id.theme_name);
            container = (RelativeLayout) view.findViewById(R.id.container_list);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savePreferences(getThemeNameEng(position));
                }
            });
        }
    }

    public Drawable getThemeImage(int pos) {
        return getResources().getDrawable(themeImages[pos]);
    }

    public String getThemeName(int pos) {
        return getResources().getString(themeNames[pos]);
    }

    public String getThemeNameDetails(int pos) {
        return getResources().getString(themeNamesDetails[pos]);
    }

    public String getThemeNameEng(int pos) {
        return themeNamesEng[pos];
    }

    public void savePreferences(String name) {
        SharedPreferences prefs = getBaseContext().getSharedPreferences(THEMES_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(MainActivity.THEME, name);
        editor.apply();

        NavUtils.navigateUpFromSameTask(this);
    }
}
