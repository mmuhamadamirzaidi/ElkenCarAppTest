package com.mmuhamadamirzaidi.elkencarapptest.Modules;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mmuhamadamirzaidi.elkencarapptest.Adapter.CarListAdapter;
import com.mmuhamadamirzaidi.elkencarapptest.Model.Car;
import com.mmuhamadamirzaidi.elkencarapptest.R;
import com.mmuhamadamirzaidi.elkencarapptest.SQLiteHelper;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.mmuhamadamirzaidi.elkencarapptest.Common.Common.sqLiteHelper;

public class ListCarActivity extends AppCompatActivity {

    ImageView main_icon_add;

    ListView listCar;

    ArrayList<Car> cars;
    CarListAdapter carListAdapter = null;

    final int REQUEST_CODE_GALLERY = 888;

    ImageView update_detail_image_car_preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_car);

        main_icon_add = findViewById(R.id.main_icon_add);

        listCar = findViewById(R.id.listCar);

        cars = new ArrayList<>();
        carListAdapter = new CarListAdapter(this, R.layout.item_car, cars);
        listCar.setAdapter(carListAdapter);

        // Create database
        sqLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);

        // Create table
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT, manufacturer VARCHAR, name VARCHAR, price VARCHAR, plat VARCHAR, image BLOB)");

        main_icon_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListCarActivity.this, AddCarDetailActivity.class);
                startActivity(intent);
            }
        });

        // Get all data from sqlite
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM RECORD");
        cars.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String manufacturer = cursor.getString(1);
            String name = cursor.getString(2);
            String price = cursor.getString(3);
            String plat = cursor.getString(4);

            byte[] image = cursor.getBlob(5);

            cars.add(new Car(id, manufacturer, name, price, plat, image));
        }
        carListAdapter.notifyDataSetChanged();

        if (cars.size() == 0) {
            // No record, list view empty

            Toast.makeText(this, "No record found!", Toast.LENGTH_SHORT).show();
        }

        listCar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                // Set action when on item long click
                final CharSequence[] items = {"Update", "Delete"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(ListCarActivity.this);

                dialog.setTitle("Choose An Action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (i == 0) {
                            // Update
                            Cursor c = sqLiteHelper.getData("SELECT id FROM RECORD");
                            ArrayList<Integer> arrayList = new ArrayList<Integer>();

                            while (c.moveToNext()) {
                                arrayList.add(c.getInt(0));
                            }

                            // Show update dialog
                            showDialogUpdate(ListCarActivity.this, arrayList.get(position));
                        }
                        if (i == 1) {
                            //Delete
                            Cursor c = sqLiteHelper.getData("SELECT id FROM RECORD");
                            ArrayList<Integer> arrayList = new ArrayList<Integer>();

                            while (c.moveToNext()) {
                                arrayList.add(c.getInt(0));
                            }

                            // Show delete dialog
                            showDialogDelete(arrayList.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    private void showDialogDelete(final int idRecord) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(ListCarActivity.this);
        dialogDelete.setTitle("Delete");
        dialogDelete.setMessage("Are you sure want to delete this record?");

        dialogDelete.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    sqLiteHelper.deleteData(idRecord);
                    Toast.makeText(ListCarActivity.this, "Record deleted successfully!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
                updateRecordList();
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogDelete.show();
    }

    private void showDialogUpdate(Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_activity_update_car);
        dialog.setTitle("Update");

        update_detail_image_car_preview = dialog.findViewById(R.id.update_detail_image_car_preview);
        ImageView update_detail_image_car_choose = dialog.findViewById(R.id.update_detail_image_car_choose);

        final EditText update_detail_manufacturer = dialog.findViewById(R.id.update_detail_manufacturer);
        final EditText update_detail_name = dialog.findViewById(R.id.update_detail_name);
        final EditText update_detail_price = dialog.findViewById(R.id.update_detail_price);
        final EditText update_detail_plat_number = dialog.findViewById(R.id.update_detail_plat_number);

        Button button_update = dialog.findViewById(R.id.button_update);
        Button button_cancel = dialog.findViewById(R.id.button_cancel);

        // Get all details from item derived from sqlite
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM RECORD WHERE id=" + position);
        cars.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);

            String manufacturer = cursor.getString(1);
            update_detail_manufacturer.setText(manufacturer);

            String name = cursor.getString(2);
            update_detail_name.setText(name);

            String price = cursor.getString(3);
            update_detail_price.setText(price);

            String plat = cursor.getString(4);
            update_detail_plat_number.setText(plat);

            byte[] image = cursor.getBlob(5);
            update_detail_image_car_preview.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

            cars.add(new Car(id, manufacturer, name, price, plat, image));
        }

        dialog.show();

        // Update new image
        update_detail_image_car_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check external storage permission
                ActivityCompat.requestPermissions(ListCarActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
            }
        });

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update changes into database
                try {
                    sqLiteHelper.updateData(
                            update_detail_manufacturer.getText().toString().trim(),
                            update_detail_name.getText().toString().trim(),
                            update_detail_price.getText().toString().trim(),
                            update_detail_plat_number.getText().toString().trim(),
                            ListCarActivity.imageViewToByte(update_detail_image_car_preview),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(ListCarActivity.this, "Details updated successfully!", Toast.LENGTH_SHORT).show();


                } catch (Exception error) {
                    Log.e("Update error: ", error.getMessage());
                }
                updateRecordList();
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                // Get all data from sqlite
                Cursor cursor = sqLiteHelper.getData("SELECT * FROM RECORD");
                cars.clear();

                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String manufacturer = cursor.getString(1);
                    String name = cursor.getString(2);
                    String price = cursor.getString(3);
                    String plat = cursor.getString(4);

                    byte[] image = cursor.getBlob(5);

                    cars.add(new Car(id, manufacturer, name, price, plat, image));
                }
                carListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateRecordList() {
        // Fetch all data from database
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM RECORD");
        cars.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String manufacturer = cursor.getString(1);
            String name = cursor.getString(2);
            String price = cursor.getString(3);
            String plat = cursor.getString(4);

            byte[] image = cursor.getBlob(5);

            cars.add(new Car(id, manufacturer, name, price, plat, image));
        }
        carListAdapter.notifyDataSetChanged();
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
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
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                update_detail_image_car_preview.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
