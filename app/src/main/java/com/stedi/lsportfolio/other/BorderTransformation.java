package com.stedi.lsportfolio.other;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

public class BorderTransformation implements Transformation {
    private static final float STROKE_WIDTH = Utils.dp2px(1);

    @Override
    public Bitmap transform(Bitmap source) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);

        Bitmap result = source;
        if (!result.isMutable()) {
            result = source.copy(Bitmap.Config.ARGB_8888, true);
            source.recycle();
        }

        Canvas canvas = new Canvas(result);
        canvas.drawRect(0, 0, result.getWidth(), result.getHeight(), paint);

        return result;
    }

    @Override
    public String key() {
        return getClass().getSimpleName();
    }
}
