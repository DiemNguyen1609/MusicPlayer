package com.example.mymusic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MusicListAdapter extends ArrayAdapter {
    private  final Activity context;


    private final String[] nameArray;

    public MusicListAdapter(Activity context, String[] nameArrayParam){

        super(context,R.layout.listview_row , nameArrayParam);

        this.context=context;
        this.nameArray=nameArrayParam;


    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row, null, true);

        TextView nameTextField = (TextView) rowView.findViewById(R.id.nameTextViewID);
        //TextView infoTextField = (TextView) rowView.findViewById(R.id.infoTextViewID);

        nameTextField.setText(nameArray[position]);

        return rowView;
    }
}
