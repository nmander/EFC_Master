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
import com.example.niklas.efc_master.profiles.myYvaluesFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class StatsTabUsageFragment extends Fragment {

	public MainActivity mainActivity;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stats_tab_usage, container, false);
		BarChart chart = (BarChart) rootView.findViewById(R.id.bar_chart);
		chart.setTouchEnabled(false);
		chart.setData(getData());
		chart.getXAxis().setAxisMaximum(40);
		chart.getXAxis().setAxisMinimum(0);
		chart.getAxisLeft().setAxisMinimum(0);
		chart.getAxisRight().setEnabled(false);
		chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
		chart.setDrawBorders(true);
		chart.getXAxis().setDrawGridLines(false);
		chart.getAxisLeft().setSpaceBottom(0);
		chart.getXAxis().setLabelCount(20);
		chart.getAxisLeft().setLabelCount(10);
		chart.getDescription().setEnabled(false);
		chart.getLegend().setTextSize(13);
		chart.getXAxis().setValueFormatter(new myXAxisValueFormatter());
		chart.invalidate();

		return rootView;
	}

	private BarData getData() {
		List<BarEntry> entries = new ArrayList<>();
		entries.add(new BarEntry(1, 5));
		entries.add(new BarEntry(3, 7));
		entries.add(new BarEntry(5, 4));
		entries.add(new BarEntry(9, 3));
		entries.add(new BarEntry(11, 7));
		entries.add(new BarEntry(13, 9));
		entries.add(new BarEntry(15, 7));
		entries.add(new BarEntry(17, 6));
		entries.add(new BarEntry(25, 3));

		BarDataSet set = new BarDataSet(entries, "Life time run-times");
		set.setColor(Color.rgb(13,173,90));
		set.setHighlightEnabled(false);

		BarData data = new BarData(set);
		data.setValueTextSize(10);
		data.setBarWidth(1.8f); // set custom bar width
		data.setValueFormatter(new myYvaluesFormatter());

		return data;

	}
}
