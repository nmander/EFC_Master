package com.example.niklas.efc_master;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.niklas.efc_master.databinding.FragmentStartBinding;
public class StartFragment extends Fragment {

	public MainActivity mainActivity;
	private static final String TAG = "fragment_start";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		FragmentStartBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_start, container, false);
		View view = binding.getRoot();
		//here data must be an instance of the class MarsDataProvider
		mainActivity = (MainActivity) getActivity();
		binding.setIgndata(mainActivity.live_data);


		//below is where you get a variable from the main activity
    	int presses = TempToPress.myNumOfPress(mainActivity.live_data.getTemperature());
		if (presses == 1)
		{
			mainActivity.live_data.setPrimerBulb("PUSH PRIMER BULB " + presses + " TIME");
		}
		else
		{
	    	mainActivity.live_data.setPrimerBulb("PUSH PRIMER BULB " + presses + " TIMES");
		}
		return view;
	}
}
