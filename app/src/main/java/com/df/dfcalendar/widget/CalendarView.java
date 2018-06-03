package com.df.dfcalendar.widget;

/**
 * Created by Danfeng on 2018/4/20.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.df.dfcalendar.R;
import com.df.dfcalendar.constant.CommonConstant;
import com.df.dfcalendar.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Danfeng on 2018/4/20.
 */
public class CalendarView extends LinearLayout {
    private TextView title;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private Calendar curDate = Calendar.getInstance();
    //从服务器获取的日期
    private Date dateFromServer;

    //外层主recyclerview的adapter
    private MainRvAdapter mainAdapter;
    private List<CalendarCell> months = new ArrayList<>();
    private Context context;

    //相关属性
    private int titleColor;
    private int titleSize;

    private int enableSelectColor;
    private int disableSeletColor;
    private int todayColor;
    private int todayEmptyColor;
    private int todayFillColor;

    //卡片第一个月
    private int firstMonth = -1;//卡片第一个月（中间）
    private int secondMonth = -1;//卡片第二个月（中间）
    private int thirdMonth = -1;//卡片第三个月（中间）

    private List<String> titles = new ArrayList<>();

    //点击的开始时间与结束时间
    private Date sDateTime;
    private Date eDateTime;
    private boolean isSelectingSTime = true;

    private HashMap<Integer, SubRvAdapter> allAdapters = new HashMap<>();


    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyCalendar);
        titleColor = ta.getColor(R.styleable.MyCalendar_titleColor, Color.WHITE);
        titleSize = (int) ta.getDimension(R.styleable.MyCalendar_titleSize, 18);
        enableSelectColor = ta.getColor(R.styleable.MyCalendar_dayInMonthColor, context.getResources().getColor(R.color.white));
        disableSeletColor = ta.getColor(R.styleable.MyCalendar_dayOutMonthcolor, context.getResources().getColor(R.color.transparent40_white));
        todayColor = ta.getColor(R.styleable.MyCalendar_todayColor, Color.BLUE);
        todayEmptyColor = ta.getColor(R.styleable.MyCalendar_todayEmptycircleColor, Color.CYAN);
        todayFillColor = ta.getColor(R.styleable.MyCalendar_todayFillcircleColor, Color.CYAN);
        ta.recycle();
        this.context = context;
        initTime(context, "");
        init(context);
    }
    //该方法用于设置从服务器获取的时间，如果没有从服务器获取的时间将使用手机本地时间
    private void initTime(Context context, String time) {
        if (!time.equals("")) {
            curDate = DateUtil.strToCalendar(time, CommonConstant.TFORMATE_YMD);
            dateFromServer = DateUtil.strToDate(time, CommonConstant.TFORMATE_YMD);
        } else {
            curDate = Calendar.getInstance();
            dateFromServer = new Date();
        }
    }

    private void init(Context context) {
        bindView(context);
        bindEvent();
    }


    private void bindView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.appoint_calendarview, this, false);
        title = (TextView) view.findViewById(R.id.calendar_title);
        title.setTextColor(titleColor);
        title.setTextSize(titleSize);
        recyclerView = (RecyclerView) view.findViewById(R.id.calendar_rv);
        linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        addView(view);
    }

    private void bindEvent() {
        renderCalendar("");
    }

    public void renderCalendar(String time) {
        months.clear();
        initTime(context, time);
        for (int i = 0; i < 3; i++) {
            ArrayList<Date> cells = new ArrayList<>();
            if (i != 0) {
                curDate.add(Calendar.MONTH, 1);//后推一个月
            } else {
                curDate.add(Calendar.MONTH, 0);//当前月
            }
            Calendar calendar = (Calendar) curDate.clone();
            //将日历设置到当月第一天
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            //获得当月第一天是星期几，如果是星期一则返回1此时1-1=0证明上个月没有多余天数
            int prevDays = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            //将calendar在1号的基础上向前推prevdays天。
            calendar.add(Calendar.DAY_OF_MONTH, -prevDays);
            //最大行数是6*7也就是，1号正好是星期六时的情况
            int maxCellcount = 6 * 7;
            while (cells.size() < maxCellcount) {
                cells.add(calendar.getTime());
                //日期后移一天
                calendar.add(calendar.DAY_OF_MONTH, 1);
            }
            months.add(new CalendarCell(i, cells));
        }

        firstMonth = dateFromServer.getMonth();//第一个月
        secondMonth = months.get(1).getCells().get(20).getMonth();//第二个月
        thirdMonth = months.get(2).getCells().get(20).getMonth();//第三个月
        for (int i = 0; i < months.size(); i++) {
            //title格式 2018年6月3日
            String title = (months.get(i).getCells().get(20).getYear() + 1900) +
                    context.getResources().getString(R.string.year) +
                    (months.get(i).getCells().get(20).getMonth() + 1) + context.getResources().getString(R.string.month);
            titles.add(title);
        }
        title.setText(titles.get(0));
        //只限定3个月，因此模拟给3个数值即可
        mainAdapter = new MainRvAdapter(R.layout.appoint_calendarview_item, months);
        recyclerView.setAdapter(mainAdapter);
        //recyclerview 的滚动监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                title.setText(titles.get(linearLayoutManager.findLastVisibleItemPosition()));
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    /**
     * 最外层水平recyclerview的adapter
     */
    private class MainRvAdapter extends BaseQuickAdapter<CalendarCell, BaseViewHolder> {

        public MainRvAdapter(int layoutResId, @Nullable List<CalendarCell> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final CalendarCell item) {
            if (((RecyclerView) helper.getView(R.id.appoint_calendarview_item_rv)).getLayoutManager() == null) {
                //RecyclerView不能都使用同一个LayoutManager
                GridLayoutManager manager = new GridLayoutManager(mContext, 7);
                //recyclerview嵌套高度不固定（wrap_content）时必须setAutoMeasureEnabled(true)，否则测量时控件高度为0
                manager.setAutoMeasureEnabled(true);
                ((RecyclerView) helper.getView(R.id.appoint_calendarview_item_rv)).setLayoutManager(manager);
            }
            SubRvAdapter subRvAdapter = null;
            if (allAdapters.get(helper.getPosition()) == null) {
                subRvAdapter = new SubRvAdapter(R.layout.calendar_text_day, item.getCells());
                allAdapters.put(helper.getPosition(), subRvAdapter);
                ((RecyclerView) helper.getView(R.id.appoint_calendarview_item_rv)).setAdapter(subRvAdapter);
            } else {
                subRvAdapter = allAdapters.get(helper.getPosition());
                ((RecyclerView) helper.getView(R.id.appoint_calendarview_item_rv)).setAdapter(subRvAdapter);
            }
            //item 点击事件响应
            subRvAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Date date = item.getCells().get(position);
                    int day = date.getDate();
                    if (date.getMonth() == secondMonth
                            || (date.getDate() > dateFromServer.getDate() && date.getMonth() == firstMonth)
                            || (date.getDate() <= dateFromServer.getDate() && date.getMonth() == thirdMonth)) {
                        //可点击数据
                        if (isSelectingSTime) {
                            //正在选择开始时间
                            selectSDate(item.getCells().get(position));
                        } else {
                            //正在选择结束时间
                            selectEDate(item.getCells().get(position));
                        }
                    }
                    //更新所有的adapter，比如今天6月，需要更新6、7、8三个月份不同adapter
                    Iterator iterator = allAdapters.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        ((SubRvAdapter) entry.getValue()).notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public void selectSDate(Date date) {
        if (sDateTime != null && eDateTime != null) {
            sDateTime = date;

            notifyDateSelectChanged();
        } else {
            sDateTime = date;
            notifyDateSelectChanged();
        }
        eDateTime = null;
        isSelectingSTime = false;
    }

    public void selectEDate(Date date) {
        if (sDateTime != null) {
            if (date.getTime() > sDateTime.getTime()) {
                eDateTime = date;
                isSelectingSTime = true;
                notifyDateSelectChanged();
            }
        }

    }

    /**
     * 通知开始时间跟结束时间均改变
     */
    public void notifyDateSelectChanged() {
        if (mETimeSelectListener != null && eDateTime != null) {
            mETimeSelectListener.onETimeSelect(eDateTime);
        }
        if (mSTimeSelectListener != null && sDateTime != null) {
            mSTimeSelectListener.onSTimeSelect(sDateTime);
        }
    }


    private class SubRvAdapter extends BaseQuickAdapter<Date, BaseViewHolder> {

        public SubRvAdapter(int layoutResId, @Nullable List<Date> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Date date) {
            helper.setIsRecyclable(false);//不让recyclerview进行复用，复用会出问题
            ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).setEmptyColor(todayEmptyColor);
            ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).setFillColor(todayFillColor);
            int day = date.getDate();
            //设置文本
            ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).setText(String.valueOf(day));
            //设置颜色
            if (date.getMonth() == secondMonth
                    || (date.getDate() > dateFromServer.getDate() && date.getMonth() == firstMonth)
                    || (date.getDate() <= dateFromServer.getDate() && date.getMonth() == thirdMonth)) {
                //可选时间
                ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).setTextColor(enableSelectColor);
            } else {
                //不可选时间
                ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).setTextColor(disableSeletColor);
            }
            if (eDateTime != null && date.getTime() == eDateTime.getTime()) {
                //结束时间
                ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).isETime(true);
                ((CalendarDayRelativeLayout) helper.getView(R.id.calendar_day_rl)).isETime(true);
            }
            if (sDateTime != null && date.getTime() == sDateTime.getTime()) {
                //开始时间
                if (eDateTime != null) {
                    ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).isSTime(true);
                    ((CalendarDayRelativeLayout) helper.getView(R.id.calendar_day_rl)).isSTime(true);
                } else {
                    ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).isSTime(true);
                }
            }
            if (sDateTime != null && eDateTime != null && date.getTime() > sDateTime.getTime() && date.getTime() < eDateTime.getTime()) {
                if (date.getDay() == 6) {
                    ((CalendarDayRelativeLayout) helper.getView(R.id.calendar_day_rl)).isDurationSat(true);
                } else if (date.getDay() == 0) {
                    ((CalendarDayRelativeLayout) helper.getView(R.id.calendar_day_rl)).isDurationSun(true);
                } else {
                    helper.getView(R.id.calendar_day_rl).setBackgroundColor(getResources().getColor(R.color.date_duration_bg));
                }
            }
            if (date.getDate() == dateFromServer.getDate() && date.getMonth() == firstMonth) {
                ((CalendarDayTextView) helper.getView(R.id.calendar_day_tv)).setToday(true);
            }
        }
    }

    private class CalendarCell {
        private int position;
        ArrayList<Date> cells;

        public CalendarCell(int position, ArrayList<Date> cells) {
            this.position = position;
            this.cells = cells;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public ArrayList<Date> getCells() {
            return cells;
        }

        public void setCells(ArrayList<Date> cells) {
            this.cells = cells;
        }
    }


    //开始时间的选择监听
    public interface CalendarSTimeSelListener {
        void onSTimeSelect(Date date);
    }

    private CalendarSTimeSelListener mSTimeSelectListener;

    public void setSTimeSelListener(CalendarSTimeSelListener li) {
        mSTimeSelectListener = li;
    }

    //结束时间的监听事件
    public interface CalendatEtimSelListener {
        void onETimeSelect(Date date);
    }

    private CalendatEtimSelListener mETimeSelectListener;

    public void setETimeSelListener(CalendatEtimSelListener li) {
        mETimeSelectListener = li;
    }

}
