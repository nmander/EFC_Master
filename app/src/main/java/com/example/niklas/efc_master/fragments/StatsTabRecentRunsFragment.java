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

	public MainActivity mainActivity;


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
		List<BarEntry> entries = new ArrayList<>();
		entries.add(new BarEntry(2750f, 35f));
		entries.add(new BarEntry(3250f, 3f));
		entries.add(new BarEntry(3750f, 1f));
		entries.add(new BarEntry(4750f, 8f));
		entries.add(new BarEntry(5250f, 20f));
		entries.add(new BarEntry(5750f, 6f));
		entries.add(new BarEntry(6750f, 37f));
		entries.add(new BarEntry(7250f, 72f));
		entries.add(new BarEntry(7750f, 62f));
		entries.add(new BarEntry(8250f, 31f));

		BarDataSet set = new BarDataSet(entries, "Last run speed profile.");
		set.setColor(Color.rgb(123,102,196));
		set.setHighlightEnabled(false);

		BarData data = new BarData(set);
		data.setValueTextSize(10);
		data.setBarWidth(450f); // set custom bar width

		return data;

	}
}
