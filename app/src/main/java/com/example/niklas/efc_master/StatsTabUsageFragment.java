package com.example.niklas.efc_master;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

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
		chart.getXAxis().setAxisMinValue(0);
		chart.getXAxis().setAxisMaxValue(40);
		chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
		chart.setDescription("");
		chart.setDrawBorders(true);
		chart.getXAxis().setDrawGridLines(false);
		chart.getAxisLeft().setSpaceBottom(0);
		chart.getAxisRight().setSpaceBottom(0);
		chart.setDescription("Number of runs that lasted for x number of minutes");
		chart.setDescriptionPosition(1800,100);
		chart.setFitBars(true); // make the x-axis fit exactly all bars
		chart.invalidate();

		return rootView;
	}

	private BarData getData() {
		List<BarEntry> entries = new ArrayList<>();
		entries.add(new BarEntry(1f, 5f));
		entries.add(new BarEntry(3f, 7f));
		entries.add(new BarEntry(5f, 4f));
		entries.add(new BarEntry(9f, 3f));
		entries.add(new BarEntry(11f, 7f));
		entries.add(new BarEntry(13f, 9f));
		entries.add(new BarEntry(15f, 7f));
		entries.add(new BarEntry(17f, 6f));
		entries.add(new BarEntry(25f, 3f));


		BarDataSet set = new BarDataSet(entries, "BarDataSet");
		set.setColor(Color.rgb(13,173,90));
		set.setHighlightEnabled(false);

		BarData data = new BarData(set);
		data.setValueTextSize(10);
		data.setBarWidth(1.8f); // set custom bar width

		return data;

	}
}