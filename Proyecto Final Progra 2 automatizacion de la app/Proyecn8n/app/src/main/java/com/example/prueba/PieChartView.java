package com.example.prueba;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieChartView extends View {
    
    private Paint paint;
    private RectF rectF;
    private float[] data;
    private int[] colors;
    
    public PieChartView(Context context) {
        super(context);
        init();
    }
    
    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
        
        // Default data for budget distribution
        data = new float[]{38f, 26f, 13f, 15f, 8f}; // Vivienda, Alimentación, Transporte, Ahorros, Entretenimiento, Otros
        
        // Colors for budget distribution
        colors = new int[]{
            Color.parseColor("#27AE60"), // Green - Vivienda 38%
            Color.parseColor("#3498DB"), // Blue - Alimentación 26%
            Color.parseColor("#F39C12"), // Orange - Transporte 13%
            Color.parseColor("#E74C3C"), // Red - Ahorros 15%
            Color.parseColor("#9B59B6")  // Purple - Entretenimiento 8%
        };
    }
    
    public void setData(float[] newData, int[] newColors) {
        this.data = newData;
        this.colors = newColors;
        invalidate();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = Math.min(centerX, centerY) * 0.8f;
        
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        
        float startAngle = -90f; // Start from top
        
        // Draw segments
        for (int i = 0; i < data.length; i++) {
            paint.setColor(colors[i]);
            paint.setStyle(Paint.Style.FILL);
            
            float sweepAngle = (data[i] / 100f) * 360f;
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
            
            startAngle += sweepAngle;
        }
    }
}
