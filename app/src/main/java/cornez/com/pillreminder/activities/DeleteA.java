package cornez.com.pillreminder.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cornez.com.pillreminder.Model.PillDetailM;
import cornez.com.pillreminder.R;
import cornez.com.pillreminder.adapter.DeletePillAdapter;
import cornez.com.pillreminder.adapter.ScheduleAdapter;

public class DeleteA extends AppCompatActivity {

    List<PillDetailM> pillsList;
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        listView = (ListView) findViewById(R.id.list_view);

        fetchPillsList();
    }

    //region FetchPills
    public void fetchPillsList(){

        pillsList                       = new ArrayList<>();

        AndroidOpenDBHelper dataHelper  = new AndroidOpenDBHelper(this);
        SQLiteDatabase db               = dataHelper.getReadableDatabase();
        db.beginTransaction();

        try
        {
            String selectQuery = " SELECT * FROM "+ AndroidOpenDBHelper.TABLE_NAME_ADD_PILL + " GROUP BY " + AndroidOpenDBHelper.COLUMN_NAME_NAME_OF_PILL;

            Cursor cursor = db.rawQuery(selectQuery,null);
            if(cursor.getCount() > 0)
            {
                //while there are still values
                while (cursor.moveToNext()) {
                    String nameOfPill   = cursor.getString(cursor.getColumnIndex(AndroidOpenDBHelper.COLUMN_NAME_NAME_OF_PILL));
                    String dosage       = cursor.getString(cursor.getColumnIndex(AndroidOpenDBHelper.COLUMN_NAME_DOSAGE));
                    String frequency    = cursor.getString(cursor.getColumnIndex(AndroidOpenDBHelper.COLLUMN_NAME_FREQUENCY));
                    String morningStatus= cursor.getString(cursor.getColumnIndex(AndroidOpenDBHelper.COLUMN_NAME_MORNING_STATUS_PENDING));
                    String noonStatus   = cursor.getString(cursor.getColumnIndex(AndroidOpenDBHelper.COLUMN_NAME_NOON_STATUS_PENDING));
                    String nightStatus  = cursor.getString(cursor.getColumnIndex(AndroidOpenDBHelper.COLUMN_NAME_NIGHT_STATUS_PENDING));

                    int ID = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));

                    PillDetailM pill = new PillDetailM(morningStatus.equalsIgnoreCase("yes") ? true : false,
                            noonStatus.equalsIgnoreCase("yes") ? true : false,
                            nightStatus.equalsIgnoreCase("yes") ? true : false,
                            false,
                            ID,
                            nameOfPill,
                            dosage,
                            frequency,
                            true);

                    pillsList.add(pill);
                }
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
    //endregion


    public void updateUI(){

        if (pillsList.size() == 0){

            //TextView for empty list
            TextView tvNoPills = (TextView)findViewById(R.id.tv_no_pills);
            tvNoPills.setVisibility(View.VISIBLE); //displays the TextView

            listView.setVisibility(View.INVISIBLE);

        }

        else {

            //Orders/sorts in alphabetical order so it displays A first and Z last
            Collections.sort(pillsList, new Comparator<PillDetailM>() {
                @Override
                public int compare(PillDetailM lhs, PillDetailM rhs) {
                    //returns a positive number if the variable is Alphabetically before the other
                    return lhs.getPillName().compareTo(rhs.getPillName());
                }
            });

            DeletePillAdapter adapter = new DeletePillAdapter(this, pillsList);
            listView.setAdapter(adapter);
        }
    }

    public void updateRecord(PillDetailM pill){
        {

            pillsList.remove(pill);

            AndroidOpenDBHelper dataHelper = new AndroidOpenDBHelper(this);
            SQLiteDatabase db = dataHelper.getReadableDatabase();
            db.beginTransaction();

            try {

                //Delete WHERE NameOfPill = pillname
                //This is just the string I will insert into the delete method below
                String deleteQuery = AndroidOpenDBHelper.COLUMN_NAME_NAME_OF_PILL + "='" + pill.getPillName() + "'";

                //	delete(String table, String whereClause, String[] whereArgs)
                //Convenience method for deleting rows in the database.
                db.delete(AndroidOpenDBHelper.TABLE_NAME_ADD_PILL, deleteQuery, null);

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
}
