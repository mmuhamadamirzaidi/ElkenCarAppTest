package com.mmuhamadamirzaidi.elkencarapptest.Modules;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mmuhamadamirzaidi.elkencarapptest.R;
import com.mmuhamadamirzaidi.elkencarapptest.SQLiteHelper;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

public class AddCarDetailActivity extends AppCompatActivity {

    ImageView add_detail_icon_back, add_detail_icon_save, add_detail_image_car_choose;

    EditText add_detail_manufacturer, add_detail_name, add_detail_price, add_detail_plat_number;

    final int REQUEST_CODE_GALLERY = 999;

    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_detail);

        add_detail_icon_back = findViewById(R.id.add_detail_icon_back);
        add_detail_icon_save = findViewById(R.id.add_detail_icon_save);
        add_detail_image_car_choose = findViewById(R.id.add_detail_image_car_choose);

        add_detail_manufacturer = findViewById(R.id.add_detail_manufacturer);
        add_detail_name = findViewById(R.id.add_detail_name);
        add_detail_price = findViewById(R.id.add_detail_price);
        add_detail_plat_number = findViewById(R.id.add_detail_plat_number);

        // Create database
        sqLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);

        // Create table
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT, manufacturer VARCHAR, name VARCHAR, price VARCHAR, plat VARCHAR, image BLOB)");

        add_detail_icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add_detail_icon_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save into database
                try{
                    sqLiteHelper.insertData(
                            add_detail_manufacturer.getText().toString().trim(),
                            add_detail_name.getText().toString().trim(),
                            add_detail_price.getText().toString().trim(),
                            add_detail_plat_number.getText().toString().trim(),
                            imageViewToByte(add_detail_image_car_choose)
                    );
                    Toast.makeText(AddCarDetailActivity.this, "Details added successfully!", Toast.LENGTH_SHORT).show();

                    // Reset view
                    add_detail_manufacturer.setText("");
                    add_detail_name.setText("");
                    add_detail_price.setText("");
                    add_detail_plat_number.setText("");
                    add_detail_image_car_choose.setImageResource(R.drawable.logoround);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        add_detail_image_car_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Choose image
                ActivityCompat.requestPermissions(AddCarDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
            }
        });
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(this, "Permission to read external storage required!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && requestCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                add_detail_image_car_choose.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
