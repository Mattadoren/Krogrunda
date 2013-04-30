package se.mima.jeda.frsa.krogrunda;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.mima.jeda.frsa.krogrunda.JSONparser.MyCallbackInterface;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class NearbyFrag extends ListFragment implements OnItemClickListener,
MyCallbackInterface{

	public static double lat;
	public static double lng;
	
	private static String url = "http://api.fatkoll.se/json/1.0/getPlaces.json?api_key=fd6950b1499b71037ec3c5a5e01081d6";
	

	
	static String TAG_LIST = "list";
	static String TAG_ID = "id";
	static String TAG_NAME = "name";
	static String TAG_ADDRESS = "address";
	static String TAG_TAPS_COUNT = "taps_count";
	
	ArrayList<HashMap<String, String>> pubsAndCount = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> pubIds = new ArrayList<String>();

	JSONArray list = null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View myFragmentView = inflater.inflate(R.layout.nearby, container,
				false);
		
		Thread th = new Thread() {
				Getlocation getloc = new Getlocation(getActivity(),NearbyFrag.this);
				
				
				
		};
		try {
			th.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
			
		final JSONparser parser = new JSONparser(this);
		Thread th2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (getListAdapter() == null) {
					Log.d("KÖR1GOGOGOOGOGO", "KÖR1GOGOGOGOGO");
						parser.execute(url+"&lat="+lat+"&lng="+lng+"&dist_km=5");
					Log.e("hej",url+"&lat="+lat+"&lng="+lng+"&dist_km=5" );
					
				}
				
			}
			
		});
		th2.start();
		

		
		
		return myFragmentView;
		
	 
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
				
				info.put(TAG_NAME, c.getString(TAG_NAME).toUpperCase());
				info.put(TAG_ADDRESS, c.getString(TAG_ADDRESS));
				info.put(TAG_TAPS_COUNT, c.getString(TAG_TAPS_COUNT));

				pubsAndCount.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), pubsAndCount,
				R.layout.nearby_list_item, new String[] { TAG_NAME,
						TAG_ADDRESS, TAG_TAPS_COUNT}, new int[] { R.id.pubName,
						R.id.pubAddress, R.id.tapsCount });
		
		setListAdapter(adapter);
		ListView lv = getListView();
		lv.setOnItemClickListener(this);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}
	
}
