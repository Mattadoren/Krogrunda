package se.mima.jeda.frsa.krogrunda;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.mima.jeda.frsa.krogrunda.JSONparser.MyCallbackInterface;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class TapInfoActivity extends Activity implements MyCallbackInterface {

	TextView tapInfoName, tapInfoAbv, tapInfoBrewery, tapInfoCountry,
			tapInfoType;
	ImageView tapInfoImg;
	
	private static String url = "http://api.fatkoll.se/json/1.0/getTaps.json?api_key=fd6950b1499b71037ec3c5a5e01081d6&tap_id=";

	static String TAG_LIST = "list";
	static String TAG_TAPS = "taps";
	static String TAG_ID = "id";
	static String TAG_NAME = "name";
	static String TAG_ABV = "abv";
	static String TAG_BREWERY = "brewery";
	static String TAG_COUNTRY = "country";
	static String TAG_TYPE = "type";
	static String TAG_IMG = "img";
	static String TAG_ORIGINAL = "original";

	JSONArray list = null;
	JSONObject listObject = null;
	JSONArray taps = null;

	JSONObject c = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tap_info);

		String tapId = getIntent().getExtras().getString("tapId");

		tapInfoName = (TextView) findViewById(R.id.tapInfoName);
		tapInfoAbv = (TextView) findViewById(R.id.tapInfoAbv);
		tapInfoBrewery = (TextView) findViewById(R.id.tapInfoBrewery);
		tapInfoCountry = (TextView) findViewById(R.id.tapInfoCountry);
		tapInfoType = (TextView) findViewById(R.id.tapInfoType);

		tapInfoImg = (ImageView) findViewById(R.id.tapInfoImg);

		JSONparser parser = new JSONparser(this);
		Log.d("Url", url+tapId);
		parser.execute(url + tapId);

	}

	@Override
	public void onRequestComplete(JSONObject result) {

		JSONObject json = result;

		try {
			list = json.getJSONArray(TAG_LIST);
		
				c = list.getJSONObject(0);

				tapInfoName.setText(c.getString(TAG_NAME));
				tapInfoAbv.setText(c.getString(TAG_ABV));
				tapInfoBrewery.setText(c.getString(TAG_BREWERY));
				tapInfoCountry.setText(c.getString(TAG_COUNTRY));
				tapInfoType.setText(c.getString(TAG_TYPE));

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
									tapInfoImg.setImageBitmap(image);
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
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
