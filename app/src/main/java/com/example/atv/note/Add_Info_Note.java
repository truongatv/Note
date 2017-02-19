package com.example.atv.note;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import layout.ChoseTime;

public class Add_Info_Note extends AppCompatActivity {
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private TextView tvTimeCreate;
    private EditText edtTitle,edtNote;
    private TextView tvAlarm;
    private RelativeLayout lnControl;
    private String timeAlarm = null;
    private ImageView image;
    public static String color= "#f5f1f1";
    private ClipData.Item addColor;
    private RelativeLayout rlAddNote;
    private Dialog dialog;
    private Note note = new Note();
    Fragment fragment;
    private MenuItem menu;
    View frag1;
    public static int temp = 0;
    private int position;
    private static final int REQUEST_CODE = 1;
    private Bitmap bitmap;
    private int mStage = 0;
    private ImageView imDelete ,imShare;
    private List<Note> listNote;
    private  NoteDatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_add__info__note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        tvTimeCreate = (TextView)findViewById(R.id.tvTimeCreate);
        edtTitle = (EditText)findViewById(R.id.edtTitle);
        edtNote = (EditText)findViewById(R.id.edtNote);
// textView is the TextView view that should display it
        tvTimeCreate.setText(currentDateTimeString);
        image = (ImageView)findViewById(R.id.image);
        edtTitle.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorText), PorterDuff.Mode.SRC_ATOP);
        edtNote.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorText), PorterDuff.Mode.SRC_ATOP);
        frag1 = findViewById(R.id.fragment);
        imDelete = (ImageView)findViewById(R.id.imDelete);
        // event click delete note
        imDelete.setOnClickListener(new MyProcessOnClickDelete());

        imShare = (ImageView)findViewById(R.id.imShare);
        // event click share sociabal
        imShare.setOnClickListener(new MyProcessOnClickShare());
        frag1.setVisibility(View.GONE);
        tvAlarm = (TextView)findViewById(R.id.tvAlarm);
        lnControl = (RelativeLayout)findViewById(R.id.lnControl);
        rlAddNote = (RelativeLayout)findViewById(R.id.rlAddNote);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        position = bundle.getInt("position");
//        MainActivity.flagAlarm = 0;
        if(MainActivity.flagControl == 0 ) {
            lnControl.setVisibility(View.GONE);
            color = "#f5f1f1";
            MainActivity.flagAlarm = 0;
        }
        if(position==-2){
            ImageView imBack,imNext;
            imBack = (ImageView)findViewById(R.id.imBack);
            imBack.setVisibility(View.GONE);
            imNext = (ImageView)findViewById(R.id.imNext);
            imNext.setVisibility(View.GONE);
            lnControl.setVisibility(View.VISIBLE);
            mStage = 1;
//            Toast.makeText(this,"ttsa",Toast.LENGTH_SHORT).show();
            Note tempNote = new Note();
            tempNote.setNoteTitle(bundle.getString("title"));
            tempNote.setNoteContent(bundle.getString("content"));
            tempNote.setNoteColor(bundle.getString("color"));
            tempNote.setNoteCreatTime(bundle.getString("createTime"));
            tempNote.setNoteAlarmTime(bundle.getString("alarmTime"));
//            Toast.makeText(this,tempNote.getNoteTitle(),Toast.LENGTH_SHORT).show();
            show(tempNote);
        }
        if(position>=0 ){
//            Toast.makeText(Add_Info_Note.this,"flagControl=1",Toast.LENGTH_LONG).show();
//            Toast.makeText(this,String.valueOf(position),Toast.LENGTH_LONG).show();
            lnControl.setVisibility(View.VISIBLE);
            showNote(MainActivity.position);
            MainActivity.flagControl = 0;
//            Toast.makeText(this,"akdsf",Toast.LENGTH_SHORT).show();
        }

    }
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.Add_Color).setVisible(false);
//        return super.onPrepareOptionsMenu(menu);
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_info, menu);
        if(mStage == 1){
            menu.findItem(R.id.Add_Color).setVisible(false);
            menu.findItem(R.id.Add_Camera).setVisible(false);
            menu.findItem(R.id.Done).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final TextView setWhite,setYellow,setBlue,setGreen;
        final TableRow tbrTakePhoto,tbrChoosePicture;
        final int i=0;
        dialog = new Dialog(Add_Info_Note.this);

        switch (item.getItemId()) {
            case R.id.Add_Color: {
                dialog.setContentView(R.layout.chose_color);
                setWhite = (TextView)dialog.findViewById(R.id.setWhite);
                setYellow = (TextView)dialog.findViewById(R.id.setYellow);
                setBlue = (TextView)dialog.findViewById(R.id.setBlue);
                setGreen = (TextView)dialog.findViewById(R.id.setGreen);
                dialog.show();

                setWhite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlAddNote.setBackgroundColor(Color.parseColor("#f5f1f1"));
                        color = "#f5f1f1";
                        dialog.dismiss();
                    }
                });
                setYellow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlAddNote.setBackgroundColor(Color.parseColor("#ffbb22"));
                        color = "#ffbb22";
                        dialog.dismiss();
                    }
                });
                setGreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlAddNote.setBackgroundColor(Color.parseColor("#77ddbb"));
                        color = "#77ddbb";
                        dialog.dismiss();
                    }
                });
                setBlue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlAddNote.setBackgroundColor(Color.parseColor("#66ccdd"));
                        color = "#66ccdd";
                        dialog.dismiss();
                    }
                });
                return true;
            }
            case R.id.Add_Camera:{
                dialog.setContentView(R.layout.insert_picture);
                dialog.show();
                tbrTakePhoto = (TableRow)dialog.findViewById(R.id.tbrTakePhoto);
                tbrTakePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//                        String nameFile = getPicture();
//                        startActivityForResult(intent,0);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File pictureDirectory =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        String pictureName = getPictureName();
                        File imageFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        try {
                            File imageStore = File.createTempFile(pictureName,".jpg",imageFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String mCurrentPhoto = "file:"+ imageFile.getAbsolutePath();

                        Uri pictureUri = Uri.fromFile(imageFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,pictureUri);
                        startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);

                    }

                    private String getPictureName() {
                        SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMdd_HHmmss");
                        String timestamp = sdf.format(new Date());
                        return  "PlantPlacesImage"+timestamp;
                    }

                    private String getPicture() {
                        return null;
                    }
                });
                return true;
            }
            case R.id.Done : {
                if(MainActivity.status == 0){
                    sendIntent(getInfoNote(),1);
                }
                else {

                    sendIntent(getInfoNote(),2);
                    MainActivity.status = 0;
                }
//                Toast.makeText(this,"test",Toast.LENGTH_LONG).show();
                finish();
                return true;
            }
            case android.R.id.home: {
                if (MainActivity.status == 0) {
                    sendIntent(getInfoNote(), 1);

                } else {
                    sendIntent(getInfoNote(), 2);
                    MainActivity.status = 0;
                }
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void ShowTime(View view){

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction  = fragmentManager.beginTransaction();
//        ChoseTime choseTime = (ChoseTime)fragmentManager.findFragmentById(R.id.fragment);
        frag1.setVisibility(View.VISIBLE);
        MainActivity.flagAlarm = 1;
//      fragmentTransaction.hide(frag);
        tvAlarm.setVisibility(View.GONE);
    }
    public void CancelChoseTime(View view){
        frag1.setVisibility(View.GONE);
        MainActivity.flagAlarm = 0;
        tvAlarm.setVisibility(View.VISIBLE);
    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Bitmap bp = (Bitmap) data.getExtras().get("data");
//
//    }
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
        if (resultCode == RESULT_OK) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            this.image.setImageBitmap(bp);
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
        }
    }
}
    //show info off note
    public  void showNote(final int position){
        List<Note> listNote = MainActivity.db.getAllNotes();
        Note note = listNote.get(position);
        ImageView imBack = (ImageView)findViewById(R.id.imBack);
        ImageView imBackHide = (ImageView)findViewById(R.id.imBackHide);
        ImageView imNext = (ImageView)findViewById(R.id.imNext);
        ImageView imNextHide = (ImageView)findViewById(R.id.imNextHide);
//        Toast.makeText(this,String.valueOf(MainActivity.flagAlarm),Toast.LENGTH_SHORT).show();
//        imNext.setVisibility(View.VISIBLE);
        show(note);
//        imNextHide.setVisibility(View.GONE);
        if(position == MainActivity.db.getAllNotes().size()-1 && MainActivity.db.getNodesCount() != 1){
            imNext.setVisibility(View.GONE);
            imNextHide.setVisibility(View.VISIBLE);
            imBackHide.setVisibility(View.GONE);
            imBack.setVisibility(View.VISIBLE);
        }
        if(position == 0 && MainActivity.db.getNodesCount() != 1){
            imBack.setVisibility(View.GONE);
            imNextHide.setVisibility(View.GONE);
            imBackHide.setVisibility(View.VISIBLE);
            imNext.setVisibility(View.VISIBLE);
        }
        if(position == 0 && MainActivity.db.getNodesCount() == 1){
            imBack.setVisibility(View.GONE);
            imBackHide.setVisibility(View.VISIBLE);
            imNext.setVisibility(View.GONE);
            imNextHide.setVisibility(View.VISIBLE);
            return ;
        }
        if(position != 0 && position != MainActivity.db.getNodesCount()-1){
            imBack.setVisibility(View.VISIBLE);
            imBackHide.setVisibility(View.GONE);
            imNext.setVisibility(View.VISIBLE);
            imNextHide.setVisibility(View.GONE);
        }
        imBack.setOnClickListener(new MyProcessOnClickBack());
        imNext.setOnClickListener(new MyProcessOnClickNext());

//        imBackHide.setOnClickListener(new MyProcessOnClickBack());
        MainActivity.flagControl = 0;
    }
    // myprocessonclickback
    private class MyProcessOnClickBack implements View.OnClickListener{
        ImageView imBack = (ImageView)findViewById(R.id.imBack);
        ImageView imBackHide = (ImageView)findViewById(R.id.imBackHide);
        ImageView imNext = (ImageView)findViewById(R.id.imNext);
        ImageView imNextHide = ( ImageView)findViewById(R.id.imNextHide);
        Note noteChanged = new Note();
        List<Note> listNote = MainActivity.db.getAllNotes();

        Note isNote = new Note();
        @Override
        public void onClick(View v) {
            imNext.setVisibility(View.VISIBLE);
            imNextHide.setVisibility(View.GONE);
//            Toast.makeText(getApplication(),String.valueOf(MainActivity.position),Toast.LENGTH_SHORT).show();
//            this.temp = temp +1;
            MainActivity.position = MainActivity.position-1;
            if(MainActivity.position == 0 ){
                isNote = listNote.get(MainActivity.position+1);
                noteChanged = getInfoNote();
                MainActivity.db.updateNote(isNote,noteChanged);
                imBack.setVisibility(View.GONE);
                imBackHide.setVisibility(View.VISIBLE);
                listNote = MainActivity.db.getAllNotes();
                show(listNote.get(MainActivity.position));
                return;
            }
            isNote = listNote.get(MainActivity.position+1);

            noteChanged = getInfoNote();
            MainActivity.db.updateNote(isNote,noteChanged);
//            Toast.makeText(getApplication(),listNote.get(MainActivity.position+1).getNoteTitle(),Toast.LENGTH_SHORT).show();
            listNote = MainActivity.db.getAllNotes();
            show(listNote.get(MainActivity.position));

        }
    }
    private class MyProcessOnClickNext implements View.OnClickListener{
        ImageView imNext = (ImageView)findViewById(R.id.imNext);
        ImageView imNextHide = ( ImageView)findViewById(R.id.imNextHide);
        ImageView imBack = (ImageView)findViewById(R.id.imBack);
        ImageView imBackHide = (ImageView)findViewById(R.id.imBackHide);
        List<Note> listNote = MainActivity.db.getAllNotes();
        Note isNote = new Note();
        Note noteChange = new Note();
        @Override
        public void onClick(View v) {
            imBack.setVisibility(View.VISIBLE);
            imBackHide.setVisibility(View.GONE);
//            Toast.makeText(getApplication(),String.valueOf(MainActivity.position),Toast.LENGTH_SHORT).show();
            MainActivity.position++;
            if(MainActivity.position == listNote.size()-1){
                isNote = listNote.get(MainActivity.position-1);
                noteChange = getInfoNote();
                MainActivity.db.updateNote(isNote,noteChange);
//                Toast.makeText(getApplication(),"return",Toast.LENGTH_SHORT).show();
                imNext.setVisibility(View.GONE);
                imNextHide.setVisibility(View.VISIBLE);
//                MainActivity.position++;
                listNote = MainActivity.db.getAllNotes();
                show(listNote.get(MainActivity.position));
                return;
            }
            isNote = listNote.get(MainActivity.position-1);
            noteChange = getInfoNote();
            MainActivity.db.updateNote(isNote,noteChange);
            listNote = MainActivity.db.getAllNotes();
            show(listNote.get(MainActivity.position));
        }
    }
    //class process event click to button delete
    private class MyProcessOnClickDelete implements View.OnClickListener{
        Note isNote = new Note();
        @Override
        public void onClick(View v) {
            note = getInfoNote();
            new AlertDialog.Builder(Add_Info_Note.this)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this?")
//                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
//                            MainActivity.db.deleteNote(note);
                            if(position>=0)
                                sendIntent(getInfoNote(),3);
                            if(position==-2){
                                MainActivity.db.deleteNote(getInfoNote());
                                MainActivity.grAdapter = new CustomGridView(Add_Info_Note.this,MainActivity.db.getAllNotes());
                                MainActivity.gridView.setAdapter(MainActivity.grAdapter);
//                                finish();
                            }
                            finish();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }
    // class process event click to button share
    public class MyProcessOnClickShare implements View.OnClickListener{

        @Override
        public void onClick(View v) {
//            Toast.makeText(getApplication(),"asdfa",Toast.LENGTH_SHORT).show();
            // method share
            shareIt();
        }
    }
    // implement method share
    public void shareIt(){
        Note note = getInfoNote();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        String shareBody = note.getNoteTitle() + note.getNoteContent();
//        intent.putExtra(Intent.EXTRA_SUBJECT,"Subject here");
//        intent.putExtra(Intent.EXTRA_TITLE,shareBody);
        intent.putExtra(Intent.EXTRA_TEXT,shareBody);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent,"Share via"));
    }
    public void show(Note note){
//        List<Note> listNote = MainActivity.db.getAllNotes();
//        note = listNote.get(position);
        tvTimeCreate.setText(note.getNoteCreatTime());
        edtTitle.setText(note.getNoteTitle());
        edtNote.setText(note.getNoteContent());
        rlAddNote.setBackgroundColor(Color.parseColor(note.getNoteColor()));

        if(note.getNoteAlarmTime().equals("")){
//            Toast.makeText(this,"null alarm ",Toast.LENGTH_SHORT).show();
            MainActivity.flagAlarm = 0;
            frag1.setVisibility(View.GONE);
            tvAlarm.setVisibility(View.VISIBLE);
        }
        else{
            MainActivity.flagAlarm = 1;
            frag1.setVisibility(View.VISIBLE);
            tvAlarm.setVisibility(View.GONE);
//            Toast.makeText(this,"test",Toast.LENGTH_SHORT).show();
            String timeAalrm = note.getNoteAlarmTime();
            String day = timeAalrm.substring(0,timeAalrm.length()-5);
            String time = timeAalrm.substring(day.length());
            ChoseTime.day[3] = day;
            ChoseTime.spnDay.setSelection(3);
            if(!time.equals("09:00")||!time.equals("13:00")||!time.equals("17:00")||!time.equals("20:00")){
                ChoseTime.time[4] = time;
                ChoseTime.spnTime.setSelection(4);
            }
            if(time.equals("09:00")){
                ChoseTime.spnTime.setSelection(0);
            }
            if(time.equals("13:00")){
                ChoseTime.spnTime.setSelection(1);
            }
            if(time.equals("17:00")){
                ChoseTime.spnTime.setSelection(2);
            }
            if(time.equals("20:00")){
                ChoseTime.spnTime.setSelection(3);
            }
        }
        color = note.getNoteColor();
    }
    public Note getInfoNote() {
        Note note = new Note();
        note.setNoteTitle(edtTitle.getText().toString());
//                note.setNoteTitle("adsjfhasj");
        note.setNoteContent(edtNote.getText().toString());
        note.setNoteCreatTime(tvTimeCreate.getText().toString());
        // get background color layout
//                ColorDrawable viewColor = (ColorDrawable) rlAddNote.getBackground();
//                int colorId = viewColor.getColor();
        note.setNoteColor(color);
//        Toast.makeText(this,note.getNoteColor(),Toast.LENGTH_SHORT).show();
        //set time alarm for note
        if (MainActivity.flagAlarm == 0) {
//            Toast.makeText(this,"test",Toast.LENGTH_SHORT).show();
            note.setNoteAlarmTime("");
        } else {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int days = c.get(Calendar.DAY_OF_MONTH);
            String tempTime = null;
            if (MainActivity.flagChoseDay == 0 || MainActivity.flagChoseDay == 1 || MainActivity.flagChoseDay == 2) {
                if(days<10 && month<10) {
                    tempTime = "0"+String.valueOf(days + MainActivity.flagChoseDay) + "/0" + month + "/" + String.valueOf(year);
                }
                if(days>9 && month<10) {
                    tempTime = String.valueOf(days + MainActivity.flagChoseDay) + "/0" + month + "/" + String.valueOf(year);
                }
                if(days<10 && month>9) {
                    tempTime = "0"+String.valueOf(days + MainActivity.flagChoseDay) + "/" + month + "/" + String.valueOf(year);
                }
                if(days>9 && month>9) {
                    tempTime = String.valueOf(days + MainActivity.flagChoseDay) + "/" + month + "/" + String.valueOf(year);
                }
            }

            if (MainActivity.flagChoseDay == 3) {
                tempTime = ChoseTime.day[3];
            }
            note.setNoteAlarmTime(tempTime + " " + ChoseTime.time[MainActivity.flagChoseTime]);
        }
    return note;
    }
    public void sendIntent(Note note,int requestCode){
        Intent intent = new Intent(this,MainActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("title",note.getNoteTitle());
        intent.putExtra("content",note.getNoteContent());
        intent.putExtra("color",note.getNoteColor());
        intent.putExtra("time_create",note.getNoteCreatTime());
        intent.putExtra("time_alarm",note.getNoteAlarmTime());
//                intent.putExtra("bundle",bundle);
        setResult(requestCode,intent);
    }

}
