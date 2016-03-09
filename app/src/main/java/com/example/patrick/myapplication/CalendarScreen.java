package com.example.patrick.myapplication;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.RelativeLayout;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;
import java.util.Date;

public class CalendarScreen extends AppCompatActivity {

    RelativeLayout layout;
   // MaterialCalendarView calendarView;
    //TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        DataBaseHelper dbh = new DataBaseHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        TypedArray ar = this.getResources().obtainTypedArray(R.array.img_id_arr);
        int len = ar.length();
        int[] resIds = new int[len];
        for (int i = 0; i < len; i++)
            resIds[i] = ar.getResourceId(i, 0);
        ar.recycle();

        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        layout=(RelativeLayout)findViewById(R.id.second_row);
        final MaterialCalendarView calendarView = (MaterialCalendarView) layout.findViewById(R.id.calendarView);
        //Calendar calendar = Calendar.getInstance();
        //CalendarDay day2 = CalendarDay.from(calendar);
        //***********************************************//
        //                                               //
        //                                               //
        // Get start date of app from shared preferences //
        //                                               //
        //                                               //
        //***********************************************//

        Calendar cal = Calendar.getInstance();
        //Get the current day, days after should not have a rating
        //Clone is necessary, as times may be slightly different otherwise
        //   and therefore can never be equal, (for the while loop condition)
        Calendar tomorrow = (Calendar) cal.clone();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 0);

        CalendarDay day;
        while (!cal.equals(tomorrow)) {
            int rating = dbh.getRating(cal);
            if(rating != -1) {
                day = CalendarDay.from(cal);
                calendarView.addDecorators(new OneDayDecorator(day,
                        ContextCompat.getDrawable(this,resIds[rating - 1])));
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
            //day = CalendarDay.from(2016,2,23);
            //calendarView.addDecorators(new OneDayDecorator(day,ContextCompat.getDrawable(this,resIds[6])));



}

    public class OneDayDecorator implements DayViewDecorator {

        private CalendarDay date;
        private Drawable d;

        public OneDayDecorator(CalendarDay day, Drawable d) {
            date = day;
            this.d = d;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return date != null && day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {

            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(1.2f));
            view.setSelectionDrawable(d);
            view.addSpan(new ForegroundColorSpan(Color.WHITE));

        }

        /**
         * We're changing the internals, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
         */
        public void setDate(Date date) {
            this.date = CalendarDay.from(date);
        }
    }
    /*public class EventDecorator implements DayViewDecorator {

        private final int colour;
        private final HashSet<CalendarDay> dates;
        private final Drawable d;


        public EventDecorator(int colour, Collection<CalendarDay> dates,Drawable d) {
            this.colour = colour;
            this.dates = new HashSet<>(dates);
            this.d = d;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if(dates.contains(day)){
                Log.d("Yup","Exists");
            }
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            Log.d("Decorate", "Did it");
            view.addSpan(new DotSpan(colour));
        }
    }*/
}

/*
        int prevTextViewId =0;
        for(int i = 0; i < 3; i++)
        {
            final TextView textView = new TextView(this);
            textView.setTextSize(22);
            textView.setText(message);
            int curTextViewId = prevTextViewId + 1;
            textView.setId(curTextViewId);
            final RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);

            params.addRule(RelativeLayout.RIGHT_OF, prevTextViewId);
            params.setMargins(30, 0, 0, 0);
            textView.setLayoutParams(params);
            prevTextViewId = curTextViewId;
            layout.addView(textView, params);
        }*/