package se.mima.jeda.frsa.krogrunda;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import se.mima.jeda.frsa.krogrunda.JSONparser.MyCallbackInterface;
import android.R.drawable;
import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class BeerNewsInfo extends Activity implements MyCallbackInterface {

	TextView nameinfo, producerinfo, sysidinfo, countryinfo, alcinfo,
			priceinfo, idinfo;

	ImageView beerimginfo;

	private static String url = "http://systembevakningsagenten.se/api/json/1.0/inventoryForProduct.json?id=";
	private static String imgurl = "http://systembevakningsagenten.se/images/product/id/";

	static String TAG_PRODUCT = "product";
	static String TAG_NAME = "name";
	static String TAG_SYSID = "sysid";
	static String TAG_COUNTRY = "country";
	static String TAG_ALC = "alcohol_vol";
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

		nameinfo = (TextView) findViewById(R.id.nameinfo);
		sysidinfo = (TextView) findViewById(R.id.sysidinfo);
		countryinfo = (TextView) findViewById(R.id.countryinfo);
		alcinfo = (TextView) findViewById(R.id.alcinfo);
		producerinfo = (TextView) findViewById(R.id.producerinfo);
		beerimginfo = (ImageView) findViewById(R.id.beerimginfo);

		JSONparser parser = new JSONparser(this);
		parser.execute(url + drinkIds);
	}

	@Override
	public void onRequestComplete(JSONObject result) {
		JSONObject json = result;
		final String drinkIds = getIntent().getExtras().getString("drinkIds");

		try {
			items = json.getJSONObject(TAG_PRODUCT);
			Log.e("onRequestComplete", "hämtar öldatan");
			Log.d("Hämtar öl", "öl");

			nameinfo.setText(items.getString(TAG_NAME));
			sysidinfo.setText(items.getString(TAG_SYSID));
			countryinfo.setText(items.getString(TAG_COUNTRY));
			alcinfo.setText(items.getString(TAG_ALC) + "%");
			producerinfo.setText(items.getString(TAG_PRODUCER));

			new Thread(new Runnable() {

				@Override
				public void run() {

					try {
						URL imgUrl = new URL(
								"http://systembevakningsagenten.se/	images/product/id/"
										+ drinkIds + ".jpg");
						final Bitmap image = BitmapFactory.decodeStream(imgUrl
								.openConnection().getInputStream());

						if (image != null) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									beerimginfo.setImageBitmap(image);
								}
							});
						} else {
							beerimginfo
									.setImageResource(drawable.arrow_down_float);
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}).start();

		} catch (JSONException e) {
			e.printStackTrace();

		}

	}

}
