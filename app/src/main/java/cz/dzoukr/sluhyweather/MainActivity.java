package cz.dzoukr.sluhyweather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.dzoukr.sluhyweather.fragments.BarometerFragment;
import cz.dzoukr.sluhyweather.fragments.HumidityFragment;
import cz.dzoukr.sluhyweather.fragments.RainFragment;
import cz.dzoukr.sluhyweather.fragments.SummaryFragment;
import cz.dzoukr.sluhyweather.fragments.TemperatureFragment;
import cz.dzoukr.sluhyweather.fragments.WindFragment;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private static final int TABS_COUNT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(TABS_COUNT);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        getSupportActionBar().setTitle(getString(R.string.app_name).toUpperCase());
    }

    public void update() {
        new GetJsonDataTask().execute(false);
    }

    public void updateForced() {
        new GetJsonDataTask().execute(true);
    }

    private void SetWindIconsRotation(JSONObject wind) {
        try {
            String x = wind.getString("CurrentDirection").split(" ")[0];
            com.github.pwittchen.weathericonview.WeatherIconView iconSum = (com.github.pwittchen.weathericonview.WeatherIconView) findViewById(R.id.wind_icon_summary);
            com.github.pwittchen.weathericonview.WeatherIconView iconDetail = (com.github.pwittchen.weathericonview.WeatherIconView) findViewById(R.id.wind_icon_detail);
            iconSum.setRotation(Float.parseFloat(x));
            iconDetail.setRotation(Float.parseFloat(x));
        } catch (Exception ex) {

        }
    }

    private void SetSummaryUI(String lastUpdated, JSONObject temperature, JSONObject wind, JSONObject humidity, JSONObject rain, JSONObject barometer) throws JSONException {
        ((TextView) findViewById(R.id.last_updated)).setText(lastUpdated);
        ((TextView) findViewById(R.id.summary_temperature)).setText(temperature.getString("Current"));
        ((TextView) findViewById(R.id.summary_wind)).setText(wind.getString("CurrentSpeed"));
        ((TextView) findViewById(R.id.summary_humidity)).setText(humidity.getString("Current"));
        ((TextView) findViewById(R.id.summary_rain)).setText(rain.getString("Rate"));
        ((TextView) findViewById(R.id.summary_barometer)).setText(barometer.getString("Current"));
    }

    private void SetTemperatureUI(JSONObject temperature) throws JSONException {

        ((TextView) findViewById(R.id.temperature_current)).setText(temperature.getString("Current"));

        ((TextView) findViewById(R.id.temperature_day_average)).setText(temperature.getString("DayAverage"));
        ((TextView) findViewById(R.id.temperature_day_max)).setText(temperature.getString("DayHigh"));
        ((TextView) findViewById(R.id.temperature_day_min)).setText(temperature.getString("DayLow"));

        ((TextView) findViewById(R.id.temperature_month_average)).setText(temperature.getString("MonthAverage"));
        ((TextView) findViewById(R.id.temperature_month_max)).setText(temperature.getString("MonthHigh"));
        ((TextView) findViewById(R.id.temperature_month_min)).setText(temperature.getString("MonthLow"));

        ((TextView) findViewById(R.id.temperature_year_average)).setText(temperature.getString("YearAverage"));
        ((TextView) findViewById(R.id.temperature_year_max)).setText(temperature.getString("YearHigh"));
        ((TextView) findViewById(R.id.temperature_year_min)).setText(temperature.getString("YearLow"));

        ((TextView) findViewById(R.id.temperature_total_average)).setText(temperature.getString("AllTimeAverage"));
        ((TextView) findViewById(R.id.temperature_total_max)).setText(temperature.getString("AllTimeHigh"));
        ((TextView) findViewById(R.id.temperature_total_min)).setText(temperature.getString("AllTimeLow"));
    }

    private void SetWindUI(JSONObject wind) throws JSONException {

        ((TextView) findViewById(R.id.wind_current)).setText(wind.getString("CurrentSpeed"));

        ((TextView) findViewById(R.id.wind_day_average)).setText(wind.getString("DayAvgSpeed"));
        ((TextView) findViewById(R.id.wind_day_max)).setText(wind.getString("DayHighSpeed"));

        ((TextView) findViewById(R.id.wind_week_average)).setText(wind.getString("WeekAvgSpeed"));

        ((TextView) findViewById(R.id.wind_month_average)).setText(wind.getString("MonthAvgSpeed"));
        ((TextView) findViewById(R.id.wind_month_max)).setText(wind.getString("MonthHighSpeed"));

        ((TextView) findViewById(R.id.wind_year_average)).setText(wind.getString("YearAvgSpeed"));
        ((TextView) findViewById(R.id.wind_year_max)).setText(wind.getString("YearHighSpeed"));

        ((TextView) findViewById(R.id.wind_total_average)).setText(wind.getString("AllTimeAvgSpeed"));
        ((TextView) findViewById(R.id.wind_total_max)).setText(wind.getString("AllTimeHighSpeed"));
    }

    private void SetHumidityUI(JSONObject humidity) throws JSONException {

        ((TextView) findViewById(R.id.humidity_current)).setText(humidity.getString("Current"));

        ((TextView) findViewById(R.id.humidity_day_average)).setText(humidity.getString("DayAverage"));
        ((TextView) findViewById(R.id.humidity_day_max)).setText(humidity.getString("DayHigh"));
        ((TextView) findViewById(R.id.humidity_day_min)).setText(humidity.getString("DayLow"));

        ((TextView) findViewById(R.id.humidity_month_average)).setText(humidity.getString("MonthAverage"));
        ((TextView) findViewById(R.id.humidity_month_max)).setText(humidity.getString("MonthHigh"));
        ((TextView) findViewById(R.id.humidity_month_min)).setText(humidity.getString("MonthLow"));

        ((TextView) findViewById(R.id.humidity_year_average)).setText(humidity.getString("YearAverage"));
        ((TextView) findViewById(R.id.humidity_year_max)).setText(humidity.getString("YearHigh"));
        ((TextView) findViewById(R.id.humidity_year_min)).setText(humidity.getString("YearLow"));

        ((TextView) findViewById(R.id.humidity_total_average)).setText(humidity.getString("AllTimeAverage"));
        ((TextView) findViewById(R.id.humidity_total_max)).setText(humidity.getString("AllTimeHigh"));
        ((TextView) findViewById(R.id.humidity_total_min)).setText(humidity.getString("AllTimeLow"));
    }
    
    private void SetRainUI(JSONObject rain) throws JSONException {

        ((TextView) findViewById(R.id.rain_current)).setText(rain.getString("Rate"));

        ((TextView) findViewById(R.id.rain_day_sum)).setText(rain.getString("Day"));
        ((TextView) findViewById(R.id.rain_day_max)).setText(rain.getString("DayHighRate"));

        ((TextView) findViewById(R.id.rain_month_sum)).setText(rain.getString("Month"));
        ((TextView) findViewById(R.id.rain_month_max)).setText(rain.getString("MonthHighRate"));

        ((TextView) findViewById(R.id.rain_year_sum)).setText(rain.getString("Year"));
        ((TextView) findViewById(R.id.rain_year_max)).setText(rain.getString("YearHighRate"));

        ((TextView) findViewById(R.id.rain_total_max)).setText(rain.getString("AllTimeHighRate"));
    }

    private void SetBarometerUI(JSONObject barometer) throws JSONException {

        ((TextView) findViewById(R.id.barometer_current)).setText(barometer.getString("Current"));

        ((TextView) findViewById(R.id.barometer_day_average)).setText(barometer.getString("DayAverage"));
        ((TextView) findViewById(R.id.barometer_day_max)).setText(barometer.getString("DayHigh"));
        ((TextView) findViewById(R.id.barometer_day_min)).setText(barometer.getString("DayLow"));

        ((TextView) findViewById(R.id.barometer_month_average)).setText(barometer.getString("MonthAverage"));
        ((TextView) findViewById(R.id.barometer_month_max)).setText(barometer.getString("MonthHigh"));
        ((TextView) findViewById(R.id.barometer_month_min)).setText(barometer.getString("MonthLow"));

        ((TextView) findViewById(R.id.barometer_year_average)).setText(barometer.getString("YearAverage"));
        ((TextView) findViewById(R.id.barometer_year_max)).setText(barometer.getString("YearHigh"));
        ((TextView) findViewById(R.id.barometer_year_min)).setText(barometer.getString("YearLow"));

        ((TextView) findViewById(R.id.barometer_total_average)).setText(barometer.getString("AllTimeAverage"));
        ((TextView) findViewById(R.id.barometer_total_max)).setText(barometer.getString("AllTimeHigh"));
        ((TextView) findViewById(R.id.barometer_total_min)).setText(barometer.getString("AllTimeLow"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.menu_refresh) {
            updateForced();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {

                case 0:
                    return new SummaryFragment();
                case 1:
                    return new TemperatureFragment();
                case 2:
                    return new WindFragment();
                case 3:
                    return new HumidityFragment();
                case 4:
                    return new RainFragment();
                case 5:
                    return new BarometerFragment();
                default:
                    return new SummaryFragment();
            }
        }

        @Override
        public int getCount() {
            return TABS_COUNT;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.summary).toUpperCase(l);
                case 1:
                    return getString(R.string.temperature).toUpperCase(l);
                case 2:
                    return getString(R.string.wind).toUpperCase(l);
                case 3:
                    return getString(R.string.humidity).toUpperCase(l);
                case 4:
                    return getString(R.string.rain).toUpperCase(l);
                case 5:
                    return getString(R.string.barometer).toUpperCase(l);

            }
            return null;
        }
    }


    private class GetJsonDataTask extends AsyncTask<Boolean, Void, JSONObject>
    {
        private static final int UPDATE_INTERVAL_MINUTES = 15;
        private static final String DATA_PARAM = "Data";
        private static final String LAST_UPDATE_PARAM = "LastUpdate";
        private static final String PREFERENCE_KEY = "SluhyWeather";
        private final SimpleDateFormat DateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        private ProgressDialog progressDialog;

        private Boolean refreshDataNeeded() {

            SharedPreferences sharedPref = getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE );
            if(sharedPref.contains(LAST_UPDATE_PARAM)) {
                String lastUpdateString = sharedPref.getString(LAST_UPDATE_PARAM, "01/01/1900 00:00:00");
                Date lastUpdate = null;
                try {
                    lastUpdate = DateFormat.parse(lastUpdateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date now = new Date();
                long diff = now.getTime() - lastUpdate.getTime();
                return diff > (1000 * 60 * UPDATE_INTERVAL_MINUTES);
            } else {
                return true;
            }
        }

        private JSONObject getStoredData() {
            SharedPreferences sharedPref = getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE );
            try {
                return new JSONObject(sharedPref.getString(DATA_PARAM,"{}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }

        private void storeData(JSONObject object) {
            SharedPreferences sharedPref = getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE );
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(DATA_PARAM, object.toString());
            editor.putString(LAST_UPDATE_PARAM, DateFormat.format(new Date()));
            editor.commit();
        }

        @Override
        protected JSONObject doInBackground(Boolean... forcedRefresh)
        {
            JSONObject json = new JSONObject();
            if(forcedRefresh[0] || this.refreshDataNeeded()) {
                JsonDownloader downloader = new JsonDownloader();
                json = downloader.GetData();
                if(json.length() > 0) {
                    storeData(json);
                }
            } else {
                json = getStoredData();
            }
            return json;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle(getString(R.string.loading_title));
            progressDialog.setMessage(getString(R.string.loading_message));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(JSONObject jsonData) {
            progressDialog.dismiss();


                if(jsonData.length() == 0) {
                    Toast.makeText(MainActivity.this, getString(R.string.update_failed), Toast.LENGTH_LONG).show();

                    JSONObject oldData = getStoredData();
                    if(oldData.length() > 0) {
                        jsonData = oldData;
                    } else {
                        return;
                    }
                }

                SetUI(jsonData);
        }

        private void SetUI(JSONObject jsonData) {
            try {
                String lastUpdated = jsonData.getString("LastUpdatedCZ");
                JSONObject temperature = jsonData.getJSONObject("Temperature");
                JSONObject wind = jsonData.getJSONObject("Wind");
                JSONObject humidity = jsonData.getJSONObject("Humidity");
                JSONObject rain = jsonData.getJSONObject("Rain");
                JSONObject barometer = jsonData.getJSONObject("Barometer");
                SetSummaryUI(lastUpdated, temperature, wind, humidity, rain, barometer);
                SetTemperatureUI(temperature);
                SetWindUI(wind);
                SetHumidityUI(humidity);
                SetRainUI(rain);
                SetBarometerUI(barometer);
                SetWindIconsRotation(wind);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
