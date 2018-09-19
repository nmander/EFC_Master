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

public class StatsTabUsageFragment extends Fragment {

	public MainActivity mainActivity;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stats_tab_usage, container, false);
		BarChart chart = (BarChart) rootView.findViewById(R.id.bar_chart);
		chart.setTouchEnabled(false);
		chart.setData(getData());
		chart.getXAxis().setAxisMinValue(-10);
		chart.getXAxis().setAxisMaxValue(90);
		chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
		chart.setDescription("");
		chart.setDrawBorders(true);
		chart.getXAxis().setDrawGridLines(false);
		chart.getAxisLeft().setSpaceBottom(0);
		chart.getAxisRight().setSpaceBottom(0);
		//chart.setDescription("Igntion module temperature at startup");
		chart.setDescriptionPosition(1800,100);
		chart.setFitBars(true); // make the x-axis fit exactly all bars
		chart.invalidate();

		return rootView;
	}

	private BarData getData() {
		List<BarEntry> entries = new ArrayList<>();
		entries.add(new BarEntry(10f, 1f));
		entries.add(new BarEntry(15f, 5f));
		entries.add(new BarEntry(20f, 8f));
		entries.add(new BarEntry(25f, 14f));
		entries.add(new BarEntry(30f, 7f));
		entries.add(new BarEntry(35f, 3f));
		entries.add(new BarEntry(40f, 1f));
		//entries.add(new BarEntry(8000f, 62f));
		//entries.add(new BarEntry(9000f, 42f));
		entries.add(new BarEntry(55f, 10f));
		entries.add(new BarEntry(60f, 13f));
		entries.add(new BarEntry(65f, 8f));

		BarDataSet set = new BarDataSet(entries, "BarDataSet");
		set.setColor(Color.rgb(13,74,173));
		set.setHighlightEnabled(false);

		BarData data = new BarData(set);
		data.setValueTextSize(10);
		data.setBarWidth(4.5f); // set custom bar width

		return data;

	}
}
