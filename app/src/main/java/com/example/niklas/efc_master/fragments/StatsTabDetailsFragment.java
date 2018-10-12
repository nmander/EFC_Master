package com.example.niklas.efc_master.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niklas.efc_master.R;
import com.example.niklas.efc_master.activities.MainActivity;

public class StatsTabDetailsFragment extends Fragment
{
	public MainActivity mainActivity;
	//public TextView modLastRunDateTimeCell;
	public TextView modLastRunTimerCell;
	public TextView modTotalRunTimeCell;
	public TextView txtOilLifeValue;
	public Button btnResetOilLife;
	public String myLastRunTimeDate;
	public String[] LastRun;
	private DashboardFragment dashboardFragment = new DashboardFragment();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stats_tab_detail, container, false);
		mainActivity = (MainActivity) getActivity();
		//modLastRunDateTimeCell = rootView.findViewById(R.id.cell_last_runtime_datetime);
		modLastRunTimerCell = rootView.findViewById(R.id.cell_last_runtime_stopwatch);
		modTotalRunTimeCell = rootView.findViewById(R.id.cell_total_runtime);
		txtOilLifeValue = rootView.findViewById(R.id.cell_oil_life_value);
		btnResetOilLife = rootView.findViewById(R.id.btn_reset_oil);

		Bundle bundle = getArguments();
		if (bundle != null) {
			if (bundle.containsKey("LAST_RUN_DATE")) {
				myLastRunTimeDate = getArguments().getString("LAST_RUN_DATE");
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
			}
			else if (bundle.containsKey("TOTAL_RUN_TIME"))
			{
				String total_run_time = String.valueOf(getArguments().getFloat("TOTAL_RUN_TIME"));
			}
		}
		//txtOilLifeValue.setText(mainActivity.live_data.getOil_life_cntr());

		btnResetOilLife.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
/*				if (!txtOilLifeValue.getText().equals("100 %")) {
					txtOilLifeValue.setText("100 %");
					//mainActivity.live_data.setOil_life_cntr(0);

				}*/
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

				builder.setTitle("Confirm");
				builder.setMessage("Are you sure?");

				builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// Do nothing but close the dialog

						dialog.dismiss();
					}
				});

				builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						// Do nothing
						dialog.dismiss();
					}
				});

				AlertDialog alert = builder.create();
				alert.show();
			}
		});
 		return rootView;
	}
}
