package com.example.niklas.efc_master.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

	private MainActivity mainActivity = new MainActivity();


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stats_tab_recent_runs, container, false);
		BarChart chart = rootView.findViewById(R.id.bar_chart);
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

		int[] LastRunData = getLastRunProfileData();
		List<BarEntry> entries = new ArrayList<>();
		entries.add(new BarEntry(2500f, LastRunData[0]));
		entries.add(new BarEntry(3000f, LastRunData[1]));
		entries.add(new BarEntry(3500f, LastRunData[2]));
		entries.add(new BarEntry(4000f, LastRunData[3]));
		entries.add(new BarEntry(4500f, LastRunData[4]));
		entries.add(new BarEntry(5000f, LastRunData[5]));
		entries.add(new BarEntry(5500f, LastRunData[6]));
		entries.add(new BarEntry(6000f, LastRunData[7]));
		entries.add(new BarEntry(6500f, LastRunData[8]));
		entries.add(new BarEntry(7000f, LastRunData[9]));
		entries.add(new BarEntry(7500f, LastRunData[10]));
		entries.add(new BarEntry(8000f, LastRunData[11]));
		entries.add(new BarEntry(8500f, LastRunData[12]));
		entries.add(new BarEntry(9000f, LastRunData[13]));
		entries.add(new BarEntry(9500f, LastRunData[14]));
		entries.add(new BarEntry(10000f, LastRunData[15]));
		entries.add(new BarEntry(10500f, LastRunData[16]));
		entries.add(new BarEntry(11000f, LastRunData[17]));
		entries.add(new BarEntry(11500f, LastRunData[18]));
		entries.add(new BarEntry(12000f, LastRunData[19]));

		BarDataSet set = new BarDataSet(entries, "Last run speed profile.");
		set.setColor(Color.rgb(123,102,196));
		set.setHighlightEnabled(false);

		BarData data = new BarData(set);
		data.setValueTextSize(10);
		data.setBarWidth(450f); // set custom bar width

		return data;

	}

	public int[] getLastRunProfileData() {
		int runtime = 45;

		int data2500 = mainActivity.arrLastRunSpeed2500.size() / runtime; // 0
		int data3000 = mainActivity.arrLastRunSpeed3000.size() / runtime; // 1
		int data3500 = mainActivity.arrLastRunSpeed3500.size() / runtime; // 2
		int data4000 = mainActivity.arrLastRunSpeed4000.size() / runtime; // 3
		int data4500 = mainActivity.arrLastRunSpeed4500.size() / runtime; // 4
		int data5000 = mainActivity.arrLastRunSpeed5000.size() / runtime; // 5
		int data5500 = mainActivity.arrLastRunSpeed5500.size() / runtime; // 6
		int data6000 = mainActivity.arrLastRunSpeed6000.size() / runtime; // 7
		int data6500 = mainActivity.arrLastRunSpeed6500.size() / runtime; // 8
		int data7000 = mainActivity.arrLastRunSpeed7000.size() / runtime; // 9
		int data7500 = mainActivity.arrLastRunSpeed7500.size() / runtime; // 10
		int data8000 = mainActivity.arrLastRunSpeed8000.size() / runtime; // 11
		int data8500 = mainActivity.arrLastRunSpeed8500.size() / runtime; // 12
		int data9000 = mainActivity.arrLastRunSpeed9000.size() / runtime; // 13
		int data9500 = mainActivity.arrLastRunSpeed9500.size() / runtime; // 14
		int data10000 = mainActivity.arrLastRunSpeed10000.size() / runtime; // 15
		int data10500 = mainActivity.arrLastRunSpeed10500.size() / runtime; // 16
		int data11000 = mainActivity.arrLastRunSpeed11000.size() / runtime; // 17
		int data11500 = mainActivity.arrLastRunSpeed11500.size() / runtime; // 18
		int data12000 = mainActivity.arrLastRunSpeed12000.size() / runtime; // 19

		return new int[]{data2500, data3000, data3500, data4000, data4500, data5000, data5500, data6000, data6500, data7000,
				data7500, data8000, data8500, data9000, data9500, data10000, data10500, data11000, data11500, data12000};
	}
}
