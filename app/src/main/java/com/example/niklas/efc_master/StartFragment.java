package com.example.niklas.efc_master;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
//import com.example.niklas.efc_master.databinding.FragmentStartBinding;
public class StartFragment extends Fragment {

	public MainActivity mainActivity;
	private static final String TAG = "fragment_start";
	public int myTemp;
	public static Button primeBulb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

/*		FragmentStartBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_start, container, false);
		View view = binding.getRoot();
		mainActivity = (MainActivity) getActivity();
		binding.setIgndata(mainActivity.live_data);*/

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_start, container, false);
		mainActivity = (MainActivity) getActivity();
		myTemp = mainActivity.live_data.getTemperature();
		primeBulb = rootView.findViewById(R.id.instruction_prime_bulb);
		//updatePrimerBulb(myTemp);
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
}
