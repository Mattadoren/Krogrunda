package se.mima.jeda.frsa.krogrunda;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class NearbyFrag extends Fragment {

	public double lat;
	public double lng;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View myFragmentView = inflater.inflate(R.layout.nearby, container,
				false);
		
		Thread th = new Thread() {
				Getlocation getloc = new Getlocation(getActivity(),NearbyFrag.this);
		};
		try {
			th.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			

		Toast.makeText(getActivity(), "LAT: "+lat+" LNG: "+lng, Toast.LENGTH_LONG).show();
		
		return myFragmentView;
	}
}
