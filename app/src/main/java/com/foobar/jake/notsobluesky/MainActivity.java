package com.foobar.jake.notsobluesky;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Random;


public class MainActivity extends Activity {
	private boolean setForRain = false;
	private TextView contentBlurb;
	private ImageView image;
	private Button affirmativeButton;
	private Button negativeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	contentBlurb = (TextView) findViewById(R.id.statusBlurb);
    	affirmativeButton = (Button) findViewById(R.id.affirmativeButton);
	negativeButton = (Button) findViewById(R.id.negativeButton);
	image = (ImageView) findViewById(R.id.imageView);
    }

	public void updateRainStatus() throws IOException{
		boolean rain = rainStatus();
		//GO HOME!!!
		if (setForRain == rain) {
			return;
		}

		//Update interface

		if (rain) {
			contentBlurb.setText(getString(R.string.rain));
			affirmativeButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.affirmativegray, 0);
			negativeButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.negativeblue, 0);
			showRainPicture(true);
			setForRain = true;
		} else {
			contentBlurb.setText(getString(R.string.notRain));
			affirmativeButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.negativegray, 0);
			negativeButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.affirmativeblue, 0);
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

	public void affirmativeButtonClicked(View view) throws IOException{
		updateRainStatus();
	}

	public void negativeButtonClicked(View view) throws IOException{
		updateRainStatus();
	}

	private boolean rainStatus() throws IOException{
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location loc = lm.getLastKnownLocation(lm.getBestProvider(new android.location.Criteria() ,false));
		double latitude = loc.getLatitude();
		double longitude = loc.getLongitude();
		java.util.Locale locale = new java.util.Locale(String.valueOf(latitude), String.valueOf(longitude));
		Geocoder geo = new Geocoder(this);
		List<Address> addressesList = geo.getFromLocation(latitude, longitude, 1);
		String city =  addressesList.get(0).getLocality();
		Toast.makeText(this, city, Toast.LENGTH_SHORT).show();

		Random rnd = new Random();
		int res = rnd.nextInt(2);
		if (res == 1)
			return true;
		return false;
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
}
