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
import com.example.niklas.efc_master.profiles.myXAxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class StatsTabRecentRunsFragment extends Fragment {

	public static TextView y_axis_text;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stats_tab_recent_runs, container, false);
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
		chart.getXAxis().setLabelCount(12);
		chart.getDescription().setEnabled(false);
		chart.getLegend().setTextSize(13);
		chart.getXAxis().setValueFormatter(new myXAxisValueFormatter());
		chart.invalidate();

		return rootView;
	}

	private BarData getData() {

		float[] LastRunData = getLastRunProfileData();
		List<BarEntry> entries = new ArrayList<>();
		if (LastRunData[0] != 0)
			entries.add(new BarEntry(500f, LastRunData[0]));
		if (LastRunData[1] != 0)
			entries.add(new BarEntry(1000f, LastRunData[1]));
		if (LastRunData[2] != 0)
			entries.add(new BarEntry(1500f, LastRunData[2]));
		if (LastRunData[3] != 0)
			entries.add(new BarEntry(2000f, LastRunData[3]));
		if (LastRunData[4] != 0)
			entries.add(new BarEntry(2500f, LastRunData[4]));
		if (LastRunData[5] != 0)
			entries.add(new BarEntry(3000f, LastRunData[5]));
		if (LastRunData[6] != 0)
			entries.add(new BarEntry(3500f, LastRunData[6]));
		if (LastRunData[7] != 0)
			entries.add(new BarEntry(4000f, LastRunData[7]));
		if (LastRunData[8] != 0)
			entries.add(new BarEntry(4500f, LastRunData[8]));
		if (LastRunData[9] != 0)
			entries.add(new BarEntry(5000f, LastRunData[9]));
		if (LastRunData[10] != 0)
			entries.add(new BarEntry(5500f, LastRunData[10]));
		if (LastRunData[11] != 0)
			entries.add(new BarEntry(6000f, LastRunData[11]));
		if (LastRunData[12] != 0)
			entries.add(new BarEntry(6500f, LastRunData[12]));
		if (LastRunData[13] != 0)
			entries.add(new BarEntry(7000f, LastRunData[13]));
		if (LastRunData[14] != 0)
			entries.add(new BarEntry(7500f, LastRunData[14]));
		if (LastRunData[15] != 0)
			entries.add(new BarEntry(8000f, LastRunData[15]));
		if (LastRunData[16] != 0)
			entries.add(new BarEntry(8500f, LastRunData[16]));
		if (LastRunData[17] != 0)
			entries.add(new BarEntry(9000f, LastRunData[17]));
		if (LastRunData[18] != 0)
			entries.add(new BarEntry(9500f, LastRunData[18]));
		if (LastRunData[19] != 0)
			entries.add(new BarEntry(10000f, LastRunData[19]));
		if (LastRunData[20] != 0)
			entries.add(new BarEntry(10500f, LastRunData[20]));
		if (LastRunData[21] != 0)
			entries.add(new BarEntry(11000f, LastRunData[21]));
		if (LastRunData[22] != 0)
			entries.add(new BarEntry(11500f, LastRunData[22]));
		if (LastRunData[23] != 0)
			entries.add(new BarEntry(12000f, LastRunData[23]));


		BarDataSet set = new BarDataSet(entries, "Last run speed profile.");
		set.setColor(Color.rgb(123,102,196));
		set.setHighlightEnabled(false);

		BarData data = new BarData(set);
		data.setValueTextSize(10);
		data.setBarWidth(450f); // set custom bar width

		return data;

	}

	public static float[] getLastRunProfileData() {
		float[] mArray = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};  //24 possible speeds
		float value;
		boolean over_60_sec = false;

		for (int i=4; i<22; i++) {
			mArray[i] = (float)(MainActivity.array_last_run[i - 4])/10;  //time in 0.1 seconds resolution before div by 10
			if (mArray[i] < 0)
				mArray[i] = 0;
			if (mArray[i] >= 60 && !over_60_sec )  //if over 60 seconds at any speed we will convert to minutes
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
