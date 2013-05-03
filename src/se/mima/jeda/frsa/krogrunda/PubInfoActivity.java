package se.mima.jeda.frsa.krogrunda;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.mima.jeda.frsa.krogrunda.JSONparser.MyCallbackInterface;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PubInfoActivity extends Activity implements MyCallbackInterface, OnItemClickListener {

	TextView nameText, addressText, openhoursText, urlText, tapsNo;
	TextView welcomeText;

	ImageView pubImg;
	
	ListView listview;

	private static String url = "http://api.fatkoll.se/json/1.0/getPlaces.json?api_key=fd6950b1499b71037ec3c5a5e01081d6&place_id=";

	static String TAG_LIST = "list";
	static String TAG_TAPS = "taps";
	static String TAG_ID = "id";
	static String TAG_NAME = "name";
	static String TAG_ADDRESS = "address";
	static String TAG_OPEN_HOURS = "openhours";
	static String TAG_URL = "url";
	static String TAG_BREWERY = "brewery";
	static String TAG_ABV = "abv";
	static String TAG_IMG = "img";
	static String TAG_ORIGINAL = "original";

	ArrayList<HashMap<String, String>> tapsAndInfo = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> tapIds = new ArrayList<String>();

	JSONArray list = null;
	JSONObject listObject = null;
	JSONArray taps = null;

	JSONObject c = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pub_info);

		String pubId = getIntent().getExtras().getString("pubId");
		String tapsCount = getIntent().getExtras().getString("tapsCount");

		nameText = (TextView) findViewById(R.id.pubName);
		addressText = (TextView) findViewById(R.id.pubAddress);
		openhoursText = (TextView) findViewById(R.id.pubOpenhours);
		urlText = (TextView) findViewById(R.id.pubUrl);
		tapsNo = (TextView) findViewById(R.id.tapsNo);
		welcomeText = (TextView) findViewById(R.id.pubInfoWelcome);

		pubImg = (ImageView) findViewById(R.id.pubImg);
		
		listview = (ListView)findViewById(android.R.id.list);

		JSONparser parser = new JSONparser(this);
		parser.execute(url + pubId);

		Log.d("Antal taps: ", tapsCount);
		tapsNo.setText(tapsCount);
	}

	@Override
	public void onRequestComplete(JSONObject result) {

		JSONObject json = result;

		try {

			list = json.getJSONArray(TAG_LIST);
			Log.e("onRequestComplete", "Fått JSONArray");

			for (int i = 0; i < list.length(); i++) {

				c = list.getJSONObject(i);

				nameText.setText(c.getString(TAG_NAME));
				welcomeText.setText("Välkommen till " + c.getString(TAG_NAME)
						+ "!");
				addressText.setText(c.getString(TAG_ADDRESS));
				Log.d("Address:", c.getString(TAG_ADDRESS));

				if (c.getString(TAG_OPEN_HOURS).isEmpty()) {
					openhoursText.setText("Ingen info");
				} else {
					openhoursText.setText(c.getString(TAG_OPEN_HOURS));
					Log.d("Öppettider:", c.getString(TAG_OPEN_HOURS));
				}

				if (c.getString(TAG_URL).isEmpty()) {
					urlText.setText("Ingen info");
				} else
					urlText.setText(c.getString(TAG_URL));

				new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							URL imgUrl = new URL(c.getJSONObject(TAG_IMG)
									.getString(TAG_ORIGINAL));
							final Bitmap image = BitmapFactory
									.decodeStream(imgUrl.openConnection()
											.getInputStream());
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									pubImg.setImageBitmap(image);
								}
							});
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}).start();

			}
			listObject = list.getJSONObject(0);
			taps = listObject.getJSONArray(TAG_TAPS);

			for (int i = 0; i < taps.length(); i++) {

				c = taps.getJSONObject(i);

				Log.d("Namn:", c.getString(TAG_NAME));
				Log.d("Bryggeri:", c.getString(TAG_BREWERY));
				Log.d("Abv:", c.getString(TAG_ABV));

				tapIds.add(c.getString(TAG_ID));
				
				HashMap<String, String> info = new HashMap<String, String>();
				info.put(TAG_NAME, c.getString(TAG_NAME));
				info.put(TAG_BREWERY, c.getString(TAG_BREWERY));
				info.put(TAG_ABV, c.getString(TAG_ABV));
				
				tapsAndInfo.add(info);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		SimpleAdapter adapter = new SimpleAdapter(this, tapsAndInfo,
				R.layout.taps_list_item, new String[] { TAG_NAME }, new int[] { R.id.pubName });

		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
//		setListAdapter(adapter);
		
//		ListView lv = getListView();
//		lv.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
