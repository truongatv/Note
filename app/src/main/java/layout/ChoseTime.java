package layout;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.atv.note.MainActivity;
import com.example.atv.note.R;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChoseTime#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChoseTime extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static Spinner spnDay,spnTime;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE"); // lay ngay hien tai
    Date d = new Date();
    String dayOfTheWeek = sdf.format(d);
    public static  String  day[] = new String[4];
    public static String time[] = new String[5];
    ArrayAdapter<String> choseDay,choseTime;





    public ChoseTime() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChoseTime.
     */
    // TODO: Rename and change types and number of parameters
    public static ChoseTime newInstance(String param1, String param2) {
        ChoseTime fragment = new ChoseTime();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        day[0]="Today";
        day[1]="Tomorrow";
        day[3]="Other...";
        day[2] = "Next "+dayOfTheWeek;
        time[0]="09:00";
        time[1]="13:00";
        time[2]="17:00";
        time[3]="20:00";
        time[4]="Other ... ";
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chose_time,container,false);

        spnDay = (Spinner)v.findViewById(R.id.spnDay);
        spnTime = (Spinner)v.findViewById(R.id.spnTime);
        // add data to spinner select day
        choseDay = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,day);
        choseDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDay.setAdapter(choseDay);
        // envent handling chose day
        spnDay.setOnItemSelectedListener(new MyProcessChoseDay());
        // add data to spinner select time
        choseTime = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,time);
        choseTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTime.setAdapter(choseTime);
        // envent handling chose time
        spnTime.setOnItemSelectedListener(new MyProcessChoseTime());
        return v;

    }
    private class MyProcessChoseDay implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int days = c.get(Calendar.DAY_OF_MONTH);
            String temp = day[position];
            if(temp.equals("Other...")){
                MainActivity.flagChoseDay=3;
//                   Toast.makeText(getActivity(),"test",Toast.LENGTH_LONG).show();
                DatePickerDialog dDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        if(monthOfYear<10 && dayOfMonth <10){
                            String date = "0"+(dayOfMonth)+ "/0" + (monthOfYear+1) + "/" + year;
                            day[3] = date;
                        }
                        if(dayOfMonth<10 && monthOfYear>9){
                            String date = "0"+(dayOfMonth)+ "/" + (monthOfYear+1) + "/" + year;
                            day[3] = date;
                        }
                        if(monthOfYear<10 && dayOfMonth>9){
                            String date = (dayOfMonth)+ "/0" + (monthOfYear+1) + "/" + year;
                            day[3] = date;
                        }
                        if(dayOfMonth>9 && monthOfYear>9) {
                            String date = (dayOfMonth)+ "/" + (monthOfYear+1) + "/" + year;
                            day[3] = date;
                        }
//                        Toast.makeText(getActivity(), date, Toast.LENGTH_LONG).show();

                        choseDay.notifyDataSetChanged();
                    }
                }, year, month, days);
                dDialog.show();
                dDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        spnDay.setSelection(0);
                        dialog.dismiss();
                    }
                });
            }
            switch (position){
                case 0:{
                    MainActivity.flagChoseDay=0;
                    day[3] = "Other...";
                    break;
                }
                case 1:{
                    MainActivity.flagChoseDay=1;
                    day[3] = "Other...";
                    break;
                }
                case 2: {
                    MainActivity.flagChoseDay=2;
                    day[3] = "Other...";
                    break;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    private class  MyProcessChoseTime implements AdapterView.OnItemSelectedListener{
        String strHour ;
        String strMinute;
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final Calendar c = Calendar.getInstance();
            final int hour = c.get(Calendar.HOUR_OF_DAY);
            final int minute = c.get(Calendar.MINUTE);
            String temp = time[position];
            if(temp.equals("Other...")) {
                MainActivity.flagChoseTime = 4;
                TimePickerDialog tDialog = new TimePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog , new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                        if(hourOfDay<10){
                            strHour = String.valueOf(hourOfDay);
                            strHour = "0"+strHour;
                        }
                        if(minuteOfDay <10){
                            strMinute = String.valueOf(minuteOfDay);
                            strMinute = "0"+strMinute;
                        }
                        if(hourOfDay>9){
                            strHour = String.valueOf(hourOfDay);
                        }
                        if(minuteOfDay>9){
                            strMinute = String.valueOf(minuteOfDay);
                        }
                        String times = (strHour) + ":" + (strMinute) ;
                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:aa");
                        time[4] = times;
                        choseTime.notifyDataSetChanged();
                    }
                }, hour,minute,true);
                tDialog.show();
                tDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        spnTime.setSelection(0);
                        dialog.dismiss();
                    }
                });
            }
            switch (position){
                case 0:{
                    MainActivity.flagChoseTime = 0;
                    time[4] = "Other...";
                    break;
                }
                case 1:{
                    MainActivity.flagChoseTime = 1;
                    time[4] = "Other...";
                    break;
                }
                case 2:{
                    MainActivity.flagChoseTime = 2;
                    time[4] = "Other...";
                    break;
                }
                case 3: {
                    MainActivity.flagChoseTime = 3;
                    time[4] = "Other...";
                    break;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


}
