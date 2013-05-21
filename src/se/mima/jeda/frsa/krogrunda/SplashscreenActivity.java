package se.mima.jeda.frsa.krogrunda;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

public class SplashscreenActivity extends Activity {

    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);
		
		Log.d("Splashscreen", "Börjar kontroll");
		haveNetworkConnection();
		
		if(haveConnectedWifi || haveConnectedMobile){
			Log.d("Splashscreen", "Nätverk finns!");
			Intent startMainActivity = new Intent(this, MainActivity.class);
			startActivity(startMainActivity);
			finish();
		} else {
			Log.d("Splashscreen", "Nätverk finns ej!");
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
			Log.d("Splashscreen", "Skapar dialog");
			dialogBuilder.setTitle("Ingen anslutning").setMessage("Ingen anslutning upptäcktes, var god kontrollera och starta applikationen igen").setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			AlertDialog dialog = dialogBuilder.create();
			dialog.show();
			Log.d("Splashscreen", "Dialog skapad!");
		}
	}
	
	private void haveNetworkConnection() {

	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	}
}