package com.mmuhamadamirzaidi.elkencarapptest.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmuhamadamirzaidi.elkencarapptest.Model.Car;
import com.mmuhamadamirzaidi.elkencarapptest.R;

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

    public class ViewHolder{
        ImageView item_car_image;

        TextView item_car_manufacturer, item_car_name, item_car_price, item_car_plat_number;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;
        ViewHolder holder = new ViewHolder();

        if (item == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            item = inflater.inflate(layout, null);

            holder.item_car_image = item.findViewById(R.id.item_car_image);

            holder.item_car_manufacturer = item.findViewById(R.id.item_car_manufacturer);
            holder.item_car_name = item.findViewById(R.id.item_car_name);
            holder.item_car_price = item.findViewById(R.id.item_car_price);
            holder.item_car_plat_number = item.findViewById(R.id.item_car_plat_number);
            item.setTag(holder);
        }
        else{
            holder = (ViewHolder)item.getTag();
        }

        Car car = recordList.get(position);

        holder.item_car_manufacturer.setText(car.getManufacturer());
        holder.item_car_name.setText(car.getName());
        holder.item_car_price.setText(car.getPrice());
        holder.item_car_plat_number.setText(car.getPlat());

        byte[] recordImage = car.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);
        holder.item_car_image.setImageBitmap(bitmap);

        return item;
    }
}
