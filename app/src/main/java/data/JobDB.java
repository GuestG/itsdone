package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.itsdone.R;
import edu.tacoma.uw.itsdone.model.Job;

public class JobDB {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Job.db";

    private JobDBHelper mJobDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public JobDB(Context context) {
        mJobDBHelper = new JobDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mJobDBHelper.getWritableDatabase();
    }

    /**
     * Inserts the job into the local sqlite table. Returns true if successful, false otherwise.
     * @param jobID
     * @param creatorUsername
     * @param title
     * @param shortDesc
     * @param longDesc
     * @param place
     * @param price
     * @param photo
     * @return true or false
     */
    public boolean insertJob(String jobID, String creatorUsername, String title, String shortDesc,
                             String longDesc, String place, String price, int photo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("JobID", jobID);
        contentValues.put("CreatorUsername", creatorUsername);
        contentValues.put("Title", title);
        contentValues.put("ShortDesc", shortDesc);
        contentValues.put("LongDesc", longDesc);
        contentValues.put("Place", place);
        contentValues.put("Price", price);
        contentValues.put("Photo", photo);

        long rowId = mSQLiteDatabase.insert("Job", null, contentValues);
        return rowId != -1;
    }

    /**
     * Delete all the data from the Courses
     */
    public void deleteJobs() {
        mSQLiteDatabase.delete("Job", null, null);
    }

    /**
     * Returns the list of courses from the local Course table.
     * @return list
     */
    public List<Job> getCourses() {

        String[] columns = {
                "JobID", "CreatorUsername", "Title", "ShortDesc",
                "LongDesc", "Place", "Price", "Photo"
        };

        Cursor c = mSQLiteDatabase.query(
                "Job",                    // The table to query
                columns,                        // The columns to return
                null,                  // The columns for the WHERE clause
                null,               // The values for the WHERE clause
                null,                   // don't group the rows
                null,                    // don't filter by row groups
                null                    // The sort order
        );
        c.moveToFirst();
        List<Job> list = new ArrayList<Job>();
        for (int i=0; i<c.getCount(); i++) {
            String id = c.getString(0);
            String creatorUsername = c.getString(1);
            String title = c.getString(2);
            String sDesc = c.getString(3);
            String lDesc = c.getString(4);
            String loc = c.getString(5);
            String pr = c.getString(6);
            String pic = c.getString(7);
            Job job = new Job(id, creatorUsername, title, sDesc, lDesc, loc, pr);
            job.setPicture(Integer.parseInt(pic));
            list.add(job);
            c.moveToNext();
        }

        return list;
    }



    class JobDBHelper extends SQLiteOpenHelper {

        private final String CREATE_JOB_SQL;

        private final String DROP_JOB_SQL;

        public JobDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_JOB_SQL = context.getString(R.string.CREATE_JOB_SQL);
            DROP_JOB_SQL = context.getString(R.string.DROP_JOB_SQL);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_JOB_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_JOB_SQL);
            onCreate(sqLiteDatabase);
        }
    }

}
