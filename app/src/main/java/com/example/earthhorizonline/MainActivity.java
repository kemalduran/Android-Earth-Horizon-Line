package com.example.earthhorizonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    DrawingView drawingView = null;
    SensorManager sensorManager = null;

    TextView x;
    TextView y;
    TextView z;

    final int MY_PERMISSIONS_REQUEST_CAMERA = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        drawingView = findViewById(R.id.canvasview);

        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor
                (Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        x = (TextView) findViewById(R.id.textView1);
        y = (TextView) findViewById(R.id.textView2);
        z = (TextView) findViewById(R.id.textView3);

        if(CameraManager.checkPermissions(this)){
            CameraManager.startCamera(this, findViewById(R.id.camera_preview));
        }
        else{
            requestCameraPermission();
        }
    }

    public void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_CAMERA);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CameraManager.startCamera(this, findViewById(R.id.camera_preview));
                }
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause(){
        super.onPause();
        CameraManager.releaseCamera(findViewById(R.id.camera_preview));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                float xVal = event.values[0];
                float yVal = event.values[1];
                float zVal = event.values[2];

                // calculate angles
                double xAngle = Math.toDegrees( Math.asin(xVal / 10.0f));
                double yAngle = Math.toDegrees( Math.asin(yVal / 10.0f));
                double zAngle = Math.toDegrees( Math.asin(zVal / 10.0f));

                x.setText("X Value : " + get2FormattedDouble(xVal) + " Angle: " + get2FormattedDouble((float) xAngle));
                y.setText("Y Value : " + get2FormattedDouble(yVal) + " Angle: " + get2FormattedDouble((float) yAngle));
                z.setText("Z Value : " + get2FormattedDouble(zVal) + " Angle: " + get2FormattedDouble((float) zAngle));

                double slope = 0;

                if(yAngle >= -45 && yAngle <= 45)
                    slope = 90 + yAngle * ( xVal > 0 ? -1 : 1);
                else if(yAngle > 45)
                    slope = xAngle;
                else if (yAngle < -45)
                    slope = -xAngle;

                slope += 90; // for landscape mode

                drawingView.setSlope(slope);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    DecimalFormat df2 = null;

    String get2FormattedDouble(float d) {
        if (df2 == null) {
            df2 = new DecimalFormat("0.##");
        }
        return df2.format(d);
    }
}