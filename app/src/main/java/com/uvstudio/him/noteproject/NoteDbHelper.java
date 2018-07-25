package com.uvstudio.him.noteproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by him on 8/26/2017.
 */

public class NoteDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="notes.db";
    private static final String TABLE_NAME="note";
    private static final String ID="id";
    private static final String TITLE="title";
    private static final String INFO="info";
    private static  final String PRIORITY="priority";

    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String create_table="CREATE TABLE "+TABLE_NAME+" (" +ID +" INTEGER PRIMARY KEY,"+TITLE+" TEXT,"+INFO +
                " TEXT," +PRIORITY
                +" TEXT)";
        sqLiteDatabase.execSQL(create_table);
        Log.d(TAG, "onCreate: Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public void addNote(Note note){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TITLE,note.getTitle());
        values.put(INFO,note.getInfo());
        values.put(PRIORITY,note.getPriority());
        sqLiteDatabase.insert(TABLE_NAME,null,values);
        sqLiteDatabase.close();
    }
    public List<Note> getNOtesList(){
        List<Note> notesList=new ArrayList<Note>();
        String getItemsQuery="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(getItemsQuery,null);
        if(cursor.moveToFirst()){
            do{
                Note note=new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setInfo(cursor.getString(2));
                note.setPriority(cursor.getString(3));
                notesList.add(note);


            }while (cursor.moveToNext());

        }

        return notesList;
    }

    public Note getOneNote(int id){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.query(TABLE_NAME,new String[]{ID,TITLE,INFO,PRIORITY},ID +" =?",
                new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        Note note=new Note(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),
                cursor.getString(3));
        return note;
    }
    public int updateNote(Note note){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TITLE,note.getTitle());
        values.put(INFO,note.getInfo());
        values.put(PRIORITY,note.getPriority());

        return sqLiteDatabase.update(TABLE_NAME,values,ID+" =?",
                new String[]{String.valueOf(note.getId())});
    }
    public void deleteNote(Note note){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME,ID+" =?",new String[]{String.valueOf(note.getId())});
        sqLiteDatabase.close();
    }
}
