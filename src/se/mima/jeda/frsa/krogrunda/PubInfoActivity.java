package se.mima.jeda.frsa.krogrunda;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.mima.jeda.frsa.krogrunda.JSONparser.MyCallbackInterface;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PubInfoActivity extends Activity implements MyCallbackInterface, OnClickListener {

	TextView nameText, addressText, openhoursText, urlText, tapsNo;
	ImageView pubImg;
	Button tapsListButton;
	String pubId;

	private static String url = "http://api.fatkoll.se/json/1.0/getPlaces.json?api_key=fd6950b1499b71037ec3c5a5e01081d6&place_id=";

	static String TAG_LIST = "list";
	static String TAG_ID = "id";
	static String TAG_NAME = "name";
	static String TAG_ADDRESS = "address";
	static String TAG_OPEN_HOURS = "openhours";
	static String TAG_URL = "url";
	static String TAG_IMG = "img";
	static String TAG_ORIGINAL = "original";

	JSONArray list = null;
	JSONObject listObject = null;
	JSONArray taps = null;

	JSONObject c = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pub_info);
		
		

		pubId = getIntent().getExtras().getString("pubId");
		String tapsCount = getIntent().getExtras().getString("tapsCount");

		nameText = (TextView) findViewById(R.id.pubInfoName);
		addressText = (TextView) findViewById(R.id.pubInfoAddress);
		openhoursText = (TextView) findViewById(R.id.pubInfoOpenhours);
		urlText = (TextView) findViewById(R.id.pubInfoUrl);
		tapsNo = (TextView) findViewById(R.id.pubInfoTapsNo);
		tapsListButton = (Button)findViewById(R.id.pubInfoButton);
		tapsListButton.setOnClickListener(this);

		pubImg = (ImageView) findViewById(R.id.pubInfoImg);

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
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		Intent startTapsListIntent = new Intent(this,
				TapsActivity.class);
		startTapsListIntent.putExtra("pubId", pubId);
		startActivity(startTapsListIntent);
		
	}
}