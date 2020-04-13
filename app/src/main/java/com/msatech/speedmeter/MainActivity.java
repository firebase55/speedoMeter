package com.msatech.speedmeter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {
    // variables
    private TextView sppedtext;
    private TextView topid, speedmeter, tripid, avegidd;
    private TextView altId, smeterId, trinewID, trispeedId;
    private TextView directionId;
    private TextClock textClock;
    private TextView percant, kilometer;
    private int bPercent;
    private LocationProvider _locationProvider;
    private LocationManager _locationManager;

    // compass angle
    private float currentDegree = 0f;
    private SensorManager mSensorManager;


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            percant = (TextView) findViewById(R.id.percent);
            bPercent = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            percant.setText(String.valueOf(+bPercent + "%"));


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        setContentView(R.layout.activity_main);
        Typecasting();
        fontstyle();
        this.registerReceiver(this.broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


    }

    public void fontstyle() {
        Typeface font = Typeface.createFromAsset(getBaseContext().getAssets(), "font/lcdn.ttf");
        sppedtext.setTypeface(font);
        topid.setTypeface(font);
        speedmeter.setTypeface(font);
        tripid.setTypeface(font);
        avegidd.setTypeface(font);
        altId.setTypeface(font);
        smeterId.setTypeface(font);
        trispeedId.setTypeface(font);
        textClock.setTypeface(font);
        directionId.setTypeface(font);
        trinewID.setTypeface(font);
        percant.setTypeface(font);
        kilometer.setTypeface(font);
    }

    public void Typecasting() {
        sppedtext = (TextView) findViewById(R.id.speed_id);
        topid = (TextView) findViewById(R.id.Topid);
        speedmeter = (TextView) findViewById(R.id.sppedkm);
        tripid = (TextView) findViewById(R.id.tripspped_id);
        avegidd = (TextView) findViewById(R.id.averid);
        altId = (TextView) findViewById(R.id.alt_id);
        smeterId = (TextView) findViewById(R.id.smeter);
        trinewID = (TextView) findViewById(R.id.tripidneww);
        trispeedId = (TextView) findViewById(R.id.tripidnew);
        textClock = (TextClock) findViewById(R.id.time_id);
        directionId = (TextView) findViewById(R.id.direction_id);
        kilometer = (TextView) findViewById(R.id.kilometer_id);
        percant = (TextView) findViewById(R.id.percent);

        this.ChangeSpeed(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);

        } else {
            checkpermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);

        directionId.setText(Float.toString(degree) + " ");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        currentDegree = -degree;

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }


    // location permission code


    @Override
    public void onLocationChanged(Location location)
    {
        if(location!=null)
        {
            clocation Mclocation = new clocation(location);
            this.ChangeSpeed(Mclocation);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void checkpermission() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        Toast.makeText(this, "Location Connection...", Toast.LENGTH_SHORT).show();
    }
    public void ChangeSpeed(Location location) {
        float CurrentSpeed = 0;
        if (location != null) {
            CurrentSpeed = location.getSpeed();
            Formatter formatter = new Formatter(new StringBuilder());
            formatter.format(Locale.US, "%5.2f", CurrentSpeed);
            String speed = formatter.toString();
            speed = speed.replace("", "0");
            sppedtext.setText(CurrentSpeed+"");


        }
    }
    public void useunit()
    {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1000)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                checkpermission();
            }
            else
            {
                finish();
            }
        }
    }
    public void getaltitude()
    {

    }
}
