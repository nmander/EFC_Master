package com.example.niklas.efc_master.profiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.niklas.efc_master.R;
import com.example.niklas.efc_master.listeners.TempChangeListener;

public class Thermometer extends View implements TempChangeListener {

	Paint paint = new Paint();
	Paint paintTemp = new Paint();
	Paint paintOutline = new Paint();

	public int temp = 50;
	public static final int MAX_TEMP = 90;
	public static final int MIN_TEMP = 0;

	private int[] getTempLineColors() {
		return new int[] {
				Color.rgb(0,0, 130),
				getResources().getColor(R.color.colorSelectedNav),
				Color.rgb(0, 143, 0),
				Color.rgb(55, 180, 0),
				Color.rgb(255, 200, 0),
				Color.rgb(130, 0, 0)};
		}

	private void init() {
		int[] tempColors = getTempLineColors();
		Shader shader = new LinearGradient(0,0,0, 600, tempColors, null, Shader.TileMode.MIRROR);
		Matrix matrix = new Matrix();
		matrix.setRotate(90);
		shader.setLocalMatrix(matrix);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(30f);
		paint.setShader(shader);

		paintTemp.setColor(Color.BLACK);
		paintTemp.setStyle(Paint.Style.STROKE);
		paintTemp.setStrokeWidth((20f));

		paintOutline.setStyle(Paint.Style.STROKE);
		paintOutline.setColor(Color.BLACK);
		paintOutline.setStrokeWidth(3f);
	}

	public Thermometer(Context context) {
		super(context);
	}

	public Thermometer(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.Thermometer,
				0, 0);
		temp = a.getInt(R.styleable.Thermometer_currentTemp, 0);
		init();
	}

	public Thermometer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void DrawCurrentTempPosition(Canvas canvas)
	{
		if (temp >= MAX_TEMP)
		{
			canvas.drawLine(605, 220, 605, 280, paintTemp);
		}
		if (temp <= MIN_TEMP)
		{
            canvas.drawLine(10, 220, 10, 280, paintTemp);
		}
		else
			canvas.drawLine((temp*6.6f), 220, (temp*6.6f), 280, paintTemp);
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawLine(0, 250, 605, 250, paint);
		canvas.drawRect(1, 233, 605, 265, paintOutline);

		DrawCurrentTempPosition(canvas);
	}

	@Override
	public void onTempChanged(int temp)
	{
		this.setCurrentTemp(temp);
		this.invalidate();
	}

	public void setCurrentTemp(int temp)
	{
		this.temp = temp;
	}

	public float getCurrentTemp() {
		return temp;
	}
}
