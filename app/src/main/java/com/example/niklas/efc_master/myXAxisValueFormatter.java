package com.example.niklas.efc_master;

import java.text.DecimalFormat;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class myXAxisValueFormatter implements IAxisValueFormatter
{
    private DecimalFormat mFormat;

    public myXAxisValueFormatter() {

        // format values to 0 decimal digit
        mFormat = new DecimalFormat("###");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        if (value == axis.getAxisMaximum()) {
            if (value == 90)
            {
                return "°C";
            }
            else if (value == 40)
            {
                return "min";
            }
            else if (value == 12000)
            {
                return "rpm";
            }
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
