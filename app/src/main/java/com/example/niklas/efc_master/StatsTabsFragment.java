package com.example.niklas.efc_master;

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

public class StatsTabsFragment extends Fragment {

	public static TabLayout tabLayout;
	public static ViewPager viewPager;
	public static int int_items = 5;

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
					return new StatsTabDetailsFragment();
				case 1:
                    return new StatsTabUsageFragment();
				case 2:
                    return new StatsTabStartTempFragment();
				case 3:
                    return new StatsTabRPMBinsFragment();
				case 4:
					return new StatsTab4Fragment();
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
					String details = "Details";
					return details;
				case 1:
					String usage = "Usage";
					return usage;
				case 2:
					String start_temp = "Start Temp";
					return start_temp;
				case 3:
					String rpm_bins = "RPM Bins";
					return rpm_bins;

				case 4:
					String graph_4 = "Graph 4";
					return graph_4;
			}
			return null;
		}
	}
}
