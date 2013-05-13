package se.mima.jeda.frsa.krogrunda;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.mima.jeda.frsa.krogrunda.JSONparser.MyCallbackInterface;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class TapsActivity extends ListActivity implements OnItemClickListener,
		MyCallbackInterface {

	static String TAG_LIST = "list";
	static String TAG_TAPS = "taps";
	static String TAG_ID = "id";
	static String TAG_NAME = "name";
	static String TAG_ABV = "abv";
	static String TAG_BREWERY = "brewery";
	
	private static String url = "http://api.fatkoll.se/json/1.0/getPlaces.json?api_key=fd6950b1499b71037ec3c5a5e01081d6&place_id=";

	ArrayList<HashMap<String, String>> tapsList = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> tapIds = new ArrayList<String>();

	JSONArray list = null;
	JSONArray taps = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taps);

		String pubId = getIntent().getExtras().getString("pubId");

		JSONparser parser = new JSONparser(this);
		parser.execute(url+pubId);
		
	}

	@Override
	public void onRequestComplete(JSONObject result) {
		JSONObject json = result;

		try {
			list = json.getJSONArray(TAG_LIST);
			JSONObject listObject = list.getJSONObject(0);
			taps = listObject.optJSONArray(TAG_TAPS);
		

			for (int i = 0; i < taps.length(); i++) {
				JSONObject c = taps.getJSONObject(i);

				tapIds.add(c.getString(TAG_ID));
				
				HashMap<String, String> info = new HashMap<String, String>();
				info.put(TAG_NAME, c.getString(TAG_NAME));
				info.put(TAG_BREWERY, c.getString(TAG_BREWERY));
				info.put(TAG_ABV, c.getString(TAG_ABV));
				
				tapsList.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		SimpleAdapter adapter = new SimpleAdapter(this, tapsList,
				R.layout.taps_list_item, new String[] { TAG_NAME,
						TAG_BREWERY, TAG_ABV}, new int[] { R.id.tapsListName,
						R.id.tapsListBrewery, R.id.tapsListAbv});

		setListAdapter(adapter);
		
		ListView lv = getListView();
		lv.setOnItemClickListener(this);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String tapId = tapIds.get(position).toString();
		Intent startTapInfoIntent = new Intent(this, TapInfoActivity.class);
		startTapInfoIntent.putExtra("tapId", tapId);
		startActivity(startTapInfoIntent);
	}
}