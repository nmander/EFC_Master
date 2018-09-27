package com.example.niklas.efc_master.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

import com.example.niklas.efc_master.R;
import com.example.niklas.efc_master.profiles.TempToPress;
import com.example.niklas.efc_master.activities.MainActivity;

public class StartFragment extends Fragment {

	public MainActivity mainActivity;
	private static final String TAG = "fragment_start";
	public int myTemp;
	public static Button primeBulb;
	public static Button squeezeThrottle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_start, container, false);
		mainActivity = (MainActivity) getActivity();
		//myTemp = mainActivity.live_data.getTemperature();
		primeBulb = rootView.findViewById(R.id.instruction_prime_bulb);
		squeezeThrottle = rootView.findViewById(R.id.instruction_squeeze_throttle);
		return rootView;
	}

	public void updatePrimerBulb(int temperature)
	{
		int presses = TempToPress.myNumOfPress(temperature);
		if (presses == 1)
		{
			primeBulb.setText("PUSH PRIMER BULB " + presses + " TIME");
		}
		else
		{
			primeBulb.setText("PUSH PRIMER BULB " + presses + " TIMES");
		}
	}

	public void flashSqueezeThrottle()
	{
		//squeezeThrottle.clearAnimation();
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
		if (mainActivity.live_data.getTps_status() == 1) {
			//squeezeThrottle.setBackgroundColor(getResources().getColor(R.color.colorStopButton));
			squeezeThrottle.setTextColor(getResources().getColor(R.color.colorStopButton));
			anim.setDuration(150); //You can manage the blinking time with this parameter
			anim.setStartOffset(100);
			anim.setRepeatMode(Animation.REVERSE);
			anim.setRepeatCount(6);
			squeezeThrottle.startAnimation(anim);

			anim.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					squeezeThrottle.setTextColor(getResources().getColor(R.color.colorBlack));
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}
			});
		}
//		else
//			squeezeThrottle.clearAnimation();

	}
}
