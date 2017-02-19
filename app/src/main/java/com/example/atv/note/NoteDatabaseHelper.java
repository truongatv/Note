package com.example.atv.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by luatnguyen88 on 8/11/2016.
 */
public class NoteDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Node.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NOTE = "tb_note";
    public static final String COLUMN_NOTE_ID = "id";
    public static final String COLUMN_NOTE_TITLE = "title";
    public static final String COLUMN_NOTE_CONTENT = "content";
    public static final String COLUMN_NOTE_CREAT_TIME = "timecreate";
    public static final String COLUMN_NOTE_ALARM_TIME = "timealarm";
    public static final String COLUMN_NOTE_COLOR = "color";
    public static final String COLUMN_NOTE_FLAG_ALARM = "flag";
    Context c;

    public NoteDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        c= context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE "   + TABLE_NOTE + "(" + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +COLUMN_NOTE_TITLE +" TEXT,"+
                COLUMN_NOTE_CONTENT+" TEXT,"+ COLUMN_NOTE_COLOR +" TEXT,"+COLUMN_NOTE_ALARM_TIME +" TEXT,"+COLUMN_NOTE_CREAT_TIME+" TEXT,"
                +COLUMN_NOTE_FLAG_ALARM+" INTEGER"+")";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NOTE);
        onCreate(db);
    }
    public void createDefaultNotesIfNeed(){
        int count = this.getNodesCount();
        if(count == 0){
            Note note1 = new Note(1,"node1","test note 1","#ffbb22","11/08 22:20","11/08 22:10");
            Note note2 = new Note(2,"node2","test note 2","#ffbb22","12/08 22:20","12/08 22:10");
            this.addNote(note1);
            this.addNote(note2);
        }
    }
    public void addNote(Note note){
//        Toast.makeText(c,"add note",Toast.LENGTH_LONG).show();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_ID,note.getNoteId());
        values.put(COLUMN_NOTE_TITLE,note.getNoteTitle());
        values.put(COLUMN_NOTE_CONTENT,note.getNoteContent());
        values.put(COLUMN_NOTE_COLOR,note.getNoteColor());
        values.put(COLUMN_NOTE_ALARM_TIME,note.getNoteAlarmTime());
        values.put(COLUMN_NOTE_CREAT_TIME,note.getNoteCreatTime());
        db.insert(TABLE_NOTE,null,values);
        db.close();
    }
//    public Note getNote(int id){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query()
//        return note;
//    }
    public int updateNote(Note note,Note note2) {
//        List<Note> localArryList = new ArrayList();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE,note2.getNoteTitle());
        values.put(COLUMN_NOTE_CONTENT,note2.getNoteContent());
        values.put(COLUMN_NOTE_COLOR,note2.getNoteColor());
        values.put(COLUMN_NOTE_CREAT_TIME,note2.getNoteCreatTime());
        values.put(COLUMN_NOTE_ALARM_TIME,note2.getNoteAlarmTime());
        Toast.makeText(c,note2.getNoteColor(),Toast.LENGTH_SHORT).show();
        int i =  db.update(TABLE_NOTE,values,COLUMN_NOTE_TITLE + " = ? AND "+ COLUMN_NOTE_CONTENT + " = ? AND "+
                COLUMN_NOTE_COLOR + " = ? AND "+ COLUMN_NOTE_CREAT_TIME +" = ? ",new String[]{note.getNoteTitle(),note.getNoteContent(),
                note.getNoteColor(),note.getNoteCreatTime()});

//        int i =  db.update(TABLE_NOTE,values,COLUMN_NOTE_TITLE + " = ? ",new String[]{note.getNoteTitle()});
//        Toast.makeText(c,String.valueOf(i),Toast.LENGTH_LONG).show();
        return i;
    }
    public void deleteNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE,COLUMN_NOTE_TITLE + " =? AND "+ COLUMN_NOTE_CONTENT + " = ? AND "+
                COLUMN_NOTE_COLOR + " = ? AND "+ COLUMN_NOTE_CREAT_TIME +" = ? ",new String[]{String.valueOf(note.getNoteTitle()),String.valueOf(note.getNoteContent()),
                String.valueOf(note.getNoteColor()),String.valueOf(note.getNoteCreatTime())});
    }
    public List<Note> getAllNotes(){
        List<Note> localArrayList = new ArrayList();
        String noteList = "SELECT * FROM "+TABLE_NOTE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(noteList,null);
        if(cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setNoteTitle(cursor.getString(1));
                note.setNoteContent(cursor.getString(2));
                note.setNoteColor(cursor.getString(3));
                note.setNoteAlarmTime(cursor.getString(4));
                note.setNoteCreatTime(cursor.getString(5));
                note.setNoteId(cursor.getInt(0));
                localArrayList.add(note);
            }while (cursor.moveToNext());
        }
        return localArrayList;
    }
    public int getNodesCount(){
//        Toast.makeText(c,"get note count",Toast.LENGTH_LONG).show();
        String countQuery = "SELECT * FROM "+TABLE_NOTE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);
        int count = cursor.getCount();
        cursor.close();
        return  count;
    }
}
