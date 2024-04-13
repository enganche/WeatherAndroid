//package com.example.weather;
//
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import java.util.List;
//
//public class DailyAdapter extends BaseAdapter {
//    private List<DailyInfo> items;
//
//    public DailyAdapter(List<DailyInfo> items) {
//        this.items = items;
//    }
//    @Override
//    public int getCount() {
//        return items.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return items.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view;
//        if (convertView == null) {
//            view = View.inflate(parent.getContext(), R.layout.daily_forecast, null);
//        } else view = convertView;
//
//        TextView time = (TextView)view.findViewById(R.id.time);
//        TextView tempMin = (TextView)view.findViewById(R.id.tempMin);
//        TextView tempMax = (TextView)view.findViewById(R.id.tempMax);
//        Log.d("are you okay?", items.get(position).time);
//        time.setText(items.get(position).time);
//        tempMin.setText(items.get(position).tempMin);
//        tempMax.setText(items.get(position).tempMax);
//        return view;
//    }
//}
