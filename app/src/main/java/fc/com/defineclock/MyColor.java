package fc.com.defineclock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by gx on 2016/11/1.
 */
public class MyColor extends View{

    private int color;

    public MyColor(Context context) {
        this(context, null);
    }

    public MyColor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyColor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.MyColor);
        try {
            color = typedArray.getColor(R.styleable.MyColor_aa, Color.parseColor("#00ff00"));
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(color);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setStrokeWidth(1);
        p.setAntiAlias(true);
        canvas.drawARGB(255,200,200,200);
        canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,getMeasuredWidth()/5,p);
    }
}
