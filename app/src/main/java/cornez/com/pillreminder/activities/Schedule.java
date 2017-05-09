package cornez.com.pillreminder.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import cornez.com.pillreminder.Model.PillDetailM;
import cornez.com.pillreminder.R;
import cornez.com.pillreminder.adapter.ScheduleAdapter;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Schedule extends AppCompatActivity {
    //public class Schedule extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    String todayDate;

    List<PillDetailM> pillsList;

    private Context context;

    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        context     = this;
        pillsList   = new ArrayList<>();

        getTodayDate();

        //Set Date Value
        TextView dateText = (TextView) findViewById(R.id.tv_date);
        dateText.setText("Date: " + todayDate);

        AndroidOpenDBHelper dataHelper  = new AndroidOpenDBHelper(context);
        SQLiteDatabase db               = dataHelper.getReadableDatabase();
        db.beginTransaction();

        try
        {

            //Grab everything (only if its from todays date)
            String selectQuery = " SELECT * FROM "+ AndroidOpenDBHelper.TABLE_NAME_ADD_PILL + " WHERE "
                    + AndroidOpenDBHelper.COLUMN_NAME_DATE + "='" + todayDate + "'";


            //Retrieving data from SQLite databases in Android is done using Cursors. The Android SQLite query method returns a Cursor object containing the results of the query.
            //Cursors store query result records in rows and grant many methods to access and iterate through the records
            //------------>
            //	rawQuery(String sql, String[] selectionArgs)
            //Runs the provided SQL and returns a Cursor over the result set.
            Cursor cursor = db.rawQuery(selectQuery,null);
            if(cursor.getCount() > 0)
            {
                while (cursor.moveToNext()) {
                    String nameOfPill   = cursor.getString(cursor.getColumnIndex(AndroidOpenDBHelper.COLUMN_NAME_NAME_OF_PILL));
                    String dosage       = cursor.getString(cursor.getColumnIndex(AndroidOpenDBHelper.COLUMN_NAME_DOSAGE));
                    String frequency    = cursor.getString(cursor.getColumnIndex(AndroidOpenDBHelper.COLLUMN_NAME_FREQUENCY));
                    String morningStatus= cursor.getString(cursor.getColumnIndex(AndroidOpenDBHelper.COLUMN_NAME_MORNING_STATUS_PENDING));
                    String noonStatus   = cursor.getString(cursor.getColumnIndex(AndroidOpenDBHelper.COLUMN_NAME_NOON_STATUS_PENDING));
                    String nightStatus  = cursor.getString(cursor.getColumnIndex(AndroidOpenDBHelper.COLUMN_NAME_NIGHT_STATUS_PENDING));

                    //ID of information in the row of the cursor
                    int ID = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));

                    //put String values from Cursor into PillDetailM
                    PillDetailM pill = new PillDetailM(morningStatus.equalsIgnoreCase("yes") ? true : false,
                            noonStatus.equalsIgnoreCase("yes") ? true : false,
                            nightStatus.equalsIgnoreCase("yes") ? true : false,
                            false,
                            ID,
                            nameOfPill,
                            dosage,
                            frequency,
                            true);
                    //then add the object of the values into ArrayList
                    pillsList.add(pill); //add to ArrayList
                }

                fetchUniquePills(db);

            }

            else{

                fetchUniquePills(db);
            }

            updateUI();

            db.setTransactionSuccessful();

        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

    }


    public void fetchUniquePills(SQLiteDatabase db){
        String pillsQuery = " SELECT * FROM "+ AndroidOpenDBHelper.TABLE_NAME_ADD_PILL + " GROUP BY "+ AndroidOpenDBHelper.COLUMN_NAME_NAME_OF_PILL;

        Cursor cursor1 = db.rawQuery(pillsQuery,null);
        if(cursor1.getCount() > 0)
        {


            while (cursor1.moveToNext()) {
                String nameOfPill   = cursor1.getString(cursor1.getColumnIndex(AndroidOpenDBHelper.COLUMN_NAME_NAME_OF_PILL));

                boolean found = false;
                for (PillDetailM pill : pillsList){
                    //nameOfPill is the String from the Cursor.
                    //pill is PillDetailM pill from ArrayList
                    if (pill.getPillName().equalsIgnoreCase(nameOfPill)) {
                        found = true;
                        break;
                    }

                }

                if(found){
                    continue;
                }else {

                    String dosage       = cursor1.getString(cursor1.getColumnIndex(AndroidOpenDBHelper.COLUMN_NAME_DOSAGE));
                    String frequency    = cursor1.getString(cursor1.getColumnIndex(AndroidOpenDBHelper.COLLUMN_NAME_FREQUENCY));
                    String morningStatus= "yes";
                    String noonStatus   = "yes";
                    String nightStatus  = "yes";

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

                    pillsList.add(pill); //add to ArrayList
                }
            }
        }
    }

    public void updateUI(){

        Collections.sort(pillsList, new Comparator<PillDetailM>() {
            @Override
            public int compare(PillDetailM lhs, PillDetailM rhs) {
                return lhs.getPillName().compareTo(rhs.getPillName());
            }
        });

        ScheduleAdapter adapter = new ScheduleAdapter(this, pillsList);
        listView                = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

    }

    public void updateRecord(int ID, int position, String column, String status, PillDetailM pill){


        if (!pill.isAddedRecord()){

            insertPill(position, pill);

        }else {

            AndroidOpenDBHelper dataHelper = new AndroidOpenDBHelper(context);
            SQLiteDatabase db = dataHelper.getReadableDatabase();
            db.beginTransaction();

            try {
                ContentValues values = new ContentValues();
                values.put(column, status);

                db.update(AndroidOpenDBHelper.TABLE_NAME_ADD_PILL, values, BaseColumns._ID + "=" + ID, null);

                db.setTransactionSuccessful();

            } catch (SQLiteException e) {
                e.printStackTrace();

            } finally {
                db.endTransaction();
                // End the transaction.
                db.close();
                // Close database

                updateUI();
            }
        }

    }

    public void insertPill(int position, PillDetailM pill){

        AndroidOpenDBHelper androidOpenDbHelperObj = new AndroidOpenDBHelper(this);
        SQLiteDatabase sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(AndroidOpenDBHelper.COLUMN_NAME_NAME_OF_PILL, pill.getPillName());
        contentValues.put(AndroidOpenDBHelper.COLUMN_NAME_DOSAGE, pill.getDoase());
        contentValues.put(AndroidOpenDBHelper.COLLUMN_NAME_FREQUENCY, pill.getFrequency());
        contentValues.put(AndroidOpenDBHelper.COLUMN_NAME_DATE, todayDate);
        contentValues.put(AndroidOpenDBHelper.COLUMN_NAME_MORNING_STATUS_PENDING, pill.isMorningPending() ? "yes" : "no");
        contentValues.put(AndroidOpenDBHelper.COLUMN_NAME_NOON_STATUS_PENDING, pill.isNoonPending() ? "yes" : "no");
        contentValues.put(AndroidOpenDBHelper.COLUMN_NAME_NIGHT_STATUS_PENDING, pill.isNightPending() ? "yes" : "no");

        long affectedColumnId = sqliteDatabase.insert(AndroidOpenDBHelper.TABLE_NAME_ADD_PILL, null, contentValues);

        // It is a good practice to close the database connections after you are done with it
        sqliteDatabase.close();


        pill.setId((int)affectedColumnId);
        pill.setAddedRecord(true);

        pillsList.set(position, pill);

        updateUI();
    }

    public void getTodayDate(){

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf    = new SimpleDateFormat("EEE, MMM d, yyyy");
        String dateString       = sdf.format(date);
        todayDate               = dateString;
    }
}
