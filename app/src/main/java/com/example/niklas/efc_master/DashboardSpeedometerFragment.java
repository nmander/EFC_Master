package com.example.niklas.efc_master;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by Belal on 1/23/2018.
 */

public class DashboardSpeedometerFragment extends Fragment
{
	public MainActivity mainActivity;
	public Speedometer mySpeedometer;
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

		//just change the fragment_dashboard
		//with the fragment you want to inflate
		//like if the class is HomeFragment it should have R.layout.home_fragment
		//if it is DashboardFragment it should have R.layout.fragment_dashboard
		//myRPM = mainActivity.rpm;
		/*Timer timer = new Timer();

		timer.scheduleAtFixedRate(new TimerTask() {

			synchronized public void run() {
				mySpeedometer.setCurrentSpeed(myRPM);
				mySpeedometer.onSpeedChanged(myRPM);
			}
		}, TimeUnit.MINUTES.toMillis(300), TimeUnit.MINUTES.toMillis(300));*/
		mySpeedometer.setCurrentSpeed(myRPM);
		mySpeedometer.onSpeedChanged(myRPM);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mySpeedometer.setCurrentSpeed(myRPM);
		mySpeedometer.onSpeedChanged(myRPM);
	}
}