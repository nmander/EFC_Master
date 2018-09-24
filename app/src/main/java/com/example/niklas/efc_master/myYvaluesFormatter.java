package com.example.niklas.efc_master;


import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class myYvaluesFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public myYvaluesFormatter() {
        mFormat = new DecimalFormat("###"); // use no decimals
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        return mFormat.format((int)value); // e.g. append a dollar-sign
    }
}