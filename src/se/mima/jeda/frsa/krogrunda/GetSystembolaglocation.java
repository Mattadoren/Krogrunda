package se.mima.jeda.frsa.krogrunda;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.mima.jeda.frsa.krogrunda.JSONparser.MyCallbackInterface;

public class GetSystembolaglocation implements MyCallbackInterface {

	private static String url = "http://agent.nocrew.org/api/json/1.0/searchStore.json?lat=";

	static String TAG_ITEMS = "items";
	static String TAG_ID = "id";
	static String TAG_ADRESS = "address";
	static String TAG_LAT = "lat";
	static String TAG_LNG = "lng";
	private double lat;
	private double lon;

	NearbyLocationmap map;

	static ArrayList<String> addressArray = new ArrayList<String>();
	ArrayList<String> latitud = new ArrayList<String>();
	ArrayList<String> longitud = new ArrayList<String>();

	public GetSystembolaglocation(NearbyLocationmap map, double lat, double lon) {
		this.map = map;
		this.lat = lat;
		this.lon = lon;
		Startparser();
	}

	public ArrayList<String> getAddress() {
		return addressArray;

	}

	public ArrayList<String> getLongitud() {
		return longitud;
	}

	public ArrayList<String> getLatitud() {
		return latitud;
	}

	JSONArray list = null;

	public void Startparser() {
		final JSONparser parser = new JSONparser(this);
		parser.execute(url + lat + "&lng=" + lon + "&dist_km=15");
	}

	@Override
	public void onRequestComplete(JSONObject result) {
		JSONObject json = result;

		try {
			list = json.getJSONArray(TAG_ITEMS);

			for (int i = 0; i < list.length(); i++) {
				final JSONObject c = list.getJSONObject(i);

				map.runOnUiThread(new Runnable() {
					public void run() {
						try {
							map.addMarker(
									Double.parseDouble(c.getString(TAG_LAT)),
									Double.parseDouble(c.getString(TAG_LNG)),
									c.getString(TAG_ADRESS));
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}