package se.mima.jeda.frsa.krogrunda;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.mima.jeda.frsa.krogrunda.JSONparser.MyCallbackInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class BeerNewsFrag extends ListFragment implements OnItemClickListener,
		MyCallbackInterface {

	private static String url = "http://systembevakningsagenten.se/api/json/1.0/newProducts.json";

	static String TAG_ITEMS = "items";
	static String TAG_ID = "id";
	static String TAG_NAME = "name";
	static String TAG_SYSID = "sysid";
	static String TAG_COUNTRY = "country";
	static String TAG_ALC = "alcohol_vol";

	private static String name;
	private static String sysid;
	private static String country;
	private static String alc;

	ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> drinks = new ArrayList<String>();

	JSONArray list = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View myFragmentView = inflater.inflate(R.layout.beer_news, container,
				false);

		if (getListAdapter() == null) {

			JSONparser parser = new JSONparser(this);
			parser.execute(url);
		}

		return myFragmentView;
	}

	@Override
	public void onRequestComplete(JSONObject result) {
		JSONObject json = result;

		try {

			list = json.getJSONArray(TAG_ITEMS);
			Log.e("onRequestComplete", "Fått JSONArray");

			for (int i = 0; i < list.length(); i++) {

				JSONObject c = list.getJSONObject(i);

				drinks.add(c.getString(TAG_ID));

				HashMap<String, String> info = new HashMap<String, String>();
				info.put(TAG_NAME, c.getString(TAG_NAME));
				info.put(TAG_SYSID, "Artikelnr: " + c.getString(TAG_SYSID));
				info.put(TAG_COUNTRY, c.getString(TAG_COUNTRY));
				info.put(TAG_ALC, c.getString(TAG_ALC) + "%");

				data.add(info);

				name = c.getString(TAG_NAME);
				sysid = c.getString(TAG_SYSID);
				country = c.getString(TAG_COUNTRY);
				alc = c.getString(TAG_ALC);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), data,
				android.R.layout.simple_list_item_2, new String[] { TAG_NAME,
						TAG_SYSID, TAG_COUNTRY, TAG_ALC }, new int[] {
						android.R.id.text1, android.R.id.text2 });

		setListAdapter(adapter);

		ListView lv = getListView();
		lv.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

}
