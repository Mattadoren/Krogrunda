package se.mima.jeda.frsa.krogrunda;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.mima.jeda.frsa.krogrunda.JSONparser.MyCallbackInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
	static String TAG_PRICE = "price";
	static String TAG_PRODUCER = "producer";
	static String TAG_RATING_RB_OVERALL = "rating_rb_overall";

	ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> drinks = new ArrayList<String>();
	private ArrayList<String> prices = new ArrayList<String>();
	private ArrayList<String> ratings = new ArrayList<String>();

	ListView lv;
	
	JSONArray list = null;

	@Override
	public void onStart() {
		super.onStart();
		
		lv = getListView();
		lv.setOnItemClickListener(this);
		}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View myFragmentView = inflater.inflate(R.layout.beer_news, container,
				false);

		if (getListAdapter() == null) {
			JSONparser parser = new JSONparser(this);
			parser.execute(url);

			Thread th = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			th.start();
		}
		return myFragmentView;
	}

	@Override
	public void onRequestComplete(JSONObject result) {

		JSONObject json = result;
		
		try {
			list = json.getJSONArray(TAG_ITEMS);

			for (int i = 0; i < list.length(); i++) {
				JSONObject c = list.getJSONObject(i);

				prices.add(c.getString(TAG_PRICE));
				drinks.add(c.getString(TAG_ID));
				ratings.add(c.getString(TAG_RATING_RB_OVERALL));

				HashMap<String, String> info = new HashMap<String, String>();
				
				info.put(TAG_NAME, c.getString(TAG_NAME));
				info.put(TAG_SYSID, "Artikelnr: " + c.getString(TAG_SYSID));
				info.put(TAG_COUNTRY, "Land: " + c.getString(TAG_COUNTRY));
				info.put(TAG_ALC, c.getString(TAG_ALC) + "%");
				info.put(TAG_PRICE, c.getString(TAG_PRICE) + "kr");
				info.put(TAG_PRODUCER, "Bryggeri: " + c.getString(TAG_PRODUCER));
				data.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		SimpleAdapter adapter = new SimpleAdapter(getActivity(), data,
				R.layout.newbeer_list_item, new String[] { TAG_NAME, TAG_SYSID,
						TAG_COUNTRY, TAG_ALC }, new int[] { R.id.name,
						R.id.country });

		setListAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String beerPrice = prices.get(position).toString();
		String drinkIds = drinks.get(position).toString();
		String ratingIds = ratings.get(position).toString();
	
		Intent startBeerNewsInfo = new Intent(getActivity(), BeerNewsInfo.class);
		startBeerNewsInfo.putExtra("drinkIds", drinkIds);
		startBeerNewsInfo.putExtra("beerPrices", beerPrice);
		startBeerNewsInfo.putExtra("ratingIds", ratingIds);
		
		startActivity(startBeerNewsInfo);
	}
}