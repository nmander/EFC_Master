package com.example.niklas.efc_master.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.niklas.efc_master.R;
import com.example.niklas.efc_master.activities.MainActivity;

public class StatsTabDetailsFragment extends Fragment
{
	public MainActivity mainActivity;
	//public TextView modLastRunDateTimeCell;
	public TextView modLastRunTimerCell;
	public String myLastRunTimeDate;
	public String[] LastRun;
	private DashboardFragment dashboardFragment = new DashboardFragment();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stats_tab_detail, container, false);
		mainActivity = (MainActivity) getActivity();
		//modLastRunDateTimeCell = rootView.findViewById(R.id.cell_last_runtime_datetime);
		modLastRunTimerCell = rootView.findViewById(R.id.cell_last_runtime_stopwatch);

		Bundle bundle = getArguments();
		if (bundle != null) {
			if (bundle.containsKey("LAST_RUN_DATE")) {
				myLastRunTimeDate = getArguments().getString("LAST_RUN_DATE");
				if (myLastRunTimeDate != null) {
					LastRun = myLastRunTimeDate.split("-");
					if ((Integer.valueOf(LastRun[0]) < 60))
						modLastRunTimerCell.setText(dashboardFragment.getModuleRunTimeFormat(Integer.valueOf(LastRun[0])) + " sec" + "  ---  " + LastRun[1]);
					if ((Integer.valueOf(LastRun[0]) >= 60 && (Integer.valueOf(LastRun[0]) < 3600)))
						modLastRunTimerCell.setText(dashboardFragment.getModuleRunTimeFormat(Integer.valueOf(LastRun[0])) + " min" + "  ---  " + LastRun[1]);
					if ((Integer.valueOf(LastRun[0]) >=3600 && (Integer.valueOf(LastRun[0]) < 36000)))
						modLastRunTimerCell.setText(dashboardFragment.getModuleRunTimeFormat(Integer.valueOf(LastRun[0])) + " hrs" + "  ---  " + LastRun[1]);
					else if ((Integer.valueOf(LastRun[0]) >= 360000))
						modLastRunTimerCell.setText("100+ hrs");
					//modLastRunDateTimeCell.setText(LastRun[1]);
				}
				else
				{
					modLastRunTimerCell.setText("");
					//modLastRunDateTimeCell.setText("");
				}
			}
		}
 		return rootView;
	}
}
