![这里写图片描述](https://img-blog.csdn.net/20180601113507632?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2RhbmZlbmd3/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

前阵子app里需要用到日历，效果图如上，做日历我本来是不担心的，因为之前就有写过，比如我之前的文章
 https://blog.csdn.net/danfengw/article/details/72764995
 就是写自定义日历的，但是看到具体的产品需求以及效果图，我就”嗯，很好……“，不是我不喜欢自定义，而是效果图里面的细节真的是有点多，好了，我们来看看细节：
 
<font color=#00f> 1、产品需求只显示近3个月，当天时间之前的日期灰色，三个月后当前日期之后的日期也是灰色人，嗯，不明白？那我们来举个例子：今天6月3号，显示3个月，也就是6-8月，6.3号之前是灰色，6.3号是黄色，8.3号之后是灰色。
 
 2、选择日期，第一次选择是开始日期，第二次选择是结束日期，再次选择则是开始日期（这里你可能会考虑为什么不是我第三次选择日期如果日期比结束日期晚则作为结束日期，比开始日期早则作为开始日期呢？这个逻辑我之前也尝试过，对于用户操作（1）有时候操作不方便（2）关于选择日期会有疑惑，所以才采用了这种相对容易理解的方式）。
 
 3、选择的开始日期与结束日期分别加了圆圈背景，同时在开始日期与结束日期之间的区域，周六跟周天分别是右半圆跟左半圆背景，其他日期都是矩形背景。
 </font>
 
 好了，细节大概是说完了，如果想要理解这篇自定义view建议先去看我上面链接里的自定义日历这篇文章，循序渐进。
 
 下面再说一下实现思路：<font color=#f00>recyclerview嵌套，一个水平方向LinearLayoutManager嵌套一个GridLayoutManager</font>说完你大概是理解了吧？

<font color=#00f>ps：定制版控件，只适合用来学习思路，如果要用到你自己的项目，还是需要修改的。</font>

#csdn 地址
https://blog.csdn.net/danfengw/article/details/80556062

#代码思路
1、recyclerview嵌套，设置日期

2、继承textview自定义textview，主要用于修改开始日期与结束日期的圆形背景

3、继承relativelayout自定义relativelayout，主要用于设置被选中日期的中间时间段。

##1、CalendarView
(0)初始化布局
```
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
```
appoint_calendarview.xml(可以将布局直接拷贝到自己项目，能更好的理解，所以我把布局给贴出来了)
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_marginTop="@dimen/y42"
    android:orientation="vertical">

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center_vertical"
        android:orientation="horizontal">

        <TextView
            android:id="@+id/calendar_title"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginLeft="@dimen/x60"
            android:text="2018年"
            android:textColor="@color/white"
            android:textSize="@dimen/text_size_14"
            android:textStyle="bold" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerVertical="true"
            android:layout_marginLeft="@dimen/x20"
            android:layout_toRightOf="@+id/calendar_title"
            android:text="未来三个月可预约时间"
            android:textColor="@color/transparent80_white"
            android:textSize="@dimen/text_size_13"
            android:textStyle="bold" />
    </RelativeLayout>

    <LinearLayout
        android:id="@+id/calendar_week_header"
        android:layout_width="match_parent"
        android:layout_height="@dimen/y42"
        android:layout_marginLeft="@dimen/x30"
        android:layout_marginRight="@dimen/x30"
        android:layout_marginTop="@dimen/y34"
        android:gravity="center_vertical"
        android:orientation="horizontal"
        >
        <TextView
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_gravity="center_vertical"
            android:layout_weight="1"
            android:text="日"
            android:textAlignment="center"
            android:textColor="@color/white"
            android:textSize="@dimen/text_size_15"
            android:textStyle="bold" />

        <TextView
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_gravity="center_vertical"
            android:layout_weight="1"
            android:text="一"
            android:textAlignment="center"
            android:textColor="@color/white"
            android:textSize="@dimen/text_size_15"
            android:textStyle="bold" />

        <TextView
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_gravity="center_vertical"
            android:layout_weight="1"
            android:text="二"
            android:textAlignment="center"
            android:textColor="@color/white"
            android:textSize="@dimen/text_size_15"
            android:textStyle="bold" />

        <TextView
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_gravity="center_vertical"
            android:layout_weight="1"
            android:text="三"
            android:textAlignment="center"
            android:textColor="@color/white"
            android:textSize="@dimen/text_size_15"
            android:textStyle="bold" />

        <TextView
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_gravity="center_vertical"
            android:layout_weight="1"
            android:text="四"
            android:textAlignment="center"
            android:textColor="@color/white"
            android:textSize="@dimen/text_size_15"
            android:textStyle="bold" />

        <TextView
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_gravity="center_vertical"
            android:layout_weight="1"
            android:text="五"
            android:textAlignment="center"
            android:textColor="@color/white"
            android:textSize="@dimen/text_size_15"
            android:textStyle="bold" />

        <TextView
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_gravity="center_vertical"
            android:layout_weight="1"
            android:text="六"
            android:textAlignment="center"
            android:textColor="@color/white"
            android:textSize="@dimen/text_size_15"
            android:textStyle="bold" />
    </LinearLayout>

    <android.support.v7.widget.RecyclerView
        android:id="@+id/calendar_rv"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginLeft="@dimen/x30"
        android:layout_marginRight="@dimen/x30"
        android:layout_marginTop="@dimen/y22"></android.support.v7.widget.RecyclerView>
</LinearLayout>
```

(1)设置时间
```
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
```
(2)设置3个月时间
这部分跟上一篇的自定义日历基本一致，但添加了3个月的限制。
```
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

```
appoint_calendarview_item.xml
```
<?xml version="1.0" encoding="utf-8"?>
<android.support.v7.widget.RecyclerView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/appoint_calendarview_item_rv"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

</android.support.v7.widget.RecyclerView>
```
(3)设置最外层水平recyclerview的adapter，需要注意注释的几个点
```
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
```
（4）SubRvAdapter 
这里主要是通过时间进行判断，具体逻辑根据效果图考虑
```
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
                if (date.getDay() == 6) {//星期六
                    ((CalendarDayRelativeLayout) helper.getView(R.id.calendar_day_rl)).isDurationSat(true);
                } else if (date.getDay() == 0) {//星期日
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

```
SubRvAdapter 需要的对应的布局
```
<?xml version="1.0" encoding="utf-8"?>
<com.df.dfcalendar.widget.CalendarDayRelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="@dimen/y66"
    android:gravity="center"
    android:id="@+id/calendar_day_rl"
    android:layout_marginTop="@dimen/y7"
    android:layout_marginBottom="@dimen/y7"
    >

    <com.df.dfcalendar.widget.CalendarDayTextView
        android:id="@+id/calendar_day_tv"
        android:layout_width="@dimen/y66"
        android:layout_height="@dimen/y66"
        android:layout_centerInParent="true"
        android:gravity="center"
        android:textColor="@color/white"
        android:text="31"
        android:includeFontPadding="false"
        android:textSize="@dimen/text_size_18"></com.df.dfcalendar.widget.CalendarDayTextView>
</com.df.dfcalendar.widget.CalendarDayRelativeLayout>

```
##2、CalendarDayTextView
其实这里画笔只需要一个因为开始时间与结束时间样式一样，但是最开始的时候设计的是颜色不一样，所以就写了2个画笔
```
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
```
##3.CalendarDayRelativeLayout 
自定义CalendarDayRelativeLayout，来设置不同背景：（1）长方形背景 (2)星期六（有半圆）(3)星期天(左半圆)
```
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

```
