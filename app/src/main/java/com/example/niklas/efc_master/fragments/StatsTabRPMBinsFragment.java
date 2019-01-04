package com.example.niklas.efc_master.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.niklas.efc_master.R;
import com.example.niklas.efc_master.activities.MainActivity;
import com.example.niklas.efc_master.profiles.myLastRunYvalueFormatter;
import com.example.niklas.efc_master.profiles.myXAxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class StatsTabRPMBinsFragment extends Fragment {

	public static TextView y_axis_text;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stats_tab_rpm_bins, container, false);
		BarChart chart = rootView.findViewById(R.id.bar_chart);
		y_axis_text = rootView.findViewById(R.id.y_axis_text);
		chart.setTouchEnabled(false);
		chart.setData(getData());
		chart.getXAxis().setAxisMaximum(12000);
		chart.getXAxis().setAxisMinimum(0);
		chart.getAxisLeft().setAxisMinimum(0);
		chart.getAxisRight().setEnabled(false);
		chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
		chart.setDrawBorders(true);
		chart.getXAxis().setDrawGridLines(false);
		chart.getAxisLeft().setSpaceBottom(0);
		chart.getAxisRight().setSpaceBottom(0);
		chart.getXAxis().setLabelCount(12);
		chart.getDescription().setEnabled(false);
		chart.getLegend().setTextSize(13);
		chart.getXAxis().setValueFormatter(new myXAxisValueFormatter());
		chart.invalidate();

		return rootView;
	}

	private BarData getData() {
		float[] LifeTimeData = getTotalRunProfileData();
				List<BarEntry> entries = new ArrayList<>();
				for (int i=0; i<24; i++)
				{
					if (LifeTimeData[i] != 0)
						entries.add(new BarEntry((500+500*i), LifeTimeData[i]));
				}

		BarDataSet set = new BarDataSet(entries, "Life time speed profile.");
		set.setColor(Color.rgb(173,13,90));
		set.setHighlightEnabled(false);

		BarData data = new BarData(set);
		data.setValueTextSize(10);
		data.setBarWidth(450f); // set custom bar width
		data.setValueFormatter(new myLastRunYvalueFormatter());

		return data;
	}

	public static float[] getTotalRunProfileData() {
		float[] mArray = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};  //24 possible speeds
		boolean over_60_sec = false;

		for (int i=4; i<22; i++) {
			mArray[i] = (float)(MainActivity.array_total_run[i - 4])*419/100;  //time in 4.192 seconds resolution
			if (mArray[i] < 0)
				mArray[i] = 0;
			else if (mArray[i] >= 60 && !over_60_sec )  //if over 60 seconds at any speed we will convert to minutes
				over_60_sec = true;
			y_axis_text.setText("time spent (seconds)");
		}

		if (over_60_sec) {                 //Convert to minutes
			for (int i=4; i<22; i++) {
				mArray[i] = mArray[i]/60;
			}
			y_axis_text.setText("time spent (minutes)");
		}
		return mArray;
	}

}
