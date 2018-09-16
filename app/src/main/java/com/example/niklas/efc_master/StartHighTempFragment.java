package com.example.niklas.efc_master;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class StartHighTempFragment extends Fragment {

	public MainActivity mainActivity;
	private static final String TAG = "fragment_start_high_temp";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_start_high_temp, container, false);
		mainActivity = (MainActivity) getActivity();
 		return rootView;
	}
}
