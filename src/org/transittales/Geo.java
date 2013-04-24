package org.transittales;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

public class Geo implements LocationListener {
	static final String tag = "Geo";
	private static Geo instance = null;
	private static long msInterval = 30000;
	private static float mtDistance = 10.0f;
	public LocationManager lm = null;
	private boolean listeningToUpdates = false;
	private boolean providerEnabled = false;

	public static Geo getInstance() {
		if (null == instance) {
			instance = new Geo();
		}
		return instance;
	}

	public void setLM(LocationManager _lm) {
		if (null == lm) {
			lm = _lm;
		}
	}

	public void pause() {
		if (!AppUtils.getInstance().isRunning()) {
			lm.removeUpdates(this);
			listeningToUpdates = false;
		}
	}

	public void resume() {
		if (!listeningToUpdates) {
			try {
				lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
						msInterval, mtDistance, this);
				listeningToUpdates = true;
			} catch (Exception e) {
				Log.e(tag, e.getMessage());
			}
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void onLocationChanged(Location location) {
		double lon = location.getLongitude();
		double lat = location.getLatitude();
	}

	@Override
	public void onProviderDisabled(String s) {
		// GPS is disabled in settings
		Log.d(tag, "onProviderDisabled: " + s);
		providerEnabled = false;
	}

	@Override
	public void onProviderEnabled(String s) {
		Log.d(tag, "onProviderEnabled: " + s);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch (status) {
		case LocationProvider.OUT_OF_SERVICE:
			Log.v(tag, "Status Changed: Out of Service");
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			Log.v(tag, "Status Changed: Temporarily Unavailable");
			break;
		case LocationProvider.AVAILABLE:
			Log.v(tag, "Status Changed: Available");
			break;
		}
	}
}
