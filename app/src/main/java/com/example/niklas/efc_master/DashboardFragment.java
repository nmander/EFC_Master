package com.example.niklas.efc_master;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextClock;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardFragment extends Fragment
{
	public MainActivity mainActivity;
	public static Speedometer mySpeedometer;
	public TextView myRunTimer;
	public int myRPM;
	public int myRUNTIME;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);
		mainActivity = (MainActivity) getActivity();

		myRPM = mainActivity.live_data.getRpm();
		mySpeedometer = rootView.findViewById(R.id.Speedometer);
		updateSpeedometer(myRPM);

		myRUNTIME = mainActivity.live_data.getRun_time();
		myRunTimer = rootView.findViewById(R.id.runtime_clock);

		String myFoundModRunTime = getModuleRunTimeFormat(mainActivity.live_data.getRun_time());
		myRunTimer.setText(myFoundModRunTime);
		return rootView;
	}

	public void updateSpeedometer(int rpm)
	{
		mySpeedometer.setCurrentSpeed(rpm);
		mySpeedometer.onSpeedChanged(rpm);
	}

	public void updateRunTimer(int time)
	{
		String temp = getModuleRunTimeFormat(time);
		myRunTimer.setText(temp);
	}

	public String getModuleRunTimeFormat(int timeSeconds)
	{
		int hours = timeSeconds / 3600;
		int secondsLeft = timeSeconds - hours * 3600;
		int minutes = secondsLeft / 60;
		int seconds = secondsLeft - minutes * 60;
		//int miliSeconds = seconds /1000;

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

/*		if (miliSeconds < 10)
			formatTime += "0";
		formatTime += miliSeconds;*/

		return formatTime;
	}
}