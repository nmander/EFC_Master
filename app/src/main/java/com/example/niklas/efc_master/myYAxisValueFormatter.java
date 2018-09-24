package com.example.niklas.efc_master;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class myYAxisValueFormatter implements IAxisValueFormatter
{
    private DecimalFormat mFormat;

    public myYAxisValueFormatter() {

        // format values to 0 decimal digit
        mFormat = new DecimalFormat("###");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        if ( value >= (axis.mAxisMaximum - (axis.mAxisMaximum/axis.mEntryCount))) {
            return "sec";
        }

        return mFormat.format(value);

    }

    /**
     * this is only needed if numbers are returned, else return 0
     */
    @Override
    public int getDecimalDigits() {
        return 1;
    }
}
