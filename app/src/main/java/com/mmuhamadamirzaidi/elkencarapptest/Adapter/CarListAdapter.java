package com.mmuhamadamirzaidi.elkencarapptest.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mmuhamadamirzaidi.elkencarapptest.Model.Car;

import java.util.ArrayList;

public class CarListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Car> recordList;

    public CarListAdapter(Context context, int layout, ArrayList<Car> recordList) {
        this.context = context;
        this.layout = layout;
        this.recordList = recordList;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
