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
import android.widget.ImageView;
import android.widget.TextView;

public class PubInfoActivity extends Activity implements MyCallbackInterface {

	TextView nameText, addressText, openhoursText, urlText;

	ImageView pubImg;

	private static String url = "http://api.fatkoll.se/json/1.0/getPlaces.json?api_key=fd6950b1499b71037ec3c5a5e01081d6&place_id=";

	static String TAG_LIST = "list";
	static String TAG_ID = "id";
	static String TAG_NAME = "name";
	static String TAG_ADDRESS = "address";
	static String TAG_OPEN_HOURS = "openhours";
	static String TAG_URL = "url";
	static String TAG_IMG = "img";
	static String TAG_ORIGINAL = "original";

	ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> tapIds = new ArrayList<String>();

	JSONArray list = null;
	JSONArray taps = null;

	JSONObject c = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pub_info);

		String pubId = getIntent().getExtras().getString("pubId");

		nameText = (TextView) findViewById(R.id.pubName);
		addressText = (TextView) findViewById(R.id.pubAddress);
		openhoursText = (TextView) findViewById(R.id.pubOpenhours);
		urlText = (TextView) findViewById(R.id.pubUrl);

		pubImg = (ImageView) findViewById(R.id.pubImg);

		JSONparser parser = new JSONparser(this);
		parser.execute(url + pubId);
	}

	@Override
	public void onRequestComplete(JSONObject result) {

		JSONObject json = result;

		try {

			list = json.getJSONArray(TAG_LIST);
			Log.e("onRequestComplete", "Fått JSONArray");

			for (int i = 0; i < list.length(); i++) {

				c = list.getJSONObject(i);

				// tapIds.add(c.getString(TAG_ID)); // Flytta?
				//
				// HashMap<String, String> info = new HashMap<String, String>();
				// // Flytta?
				// info.put(TAG_NAME, c.getString(TAG_NAME)); // Flytta?
				// info.put(TAG_ADDRESS, c.getString(TAG_ADDRESS)); // Flytta?

				// data.add(info); // Flytta?

				nameText.setText(c.getString(TAG_NAME));
				addressText.setText(c.getString(TAG_ADDRESS));

				if (c.getString(TAG_OPEN_HOURS).isEmpty()) {
					openhoursText.setText("Ingen info");
				} else
					openhoursText.setText(c.getString(TAG_OPEN_HOURS));
				

				if (c.getString(TAG_URL).isEmpty()) {
					urlText.setText("Ingen info");
				} else
					urlText.setText(c.getString(TAG_URL));

				// this.runOnUiThread(new Runnable() {
				//
				// @Override
				// public void run() {
				//
				// try {
				// URL imgUrl = new
				// URL(c.getJSONObject(TAG_IMG).getString(TAG_LARGE));
				// Bitmap image =
				// BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());
				// pubImg.setImageBitmap(image);
				// } catch (MalformedURLException e) {
				// e.printStackTrace();
				// } catch (IOException e){
				// e.printStackTrace();
				// } catch (JSONException e){
				// e.printStackTrace();
				// }
				//
				// }
				// });

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
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
