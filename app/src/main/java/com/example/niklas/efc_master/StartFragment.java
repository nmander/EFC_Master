package com.example.niklas.efc_master;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class StartFragment extends Fragment {

	public MainActivity mainActivity;
	private Button btnPrimeBulb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_start, container, false);

		mainActivity = (MainActivity) getActivity();
		//below is where you get a variable from the main activity
		//mainActivity.module_temperature = myTemperature;

		int myTemperature = mainActivity.module_temperature;

		int presses = TempToPress.myNumOfPress(myTemperature);
		btnPrimeBulb = rootView.findViewById(R.id.instruction_prime_bulb);
		if (presses == 1)
			btnPrimeBulb.setText("PUSH PRIMER BULB " + presses + " TIME");
		else
			btnPrimeBulb.setText("PUSH PRIMER BULB " + presses + " TIMES");

		return rootView;


	}
}
