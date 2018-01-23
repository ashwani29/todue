package com.example.ashwani.a4_11_17;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.health.TimerStat;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static java.util.Calendar.AM;
import static java.util.Calendar.PM;


/**
 * Created by ashwani on 21-11-2017.
 */

public class ReminderActivity extends AppCompatActivity {
  private   DatePicker datePicker;
  private   TimePicker timePicker;
  TodoDatabase todoDatabase;
  int retrieved_Id;

    int reqcode = 1;
    int year_al, month_al, dayOfMonth_al, hourOfDay_al, minute_al;


    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder);


        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        if(datePicker == null) {
            Log.e("TAG", "NULL OBJECT DATEPICKER");
        }


        Intent intent = getIntent();
        Log.e("TAG", "intent received");
        Bundle bundle = intent.getBundleExtra("VALUES");


        todoDatabase = (TodoDatabase) bundle.getSerializable("DATABASE OBJECT");
        retrieved_Id = bundle.getInt("ID");
        Log.e("RETRIEVED ID IS :", " "+retrieved_Id);



        final Calendar calendar = Calendar.getInstance();
//            datePicker.init(
//                    calendar.get(Calendar.YEAR),
//                    calendar.get(Calendar.MONTH),
//                    calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
//                        @Override
//                        public void onDateChanged( DatePicker view, int year, int monthOfYear, int dayOfMonth ) {
//                            Log.e("Listener", "HI");
//
//                        }
//                    } );



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(calendar.get(Calendar.MINUTE));

        }else{
            timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        }



    }

    private void setAlarm( Calendar target ) {

        Log.e("See ", "\n\n***\n"
                + "Alarm is set@ " + target.getTime() + "\n"
                + "***\n");
        String id_time = String.valueOf(target.getTime());

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), reqcode, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
       alarmManager.set(AlarmManager.RTC_WAKEUP, target.getTimeInMillis(), pendingIntent);
        if(target.getTimeInMillis() > System.currentTimeMillis()){
            Log.e("TAG", "YES");
        }
        Toast.makeText(this, "Alarm set at "+ target.getTime(), Toast.LENGTH_SHORT).show();
        todoDatabase.updateTodo(retrieved_Id ,id_time);
        Log.e("TAG", " "+retrieved_Id+" "+id_time);

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuItem item = menu.findItem(R.id.app_bar_switch);
//        MenuItemCompat.setActionView(item, R.layout.switch_item);
//        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
//
//        Switch switch_button = (Switch) notifCount.findViewById(R.id.app_bar_switch);
//
//        switch_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // do something, the isChecked will be
//                // true if the switch is in the On position

//            }
//        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.del) {

            Calendar calendar = Calendar.getInstance();
            Calendar current  = Calendar.getInstance();
//            calendar.set(Calendar.MONTH , 11);
//            calendar.set(Calendar.YEAR, 2017);
//            calendar.set(Calendar.DAY_OF_MONTH, 26
//            );
//            calendar.set(Calendar.HOUR_OF_DAY, 12);
//            calendar.set(Calendar.AM_PM, PM);
//            calendar.set(Calendar.MINUTE, 9);
//            calendar.set(Calendar.SECOND, 0);

            int hour = 0;
            int minute = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                hour = timePicker.getHour();
                minute = timePicker.getMinute();
            }else{
                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();
            }

            calendar.set(datePicker.getYear(),
                    datePicker.getMonth(),
                    datePicker.getDayOfMonth(),
                    hour,
                    minute);

            int am_pm = (hour < 12) ? 0 : 1;
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.AM_PM, am_pm );

            Log.e("TAG", "Alarm set at :"+datePicker.getYear()+" "+
                    datePicker.getMonth()+" "+
                    datePicker.getDayOfMonth()+" "+
                    hour+" "+
                    minute);

            if(calendar.compareTo(current) <= 0){
                //The set Date/Time already passed
                Toast.makeText(getApplicationContext(),
                        "Invalid Date/Time" + current.getTime(),
                        Toast.LENGTH_LONG).show();
            }else{
                setAlarm(calendar);
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
