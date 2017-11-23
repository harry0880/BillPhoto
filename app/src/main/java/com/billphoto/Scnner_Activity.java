package com.billphoto;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;
import com.utils.DbConstant;
import com.utils.Dbhandler;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by harpreetsingh on 22/11/17.
 */

public class Scnner_Activity extends AppCompatActivity implements DbConstant, DatePickerDialog.OnDateChangedListener ,DatePickerDialog.OnDateSetListener{
    FancyButton btnPhoto;
    Button btnSave;
    ImageView img;
    EditText etInvoiceNo,etInvoiceDate,etInvoiceAmt;
    Dbhandler dbh;
    Bitmap processedBitmap;

    private static final int REQUEST_CODE = 99;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_insertion_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Bill Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        btnPhoto = (FancyButton) findViewById(R.id.btnPhoto);
        btnSave = (Button) findViewById(R.id.btnSave);
        img=(ImageView) findViewById(R.id.Snap);
        etInvoiceNo=(EditText) findViewById(R.id.etInvoiceNo);
        etInvoiceDate=(EditText) findViewById(R.id.etInvoiceDate);
        etInvoiceAmt=(EditText) findViewById(R.id.etInvoiceAmt);
        dbh=new Dbhandler(Scnner_Activity.this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            /*takePictureButton.setEnabled(false);*/
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startScan(ScanConstants.OPEN_CAMERA);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues cv=new ContentValues();
                cv.put(C_InvNo,etInvoiceNo.getText().toString());
                cv.put(C_InvDate,etInvoiceDate.getText().toString());
                cv.put(C_InvAmt,etInvoiceAmt.getText().toString());
                long id=  dbh.saveData(cv);
                cv=new ContentValues();
                cv.put(C_ID,id);
                cv.put(DbConstant.C_SnapData,imgConversion(processedBitmap));
                if(dbh.saveImg(cv)>0)
                {
                    Toast.makeText(Scnner_Activity.this,"Saved",Toast.LENGTH_SHORT).show();
                    etInvoiceNo.setText("");
                    etInvoiceAmt.setText("");
                    etInvoiceDate.setText("");
                    img.setImageResource(0);
                    btnSave.setVisibility(View.GONE);
                }

            }
        });

        etInvoiceDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_UP == motionEvent.getAction()) {

                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            Scnner_Activity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.setThemeDark(true);
                    dpd.setAccentColor(Color.parseColor("#3F51B5"));
                    dpd.setMaxDate(now);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        dpd.setAllowEnterTransitionOverlap(true);
                        dpd.setAllowReturnTransitionOverlap(true);
                    }
                    // dpd.dismissOnPause(dismissDate.isChecked());
                    dpd.showYearPickerFirst(true);
                    dpd.show(Scnner_Activity.this.getFragmentManager(), "Datepickerdialog");
                }
                return true;
            }
        });

    }

    protected void startScan(int preference) {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);

                img.setImageBitmap(bitmap);
                processedBitmap=bitmap;
                btnSave.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private String imgConversion(Bitmap bitmap) {
     try {
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
         byte[] byteArray = byteArrayOutputStream.toByteArray();
         return Base64.encodeToString(byteArray, Base64.DEFAULT);
     }
     catch (Exception e)
     {
         e.printStackTrace();
         return null;
     }
    }

    private Bitmap convertByteArrayToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }


    @Override
    public void onDateChanged() {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year+"-"+(++monthOfYear)+"-"+ dayOfMonth;
        etInvoiceDate.setText(date);
    }
}
