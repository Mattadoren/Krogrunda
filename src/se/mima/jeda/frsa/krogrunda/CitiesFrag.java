package se.mima.jeda.frsa.krogrunda;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.mima.jeda.frsa.krogrunda.JSONparser.MyCallbackInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CitiesFrag extends ListFragment implements OnItemClickListener, MyCallbackInterface{
	
	private static String url = "http://api.fatkoll.se/json/1.0/getCities.json?api_key=fd6950b1499b71037ec3c5a5e01081d6";
	
	static String TAG_LIST = "list";
	static String TAG_ID ="id";
	static String TAG_NAME = "name";
	
	private ArrayList<String> cities = new ArrayList<String>();
	private ArrayList<String> cityIds = new ArrayList<String>();
	
	JSONArray list = null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View myFragmentView  = inflater.inflate(R.layout.cities, container, false);
		
		JSONparser parser = new JSONparser(this);		
		parser.execute(url);
		
				
		return myFragmentView;
	}
	
	@Override
	public void onRequestComplete(JSONObject result){

		
		JSONObject json = result;
		
		try {
			
			list = json.getJSONArray(TAG_LIST);
			Log.e("onRequestComplete", "Fått JSONArray");
			
			
			for (int i = 0; i < list.length(); i++){
				JSONObject c = list.getJSONObject(i);
				Log.e("onRequestComplete", list.getString(i));
				cityIds.add(c.getString(TAG_ID));
				cities.add(c.getString(TAG_NAME));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, cities);
		Log.e("onRequestComplete", "Skapar ArrayAdapter");
		setListAdapter(adapter);
		ListView lv = getListView();
		lv.setOnItemClickListener(this);
		Log.e("onRequestComplete", "Sätter ListAdapter");
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String cityId = cityIds.get(position).toString();
		Intent startPubsListIntent = new Intent(getActivity(), PubsActivity.class);
		startPubsListIntent.putExtra("cityId", cityId);
		startActivity(startPubsListIntent);
		
	}
	
	
} 
