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

public class StatsTabStartTempFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stats_tab_start_temp, container, false);
		BarChart chart = rootView.findViewById(R.id.bar_chart);
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
		chart.getAxisLeft().setLabelCount(10);
		chart.getLegend().setTextSize(13);
		chart.getXAxis().setValueFormatter(new myXAxisValueFormatter());
		chart.getDescription().setEnabled(false);
		chart.invalidate();

		return rootView;
	}

	private BarData getData() {
		float[] startTempData = getStartupTemperatureData();
		List<BarEntry> entries = new ArrayList<>();
		for (int i=0; i<20; i++)
		{
			if (startTempData[i] != 0)
				entries.add(new BarEntry((-7.5f+5*i), startTempData[i]));
		}

		BarDataSet set = new BarDataSet(entries, "STARTS AT DIFFERENT TEMPERATURES");
		set.setColor(Color.rgb(13,138,173));
		set.setHighlightEnabled(false);


		BarData data = new BarData(set);
		data.setValueTextSize(10);
		data.setBarWidth(4f); // set custom bar width
		data.setValueFormatter(new myYvaluesFormatter());

		return data;

	}

	public static float[] getStartupTemperatureData() {
		float[] mArray = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};  //20 possible temperature ranges

		for (int i=1; i<19; i++)
			mArray[i] = (float)(MainActivity.array_start_temp[i - 1]);  //Number of starts at certain temperature range

		return mArray;
	}
}
