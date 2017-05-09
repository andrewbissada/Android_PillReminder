package cornez.com.pillreminder.activities;


        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.provider.BaseColumns;

// A helper class to manage database creation and version management.
public class AndroidOpenDBHelper extends SQLiteOpenHelper {
    // Database attributes
    public static final String DB_NAME = "pill_reminder_db";
    public static final int DB_VERSION = 1;

    // Table attributes
    public static final String TABLE_NAME_ADD_PILL = "add_pill";
    public static final String COLUMN_NAME_NAME_OF_PILL = "name_of_pill_column";
    public static final String COLUMN_NAME_DOSAGE = "dosage_column";
    public static final String COLLUMN_NAME_FREQUENCY = "frequency_column";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_MORNING_STATUS_PENDING = "morning_status";
    public static final String COLUMN_NAME_NOON_STATUS_PENDING = "noon_status";
    public static final String COLUMN_NAME_NIGHT_STATUS_PENDING = "night_status";

    public AndroidOpenDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Called when the database is created for the first time.
    //This is where the creation of tables and the initial population of the tables should happen.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Need to check whether table that is going to be created already exists.
        //Because this method gets executed every time, I created an object of this class.
        //"create table if not exists TABLE_NAME ( BaseColumns._ID integer primary key autoincrement, FIRST_COLUMN_NAME text not null, SECOND_COLUMN_NAME integer not null);"
        String sqlQueryToCreateDetailsTable = "create table if not exists " + TABLE_NAME_ADD_PILL + " ( " + BaseColumns._ID + " integer primary key autoincrement, "
                + COLUMN_NAME_NAME_OF_PILL + " text not null, "
                + COLUMN_NAME_DOSAGE + " text not null, "
                + COLLUMN_NAME_FREQUENCY + " real not null, "
                + COLUMN_NAME_DATE + " text not null, "
                + COLUMN_NAME_MORNING_STATUS_PENDING + " text not null, "
                + COLUMN_NAME_NOON_STATUS_PENDING + " text not null, "
                + COLUMN_NAME_NIGHT_STATUS_PENDING + " text not null);";
        // Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
        db.execSQL(sqlQueryToCreateDetailsTable);
    }

    // onUpgrade method is used when you need to upgrade the database into a new version
    //As an example, the first release of the app contains DB_VERSION = 1
    //Then with the second release of the same app contains DB_VERSION = 2
    //where you may have added some new tables or alter the existing ones
    //Then need to check and do the relevant action to keep the past data and move with the next structure
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion == 1 && newVersion == 2){
            // Upgrade the database
        }
    }
}