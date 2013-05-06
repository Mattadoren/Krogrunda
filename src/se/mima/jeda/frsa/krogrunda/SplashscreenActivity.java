package se.mima.jeda.frsa.krogrunda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashscreenActivity extends Activity {

	private boolean mIsBackButtonPressed;
	private static final int SPLASH_DURATION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);

		Handler handler = new Handler();

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				finish();
				if (!mIsBackButtonPressed) {
					Intent intent = new Intent(SplashscreenActivity.this,MainActivity.class);
					SplashscreenActivity.this.startActivity(intent);
					SplashscreenActivity.this.finish();
				}
			}
		}, SPLASH_DURATION);
	}
	@Override
	public void onBackPressed() {
		mIsBackButtonPressed = true;
		super.onBackPressed();
	}
}