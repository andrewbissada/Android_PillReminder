package cornez.com.pillreminder.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import cornez.com.pillreminder.R;

public class AddPill extends AppCompatActivity implements View.OnClickListener {

    String todayDate;

    EditText editText;
    EditText editText2;
    Spinner spinner;
    Button saveBtn;

    private ArrayList addPillArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pill);

        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        spinner = (Spinner) findViewById(R.id.spinner);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);


        addPillArrayList = new ArrayList();

        getTodayDate();
    }

    public void getTodayDate(){

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf    = new SimpleDateFormat("EEE, MMM d, yyyy");
        String dateString       = sdf.format(date);
        todayDate               = dateString;

    }

    @Override
    public void onClick(View v) {
        // Get the values provided by the user via the UI
        String providedNameOfPill = editText.getText().toString();
        String providedDosage = editText2.getText().toString();
        String providedFrequency = spinner.getSelectedItem().toString();


        // Pass above values to the setter methods in POJO class
        DetailsPojo detailsPojoObj = new DetailsPojo();
        detailsPojoObj.setNameOfPill(providedNameOfPill);
        detailsPojoObj.setDosage(providedDosage);
        detailsPojoObj.setFrequency(providedFrequency);
        detailsPojoObj.setDate(todayDate);

        // Add a pill with all details to an ArrayList
        addPillArrayList.add(detailsPojoObj);

        // Inserting pill details to the database is done in a separate method
        insertPill(detailsPojoObj);


        finish();   //THIS WILL RETURN TO ORIGINAL SCREEN
    }

    public void insertPill(DetailsPojo paraDetailsPojoObj){

        // First I have to open the DBHelper class by creating a new object of that
        AndroidOpenDBHelper androidOpenDbHelperObj = new AndroidOpenDBHelper(this);

        // Get a writable SQLite database, because I'm going to insert some values
        // SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
        SQLiteDatabase sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();

        // ContentValues class is used to store a set of values that the ContentResolver can process.
        ContentValues contentValues = new ContentValues();

        // Get values from the POJO class and passing them to the ContentValues class
        contentValues.put(AndroidOpenDBHelper.COLUMN_NAME_NAME_OF_PILL, paraDetailsPojoObj.getNameOfPill());
        contentValues.put(AndroidOpenDBHelper.COLUMN_NAME_DOSAGE, paraDetailsPojoObj.getDosage());
        contentValues.put(AndroidOpenDBHelper.COLLUMN_NAME_FREQUENCY, paraDetailsPojoObj.getFrequency());
        contentValues.put(AndroidOpenDBHelper.COLUMN_NAME_DATE, paraDetailsPojoObj.getDate());
        contentValues.put(AndroidOpenDBHelper.COLUMN_NAME_MORNING_STATUS_PENDING, "yes");
        contentValues.put(AndroidOpenDBHelper.COLUMN_NAME_NOON_STATUS_PENDING, "yes");
        contentValues.put(AndroidOpenDBHelper.COLUMN_NAME_NIGHT_STATUS_PENDING, "yes");

        // Now I can insert the data in to relevant table
        // I am going pass the id value, which is going to change because of the insert method, to a long variable to show in Toast
        long affectedColumnId = sqliteDatabase.insert(AndroidOpenDBHelper.TABLE_NAME_ADD_PILL, null, contentValues);

        // It is a good practice to close the database connections after you're done with it
        sqliteDatabase.close();

        Toast.makeText(this, "Values inserted column ID is :" + affectedColumnId, Toast.LENGTH_SHORT).show();

    }

}
