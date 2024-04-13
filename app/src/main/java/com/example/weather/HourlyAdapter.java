package com.example.weather;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class HourlyAdapter extends BaseAdapter {
    private List<HourlyInfo> items;

    public HourlyAdapter(List<HourlyInfo> items) {
        this.items = items;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(parent.getContext(), R.layout.hourly_forecast, null);
        } else view = convertView;

        TextView time = (TextView)view.findViewById(R.id.time);
        TextView temp = (TextView)view.findViewById(R.id.temp);
        Log.d("are you okay?", items.get(position).time);
        time.setText(items.get(position).time);
        temp.setText(items.get(position).temp);
        return view;
    }
}
