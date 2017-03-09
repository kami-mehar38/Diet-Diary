package a098.ramzan.kamran.paleodietdiary;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * This project CampusApp is created by Kamran Ramzan on 8/31/16.
 */
class Modal extends SQLiteOpenHelper {

    private static int VERSION = 1;
    private static String DATABASE_NAME = "EVENTS.db";
    private static String TABLE_NAME = "DIET_EVENTS";
    private static String SERIAL = "SERIAL_NO";
    private static String COL_EVENT_NAME = "NAME";
    private static String COL_EVENT_DATE = "EVENT_DATE";
    private static String COL_EVENT_TIME = "EVENT_TIME";
    private static String COL_EVENT_ICON = "EVENT_ICON";
    private static String COL_EVENT_DESCRIPTION = "EVENT_DESCRIPTION";
    private static String COL_EVENT_CLASS = "EVENT_CLASS";
    private Context context;

    Modal(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                + SERIAL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_EVENT_NAME + " TEXT,"
                + COL_EVENT_ICON + " INTEGER,"
                + COL_EVENT_DATE + " TEXT,"
                + COL_EVENT_TIME + " TEXT,"
                + COL_EVENT_DESCRIPTION + " TEXT,"
                + COL_EVENT_CLASS + " TEXT"
                + " )";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    void addEvent(EventInfo eventInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EVENT_NAME, eventInfo.getEventName());
        contentValues.put(COL_EVENT_ICON, eventInfo.getEventIcon());
        contentValues.put(COL_EVENT_DATE, eventInfo.getEventDate());
        contentValues.put(COL_EVENT_TIME, eventInfo.getEventTime());
        contentValues.put(COL_EVENT_DESCRIPTION, eventInfo.getEventDescription());
        contentValues.put(COL_EVENT_CLASS, eventInfo.getEventClass());
        if (db.insert(TABLE_NAME, null, contentValues) < 0) {
            Toast.makeText(context, "Event not added", Toast.LENGTH_SHORT).show();
        }
        db.close();

    }

    void updateEvent(EventInfo eventInfo, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_EVENT_NAME, eventInfo.getEventName());
        values.put(COL_EVENT_DATE, eventInfo.getEventDate());
        values.put(COL_EVENT_TIME, eventInfo.getEventTime());
        values.put(COL_EVENT_DESCRIPTION, eventInfo.getEventDescription());
        db.update(TABLE_NAME, values, SERIAL + "=" + id, null);
        Toast.makeText(context, "Event updated", Toast.LENGTH_SHORT).show();
    }

    List<EventInfo> getEvents(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EVENT_DATE + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{date});
        cursor.moveToFirst();
        EventInfo[] eventInfo = new EventInfo[cursor.getCount()];
        List<EventInfo> eventInfoList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            eventInfo[cursor.getPosition()] = new EventInfo();
            eventInfo[cursor.getPosition()].setId(cursor.getInt(0));
            eventInfo[cursor.getPosition()].setEventName(cursor.getString(1));
            eventInfo[cursor.getPosition()].setEventIcon(cursor.getInt(2));
            eventInfo[cursor.getPosition()].setEventDate(cursor.getString(3));
            eventInfo[cursor.getPosition()].setEventTime(cursor.getString(4));
            eventInfo[cursor.getPosition()].setEventDescription(cursor.getString(5));
            eventInfo[cursor.getPosition()].setEventClass(cursor.getString(6));
            eventInfoList.add(eventInfo[cursor.getPosition()]);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return eventInfoList;
    }

    void deleteEvent(String classs, String name, String date, String time, String description, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_EVENT_CLASS + " = ? AND " + COL_EVENT_NAME + " = ? AND " + COL_EVENT_DATE + " = ? AND " + COL_EVENT_TIME + " = ? AND " + COL_EVENT_DESCRIPTION + " = ? AND " + SERIAL + " = ? ", new String[]{classs, name, date, time, description, String.valueOf(id)});
        db.close();
        Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show();
    }

    void exportDatabaseToCSV() {
        new BackupData().execute();
    }

    private class BackupData extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Backing up data...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("_dd_MM_yyyy_ss", Locale.getDefault());
                        String fileName = "PaleoDietBackup" + simpleDateFormat.format(Calendar.getInstance().getTime()) + ".csv" ;
                        SQLiteDatabase db = getWritableDatabase();
                        Cursor c = db.rawQuery("select * from " + TABLE_NAME, null);
                        int rowcount;

                        int colcount;

                        File sdCardDir = Environment.getExternalStorageDirectory();

                        // the name of the file to export with

                        File saveFile = new File(sdCardDir, fileName);

                        FileWriter fw = new FileWriter(saveFile);

                        BufferedWriter bw = new BufferedWriter(fw);

                        rowcount = c.getCount();
                        colcount = c.getColumnCount();

                        if (rowcount > 0) {
                            c.moveToFirst();

                            for (int i = 0; i < colcount; i++) {
                                if (i != colcount - 1) {
                                    bw.write(c.getColumnName(i) + ",");
                                } else {
                                    bw.write(c.getColumnName(i));
                                }
                            }
                            bw.newLine();

                            for (int i = 0; i < rowcount; i++) {
                                c.moveToPosition(i);

                                for (int j = 0; j < colcount; j++) {

                                    if (j != colcount - 1)

                                        bw.write(c.getString(j) + ",");

                                    else
                                        bw.write(c.getString(j));
                                }
                                bw.newLine();
                            }

                            bw.flush();
                            Toast.makeText(context, "Exported successfully in device", Toast.LENGTH_SHORT).show();
                            c.close();
                            db.close();
                        }

                    } catch (Exception ex) {
                        Log.i("TAG", "exportDatabaseToCSV: " + ex.getMessage());
                        Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.cancel();
        }
    }

    void shareFile() {
        new ShareFile().execute();
    }

    private class ShareFile extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Backing up data...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("_dd_MM_yyyy_ss", Locale.getDefault());
                        String fileName = "PaleoDietBackup" + simpleDateFormat.format(Calendar.getInstance().getTime()) + ".csv" ;
                        SQLiteDatabase db = getWritableDatabase();
                        Cursor c = db.rawQuery("select * from " + TABLE_NAME, null);
                        int rowcount;

                        int colcount;

                        File sdCardDir = Environment.getExternalStorageDirectory();

                        // the name of the file to export with

                        File saveFile = new File(sdCardDir, fileName);

                        FileWriter fw = new FileWriter(saveFile);

                        BufferedWriter bw = new BufferedWriter(fw);

                        rowcount = c.getCount();
                        colcount = c.getColumnCount();

                        if (rowcount > 0) {
                            c.moveToFirst();

                            for (int i = 0; i < colcount; i++) {
                                if (i != colcount - 1) {
                                    bw.write(c.getColumnName(i) + ",");
                                } else {
                                    bw.write(c.getColumnName(i));
                                }
                            }
                            bw.newLine();

                            for (int i = 0; i < rowcount; i++) {
                                c.moveToPosition(i);

                                for (int j = 0; j < colcount; j++) {

                                    if (j != colcount - 1)

                                        bw.write(c.getString(j) + ",");

                                    else
                                        bw.write(c.getString(j));
                                }
                                bw.newLine();
                            }

                            bw.flush();
                            Toast.makeText(context, "Exported successfully in device", Toast.LENGTH_SHORT).show();
                            c.close();
                            db.close();
                            Intent intentShareFile = new Intent(Intent.ACTION_SEND);

                            if(saveFile.exists()) {
                                intentShareFile.setType("application/pdf");
                                intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+"/storage/emulated/0/"+fileName));

                                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                        "Sharing File...");
                                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                                context.startActivity(Intent.createChooser(intentShareFile, "Share File"));
                            }
                        }

                    } catch (Exception ex) {
                        Log.i("TAG", "exportDatabaseToCSV: " + ex.getMessage());
                        Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.cancel();
        }
    }

}
