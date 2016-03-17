package com.example.patrick.myapplication;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;

public class DateScreen extends AppCompatActivity {
    private RelativeLayout layout;
    private DataBaseHelper dbh;
    private String imagePath ="";
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_date_screen);
        //Get date supplied by intent.
        int[] dateNums = getIntent().getIntArrayExtra("com.example.patrick.DATE");
        final Calendar cal = Calendar.getInstance();
        cal.set(dateNums[0],dateNums[1],dateNums[2]);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR));
        dbh            = new DataBaseHelper(this);
        //Get array of rating drawable locations.
        TypedArray ar = this.getResources().obtainTypedArray(R.array.img_id_arr);
        int len = ar.length();
        int[] resIds = new int[len];
        for (int i = 0; i < len; i++)
            resIds[i] = ar.getResourceId(i, 0);
        ar.recycle();

        Rating data = dbh.getRow(cal);
        layout=(RelativeLayout)findViewById(R.id.layout);
        TextView notes = (TextView) findViewById(R.id.notes);
        //Set up the OnSwipeTouchListener so that when the user swipes
        //left or rate, they move between days as a result.
        findViewById(R.id.scroll_view).setOnTouchListener(new OnSwipeTouchListener(DateScreen.this) {
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(), DateScreen.class);
                CalendarDay date = CalendarDay.from(cal);
                int[] temp = {date.getYear(), date.getMonth(), date.getDay() - 1};
                intent.putExtra("com.example.patrick.DATE", temp);
                startActivity(intent);
                finish();
            }

            public void onSwipeLeft() {
                Intent intent = new Intent(getApplicationContext(), DateScreen.class);
                CalendarDay date = CalendarDay.from(cal);
                int[] temp = {date.getYear(), date.getMonth(), date.getDay() + 1};
                intent.putExtra("com.example.patrick.DATE", temp);
                startActivity(intent);
                finish();
            }

        });
        //Set up the UI according to what data is available for the date.
        if(data != null) {
            final RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (!data.getNotes().equals("")) {
                notes.setText(data.getNotes());
            }
            else {
                notes.setText("No notes for this day.");
            }
            TextView goodThings= (TextView) findViewById(R.id.good_things);
            //Display whatever amount of good things the user submitted
            //and format them.
            String goodText ="1. ";
            if(!data.getEntryOne().equals("")) {
                goodText = goodText+data.getEntryOne();
                if(!data.getEntryTwo().equals("")){
                    goodText = goodText+"\n2. "+data.getEntryTwo();
                    if(!data.getEntryThree().equals("")){
                        goodText = goodText+"\n3. "+data.getEntryThree();
                    }
                }
            }
            else {
                //Otherwise hide the box and title.
                goodThings.setVisibility(View.GONE);
                TextView goodTitle = (TextView) findViewById(R.id.title2);
                goodTitle.setVisibility(View.GONE);
            }
            goodThings.setText(goodText);
            //Get the image path from the database and display it
            imageView = (ImageView) findViewById(R.id.image);
            imagePath = data.getImagePath();
            displayImage();
            //Set up parameters for the rating bubble and display it
            params.setMargins(0, 0, 0, 0);
            TextView rating = new TextView(this);
            rating.setLayoutParams(params);
            rating.setTextSize(66);
            rating.setText("" + data.getRating());
            rating.setWidth(300);
            rating.setHeight(300);
            rating.setTextColor(Color.parseColor("#FFFFFF"));
            rating.setGravity(17);

            Drawable d = ContextCompat.getDrawable(this, resIds[data.getRating() - 1]);
            rating.setBackground(d);
            layout.addView(rating);
        }
        //If no data exists, hide all elements apart from a title
        //saying that there is "No entry for this day"
        else {
            TextView goodThings= (TextView) findViewById(R.id.good_things);
            goodThings.setVisibility(View.GONE);
            TextView goodTitle = (TextView) findViewById(R.id.title2);
            goodTitle.setVisibility(View.GONE);
            TextView title = (TextView) findViewById(R.id.title);
            title.setText("No entry for this day.");
            imageView = (ImageView) findViewById(R.id.image);
            imageView.setVisibility(View.GONE);
            notes.setVisibility(View.GONE);
        }
    }
    private void displayImage(){
        //If the user entered an image, display it
        if(!imagePath.equals("")) {
            Uri uri = Uri.parse(imagePath);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageURI(uri);
        }
    }
    //Menu
    public void goToCalendar (MenuItem item) {
        Intent intent = new Intent(this, CalendarScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // To clean up all activities
        startActivity(intent);
        finish();
    }
    public void goToGraph(MenuItem item) {
        Intent intent = new Intent(this, GraphActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // To clean up all activities
        startActivity(intent);
        finish();
    }

    public void goToMain(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // To clean up all activities
        startActivity(intent);
        finish();
    }
    public void goToSettings(MenuItem item) {
        Intent intent = new Intent(this, NotificationSettings.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // To clean up all activities
        startActivity(intent);
        finish();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
