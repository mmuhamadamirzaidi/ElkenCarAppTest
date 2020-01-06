package com.mmuhamadamirzaidi.elkencarapptest.Modules;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mmuhamadamirzaidi.elkencarapptest.Adapter.CarListAdapter;
import com.mmuhamadamirzaidi.elkencarapptest.Model.Car;
import com.mmuhamadamirzaidi.elkencarapptest.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView main_icon_add;

    ListView listCar;

    ArrayList<Car> cars;
    CarListAdapter carListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_icon_add = findViewById(R.id.main_icon_add);

        listCar = findViewById(R.id.listCar);

        cars = new ArrayList<>();
        carListAdapter = new CarListAdapter(this, R.layout.item_car, cars);
        listCar.setAdapter(carListAdapter);

        main_icon_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCarDetailActivity.class);
                startActivity(intent);
            }
        });

        // Get all data from sqlite
        Cursor cursor = AddCarDetailActivity.sqLiteHelper.getData("SELECT * FROM RECORD");
        cars.clear();

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String manufacturer = cursor.getString(1);
            String name = cursor.getString(2);
            String price = cursor.getString(3);
            String plat = cursor.getString(4);

            byte[] image = cursor.getBlob(5);

            cars.add(new Car(id, manufacturer, name, price, plat, image));
        }
        carListAdapter.notifyDataSetChanged();

        if (cars.size() == 0){
            // No record, list view empty

            Toast.makeText(this, "No record found!", Toast.LENGTH_SHORT).show();
        }

        listCar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Set action when on item long click

                return false;
            }
        });
   }
}
