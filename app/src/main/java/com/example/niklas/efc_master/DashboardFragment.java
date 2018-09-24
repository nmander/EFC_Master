package com.example.niklas.efc_master;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class DashboardFragment extends Fragment
{
	public MainActivity mainActivity;
	public static Speedometer mySpeedometer;
	public TextView myRunTimer;
	public TextView myBUMP;
	public ImageView myToolSelection;
	public ImageView mySTRING;
	public int myRPM;
	public int myRUNTIME;

	private ShakeListener mShaker;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);
		mainActivity = (MainActivity) getActivity();

		myRPM = mainActivity.live_data.getRpm();
		mySpeedometer = rootView.findViewById(R.id.Speedometer);
		updateSpeedometer(myRPM);

		myRUNTIME = mainActivity.live_data.getRun_time();
		myRunTimer = rootView.findViewById(R.id.runtime_clock);

		String myFoundModRunTime = getModuleRunTimeFormat(mainActivity.live_data.getRun_time());
		myRunTimer.setText(myFoundModRunTime);

		myToolSelection = rootView.findViewById(R.id.dashboard_tool);
		myBUMP = rootView.findViewById(R.id.BUMP);
		//myBUMP.setTextColor(getResources().getColor(R.color.colorMaterialLight));
		myBUMP.setVisibility(View.INVISIBLE);

		Bundle bundle = getArguments();
		if (bundle != null) {
			if (bundle.containsKey("TOOL")) {
				int myStartingTool = getArguments().getInt("TOOL");
				updateToolView(myStartingTool);
			}
		}

		Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

		mShaker = new ShakeListener(getContext());
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
			public void onShake()
			{
				MainActivity.start_bump_notif = false;
				rootView.clearAnimation();
				Toast.makeText(getContext(), "YOU SHOOK ME!", Toast.LENGTH_SHORT).show();
			}
		});

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
		if (MainActivity.start_bump_notif = true) {
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
		else
		{
			myBUMP.clearAnimation();
			anim.setFillAfter(false);
			myBUMP.setTextColor(getResources().getColor(R.color.colorBlack));
			myBUMP.setVisibility(View.INVISIBLE);
		}
	}
}