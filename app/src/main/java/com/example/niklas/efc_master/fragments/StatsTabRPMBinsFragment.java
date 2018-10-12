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
	public float[] LifeTimeData;
	private float[] LifeTimeProfile;
	List<BarEntry> entries = new ArrayList<>();
//	static float data500;
//	static float data1000;
//	static float data1500;
//	static float data2000;
//	static float data2500;
//	static float data3000;
//	static float data3500;
//	static float data4000;
//	static float data4500;
//	static float data5000;
//	static float data5500;
//	static float data6000;
//	static float data6500;
//	static float data7000;
//	static float data7500;
//	static float data8000;
//	static float data8500;
//	static float data9000;
//	static float data9500;
//	static float data10000;
//	static float data10500;
//	static float data11000;
//	static float data11500;
//	static float data12000;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stats_tab_rpm_bins, container, false);
		mainActivity = (MainActivity) getActivity();

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
		chart.getAxisRight().setSpaceBottom(0);
		chart.getXAxis().setLabelCount(12);
		chart.getDescription().setEnabled(false);
		chart.getLegend().setTextSize(13);
		chart.getXAxis().setValueFormatter(new myXAxisValueFormatter());
		chart.invalidate();

		return rootView;
	}

	private BarData getData() {
		//float[] LifeTimeData =
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

//		LifeTimeProfile = addonLifeTimeProfile();
//
//		if (LifeTimeProfile[0] != 0)
//			entries.add(new BarEntry(500f, LifeTimeProfile[0]));
//		if (LifeTimeProfile[1] != 0)
//			entries.add(new BarEntry(1000f, LifeTimeProfile[1]));
//		if (LifeTimeProfile[2] != 0)
//			entries.add(new BarEntry(1500f, LifeTimeProfile[2]));
//		if (LifeTimeProfile[3] != 0)
//			entries.add(new BarEntry(2000f, LifeTimeProfile[3]));
//		if (LifeTimeProfile[4] != 0)
//			entries.add(new BarEntry(2500f, LifeTimeProfile[4]));
//		if (LifeTimeProfile[5] != 0)
//			entries.add(new BarEntry(3000f, LifeTimeProfile[5]));
//		if (LifeTimeProfile[6] != 0)
//			entries.add(new BarEntry(3500f, LifeTimeProfile[6]));
//		if (LifeTimeProfile[7] != 0)
//			entries.add(new BarEntry(4000f, LifeTimeProfile[7]));
//		if (LifeTimeProfile[8] != 0)
//			entries.add(new BarEntry(4500f, LifeTimeProfile[8]));
//		if (LifeTimeProfile[9] != 0)
//			entries.add(new BarEntry(5000f, LifeTimeProfile[9]));
//		if (LifeTimeProfile[10] != 0)
//			entries.add(new BarEntry(5500f, LifeTimeProfile[10]));
//		if (LifeTimeProfile[11] != 0)
//			entries.add(new BarEntry(6000f, LifeTimeProfile[11]));
//		if (LifeTimeProfile[12] != 0)
//			entries.add(new BarEntry(6500f, LifeTimeProfile[12]));
//		if (LifeTimeProfile[13] != 0)
//			entries.add(new BarEntry(7000f, LifeTimeProfile[13]));
//		if (LifeTimeProfile[14] != 0)
//			entries.add(new BarEntry(7500f, LifeTimeProfile[14]));
//		if (LifeTimeProfile[15] != 0)
//			entries.add(new BarEntry(8000f, LifeTimeProfile[15]));
//		if (LifeTimeProfile[16] != 0)
//			entries.add(new BarEntry(8500f, LifeTimeProfile[16]));
//		if (LifeTimeProfile[17] != 0)
//			entries.add(new BarEntry(9000f, LifeTimeProfile[17]));
//		if (LifeTimeProfile[18] != 0)
//			entries.add(new BarEntry(9500f, LifeTimeProfile[18]));
//		if (LifeTimeProfile[19] != 0)
//			entries.add(new BarEntry(10000f, LifeTimeProfile[19]));
//		if (LifeTimeProfile[20] != 0)
//			entries.add(new BarEntry(10500f, LifeTimeProfile[20]));
//		if (LifeTimeProfile[21] != 0)
//			entries.add(new BarEntry(11000f, LifeTimeProfile[21]));
//		if (LifeTimeProfile[22] != 0)
//			entries.add(new BarEntry(11500f, LifeTimeProfile[22]));
//		if (LifeTimeProfile[23] != 0)
//			entries.add(new BarEntry(12000f, LifeTimeProfile[23]));

		BarDataSet set = new BarDataSet(entries, "Life time speed profile.");
		set.setColor(Color.rgb(173,13,90));
		set.setHighlightEnabled(false);

		BarData data = new BarData(set);
		data.setValueTextSize(10);
		data.setBarWidth(450f); // set custom bar width

		return data;
	}

//	public float[] addonLifeTimeProfile()
//	{
//		if (!MainActivity.did_we_calc_life_run) {
//
//			LifeTimeData = StatsTabRecentRunsFragment.getLastRunProfileData();
//			data500 += LifeTimeData[0];
//			data1000 += LifeTimeData[1];
//			data1500 += LifeTimeData[2];
//			data2000 += LifeTimeData[3];
//			data2500 += LifeTimeData[4];
//			data3000 += LifeTimeData[5];
//			data3500 += LifeTimeData[6];
//			data4000 += LifeTimeData[7];
//			data4500 += LifeTimeData[8];
//			data5000 += LifeTimeData[9];
//			data5500 += LifeTimeData[10];
//			data6000 += LifeTimeData[11];
//			data6500 += LifeTimeData[12];
//			data7000 += LifeTimeData[13];
//			data7500 += LifeTimeData[14];
//			data8000 += LifeTimeData[15];
//			data8500 += LifeTimeData[16];
//			data9000 += LifeTimeData[17];
//			data9500 += LifeTimeData[18];
//			data10000 += LifeTimeData[19];
//			data10500 += LifeTimeData[20];
//			data11000 += LifeTimeData[21];
//			data11500 += LifeTimeData[22];
//			data12000 += LifeTimeData[23];
//
//			MainActivity.did_we_calc_life_run = true;
//		}
//		return new float[]{data500, data1000, data1500, data2000, data2500, data3000, data3500,
//				data4000, data4500, data5000, data5500, data6000, data6500, data7000,
//				data7500, data8000, data8500, data9000, data9500, data10000, data10500,
//				data11000, data11500, data12000};
//	}
}
