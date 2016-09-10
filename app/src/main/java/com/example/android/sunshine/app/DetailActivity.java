package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends ActionBarActivity {

    private final String LOG_TAG = DetailActivity.class.getSimpleName();
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        String forecast = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,forecast);
        Intent chooser = Intent.createChooser(intent,"choose app");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
        item.setIntent(intent);
        setShareIntent(item.getIntent());


        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {

            mShareActionProvider.setShareIntent(shareIntent);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        if (id == R.id.action_preferred_location) {
            openMapIntent();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openMapIntent() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String location = preferences.getString(getString(R.string.key_location), getString(R.string.pref_location_default_value));
        if (location.equals("")) {
            Log.i(LOG_TAG, "location =" + location);
            Toast.makeText(this, "you can't see the information on the map app", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri geoLocation = Uri.parse("geo:0,0?q=" + location).buildUpon().build();
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            Log.i(LOG_TAG, "Starting intent map activity");
            startActivity(intent);
        } else {
            Log.i(LOG_TAG, "cant resolve activity");
            Toast.makeText(this, "you can't see the information on the map app", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private final String LOG_TAG = "PlaceholderFragment";

        private Adapter adapter;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            String forecast = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
            Log.i(LOG_TAG, forecast);
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.forecast);
            textView.setText(forecast);
            return rootView;
        }
    }
}
