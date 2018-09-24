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

public class StatsTabStartTempFragment extends Fragment {

	public MainActivity mainActivity;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stats_tab_start_temp, container, false);
		mainActivity = (MainActivity) getActivity();

		BarChart chart = (BarChart) rootView.findViewById(R.id.bar_chart);
		chart.setTouchEnabled(false);
		chart.setData(getData());
		chart.getXAxis().setAxisMaximum(90);
		chart.getXAxis().setAxisMinimum(-10);
		chart.getAxisLeft().setAxisMinimum(0);
		chart.getAxisRight().setEnabled(false);
		chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
		chart.setDrawBorders(true);
		chart.getXAxis().setDrawGridLines(false);
		chart.getAxisLeft().setSpaceBottom(0);
		chart.getXAxis().setLabelCount(20);
		chart.getLegend().setTextSize(13);
		chart.getXAxis().setValueFormatter(new myXAxisValueFormatter());
		chart.getDescription().setEnabled(false);
		chart.invalidate();

		return rootView;
	}

	private BarData getData() {
		List<BarEntry> entries = new ArrayList<>();
		entries.add(new BarEntry(7.5f, 1f));
		entries.add(new BarEntry(12.5f, 5f));
		entries.add(new BarEntry(17.5f, 8f));
		entries.add(new BarEntry(22.5f, 14f));
		entries.add(new BarEntry(27.5f, 7f));
		entries.add(new BarEntry(32.5f, 3f));
		entries.add(new BarEntry(47.5f, 1f));
		//entries.add(new BarEntry(8000f, 62f));
		//entries.add(new BarEntry(9000f, 42f));
		entries.add(new BarEntry(57.5f, 10f));
		entries.add(new BarEntry(62.5f, 13f));
		entries.add(new BarEntry(67.5f, 8f));

		BarDataSet set = new BarDataSet(entries, "Starts at different temperatures");
		set.setColor(Color.rgb(13,138,173));
		set.setHighlightEnabled(false);


		BarData data = new BarData(set);
		data.setValueTextSize(10);
		data.setBarWidth(4.5f); // set custom bar width
		data.setValueFormatter(new myYvaluesFormatter());

		return data;

	}
}
