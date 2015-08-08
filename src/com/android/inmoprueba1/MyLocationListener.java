package com.android.inmoprueba1;

import android.location.Location;
import android.location.LocationListener;

import android.os.Bundle;

public class MyLocationListener implements LocationListener {

	@Override
	public void onLocationChanged(Location loc) {
		System.out.println("dentro de onlocationchanged ");
		// TODO Auto-generated method stub
		if (loc != null) {

			String latitude = String.valueOf(loc.getLatitude());
			String longitude = String.valueOf(loc.getLongitude());
		}// end if

		String Text = "My current location is: " + "Latitud = "
				+ loc.getLatitude() + "Longitud = " + loc.getLongitude();
		System.out.println("direccion: " + Text);

	}// end onLocationChanged

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}
