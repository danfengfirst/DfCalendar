package com.df.dfcalendar.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.df.dfcalendar.R;


/**
 * Created by Danfeng on 2018/5/30.
 */

public class CalendarDayRelativeLayout extends RelativeLayout {
    public CalendarDayRelativeLayout(Context context) {
        this(context, null);
    }

    public CalendarDayRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void isDurationSat(boolean isSaturday) {
        this.setBackground(getResources().getDrawable(R.drawable.appoint_calendar_sat_bg));
    }

    public void isDurationSun(boolean isSunday) {
        this.setBackground(getResources().getDrawable(R.drawable.appoint_calendar_sun_bg));
    }
    public void isETime(boolean etime) {
        this.setBackground(getResources().getDrawable(R.mipmap.appoint_calendar_end_bg));}
    public void isSTime(boolean stime) {
        this.setBackground(getResources().getDrawable(R.mipmap.appoint_calendar_start_bg));
    }
}
