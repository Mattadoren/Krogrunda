package se.mima.jeda.frsa.krogrunda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.mima.jeda.frsa.krogrunda.JSONparser.MyCallbackInterface;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PubsActivity extends ListActivity implements OnItemClickListener,
		MyCallbackInterface {

	private static String url = "http://api.fatkoll.se/json/1.0/getPlaces.json?api_key=fd6950b1499b71037ec3c5a5e01081d6&city_id=";

	static String TAG_LIST = "list";
	static String TAG_ID = "id";
	static String TAG_NAME = "name";
	static String TAG_ADDRESS = "address";
	
	ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> pubIds = new ArrayList<String>();

	JSONArray list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pubs);

		Intent getCityId = getIntent();
		String cityId = getCityId.getExtras().getString("cityId");

		JSONparser parser = new JSONparser(this);
		parser.execute(url + cityId);

	}

	@Override
	public void onRequestComplete(JSONObject result) {

		JSONObject json = result;

		try {

			list = json.getJSONArray(TAG_LIST);
			Log.e("onRequestComplete", "Fått JSONArray");

			for (int i = 0; i < list.length(); i++) {
				
				JSONObject c = list.getJSONObject(i);
				
				pubIds.add(c.getString(TAG_ID));

				HashMap<String, String> info = new HashMap<String, String>();
				info.put(TAG_NAME, c.getString(TAG_NAME));
				info.put(TAG_ADDRESS, c.getString(TAG_ADDRESS));

				data.add(info);
				
				if (url.isEmpty()){
					url = "Ingen info";
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		SimpleAdapter adapter = new SimpleAdapter(this, data,
				android.R.layout.simple_list_item_2, new String[] { TAG_NAME,
						TAG_ADDRESS }, new int[] { android.R.id.text1,
						android.R.id.text2 });

		setListAdapter(adapter);
		
		ListView lv = getListView();
		lv.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		String pubId = pubIds.get(position).toString();
		Intent startPubInfoIntent = new Intent(this, PubInfoActivity.class);
		startPubInfoIntent.putExtra("pubId", pubId);
		startActivity(startPubInfoIntent);

	}

}
