package com.example.niklas.efc_master;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StatsTab1Fragment extends Fragment {

	public MainActivity mainActivity;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stats_tab_1, container, false);
		mainActivity = (MainActivity) getActivity();
		//updatePrimerBulb(myTemp);
 		return rootView;
	}
}
