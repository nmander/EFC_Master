package com.example.niklas.efc_master.profiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.niklas.efc_master.R;
import com.example.niklas.efc_master.listeners.LevelChangeListener;

public class BubbleLevel extends View implements LevelChangeListener
{
    Paint paint = new Paint();
    Paint paintBubble = new Paint();

    public float pitch;
    public float roll;
    public static final float ROLL_SENS = 35;
    public static final float PITCH_SENS = 100;
    public static final int BUBBLE_RADIUS = 75;


    private void init() {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(15f);
        paintBubble.setColor(getResources().getColor(R.color.colorSelectedNav));
    }

    public BubbleLevel(Context context) {
        super(context);
    }

    public BubbleLevel(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.Speedometer,
                0, 0);
        pitch = a.getFloat(R.styleable.BubbleLevel_currentPitch, 0);
        roll = a.getFloat(R.styleable.BubbleLevel_currentRoll, 0);
        init();
    }

    public BubbleLevel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void DrawBubble(Canvas canvas)
    {
        if (pitch > 0 && roll > 0)
            canvas.drawCircle(300 + (roll*ROLL_SENS), 250 + (pitch*PITCH_SENS), BUBBLE_RADIUS, paintBubble);
        if (pitch > 0 && roll < 0)
            canvas.drawCircle(300 - (roll*-ROLL_SENS), 250 + (pitch*PITCH_SENS), BUBBLE_RADIUS, paintBubble);
        if (pitch < 0 && roll < 0)
            canvas.drawCircle(300 - (roll*-ROLL_SENS), 250 - (pitch*-PITCH_SENS), BUBBLE_RADIUS, paintBubble);
        if (pitch < 0 && roll > 0)
            canvas.drawCircle(300 + (roll*ROLL_SENS), 250 - (pitch*-PITCH_SENS), BUBBLE_RADIUS, paintBubble);
        if (pitch == 0 && roll > 0)
            canvas.drawCircle(300 + (roll*ROLL_SENS), 250, BUBBLE_RADIUS, paintBubble);
        if (pitch == 0 && roll < 0)
            canvas.drawCircle(300 - (roll*-ROLL_SENS), 250, BUBBLE_RADIUS, paintBubble);
        if (pitch > 0 && roll == 0)
            canvas.drawCircle(300, 250 + (pitch*PITCH_SENS), BUBBLE_RADIUS, paintBubble);
        if (pitch < 0 && roll == 0)
            canvas.drawCircle(300, 250 - (pitch-PITCH_SENS), BUBBLE_RADIUS, paintBubble);
        if (pitch == 0 && roll == 0)
            canvas.drawCircle(300 , 250, BUBBLE_RADIUS, paintBubble);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawLine(300, 0, 300, 500, paint);
        canvas.drawLine(0, 250, 605, 250, paint);

        DrawBubble(canvas);
    }

    @Override
    public void onLevelChanged(float myAccelX, float myAccelY)
    {
        this.setCurrentLevel(myAccelX, myAccelY);
        this.invalidate();
    }

    public void setCurrentLevel(float roll, float pitch)
    {
        this.roll = roll;
        this.pitch = pitch;
    }

    public float getCurrentRoll()
    {
        return roll;
    }

    public float getCurrentPitch()
    {
        return pitch;
    }
}
