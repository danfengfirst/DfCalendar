package com.df.dfcalendar.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.df.dfcalendar.R;


/**
 * Created by Danfeng on 2018/4/20.
 */
public class CalendarDayTextView extends android.support.v7.widget.AppCompatTextView {
    public boolean isToday;
    private boolean isSTime;
    private boolean isETime;
    private Context context;

    public void setEmptyColor(int emptyColor) {
        this.emptyColor = emptyColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    private int emptyColor = Color.parseColor("#00ff00");
    private int fillColor = Color.parseColor("#00ff00");

    private Paint mPaintSTime;
    private Paint mPaintETime;



    public CalendarDayTextView(Context context) {
        super(context);
        initview(context);
    }

    public CalendarDayTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initview(context);
    }

    private void initview(Context context) {
        this.context=context;

        mPaintSTime = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSTime.setStyle(Paint.Style.FILL);
        mPaintSTime.setColor(context.getResources().getColor(R.color.date_time_bg));
        mPaintSTime.setStrokeWidth(2);

        mPaintETime = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintETime.setStyle(Paint.Style.FILL);
        mPaintETime.setColor(context.getResources().getColor(R.color.date_time_bg));
        mPaintETime.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //根据当前逻辑开始时间必须先绘制结束时间
        if (isETime) {
            canvas.save();
            //移动到当前控件的中心，以中心为圆点绘制实心圆
            canvas.translate(getWidth() / 2, getHeight() / 2);
            canvas.drawCircle(0, 0, getWidth() / 2 , mPaintETime);
            canvas.restore();
            //此处必须将圆移动回开始位置，否则文本显示会受到影响
            canvas.translate(0, 0);
        }

        if (isSTime) {
            canvas.save();
            //移动到当前控件的中心，以中心为圆点绘制实心圆
            canvas.translate(getWidth() / 2, getHeight() / 2);
            canvas.drawCircle(0, 0, getWidth() / 2 , mPaintSTime);
            canvas.restore();
            //此处必须将圆移动回开始位置，否则文本显示会受到影响
            canvas.translate(0, 0);
        }
        super.onDraw(canvas);
    }

    public void setToday(boolean today) {
        isToday = today;
        this.setTextColor(context.getResources().getColor(R.color.date_today));
    }

    public void isETime(boolean etime) {
        isETime = etime;
        this.setTextColor(context.getResources().getColor(R.color.date_time_tv));
    }

    public void isSTime(boolean stime) {
        isSTime = stime;
        this.setTextColor(context.getResources().getColor(R.color.date_time_tv));
    }

}