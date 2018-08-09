package com.example.briti.ecslab;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import au.com.bytecode.opencsv.CSVReader;

public class LocationTracker extends Fragment implements OnMapReadyCallback,SensorEventListener {

    private GoogleMap mMap;
    private static final String TAG = LocationTracker.class.getSimpleName();
    private double latitudeValue = 0.0;
    private double longitudeValue = 0.0;
    private double acceleration = 0.0;
    private Marker mMarker;
    private Sensor accelerometer;
    float x,y,z;
    private SensorManager mSensorManager;

    //Anonymous class that Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Getting latitude of the current location
            latitudeValue = location.getLatitude();

            // Getting longitude of the current location
            longitudeValue = location.getLongitude();

            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitudeValue, longitudeValue);
            if(mMarker == null) {
                mMarker = mMap.addMarker(new MarkerOptions().position(latLng));
            } else {
                mMarker.setPosition(latLng);
            }
            // Showing the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.activity_location_tracker, container, false);
        //you can set the title for your toolbar here for different fragments different titles
        SupportMapFragment mapFragment = (SupportMapFragment)this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, accelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        // schedule the task to run starting now and then every minute...
        timer.schedule (minTask,0l,1000*60);   // 1000*10*60 every 10 minut
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Location Tracker");
    }

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*Method called on sensor event change*/
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            //acceleration retrieved from the event and the gravity is removed
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            acceleration = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Register the listener with the Location Manager to receive location updates
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) locationListener);
        }
        catch (SecurityException e) {
            Log.e(TAG,e.getMessage());// lets the user know there is a problem with the gps
        }
    }

    Timer timer = new Timer ();
    TimerTask minTask = new TimerTask () {
        @Override
        public void run () {
            DatabaseOperation dataOp = new DatabaseOperation();
            dataOp.setDataBaseData(String.valueOf(latitudeValue),String.valueOf(longitudeValue),String.valueOf(acceleration),String.valueOf(x),String.valueOf(y),String.valueOf(z));
        }
    };


    //Method to load data into the data base from the excel file
    /*public void load() throws FileNotFoundException,IOException{
        AssetFileDescriptor descriptor = getActivity().getAssets().openFd("data.txt");
        CSVReader reader = new CSVReader(new FileReader(descriptor.getFileDescriptor()));
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            // nextLine[] is an array of values from the line
            Log.e("error","data"+nextLine[0] + nextLine[1] +nextLine[2]);
        }
    }*/
}
