package com.example.wb_tennis_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wb_tennis_app.R;
import com.example.wb_tennis_app.models.Court;

import java.util.List;

public class CourtAdapter extends BaseAdapter {
    private Context context;
    private List<Court> courts;

    public CourtAdapter(Context context, List<Court> courts) {
        this.context = context;
        this.courts = courts;
    }

    @Override
    public int getCount() {
        return courts.size();
    }

    @Override
    public Object getItem(int position) {
        return courts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.court_item, parent, false);
        }

        Court court = courts.get(position);

        TextView typeTextView = convertView.findViewById(R.id.courtTypeTextView);
        TextView numberTextView = convertView.findViewById(R.id.courtNumberTextView);

        typeTextView.setText(court.getType());
        numberTextView.setText(court.getNumber());

        return convertView;
    }
}

