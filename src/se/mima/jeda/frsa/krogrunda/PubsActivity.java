package se.mima.jeda.frsa.krogrunda;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.mima.jeda.frsa.krogrunda.JSONparser.MyCallbackInterface;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PubsActivity extends ListActivity implements OnItemClickListener, MyCallbackInterface{

	private static String url = "http://api.fatkoll.se/json/1.0/getPlaces.json?api_key=fd6950b1499b71037ec3c5a5e01081d6&city_id=";
	

	static String TAG_LIST = "list";
	static String TAG_ID = "id";
	static String TAG_NAME = "name";
	static String TAG_ADDRESS = "address";
	static String TAG_OPEN_HOURS = "openhours";
	static String TAG_LAT = "lat";
	static String TAG_LNG = "lng";
	
	private static String address;
	private static String openhours;
	private static String lat;
	private static String lng;
	
	private ArrayList<String> names = new ArrayList<String>();
	private ArrayList<String> pubIds = new ArrayList<String>();
	
	JSONArray list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pubs);

		Intent getCityId = getIntent();
		String cityId = getCityId.getExtras().getString("cityId");
		
		JSONparser parser = new JSONparser(this);		
		parser.execute(url);

	}

	

	@Override
	public void onRequestComplete(JSONObject result) {
		
		JSONObject json = result;
		
		try {
			
			list = json.getJSONArray(TAG_LIST);
			Log.e("onRequestComplete", "Fått JSONArray");
			
			
			for (int i = 0; i < list.length(); i++){
				JSONObject c = list.getJSONObject(i);
				Log.e("onRequestComplete", list.getString(i));
				pubIds.add(c.getString(TAG_ID));
				names.add(c.getString(TAG_NAME));
				address = c.getString(TAG_ADDRESS);
				openhours = c.getString(TAG_OPEN_HOURS);
				lat = c.getString(TAG_LAT);
				lng = c.getString(TAG_LNG);
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
		Log.e("onRequestComplete", "Skapar ArrayAdapter");
		setListAdapter(adapter);
		Log.e("onRequestComplete", "Sätter ListAdapter");
		ListView lv = getListView();
		lv.setOnItemClickListener(this);
		
//		ListView lv = (ListView)findViewById(R.id.pubsList);
//    	lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names));
//    	lv.setOnItemClickListener(this);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		String pubId = pubIds.get(position).toString();
		Intent startPubInfoIntent = new Intent(this, PubInfoActivity.class);
		startPubInfoIntent.putExtra("pubId", pubId);
		startActivity(startPubInfoIntent);
		
	}

}
