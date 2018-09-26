package com.example.niklas.efc_master.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.niklas.efc_master.R;
import com.example.niklas.efc_master.activities.MainActivity;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StatsTabDetailsFragment extends Fragment
{
	public MainActivity mainActivity;
	public TextView modLastRunDateTimeCell;
	public String myLastRunDate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stats_tab_detail, container, false);
		mainActivity = (MainActivity) getActivity();
		modLastRunDateTimeCell = rootView.findViewById(R.id.cell_last_runtime_datetime);

		Bundle bundle = getArguments();
		if (bundle != null) {
			if (bundle.containsKey("LAST_RUN_DATE")) {
				myLastRunDate = getArguments().getString("LAST_RUN_DATE");
				modLastRunDateTimeCell.setText(myLastRunDate);
			}
		}

 		return rootView;
	}
}
