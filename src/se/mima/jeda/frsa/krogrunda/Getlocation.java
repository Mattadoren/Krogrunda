package se.mima.jeda.frsa.krogrunda;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class Getlocation {

	LocationManager mlocManager;
	LocationListener mlocListener;

	double lat = 0, lng = 0;

	private NearbyFrag nearbyFrag;

	public Getlocation(Context mContext, NearbyFrag nearbyFrag) {
		this.nearbyFrag = nearbyFrag;

		mlocManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);

		mlocListener = new MyLocationListener();

		mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
				0, mlocListener);
	}

	public class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location loc) {
			loc.getLatitude();
			loc.getLongitude();

			lat = loc.getLatitude();
			lng = loc.getLongitude();

			Callparrent();
		}

		public void Callparrent() {
			nearbyFrag.getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					NearbyFrag.lat = lat;
					NearbyFrag.lng = lng;

					mlocManager.removeUpdates(mlocListener);
				}
			});
		}

		public void onProviderDisabled(String provider) {
			Toast.makeText(nearbyFrag.getActivity(),
					"GPS ej aktiverad, var god aktivera.", Toast.LENGTH_SHORT)
					.show();
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			Toast.makeText(nearbyFrag.getActivity(),
					"GPS aktiverad, var god vänta.", Toast.LENGTH_SHORT).show();
		}
	}
}