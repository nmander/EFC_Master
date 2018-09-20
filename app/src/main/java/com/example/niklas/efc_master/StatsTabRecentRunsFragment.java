package com.example.niklas.efc_master;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
		BarChart chart = (BarChart) rootView.findViewById(R.id.bar_chart);
		chart.setTouchEnabled(false);
		chart.setData(getData());
		chart.getXAxis().setAxisMaxValue(12500);
		chart.getXAxis().setAxisMinValue(0);
		chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
		chart.setDescription("");
		chart.setDrawBorders(true);
		chart.getXAxis().setDrawGridLines(false);
		chart.getAxisLeft().setSpaceBottom(0);
		chart.getAxisRight().setSpaceBottom(0);
		chart.setDescription("Last run-time in seconds at different rpm interval");
		chart.setDescriptionPosition(1800,100);
		chart.setFitBars(true); // make the x-axis fit exactly all bars
		chart.invalidate();

		return rootView;
	}

	private BarData getData() {
		List<BarEntry> entries = new ArrayList<>();
		entries.add(new BarEntry(3000f, 35f));
		entries.add(new BarEntry(3500f, 3f));
		entries.add(new BarEntry(4000f, 1f));
		entries.add(new BarEntry(5000f, 8f));
		entries.add(new BarEntry(5500f, 20f));
		entries.add(new BarEntry(6000f, 6f));
		entries.add(new BarEntry(7000f, 37f));
		entries.add(new BarEntry(7500f, 72f));
		entries.add(new BarEntry(8000f, 62f));
		entries.add(new BarEntry(8500f, 31f));

		BarDataSet set = new BarDataSet(entries, "BarDataSet");
		set.setColor(Color.rgb(123,102,196));
		set.setHighlightEnabled(false);


		BarData data = new BarData(set);
		data.setValueTextSize(10);
		data.setBarWidth(450f); // set custom bar width

		return data;

	}
}
