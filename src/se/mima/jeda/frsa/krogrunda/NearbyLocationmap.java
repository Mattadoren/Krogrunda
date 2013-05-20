package se.mima.jeda.frsa.krogrunda;

import java.io.ObjectOutputStream.PutField;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.mima.jeda.frsa.krogrunda.JSONparser.MyCallbackInterface;
import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CameraPosition.Builder;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearbyLocationmap extends Activity implements MyCallbackInterface {
	private GoogleMap mmap;
	private double lat;
	private double lon;
	private String name;
	private String address;


	private static String url = "http://api.fatkoll.se/json/1.0/getPlaces.json?api_key=fd6950b1499b71037ec3c5a5e01081d6";
	private static String url2 = "http://agent.nocrew.org/api/json/1.0/searchStore.json?";

	static String TAG_LIST = "list";
	static String TAG_ID = "id";
	static String TAG_NAME = "name";
	static String TAG_ADDRESS = "address";
	static String TAG_TAPS_COUNT = "taps_count";
	static String TAG_LAT = "lat";
	static String TAG_LON = "lng";
	static String TAG_OPENHOURS = "openhours";



	ArrayList<HashMap<String, String>> pubsAndCount = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> pubIds = new ArrayList<String>();
	private ArrayList<String> tapsCount = new ArrayList<String>();
	private ArrayList<String> openhours = new ArrayList<String>();
	private ArrayList<String> adress = null;
	private ArrayList<String> longitud = null;
	private ArrayList<String> latitude = null;
	JSONArray list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby_locationmap);

		mmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mmap.setMyLocationEnabled(true);
		Builder cameraPosition = new CameraPosition.Builder();

		lat = getIntent().getExtras().getDouble("lattitud");
		Log.e(Double.toString(lat), "Nearby lat");
		lon = getIntent().getExtras().getDouble("longitud");
		Log.e(Double.toString(lon), "Nearby lon");

		GetSystembolaglocation gsl = new GetSystembolaglocation(this, lat, lon);

		LatLng LatLng = new LatLng(lat, lon);
		mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng, 13));

		final JSONparser parser = new JSONparser(this);
		parser.execute(url + "&lat=" + lat + "&lng=" + lon + "&dist_km=5");
		Log.e("hej", url + "&lat=" + lat + "&lng=" + lon + "&dist_km=5");
		Log.e(TAG_NAME, "namn");
		



	}

	public void addMarker(double lat, double lon, String address) {
		LatLng latlng = new LatLng(lat, lon);
		mmap.addMarker(new MarkerOptions().position(latlng).title(address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
	}

	@Override
	public void onRequestComplete(JSONObject result) {
		Log.d("KÖR2GOGOGOGOGO", "KÖR2GOGOGOGOGOGO");
		JSONObject json = result;

		try {

			list = json.getJSONArray(TAG_LIST);
			Log.e("onRequestComplete", "Fått JSONArray");

			for (int i = 0; i < list.length(); i++) {
				JSONObject c = list.getJSONObject(i);

				pubIds.add(c.getString(TAG_ID));

				HashMap<String, String> info = new HashMap<String, String>();
				LatLng latlng = new LatLng(Double.parseDouble(c
						.getString(TAG_LAT)), Double.parseDouble(c
						.getString(TAG_LON)));
				Log.e(c.getString(TAG_LAT), "latitud för pubar i närheten.");
				mmap.addMarker(new MarkerOptions().position(latlng)
						.title(c.getString(TAG_NAME))
						.snippet(c.getString(TAG_ADDRESS)));

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
