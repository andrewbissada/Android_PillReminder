package cornez.com.pillreminder.activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cornez.com.pillreminder.Model.PillDetailM;
import cornez.com.pillreminder.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addPillBtn = (Button) findViewById(R.id.addPillBtn);
        addPillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddPillView();
            }
        });

        Button scheduleBtn = (Button) findViewById(R.id.scheduleBtn);
        scheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToScheduleView();
            }
        });

        Button deletePillBtn = (Button) findViewById(R.id.deleteBtn);
        deletePillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDeletePillView();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        fetchUniquePills();
    }

    public void scheduleMorningNotification() {

        Calendar firingCal= Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        firingCal.set(Calendar.HOUR_OF_DAY, 20);
        firingCal.set(Calendar.MINUTE, 37);
        firingCal.set(Calendar.SECOND, 00);


        long intendedTime = firingCal.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        System.out.println(firingCal.get(Calendar.HOUR_OF_DAY));
        System.out.println(firingCal.get(Calendar.HOUR));

        Intent notificationIntent   = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        AlarmManager alarmManager   = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent broadcast     = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(intendedTime >= currentTime) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firingCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
        }else{

            firingCal.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = firingCal.getTimeInMillis();

            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, broadcast);
        }

    }

    public void scheduleNoonNotification() {

        /*-------------- Morning Notification ------------- */
        Calendar mfiringCal  = Calendar.getInstance();
        Calendar mcurrentCal = Calendar.getInstance();

        mfiringCal.set(Calendar.HOUR_OF_DAY, 21);
        mfiringCal.set(Calendar.MINUTE, 00);
        mfiringCal.set(Calendar.SECOND, 0);

        long intendedTime = mfiringCal.getTimeInMillis();
        long currentTime  = mcurrentCal.getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if(intendedTime >= currentTime) {

            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mfiringCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
        }else{

            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            mfiringCal.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = mfiringCal.getTimeInMillis();

            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, broadcast);
        }



        /*-------------- NOON Notification ------------- */
        Calendar nFiringCal  = Calendar.getInstance();
        Calendar nCurrentCal = Calendar.getInstance();

        nFiringCal.set(Calendar.HOUR_OF_DAY, 21);
        nFiringCal.set(Calendar.MINUTE, 1);
        nFiringCal.set(Calendar.SECOND, 0);

        long intendedTime1 = nFiringCal.getTimeInMillis();
        long currentTime1  = nCurrentCal.getTimeInMillis();

        if(intendedTime1 >= currentTime1) {

            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 101, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nFiringCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
        }else{

            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 101, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            nFiringCal.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = nFiringCal.getTimeInMillis();

            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, broadcast);
        }
    }


    public void scheduleNightNotification() {

        /*-------------- Morning Notification ------------- */
        Calendar mfiringCal  = Calendar.getInstance();
        Calendar mcurrentCal = Calendar.getInstance();

        mfiringCal.set(Calendar.HOUR_OF_DAY, 8);
        mfiringCal.set(Calendar.MINUTE, 0);
        mfiringCal.set(Calendar.SECOND, 0);

        long intendedTime = mfiringCal.getTimeInMillis();
        long currentTime  = mcurrentCal.getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if(intendedTime >= currentTime) {

            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, mfiringCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
        }else{

            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            mfiringCal.add(Calendar.DAY_OF_MONTH, 1);

            alarmManager.setRepeating(AlarmManager.RTC, mfiringCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
        }






        /*-------------- NOON Notification ------------- */
        Calendar nFiringCal  = Calendar.getInstance();
        Calendar nCurrentCal = Calendar.getInstance();

        nFiringCal.set(Calendar.HOUR_OF_DAY, 13);
        nFiringCal.set(Calendar.MINUTE, 0);
        nFiringCal.set(Calendar.SECOND, 0);

        long intendedTime1 = nFiringCal.getTimeInMillis();
        long currentTime1  = nCurrentCal.getTimeInMillis();

        if(intendedTime1 >= currentTime1) {

            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 101, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nFiringCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
        }else{

            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 101, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            nFiringCal.add(Calendar.DAY_OF_MONTH, 1);

            alarmManager.setRepeating(AlarmManager.RTC, nFiringCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
        }



        /*-------------- Night Notification ------------- */
        Calendar nightFiringCal  = Calendar.getInstance();
        Calendar nightCurrentCal = Calendar.getInstance();

        nightFiringCal.set(Calendar.HOUR_OF_DAY, 21);
        nightFiringCal.set(Calendar.MINUTE, 0);
        nightFiringCal.set(Calendar.SECOND, 0);

        long intendedTime2 = nightFiringCal.getTimeInMillis();
        long currentTime2  = nightCurrentCal.getTimeInMillis();

        if(intendedTime2 >= currentTime2) {

            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 102, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nightFiringCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
        }else{

            Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
            notificationIntent.addCategory("android.intent.category.DEFAULT");

            PendingIntent broadcast = PendingIntent.getBroadcast(this, 102, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            nightFiringCal.add(Calendar.DAY_OF_MONTH, 1);

            alarmManager.setRepeating(AlarmManager.RTC, nightFiringCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
        }

    }

    public void goToAddPillView() {
        Intent addPillActivity = new Intent(this, AddPill.class);
        startActivity(addPillActivity);
    }

    public void goToScheduleView() {
        Intent scheduleActivity = new Intent(this, Schedule.class);
        startActivity(scheduleActivity);
    }

    public void goToDeletePillView() {
        Intent deleteActivity = new Intent(this, DeleteA.class);
        startActivity(deleteActivity);
    }




    public void fetchUniquePills() {

        AndroidOpenDBHelper dataHelper = new AndroidOpenDBHelper(this);
        SQLiteDatabase db = dataHelper.getReadableDatabase();

        try {

            db.beginTransaction();

            List<PillDetailM> pillsList = new ArrayList<>();

            String pillsQuery = " SELECT * FROM " + AndroidOpenDBHelper.TABLE_NAME_ADD_PILL + " GROUP BY " + AndroidOpenDBHelper.COLUMN_NAME_NAME_OF_PILL;

            Cursor cursor1 = db.rawQuery(pillsQuery, null);
            if (cursor1.getCount() > 0) {
                while (cursor1.moveToNext()) {
                    String nameOfPill = cursor1.getString(cursor1.getColumnIndex(AndroidOpenDBHelper.COLUMN_NAME_NAME_OF_PILL));

                    String dosage = cursor1.getString(cursor1.getColumnIndex(AndroidOpenDBHelper.COLUMN_NAME_DOSAGE));
                    String frequency = cursor1.getString(cursor1.getColumnIndex(AndroidOpenDBHelper.COLLUMN_NAME_FREQUENCY));
                    String morningStatus = "yes";
                    String noonStatus = "yes";
                    String nightStatus = "yes";

                    int ID = cursor1.getInt(cursor1.getColumnIndex(BaseColumns._ID));


                    PillDetailM pill = new PillDetailM(morningStatus.equalsIgnoreCase("yes") ? true : false,
                            noonStatus.equalsIgnoreCase("yes") ? true : false,
                            nightStatus.equalsIgnoreCase("yes") ? true : false,
                            false,
                            ID,
                            nameOfPill,
                            dosage,
                            frequency,
                            false);

                    pillsList.add(pill);
                }
            }

            boolean isMorningPillExits = false;
            boolean isNoonPillExits = false;
            boolean isNighPillExits = false;

            for (PillDetailM pill : pillsList) {

                String frequency = pill.getFrequency().split("x")[0];

                if (frequency.equals("3")) {
                    isNighPillExits = true;
                    break;

                } else if (frequency.equals("2"))
                    isNoonPillExits = true;

                else if (frequency.equals("1"))
                    isMorningPillExits = true;
            }

            if (isNighPillExits) {

                scheduleNightNotification();

            } else if (isNoonPillExits) {

                scheduleNoonNotification();

            } else if (isMorningPillExits) {

                scheduleMorningNotification();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }

}
