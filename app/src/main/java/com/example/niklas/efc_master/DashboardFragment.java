package com.example.niklas.efc_master;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardFragment extends Fragment
{
	public MainActivity mainActivity;
	public static Speedometer mySpeedometer;
	public static Chronometer myChronometer;
	public int myRPM;
	public int myRUNTIME;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);
		mainActivity = (MainActivity) getActivity();
		//below is where you get a variable from the main activity
		//mainActivity.module_temperature = myTemperature;

		//myRPM = mainActivity.rpm;
		myRPM = mainActivity.live_data.getRpm();
		mySpeedometer = rootView.findViewById(R.id.Speedometer);
		updateSpeedometer(myRPM);

		myRUNTIME = mainActivity.live_data.getRun_time();
		myChronometer = rootView.findViewById(R.id.runtime_clock);


		//findModuleRunTime();
		String myFoundModRunTime = getModuleRunTimeFormat(mainActivity.live_data.getRun_time());
		//convert to base?

		//myChronometer.setBase(SystemClock.elapsedRealtime() - Long.valueOf(myFoundModRunTime));
		//myChronometer.setBase(SystemClock.elapsedRealtime() - (2* 60000 + 40 * 1000)); //02:00 2 minutes
		return rootView;
	}

	public void updateSpeedometer(int rpm)
	{
		mySpeedometer.setCurrentSpeed(rpm);
		mySpeedometer.onSpeedChanged(rpm);
	}

	public void startRuntime(int runtime)
	{
		myChronometer.start();
	}

	public void stopRuntime()
	{
		myChronometer.stop();
	}

	public String getModuleRunTimeFormat(int timeSeconds)
	{
		int hours = timeSeconds / 3600;
		int secondsLeft = timeSeconds - hours * 3600;
		int minutes = secondsLeft / 60;
		int seconds = secondsLeft - minutes * 60;

		String formatTime = "";
		if (hours < 10)
			formatTime += "0";
		formatTime += hours + ":";

		if (minutes < 10)
			formatTime += "0";
		formatTime += minutes + ":";

		if (seconds < 10)
			formatTime += "0";
		formatTime += seconds ;

		return formatTime;
	}
}