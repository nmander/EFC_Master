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

public class StatsTabRPMBinsFragment extends Fragment {

	public MainActivity mainActivity;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stats_tab_rpm_bins, container, false);
		mainActivity = (MainActivity) getActivity();

		BarChart chart = (BarChart) rootView.findViewById(R.id.bar_chart);
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
		List<BarEntry> entries = new ArrayList<>();
		entries.add(new BarEntry(1500f, 1.1f));
		entries.add(new BarEntry(2000f, 2f));
		entries.add(new BarEntry(2500f, 2.5f));
		entries.add(new BarEntry(3000f, 85.3f));
		entries.add(new BarEntry(3500f, 12.7f));
		entries.add(new BarEntry(4000f, 8.6f));
		entries.add(new BarEntry(4500f, 2));
		entries.add(new BarEntry(5000f, 8.8f));
		entries.add(new BarEntry(5500f, 20.9f));
		entries.add(new BarEntry(6000f, 18.6f));
		entries.add(new BarEntry(6500f, 32f));
		entries.add(new BarEntry(7000f, 55.7f));
		entries.add(new BarEntry(7500f, 72.4f));
		entries.add(new BarEntry(8000f, 62.9f));
		entries.add(new BarEntry(8500f, 60.1f));
		entries.add(new BarEntry(9000f, 42.0f));
		entries.add(new BarEntry(9500f, 32.3f));
		entries.add(new BarEntry(10000f, 35f));
		entries.add(new BarEntry(10500f, 22.7f));
		entries.add(new BarEntry(11000f, 33.5f));
		entries.add(new BarEntry(11500f, 10.9f));

		BarDataSet set = new BarDataSet(entries, "Life time speed profile.");
		set.setColor(Color.rgb(173,13,90));
		set.setHighlightEnabled(false);


		BarData data = new BarData(set);
		data.setValueTextSize(10);
		data.setBarWidth(450f); // set custom bar width


		return data;

	}
}
