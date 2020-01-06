package com.mmuhamadamirzaidi.elkencarapptest.Modules;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.mmuhamadamirzaidi.elkencarapptest.R;

public class MainActivity extends AppCompatActivity {

    ImageView main_icon_add;

    ListView listCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_icon_add = findViewById(R.id.main_icon_add);

        listCar = findViewById(R.id.listCar);

        main_icon_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCarDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
