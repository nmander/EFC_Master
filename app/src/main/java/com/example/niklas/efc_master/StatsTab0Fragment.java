package com.example.niklas.efc_master;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

public class StatsTab0Fragment extends Fragment
{
	public MainActivity mainActivity;
	public TableLayout tblDetails;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_stats_tab_0, container, false);
		mainActivity = (MainActivity) getActivity();

 		return rootView;
	}
}
