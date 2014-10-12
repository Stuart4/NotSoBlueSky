package com.foobar.jake.notsobluesky;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * NotSoBlueSky
 *
 * @author Jacob Stuart
 * @lab 806
 * @date October 11, 2014
 */
public class RainStatus implements Runnable{
	@Override
	public void run() throws IOException{
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location loc = lm.getLastKnownLocation(lm.getBestProvider(new android.location.Criteria() ,false));
		double latitude = loc.getLatitude();
		double longitude = loc.getLongitude();
		java.util.Locale locale = new java.util.Locale(String.valueOf(latitude), String.valueOf(longitude));
		Geocoder geo = new Geocoder(null);
		List<Address> addressesList = geo.getFromLocation(latitude, longitude, 1);
		String city =  addressesList.get(0).getLocality();
	}
}
