package se.mima.jeda.frsa.krogrunda;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BeerNewsFrag extends Fragment{

public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View myFragmentView  = inflater.inflate(R.layout.beer_news, container, false);
		
		return myFragmentView;
	}
	
} 
