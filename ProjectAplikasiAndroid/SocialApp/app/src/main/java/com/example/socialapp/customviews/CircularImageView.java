package com.example.socialapp.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class CircularImageView extends AppCompatImageView {
    private Path path = new Path();

    public CircularImageView(Context context) {
        super(context);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        RectF rect = new RectF(0, 0, getWidth(), getHeight());
        path.addCircle(getWidth() / 2, getHeight() / 2, Math.min(getWidth(), getHeight()) / 2, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}