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
		if (LastRunData[17] != 0)
			entries.add(new BarEntry(10500f, LastRunData[20]));
		if (LastRunData[18] != 0)
			entries.add(new BarEntry(11000f, LastRunData[21]));
		if (LastRunData[19] != 0)
			entries.add(new BarEntry(11500f, LastRunData[22]));
		if (LastRunData[19] != 0)
			entries.add(new BarEntry(12000f, LastRunData[23]));


		BarDataSet set = new BarDataSet(entries, "Last run speed profile.");
		set.setColor(Color.rgb(123,102,196));
		set.setHighlightEnabled(false);

		BarData data = new BarData(set);
		data.setValueTextSize(10);
		data.setBarWidth(450f); // set custom bar width

		return data;

	}

	public float[] getLastRunProfileData() {
		float data500  =  (MainActivity.arrLastRunSpeed500.size()/10f);//0
		float data1000 = (MainActivity.arrLastRunSpeed1000.size()/10f);//1
		float data1500 = (MainActivity.arrLastRunSpeed1500.size()/10f);//2
		float data2000 = (MainActivity.arrLastRunSpeed2000.size()/10f);//3
		float data2500 = (MainActivity.arrLastRunSpeed2500.size()/10f);//4
		float data3000 = (MainActivity.arrLastRunSpeed3000.size()/10f);//5
		float data3500 = (MainActivity.arrLastRunSpeed3500.size()/10f);//6
		float data4000 = (MainActivity.arrLastRunSpeed4000.size()/10f);//7
		float data4500 = (MainActivity.arrLastRunSpeed4500.size()/10f);//8
		float data5000 = (MainActivity.arrLastRunSpeed5000.size()/10f);//9
		float data5500 = (MainActivity.arrLastRunSpeed5500.size()/10f);//10
		float data6000 = (MainActivity.arrLastRunSpeed6000.size()/10f);//11
		float data6500 = (MainActivity.arrLastRunSpeed6500.size()/10f);//12
		float data7000 = (MainActivity.arrLastRunSpeed7000.size()/10f);//13
		float data7500 = (MainActivity.arrLastRunSpeed7500.size()/10f);//14
		float data8000 = (MainActivity.arrLastRunSpeed8000.size()/10f);//15
		float data8500 = (MainActivity.arrLastRunSpeed8500.size()/10f);//16
		float data9000 = (MainActivity.arrLastRunSpeed9000.size()/10f);//17
		float data9500 = (MainActivity.arrLastRunSpeed9500.size()/10f);//18
		float data10000 = (MainActivity.arrLastRunSpeed10000.size()/10f);//19
		float data10500 = (MainActivity.arrLastRunSpeed10500.size()/10f);//20
		float data11000 = (MainActivity.arrLastRunSpeed11000.size()/10f);//21
		float data11500 = (MainActivity.arrLastRunSpeed11500.size()/10f);//22
		float data12000 = (MainActivity.arrLastRunSpeed12000.size()/10f);//23

		return new float[]{data500, data1000, data1500, data2000, data2500, data3000, data3500,
				           data4000, data4500, data5000, data5500, data6000, data6500, data7000,
						   data7500, data8000, data8500, data9000, data9500, data10000, data10500,
						   data11000, data11500, data12000};
	}

	public void resetLastRunProfileData()
	{
		if (MainActivity.did_we_calc_last_run) {
			MainActivity.arrLastRunSpeed500.clear();
			MainActivity.arrLastRunSpeed1000.clear();
			MainActivity.arrLastRunSpeed1500.clear();
			MainActivity.arrLastRunSpeed2000.clear();
			MainActivity.arrLastRunSpeed2500.clear();
			MainActivity.arrLastRunSpeed3000.clear();
			MainActivity.arrLastRunSpeed3500.clear();
			MainActivity.arrLastRunSpeed4000.clear();
			MainActivity.arrLastRunSpeed4500.clear();
			MainActivity.arrLastRunSpeed5000.clear();
			MainActivity.arrLastRunSpeed5500.clear();
			MainActivity.arrLastRunSpeed6000.clear();
			MainActivity.arrLastRunSpeed6500.clear();
			MainActivity.arrLastRunSpeed7000.clear();
			MainActivity.arrLastRunSpeed7500.clear();
			MainActivity.arrLastRunSpeed8000.clear();
			MainActivity.arrLastRunSpeed8500.clear();
			MainActivity.arrLastRunSpeed9000.clear();
			MainActivity.arrLastRunSpeed9500.clear();
			MainActivity.arrLastRunSpeed10000.clear();
			MainActivity.arrLastRunSpeed10500.clear();
			MainActivity.arrLastRunSpeed11000.clear();
			MainActivity.arrLastRunSpeed11500.clear();
			MainActivity.arrLastRunSpeed12000.clear();
			MainActivity.did_we_calc_last_run = false;
		}
	}
}
