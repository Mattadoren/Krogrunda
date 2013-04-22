package se.mima.jeda.frsa.krogrunda;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class Getlocation {
	Context mContext;
	LocationManager mlocManager;
	LocationListener mlocListener;
	double lat = 0, lng = 0;
	private NearbyFrag nearByFrag;

	public Getlocation(Context mContext, NearbyFrag nearByFrag) {
		this.nearByFrag = nearByFrag;
		this.mContext = mContext;
		mlocManager = (LocationManager) mContext
				.getSystemService(mContext.LOCATION_SERVICE);
		mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
				0, mlocListener);
	}

	public class MyLocationListener implements LocationListener {
		private static final int TWO_MINUTES = 1000 * 60 * 2;

		public void onLocationChanged(Location loc) {
			loc.getLatitude();
			loc.getLongitude();

			String Text = "My current location is: " + "Latitud = "
					+ loc.getLatitude() + "Longitud = " + loc.getLongitude();
			// Toast.makeText( mContext,Text,Toast.LENGTH_SHORT).show();
			lat = loc.getLatitude();
			lng = loc.getLongitude();
			
			Callparrent();
		}

		public void Callparrent(){
			nearByFrag.getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					nearByFrag.lat = lat;
					nearByFrag.lng = lng;
					Toast.makeText(nearByFrag.getActivity(), lat+" och "+lng, Toast.LENGTH_LONG).show();
					mlocManager.removeUpdates(mlocListener);
					
				}
			});
		}

		public void onProviderDisabled(String provider) {
			Toast.makeText(mContext, "Gps Disabled", Toast.LENGTH_SHORT).show();
		}

		public void onProviderEnabled(String provider) {
			Toast.makeText(mContext, " Gps Enabled", Toast.LENGTH_SHORT).show();
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

	}

}
