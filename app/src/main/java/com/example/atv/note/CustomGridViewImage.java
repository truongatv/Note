package com.example.atv.note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by luatnguyen88 on 8/18/2016.
 */
public class CustomGridViewImage extends BaseAdapter {
    private Context mContext;
    private List<Note> note;
    LayoutInflater inflater;
    public CustomGridViewImage(Context c, List<Note> note){

        mContext = c;
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
        if (convertView == null) {
//            grid = new View(mContext);

            grid = inflater.inflate(R.layout.gridview_single, null);

        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}
