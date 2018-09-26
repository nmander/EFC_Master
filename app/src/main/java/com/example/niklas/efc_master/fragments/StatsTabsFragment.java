package com.example.niklas.efc_master.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.niklas.efc_master.R;
import com.example.niklas.efc_master.activities.MainActivity;

public class StatsTabsFragment extends Fragment {

	public static TabLayout tabLayout;
	public static ViewPager viewPager;
	public static int int_items = 5;

	public String myLastRunDate;
	private StatsTabDetailsFragment statsTabDetailsFragment = new StatsTabDetailsFragment();

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/**
		 *Inflate tab_layout and setup Views.
		 */
		View v = inflater.inflate(R.layout.fragment_stats_tabs, container, false);
		tabLayout = (TabLayout) v.findViewById(R.id.tabs);
		viewPager = (ViewPager) v.findViewById(R.id.viewpager);

		/**
		 *Set an Apater for the View Pager
		 */
		viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

		/**
		 * Now , this is a workaround ,
		 * The setupWithViewPager dose't works without the runnable .
		 * Maybe a Support Library Bug .
		 */

		tabLayout.post(new Runnable() {
			@Override
			public void run() {
				tabLayout.setupWithViewPager(viewPager);
			}
		});

		Bundle bundle = getArguments();
		if (bundle != null) {
			if (bundle.containsKey("LAST_RUN_DATE")) {
				myLastRunDate = getArguments().getString("LAST_RUN_DATE");
				//modLastRunDateTimeCell.setText(myLastRunDate);
			}
		}

		return v;

	}

	class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		/**
		 * Return fragment with respect to Position .
		 */

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0:
					Bundle bundle = new Bundle();
					bundle.putString("LAST_RUN_DATE", myLastRunDate);
					statsTabDetailsFragment.setArguments(bundle);
					return statsTabDetailsFragment;
				case 1:
                    return new StatsTabRecentRunsFragment();
				case 2:
					return new StatsTabRPMBinsFragment();
				case 3:
                    return new StatsTabStartTempFragment();
				case 4:
					return new StatsTabUsageFragment();
			}
			return null;
		}

		@Override
		public int getCount() {

			return int_items;

		}

		/**
		 * This method returns the title of the tab according to the position.
		 */

		@Override
		public CharSequence getPageTitle(int position) {

			switch (position) {
				case 0:
					return  "Information";
				case 1:
					return "Last Run";
				case 2:
					return "Life Time";
				case 3:
					return "Startup Temp";
				case 4:
					return "Usage";
			}
			return null;
		}
	}
}
