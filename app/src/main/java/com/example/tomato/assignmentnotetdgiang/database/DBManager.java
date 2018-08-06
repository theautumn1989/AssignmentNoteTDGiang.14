package com.example.tomato.assignmentnotetdgiang.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.tomato.assignmentnotetdgiang.model.Note;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {

    // Database Version
    private static final int DB_VERSION = 1;

    // Database Name
    private static final String DB_NAME = "NoteDatabase";

    // Table name
    private static final String TABLE_NAME = "NoteTable";

    // Table Columns names
    private static final String ID = "id";
    private static final String TITLE = "title";
    public static final String CONTENT = "content";
    private static final String DATE = "date";
    private static final String TIME = "time";
    public static final String IMAGE = "image";
    public static final String COLOR = "color";
    public static final String TIMENOW = "timenow";

    private Context context;

    public DBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT, " +
                CONTENT + " TEXT, " +
                DATE + " TEXT, " +
                TIME + " TEXT, " +
                IMAGE + " BLOB, " +
                COLOR + " TEXT, " +
                TIMENOW + " TEXT" +
                " )";

        db.execSQL(sqlQuery);
        Toast.makeText(context, "create successfylly", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        if (oldVersion >= newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void insertData(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE, note.getTitle());
        values.put(CONTENT, note.getContent());
        values.put(DATE, note.getDate());
        values.put(TIME, note.getTime());
        values.put(IMAGE, note.getImage());
        values.put(COLOR, note.getColor());
        values.put(TIMENOW, note.getTimeNow());

        db.insert(TABLE_NAME, null, values);
        Toast.makeText(context, "yes", Toast.LENGTH_SHORT).show();
        db.close();
    }

    // Adding new note
    public int addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE, note.getTitle());
        values.put(CONTENT, note.getContent());
        values.put(DATE, note.getDate());
        values.put(TIME, note.getTime());
        values.put(IMAGE, note.getImage());
        values.put(COLOR, note.getColor());
        values.put(TIMENOW, note.getTimeNow());

        // Inserting Row
        long ID = db.insert(TABLE_NAME, null, values);
        db.close();
        return (int) ID;
    }

    public List<Note> getAllData() {
        List<Note> list = new ArrayList<Note>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
                note.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                note.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                note.setImage(cursor.getBlob(cursor.getColumnIndex(IMAGE)));
                note.setColor(cursor.getString(cursor.getColumnIndex(COLOR)));
                note.setTimeNow(cursor.getString(cursor.getColumnIndex(TIMENOW)));

                list.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // Getting single note
    public Note getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]
                        {
                                ID,
                                TITLE,
                                CONTENT,
                                DATE,
                                TIME,
                                IMAGE,
                                COLOR,
                                TIMENOW
                        }, ID + "=?",

                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note(Integer.parseInt(
                cursor.getString(cursor.getColumnIndex(ID))),
                cursor.getString(cursor.getColumnIndex(TITLE)),
                cursor.getString(cursor.getColumnIndex(CONTENT)),
                cursor.getString(cursor.getColumnIndex(DATE)),
                cursor.getString(cursor.getColumnIndex(TIME)),
                cursor.getBlob(cursor.getColumnIndex(IMAGE)),
                cursor.getString(cursor.getColumnIndex(COLOR)),
                cursor.getString(cursor.getColumnIndex(TIMENOW)));

        return note;
    }

    // Getting all note
    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<>();

        // Select all Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();

                note.setID(cursor.getInt(cursor.getColumnIndex(ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
                note.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                note.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                note.setImage(cursor.getBlob(cursor.getColumnIndex(IMAGE)));
                note.setColor(cursor.getString(cursor.getColumnIndex(COLOR)));
                note.setTimeNow(cursor.getString(cursor.getColumnIndex(TIMENOW)));

                // Adding Reminders to list
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        return noteList;
    }


    public int updateData(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE, note.getTitle());
        values.put(CONTENT, note.getContent());
        values.put(DATE, note.getDate());
        values.put(TIME, note.getTime());
        values.put(IMAGE, note.getImage());
        values.put(COLOR, note.getColor());
        values.put(TIMENOW, note.getTimeNow());

        return db.update(TABLE_NAME, values, ID + "=?",
                new String[]{String.valueOf(note.getID())});
    }

    public void deleteData(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " =? ",
                new String[]{String.valueOf(note.getID())});
        db.close();
    }

}
