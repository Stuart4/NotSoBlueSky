package com.foobar.jake.notsobluesky;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;



public class MainActivity extends Activity {
    private boolean setForRain = false;
    private TextView contentBlurb;
    private ImageView image;
    private String apikey;
    private URL apiString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentBlurb = (TextView) findViewById(R.id.statusBlurb);
        image = (ImageView) findViewById(R.id.imageView);
        apikey = getString(R.string.forecastapi);
    }

    public void updateRainStatus(boolean rain) {
        //GO HOME!!!
        if (setForRain == rain) {
            return;
        }

        //Update interface

        if (rain) {
            contentBlurb.setText(getString(R.string.rain));
            showRainPicture(true);
            setForRain = true;
        } else {
            contentBlurb.setText(getString(R.string.notRain));
            showRainPicture(false);
            setForRain = false;
        }
    }

    private void showRainPicture(boolean isRain) {
        if (isRain) {
            image.setImageResource(R.drawable.raincloud);
        } else {
            image.setImageResource(R.drawable.suncloud);
        }
    }

    public void affirmativeButtonClicked(View view) throws IOException {
        runRainStatus();
    }

    public void negativeButtonClicked(View view) throws IOException {
        runRainStatus();
    }

    private void runRainStatus() throws IOException {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = lm.getLastKnownLocation(lm.getBestProvider(new android.location.Criteria(), false));
        double latitude = loc.getLatitude();
        double longitude = loc.getLongitude();
        setApiString(latitude, longitude);
        new JsonGetter(this).execute(apiString);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setApiString(double latitude, double longitude) {
        try {
            apiString = new URL(String.format("https://api.forecast.io/forecast/%s/%s,%s", apikey, latitude, longitude));
        } catch (MalformedURLException e) {
            Toast.makeText(this, "malformed api url", Toast.LENGTH_SHORT).show();
            System.exit(-1);
        }
    }
}

class JsonGetter extends AsyncTask<URL, Void, Double>{

    private MainActivity activity;

    public JsonGetter(MainActivity activity) {
        this.activity = activity;
    }
    @Override
    protected Double doInBackground(URL... url) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = url[0].openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            is.close();
        } catch (IOException e) {
        }
        return getPrecipProb(sb.toString());
    }

    private double getPrecipProb(String s) {
        double precipProb = -1.0;
        try {
            JSONObject obj = new JSONObject(s);
            obj = obj.getJSONObject("minutely");
            JSONArray data = obj.getJSONArray("data");
            obj = data.getJSONObject(5);
            precipProb = obj.getDouble("precipProbability");
        } catch (JSONException e) {
        }
        return precipProb;
    }

    @Override
    protected void onPostExecute(Double d) {
        super.onPostExecute(d);
        boolean rain = false;
        Toast.makeText(activity, d.toString(), Toast.LENGTH_LONG).show();
        if (d > 0.9) {
            rain = true;
        }
        activity.updateRainStatus(rain);

    }
}
