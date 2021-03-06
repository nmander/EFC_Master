package com.example.niklas.efc_master.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niklas.efc_master.R;
import com.example.niklas.efc_master.activities.MainActivity;
import com.example.niklas.efc_master.profiles.protocol;

public class StatsTabDetailsFragment extends Fragment
{
	public MainActivity mainActivity;
	public TextView modLastRunTimerCell;
	public TextView modTotalRunTimeCell;
	public TextView txtOilLifeValue;
	public Button btnResetOilLife;
	public Button btnYes;
	public Button btnNo;
	public String myLastRunTimeDate = "";
	private TextView mod_cell_device_name;
	public String[] LastRun;
	private DashboardFragment dashboardFragment = new DashboardFragment();
	AlertDialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stats_tab_detail, container, false);
		mainActivity = (MainActivity) getActivity();
		//modLastRunDateTimeCell = rootView.findViewById(R.id.cell_last_runtime_datetime);
		modLastRunTimerCell = rootView.findViewById(R.id.cell_last_runtime_stopwatch);
		modTotalRunTimeCell = rootView.findViewById(R.id.cell_total_runtime);
		mod_cell_device_name = rootView.findViewById(R.id.cell_device_name);
		mod_cell_device_name.setText(mainActivity.mBluetoothGatt.getDevice().getName());
		txtOilLifeValue = rootView.findViewById(R.id.cell_oil_life_value);
		btnResetOilLife = rootView.findViewById(R.id.btn_reset_oil);

		Bundle bundle = getArguments();
		if (bundle != null || !mainActivity.RunTimeAndDate.isEmpty()) {
			if (bundle != null && bundle.containsKey("LAST_RUN_DATE")) {
				myLastRunTimeDate = getArguments().getString("LAST_RUN_DATE");
			}
			else
			{
				myLastRunTimeDate = mainActivity.RunTimeAndDate;
			}
			if (myLastRunTimeDate != null) {
				LastRun = myLastRunTimeDate.split("-");
				if ((Integer.valueOf(LastRun[0]) < 60))
					modLastRunTimerCell.setText(dashboardFragment.getModuleRunTimeFormat(Integer.valueOf(LastRun[0])) + " sec" + "  |  " + LastRun[1]);
				if ((Integer.valueOf(LastRun[0]) >= 60 && (Integer.valueOf(LastRun[0]) < 3600)))
					modLastRunTimerCell.setText(dashboardFragment.getModuleRunTimeFormat(Integer.valueOf(LastRun[0])) + " min" + "  |  " + LastRun[1]);
				if ((Integer.valueOf(LastRun[0]) >=3600 && (Integer.valueOf(LastRun[0]) < 36000)))
					modLastRunTimerCell.setText(dashboardFragment.getModuleRunTimeFormat(Integer.valueOf(LastRun[0])) + " hrs" + "  |  " + LastRun[1]);
				else if ((Integer.valueOf(LastRun[0]) >= 360000))
					modLastRunTimerCell.setText("100+ hrs");
			}
			else
			{
				modLastRunTimerCell.setText("");
			}
			if (bundle != null && bundle.containsKey("TOTAL_RUN_TIME"))
			{
				String total_run_time = String.valueOf(getArguments().getFloat("TOTAL_RUN_TIME"));
				modTotalRunTimeCell.setText(total_run_time + " hrs");
			}
		}
		int myPercent;
		myPercent = 100 - mainActivity.live_data.getOil_life_cntr();
		if (myPercent <= 0)
		{
			myPercent = 0;
		}
		else if (myPercent <= 99)
			btnResetOilLife.setVisibility(View.VISIBLE);
		txtOilLifeValue.setText(String.valueOf(myPercent) + "%");

		btnResetOilLife.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view)
			{
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
				View row = getLayoutInflater().inflate(R.layout.dialog_reset_oil, null);
				alertDialog.setView(row);
				dialog = alertDialog.create();
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded);
				dialog.show();
				btnYes = row.findViewById(R.id.btn_yes);
				btnNo = row.findViewById(R.id.btn_no);

				btnYes.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mainActivity.writeToIgnitionModule(protocol.BTN_RESET_OIL, protocol.RESET_OIL_COUNTER);
						txtOilLifeValue.setText("100 %");
						btnResetOilLife.setVisibility(View.INVISIBLE);
						dialog.dismiss();
					}
				});

				btnNo.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			}
		});
 		return rootView;
	}
}
