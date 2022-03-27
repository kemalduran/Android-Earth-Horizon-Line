package com.example.earthhorizonline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawingView extends View {

    float slope = 0f;
    Paint paintLine;

    public DrawingView(Context c, AttributeSet attrs) {
        super(c, attrs);

        paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(Color.GREEN);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(8f);
        paintLine.setPathEffect(new DashPathEffect(new float[]{30, 20, 30, 30}, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int midHeight = this.getHeight() / 2;
        int midWidth = this.getWidth() / 2;

        canvas.save();
        canvas.rotate(slope, midWidth, midHeight);
        canvas.drawLine(midWidth - 400, midHeight, midWidth + 400, midHeight, paintLine);
        canvas.restore();
    }

    public void setSlope(double slope) {

        this.slope = (float) slope;

        invalidate();
    }
}
