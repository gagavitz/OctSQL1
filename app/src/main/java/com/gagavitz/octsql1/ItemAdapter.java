package com.gagavitz.octsql1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    Map<String, Double> map;
    List<String> names;
    List<Double> prices;

    public ItemAdapter(Context c, Map m) {
        String layoutString = Context.LAYOUT_INFLATER_SERVICE;
        mInflater = (LayoutInflater) c.getSystemService(layoutString);
        map = m;
        names = new ArrayList<>(map.keySet());
        prices = new ArrayList<>(map.values());
    }


    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.item_layout, null);
        TextView tvName = v.findViewById(R.id.tvName);
        TextView tvPrice = v.findViewById(R.id.tvPrice);

        tvName.setText(names.get(position));
        tvPrice.setText("$" + prices.get(position).toString());

        return v;
    }
}