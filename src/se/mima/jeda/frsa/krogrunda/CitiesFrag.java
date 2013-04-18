package se.mima.jeda.frsa.krogrunda;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.mima.jeda.frsa.krogrunda.JSONparser.MyCallbackInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CitiesFrag extends ListFragment implements OnItemClickListener,
		MyCallbackInterface {

	private static String url = "http://api.fatkoll.se/json/1.0/getCities.json?api_key=fd6950b1499b71037ec3c5a5e01081d6";

	static String TAG_LIST = "list";
	static String TAG_ID = "id";
	static String TAG_NAME = "name";
	static String TAG_PLACES_COUNT = "places_count";

	private ArrayList<HashMap<String, String>> citysAndCount = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> cityIds = new ArrayList<String>();

	JSONArray list = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		
		
		View myFragmentView = inflater.inflate(R.layout.cities, container,
				false);

		if (getListAdapter() == null) {
			JSONparser parser = new JSONparser(this);
			Log.d("KÖR1", "KÖR1");
			parser.execute(url);
			
		}

		return myFragmentView;
	}

	@Override
	public void onRequestComplete(JSONObject result) {
		Log.d("KÖR2", "KÖR2");
		JSONObject json = result;

		try {

			list = json.getJSONArray(TAG_LIST);
			Log.e("onRequestComplete", "Fått JSONArray");

			for (int i = 0; i < list.length(); i++) {
				JSONObject c = list.getJSONObject(i);
				
				cityIds.add(c.getString(TAG_ID));
				
				HashMap<String, String> info = new HashMap<String, String>();
				
				info.put(TAG_NAME, c.getString(TAG_NAME).toUpperCase());
				info.put(TAG_PLACES_COUNT, c.getString(TAG_PLACES_COUNT));
				
				citysAndCount.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), citysAndCount,
				R.layout.city_list_item, new String[] { TAG_NAME,
						TAG_PLACES_COUNT}, new int[] { R.id.cityNameText,
						R.id.pubsCount });
		
		setListAdapter(adapter);
		ListView lv = getListView();
		lv.setOnItemClickListener(this);
		

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String cityId = cityIds.get(position).toString();
		Intent startPubsListIntent = new Intent(getActivity(),
				PubsActivity.class);
		startPubsListIntent.putExtra("cityId", cityId);
		startActivity(startPubsListIntent);

	}

}
