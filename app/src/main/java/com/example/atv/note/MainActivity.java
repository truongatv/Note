package com.example.atv.note;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    public static GridView gridView;
    public static List<Note> note;
    private Intent intent;
    private Note noteIntent = new Note();
    private Note noteSelected = new Note();
    public static int flagControl=0; // flag = 0 : control layout is GONE and else
    public static int flagAlarm = 0; // flag = 0 : alarm time is GONE and else
    public static int flagChoseDay; //flag = 0 : day[0] ...
    public static int flagChoseTime; // flag = 0: time[0]...
    public static NoteDatabaseHelper db;
    public static CustomGridView grAdapter;
    public static int status = 0;
    static int position = 0;
    private NotificationCompat.Builder notBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        intent = new Intent(this, Add_Info_Note.class);
        gridView = (GridView)findViewById(R.id.gridView);
        db = new NoteDatabaseHelper(this);
        flagAlarm = 0;
//        db.createDefaultNotesIfNeed();
//        db.addNote(noteIntent);
        note = db.getAllNotes();
        // add grid view show to to mainactivity
         grAdapter = new CustomGridView(MainActivity.this,note);
        gridView.setAdapter(grAdapter);
        gridView.setOnItemClickListener(new MyProcessGridViewSelected());
//        Toast.makeText(this,String.valueOf(flagAlarm),Toast.LENGTH_SHORT).show();
        flagControl = 0;

    }
    private class MyProcessGridViewSelected implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            note = db.getAllNotes();
            noteSelected = (Note) note.get(position);
//            Toast.makeText(getApplication(),noteSelected.getNoteTitle(),Toast.LENGTH_LONG).show();
            flagControl = 1;
            MainActivity.position = position;
            intent.putExtra("position",position);
            status = 1;
            if(!noteSelected.getNoteAlarmTime().equals("")) flagAlarm=1;
            Add_Info_Note.color = noteSelected.getNoteColor();
            startActivityForResult(intent,2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        noteIntent.setNoteTitle(data.getStringExtra("title"));
        noteIntent.setNoteContent(data.getStringExtra("content"));
        noteIntent.setNoteColor(data.getStringExtra("color"));
        noteIntent.setNoteCreatTime(data.getStringExtra("time_create"));
        noteIntent.setNoteAlarmTime(data.getStringExtra("time_alarm"));
//        Toast.makeText(this,noteIntent.getNoteColor(), Toast.LENGTH_LONG).show();
        if(resultCode == 1) {
            noteIntent.setNoteId((int)System.currentTimeMillis());
            db.addNote(noteIntent);
            Toast.makeText(this,String.valueOf(noteIntent.getNoteId()),Toast.LENGTH_SHORT).show();
            grAdapter = new CustomGridView(MainActivity.this,db.getAllNotes());
            gridView.setAdapter(grAdapter);
            if(!noteIntent.getNoteAlarmTime().equals("")) {
                createPendingAlarm(getNotification(db.getAllNotes().get(db.getAllNotes().size() - 1))
                        , db.getAllNotes().get(db.getAllNotes().size() - 1), 1);
            }
        }
        if(resultCode == 2){
            note = db.getAllNotes();
            noteSelected = (Note) note.get(MainActivity.position);
//            Toast.makeText(this,noteIntent.getNoteColor(), Toast.LENGTH_LONG).show();
            db.updateNote(noteSelected,noteIntent);
            note = db.getAllNotes();
            if(!noteSelected.getNoteAlarmTime().equals("")) {
                createPendingAlarm(getNotification(noteSelected), noteSelected, 2);
            }
            if(!noteIntent.getNoteAlarmTime().equals("")) {
                createPendingAlarm(getNotification(noteIntent), noteIntent, 1);
            }
//            Toast.makeText(this, noteSelected.getNoteTitle(), Toast.LENGTH_LONG).show();
            // add grid view show to to mainactivity
            grAdapter = new CustomGridView(MainActivity.this,note);
            gridView.setAdapter(grAdapter);
//            gridView.setOnItemClickListener(new MyProcessGridViewSelected());
        }
        if(resultCode == 3){
//            Toast.makeText(this,"asdjfh",Toast.LENGTH_SHORT).show();
            db.deleteNote(noteIntent);
            if(!noteIntent.getNoteAlarmTime().equals("")) {
                Toast.makeText(this,"show noti",Toast.LENGTH_SHORT).show();
                createPendingAlarm(getNotification(noteIntent), noteIntent, 2);
            }
//            Toast.makeText(this,String.valueOf(i),Toast.LENGTH_SHORT).show();
            grAdapter = new CustomGridView(MainActivity.this,db.getAllNotes());
            gridView.setAdapter(grAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.Add_Note) {
            flagControl = 0;
            status = 0;
            intent.putExtra("position",-1);
            startActivityForResult(intent,1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void createPendingAlarm(Notification notification,Note note,int stage){


        Intent intent = new Intent(this,NotificationPublisher.class);
        intent.putExtra("title",note.getNoteTitle());
        intent.putExtra("content",note.getNoteContent());
        intent.putExtra("color",note.getNoteColor());
        intent.putExtra("createTime",note.getNoteCreatTime());
        intent.putExtra("alarmTime",note.getNoteAlarmTime());
        intent.putExtra("position",-2);
        intent.putExtra(NotificationPublisher.NOTIFICATION_ID,note.getNoteId());
        intent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, note.getNoteId(),  intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        if(stage == 1) {
            Calendar calendar = getTime(note.getNoteAlarmTime());
            Toast.makeText(this,String.valueOf(calendar.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()),Toast.LENGTH_SHORT).show();
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() / 2, calendar.getTimeInMillis() / 2, pendingIntent);
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, 5000, pendingIntent);
        alarmManager.set(alarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
//        Toast.makeText(this,String.valueOf(System.currentTimeMillis()),Toast.LENGTH_SHORT).show();
        }
        if(stage == 2){
            alarmManager.cancel(pendingIntent);
        }
    }
    private Notification getNotification(Note note) {
        Intent intent = new Intent(this,Add_Info_Note.class);
        intent.putExtra("title",note.getNoteTitle());
        intent.putExtra("content",note.getNoteContent());
        intent.putExtra("color",note.getNoteColor());
        intent.putExtra("createTime",note.getNoteCreatTime());
        intent.putExtra("alarmTime",note.getNoteAlarmTime());
        intent.putExtra("position",-2);
        this.notBuilder = new NotificationCompat.Builder(this);
        this.notBuilder.setAutoCancel(true);
        this.notBuilder.setSmallIcon(R.drawable.ic_launcher);
        this.notBuilder.setTicker("this is a ticket");
//        this.notBuilder.setWhen(100000000);
        this.notBuilder.setContentTitle(note.getNoteTitle());
        this.notBuilder.setContentText(note.getNoteContent());
        PendingIntent pendingItent = PendingIntent.getActivity(this,note.getNoteId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        this.notBuilder.setContentIntent(pendingItent);
        return notBuilder.build();
    }
    public Calendar getTime(String time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String day = time.substring(0,time.length()-6);
        String hours = time.substring(day.length()+1);
        String isYear = day.substring(day.length()-4,day.length());
        String isMonth = day.substring(3,5);
        String isDay = day.substring(0,2);
        String isHours = hours.substring(0,2);
        String isMinute = hours.substring(3,5);
        calendar.set(Calendar.YEAR,Integer.parseInt(isYear));
        calendar.set(Calendar.MONTH,Integer.parseInt(isMonth)-1);
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(isDay));
        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(isHours));
        calendar.set(Calendar.MINUTE,Integer.parseInt(isMinute));
        calendar.set(Calendar.SECOND,0);
//        calendar.set(Calendar.AM_PM,calendar.PM);
        return calendar;
    }
}
