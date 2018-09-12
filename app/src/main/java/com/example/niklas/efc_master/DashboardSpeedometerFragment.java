package com.example.niklas.efc_master;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DashboardSpeedometerFragment extends Fragment
{
	public MainActivity mainActivity;
	public static Speedometer mySpeedometer;
	public int myRPM;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard_speedometer, container, false);

		mainActivity = (MainActivity) getActivity();
		//below is where you get a variable from the main activity
		//mainActivity.module_temperature = myTemperature;

		//myRPM = mainActivity.rpm;
		myRPM = mainActivity.live_data.getRpm();
		mySpeedometer = rootView.findViewById(R.id.Speedometer);
		updateSpeedometer(myRPM);

		return rootView;
	}

	public void updateSpeedometer(int rpm)
	{
/*		DashboardSpeedometerFragment fragment = (DashboardSpeedometerFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
		mySpeedometer = fragment
		mySpeedometer.setCurrentSpeed(rpm);
		this.mySpeedometer.onSpeedChanged(rpm);*/

		mySpeedometer.setCurrentSpeed(rpm);
		mySpeedometer.onSpeedChanged(rpm);
	}
}