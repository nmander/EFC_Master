package com.example.niklas.efc_master;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.niklas.efc_master.databinding.FragmentStartBinding;
public class StartFragment extends Fragment {

//	public MainActivity mainActivity;
//	private Button btnPrimeBulb;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		FragmentStartBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_start, container, false);
		View view = binding.getRoot();
		//here data must be an instance of the class MarsDataProvider
		//Igndata our_data = new Igndata();
	//	binding.setIgn_data(our_data);


		return view;


//		View v = inflater.inflate(R.layout.fragment_dashboard_tab, container, false);
//		return v;
//		FragmentStartBinding binding = DataBindingUtil.inflate(inflater,
//				R.layout.fragment_start, container, false);
//
//		mainActivity = (MainActivity) getActivity();
//		//below is where you get a variable from the main activity
//		//mainActivity.module_temperature = myTemperature;
//
//		int myTemperature = mainActivity.module_temperature;
//
//		int presses = TempToPress.myNumOfPress(myTemperature);
//		btnPrimeBulb = rootView.findViewById(R.id.instruction_prime_bulb);
//		if (presses == 1)
//			btnPrimeBulb.setText("PUSH PRIMER BULB " + presses + " TIME");
//		else
//			btnPrimeBulb.setText("PUSH PRIMER BULB " + presses + " TIMES");
//
//		return binding.getRoot();

	}
}
