package fc.com.defineclock.dview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

import fc.com.defineclock.R;
import fc.com.defineclock.util.DisplayUtil;

/**
 * Created by gx on 2016/11/1.
 */
public class MyWatchView extends View {

    private Context context;
    private Paint mPaint;
    private int mRadius;//半径


    private int mPadding;
    private int mtext_size;
    private int mhour_pointer_width;
    private int minute_pointer_width;
    private int msecond_pointer_width;
    private int mpointer_corner_radius;
    private int mpointer_end_length;
    private int mscale_long_color;
    private int mscale_short_color;
    private int mhour_pointer_color;
    private int mminute_pointer_color;
    private int msecond_pointer_color;


    public MyWatchView(Context context) {
        this(context, null);
    }

    public MyWatchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyWatchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        obtainStyledAttribute(attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }


    private void obtainStyledAttribute(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyWatchView);
        try {
            mPadding = (int) typedArray.getDimension(R.styleable.MyWatchView_wb_padding, 10);
            mtext_size = (int) typedArray.getDimension(R.styleable.MyWatchView_wb_text_size, DisplayUtil.sp2px(context, 16));
            mhour_pointer_width = (int) typedArray.getDimension(R.styleable.MyWatchView_wb_hour_pointer_width, DisplayUtil.dip2px(context, 5));
            minute_pointer_width = (int) typedArray.getDimension(R.styleable.MyWatchView_wb_minute_pointer_width, DisplayUtil.dip2px(context, 3));
            msecond_pointer_width = (int) typedArray.getDimension(R.styleable.MyWatchView_wb_second_pointer_width, DisplayUtil.dip2px(context, 2));
            mpointer_corner_radius = (int) typedArray.getDimension(R.styleable.MyWatchView_wb_pointer_corner_radius, DisplayUtil.dip2px(context, 10));
            mpointer_end_length = (int) typedArray.getDimension(R.styleable.MyWatchView_wb_pointer_end_length, DisplayUtil.dip2px(context, 10));

            mscale_long_color = typedArray.getColor(R.styleable.MyWatchView_wb_scale_long_color, Color.argb(225, 0, 0, 0));
            mscale_short_color = typedArray.getColor(R.styleable.MyWatchView_wb_scale_short_color, Color.argb(125, 0, 0, 0));
            mhour_pointer_color = typedArray.getColor(R.styleable.MyWatchView_wb_hour_pointer_color, Color.argb(125, 0, 0, 0));
            mminute_pointer_color = typedArray.getColor(R.styleable.MyWatchView_wb_minute_pointer_color, Color.BLACK);
            msecond_pointer_color = typedArray.getColor(R.styleable.MyWatchView_wb_second_pointer_color, Color.RED);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int finalWidth = 800;

        if (widthMode == MeasureSpec.AT_MOST || heightMeasureSpec == MeasureSpec.AT_MOST) {
            finalWidth = 800;
        } else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            finalWidth = Math.min(widthSize, heightSize);
        } else if (widthMode == MeasureSpec.EXACTLY) {
            finalWidth = Math.min(widthSize, finalWidth);
        } else if (heightMode == MeasureSpec.EXACTLY) {
            finalWidth = Math.min(heightSize, finalWidth);
        }

        setMeasuredDimension(finalWidth, finalWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = (Math.min(w,h)-mPadding)/2;
        mpointer_end_length = mRadius/6;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        drawWatch(canvas);
        drawScale(canvas);
        drawArm(canvas);
        drawCenter(canvas);
        canvas.restore();
        postInvalidateDelayed(16);
    }


    //画表盘
    private void drawWatch(Canvas canvas){
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(0, 0, mRadius, mPaint);
    }

    //画刻度
    private void drawScale(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#000000"));
        int lineLength = 40;
        for(int i = 0 ; i < 60 ; i++){
            if(i % 5 == 0){
                lineLength = 40;
                mPaint.setStrokeWidth(5);
                canvas.drawLine(0, -mRadius + DisplayUtil.dip2px(context, mPadding), 0, -mRadius + DisplayUtil.dip2px(context, mPadding) + lineLength, mPaint);

                canvas.save();
                String time = i==0 ? 12+"":i/5+"";
                mPaint.setTextSize(mtext_size);
                Rect rect = new Rect();
                mPaint.getTextBounds(time,0,time.length(),rect);
                canvas.translate(0, -mRadius + DisplayUtil.dip2px(context, mPadding) + lineLength + (rect.bottom-rect.top));
                canvas.rotate(-6*i);
                mPaint.setStrokeWidth(1);
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawText(time,-(rect.right-rect.left) / 2, rect.bottom, mPaint);
                canvas.restore();

            }else{
                lineLength = 25;
                mPaint.setStrokeWidth(3);
                canvas.drawLine(0, -mRadius + DisplayUtil.dip2px(context, mPadding), 0, -mRadius + DisplayUtil.dip2px(context, mPadding) + lineLength, mPaint);
            }
            canvas.rotate(6);
        }
    }
    //画指针
    private void drawArm(Canvas canvas){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millSecond = calendar.get(Calendar.MILLISECOND);
        //hour
        canvas.save();
        float radius = hour*30+(((float)minute/(float)60*(float)30));
        RectF rect = new RectF(-mhour_pointer_width/2,-mRadius/2,mhour_pointer_width/2,mpointer_end_length);
        canvas.rotate(radius);
        mPaint.setColor(Color.parseColor("#000000"));
        canvas.drawRoundRect(rect, mhour_pointer_width / 2, mhour_pointer_width / 2,mPaint);
        canvas.restore();

        //minute
        canvas.save();
        float minuteRadius = minute*6+(((float)second/(float)60*(float)6));
        RectF minuteRect = new RectF(-minute_pointer_width/2,-mRadius+10,minute_pointer_width/2,mpointer_end_length);
        canvas.rotate(minuteRadius);
        mPaint.setColor(Color.parseColor("#000000"));
        canvas.drawRoundRect(minuteRect, minute_pointer_width / 2, minute_pointer_width / 2,mPaint);
        canvas.restore();

        //second
        canvas.save();
        float secondRadius = second*6+((float)millSecond/(float)1000 * (float) 6);
        Log.e("fc",millSecond+"");
        RectF secondRect = new RectF(-msecond_pointer_width/2,-mRadius+10,msecond_pointer_width/2,mpointer_end_length);
        canvas.rotate(secondRadius);
        mPaint.setColor(Color.parseColor("#ff0000"));
        canvas.drawRoundRect(secondRect, msecond_pointer_width / 2, msecond_pointer_width / 2, mPaint);
        canvas.restore();
    }
    //画中心点
    private void drawCenter(Canvas canvas) {
        mPaint.setColor(Color.parseColor("#ff0000"));
        canvas.drawCircle(0,0,10,mPaint);
    }

}
