package se.mima.jeda.frsa.krogrunda;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class MainActivity extends FragmentActivity{

	ViewPager viewPager;
	TabsAdapter tabsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Skapar ViewPager och sätter till Pagern i ids.xml
		viewPager = new ViewPager(this);
		viewPager.setId(R.id.pager);
		
		setContentView(viewPager);

		// Skapar ActionBar och sätter titel.
		final ActionBar actBar = getActionBar();
		actBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actBar.setTitle("");
		
		// Fäster Tabs till fragmentklasserna och sätter titlar.
		tabsAdapter = new TabsAdapter(this, viewPager);
		
		tabsAdapter.addTab(actBar.newTab().setText("Städer"), CitiesFrag.class, null);
		tabsAdapter.addTab(actBar.newTab().setText("I Närheten"), NearbyFrag.class, null);
		tabsAdapter.addTab(actBar.newTab().setText("Ölnyheter"), BeerNewsFrag.class, null);

		if (savedInstanceState != null) {
			actBar.setSelectedNavigationItem(savedInstanceState
					.getInt("tab", 0));
		}
	}

	protected void onSavedInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	// Skapar TabsAdapter för att skapa Tabs.
	public static class TabsAdapter extends FragmentPagerAdapter implements
			ActionBar.TabListener, ViewPager.OnPageChangeListener {

		private final Context mContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args) {
				clss = _class;
				args = _args;
			}
		}

		public TabsAdapter(FragmentActivity activity, ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mActionBar = activity.getActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			mActionBar.setSelectedNavigationItem(position);

		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++) {
				if (mTabs.get(i) == tag) {
					mViewPager.setCurrentItem(i);
				}
			}

		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

	}
} 
