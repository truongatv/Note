package com.example.atv.note;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by luatnguyen88 on 8/9/2016.
 */
public class CustomGridView extends BaseAdapter {
    private Context mContext;
//    private String[] info;
//    private String[]time;
//    private String[] title;
//    private int[] color;
//    private int imgId;
    private List<Note> note;
    LayoutInflater inflater;
    public CustomGridView(Context c, List<Note> note){

        mContext = c;
//        this.imgId = imgID;
//        this.color = color;
//        this.info = info;
//        this.time = time;
//        this.title = title;
        this.note = note;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return note.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        Note note = (Note)this.note.get(position);
        if (convertView == null) {
//            grid = new View(mContext);

            grid = inflater.inflate(R.layout.gridview_single, null);

        } else {
            grid = (View) convertView;
        }
        String color = note.getNoteColor();
        TextView tvInfo = (TextView) grid.findViewById(R.id.tvInfo);
        TextView tvTimeNote = (TextView)grid.findViewById(R.id.tvTimeNote);
        TextView tvTitle = (TextView)grid.findViewById(R.id.tvTitle);
        ImageView imAlarm = (ImageView)grid.findViewById(R.id.imAlarm);
        RelativeLayout rlColor = (RelativeLayout)grid.findViewById(R.id.rlNote);
        tvInfo.setText(note.getNoteContent());
        tvTitle.setText(note.getNoteTitle());
        tvTimeNote.setText(note.getNoteAlarmTime());
        rlColor.setBackgroundColor(Color.parseColor(color));
        if(note.getNoteAlarmTime().equals("")){
//                Toast.makeText(mContext,"test",Toast.LENGTH_SHORT).show();
            imAlarm.setVisibility(View.GONE);
        }
        return grid;
    }
}
