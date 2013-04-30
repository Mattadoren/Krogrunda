package se.mima.jeda.frsa.krogrunda;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.mima.jeda.frsa.krogrunda.JSONparser.MyCallbackInterface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class BeerNewsInfo extends Activity implements MyCallbackInterface {

	TextView text1, text2, text3, text4;

	ImageView beerimg;

	private static String url = "http://systembevakningsagenten.se/api/json/1.0/inventoryForProduct.json?id=";
	private static String imgurl = "http://systembevakningsagenten.se/images/product/id/";

	static String TAG_PRODUCT = "product";
	static String TAG_ID = "id";
	static String TAG_NAME = "name";
	static String TAG_SYSID = "sysid";
	static String TAG_COUNTRY = "country";
	static String TAG_ALC = "alcohol_vol";
	static String TAG_PRICE = "price";
	static String TAG_PRODUCER = "producer";

	ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> drinks = new ArrayList<String>();

	JSONObject items = null;

	JSONObject c = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beer_news_info);

		String drinkIds = getIntent().getExtras().getString("drinkIds");

		text1 = (TextView) findViewById(R.id.text1);
		text2 = (TextView) findViewById(R.id.text2);
		text3 = (TextView) findViewById(R.id.text3);
		text4 = (TextView) findViewById(R.id.text4);

		beerimg = (ImageView) findViewById(R.id.beerimg);

		JSONparser parser = new JSONparser(this);
		parser.execute(url + drinkIds);
	}

	@Override
	public void onRequestComplete(JSONObject result) {
		JSONObject json = result;

		try {
			items = json.getJSONObject(TAG_PRODUCT);
			Log.e("onRequestComplete", "hämtar öldatan");
			Log.d("Hämtar öl", "öl");

//			for (int i = 0; i < items.length(); i++) {
//				c = items.getJSONObject(i);

				text1.setText(items.getString(TAG_NAME));
				text2.setText(items.getString(TAG_PRODUCER));
//			}

		} catch (JSONException e) {
			e.printStackTrace();

		}

	}

}
