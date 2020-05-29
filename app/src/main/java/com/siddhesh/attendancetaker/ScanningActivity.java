package com.siddhesh.attendancetaker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class ScanningActivity extends AppCompatActivity
{
    ConnectionDetection cd;
    CodeScanner mCodeScanner;
    String addhar;
    MarkAttendence markAttendence;
    String address;
    DataBaseHelper helper;
    ToneGenerator tone;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        markAttendence = new MarkAttendence(this);
        cd=new ConnectionDetection(this);
        address="Not Available";
        helper=new DataBaseHelper(this);
        tone =new ToneGenerator(AudioManager.STREAM_ALARM,100);
        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        initScanning();
    }

    public void initScanning()
    {
        CodeScannerView scannerView = findViewById(R.id.scanner);
        mCodeScanner = new CodeScanner(this,scannerView);
        mCodeScanner.setFormats(CodeScanner.TWO_DIMENSIONAL_FORMATS);
        mCodeScanner.setAutoFocusEnabled(true);
        mCodeScanner.setFlashEnabled(true);
        startScanQRCode();
    }

    public void startScanQRCode()
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 200);
        }
        else
        {
            mCodeScanner.startPreview();
            mCodeScanner.setDecodeCallback(new DecodeCallback()
            {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    ScanningActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                //            Toast.makeText(getApplicationContext(),"Result:"+result.getText(),Toast.LENGTH_LONG).show();
                            vibrator.vibrate(1500);
                            tone.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT,100);
                            addhar=result.getText();
                            sendBackData();
                        }
                    });
                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 200)
        {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                mCodeScanner.startPreview();

            } else {
                buildAlertMessageNoCamera();
            }

        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED,intent);
        mCodeScanner.setFlashEnabled(false);
        finish();
    }

    public void sendBackData()
    {
        Intent intent = new Intent();
        intent.putExtra("checkedid",addhar);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void buildAlertMessageNoCamera()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.PermissionMsg)
                .setCancelable(false)
                .setPositiveButton("GIVE PERMISSION", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        ActivityCompat.requestPermissions(ScanningActivity.this, new String[]{Manifest.permission.CAMERA}, 200);
                    }
                })
                .setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        finishActivity(RESULT_CANCELED);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}