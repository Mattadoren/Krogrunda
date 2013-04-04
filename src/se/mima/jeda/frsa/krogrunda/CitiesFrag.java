package se.mima.jeda.frsa.krogrunda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CitiesFrag extends android.support.v4.app.Fragment{

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View myFragmentView  = inflater.inflate(R.layout.cities, container, false);
		
		return myFragmentView;
	}
	
}
