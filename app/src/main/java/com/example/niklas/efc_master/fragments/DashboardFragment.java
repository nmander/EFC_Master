package com.example.niklas.efc_master.fragments;

import android.app.job.JobInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niklas.efc_master.R;
import com.example.niklas.efc_master.profiles.Speedometer;
import com.example.niklas.efc_master.activities.MainActivity;
import com.example.niklas.efc_master.profiles.protocol;

import static android.content.ContentValues.TAG;

public class DashboardFragment extends Fragment
{
	public MainActivity mainActivity;
	public static Speedometer mySpeedometer;
	public TextView myRunTimer;
	public TextView myBUMP;
	public ImageView myToolSelection;
	public TextView myOilLife;
	public int myRPM;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);
		mainActivity = (MainActivity) getActivity();

		myRPM = mainActivity.live_data.getRpm();
		mySpeedometer = rootView.findViewById(R.id.Speedometer);
		updateSpeedometer(myRPM);

		//myRUNTIME = mainActivity.live_data.getRun_time();
		myRunTimer = rootView.findViewById(R.id.runtime_clock);

		String myFoundModRunTime = getModuleRunTimeFormat(mainActivity.live_data.getRun_time());
		myRunTimer.setText(myFoundModRunTime);

		myToolSelection = rootView.findViewById(R.id.dashboard_tool);
		myBUMP = rootView.findViewById(R.id.BUMP);
		//myBUMP.setTextColor(getResources().getColor(R.color.colorMaterialLight));
		myBUMP.setVisibility(View.INVISIBLE);

		myOilLife = rootView.findViewById(R.id.dashboard_oil_value);

		Bundle bundle = getArguments();
		if (bundle != null) {
			if (bundle.containsKey("TOOL")) {
				int myStartingTool = getArguments().getInt("TOOL");
				{
					if (mainActivity.did_we_clear_bump && myStartingTool != 5)
						updateToolView(myStartingTool);
					else if (!mainActivity.did_we_clear_bump && myStartingTool != 5)
						updateToolView(myStartingTool);
					else if (!mainActivity.did_we_clear_bump && myStartingTool == 5)
						flashBUMP();
				}
			}
		}

		if(!mainActivity.did_we_clear_change)
			flashCHANGE();

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

	public void updateOilLife(int percent)
	{
		int myPercent;
		myPercent = 100 - percent;
		myOilLife.setText(String.valueOf(myPercent) + "%");

		if (myPercent <= 10 && !mainActivity.start_change_notif)
		{
			mainActivity.start_change_notif = true;
			mainActivity.did_we_clear_change = false;
			flashCHANGE();
		}
	}

	public boolean updateToolView(int toolCode)
	{
		switch (toolCode)
		{
			case 0:
				myToolSelection.setImageResource(R.drawable.blade);
				return true;
			case 1:
				myToolSelection.setImageResource(R.drawable.blower);
				return true;
			case 2:
				myToolSelection.setImageResource(R.drawable.edger);
				return true;
			case 3:
				myToolSelection.setImageResource(R.drawable.polesaw);
				return true;
			case 4:
				myToolSelection.setImageResource(R.drawable.tiller);
				return true;
			case 5:
				myToolSelection.setImageResource(R.drawable.string);
				return true;
		}
		return false;
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

		return formatTime;
	}

	public void flashBUMP()
	{
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
		myBUMP.setVisibility(View.VISIBLE);
		myBUMP.setTextColor(getResources().getColor(R.color.colorStopButton));

		anim.setDuration(150); //You can manage the blinking time with this parameter
		anim.setStartOffset(100);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
		myBUMP.startAnimation(anim);

		anim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				myBUMP.setTextColor(Color.BLACK);
				myBUMP.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
	}

	public void flashCHANGE()
	{
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
		myOilLife.setTextColor(getResources().getColor(R.color.colorStopButton));

		anim.setDuration(150); //You can manage the blinking time with this parameter
		anim.setStartOffset(100);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
		myOilLife.startAnimation(anim);

		anim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				myOilLife.setTextColor(Color.BLACK);
				mainActivity.start_change_notif = false;
				mainActivity.did_we_clear_change = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
	}
}