package com.example.prueba;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class DonutChartView extends View {
    
    private Paint paint;
    private RectF rectF;
    private float[] data;
    private int[] colors;
    private float strokeWidth = 40f;
    
    public DonutChartView(Context context) {
        super(context);
        init();
    }
    
    public DonutChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
        
        // Default data for expenses by category
        data = new float[]{30f, 25f, 25f, 20f}; // Alimentación, Transporte, Entretenimiento, Otros
        
        // Colors matching the legend
        colors = new int[]{
            Color.parseColor("#3498DB"), // Blue - Alimentación
            Color.parseColor("#27AE60"), // Green - Transporte  
            Color.parseColor("#F39C12"), // Orange - Entretenimiento
            Color.parseColor("#7F8C8D")  // Gray - Otros
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
        float radius = Math.min(centerX, centerY) - strokeWidth / 2f;
        
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        
        float startAngle = -90f; // Start from top
        
        // Draw segments
        for (int i = 0; i < data.length; i++) {
            paint.setColor(colors[i]);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth);
            paint.setStrokeCap(Paint.Cap.ROUND);
            
            float sweepAngle = (data[i] / 100f) * 360f;
            canvas.drawArc(rectF, startAngle, sweepAngle, false, paint);
            
            startAngle += sweepAngle;
        }
        
        // Draw center circle (white background)
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, radius - strokeWidth / 2f + 10f, paint);
    }
}
