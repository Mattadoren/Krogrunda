package se.mima.jeda.frsa.krogrunda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashscreenActivity extends Activity {

	private boolean mIsBackButtonPressed;
	private static final int SPLASH_DURATION = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);

		Handler handler = new Handler();

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				// make sure we close the splash screen so the user won't come
				// back when it presses back key

				finish();

				if (!mIsBackButtonPressed) {
					// start the home screen if the back button wasn't pressed
					// already
					Intent intent = new Intent(SplashscreenActivity.this,
							MainActivity.class);
					SplashscreenActivity.this.startActivity(intent);
				}

			}

		}, SPLASH_DURATION); // time in milliseconds (1 second = 1000
								// milliseconds) until the run() method will be
								// called

	}

	@Override
	public void onBackPressed() {

		// set the flag to true so the next activity won't start up
		mIsBackButtonPressed = true;
		super.onBackPressed();

	}
}