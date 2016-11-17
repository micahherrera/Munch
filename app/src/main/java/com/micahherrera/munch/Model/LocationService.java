package com.micahherrera.munch.Model;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.micahherrera.munch.R;


/**
 * Created by micahherrera on 10/17/16.
 */

public class LocationService extends Service implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    static GoogleApiClient mGoogleApiClient;
    Location location;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG", "onCreate: creatingservice");
        mGoogleApiClient = null;
        mGoogleApiClient = getGoogleApiClient(mGoogleApiClient);
    }

    private GoogleApiClient getGoogleApiClient(GoogleApiClient googleApiClient) {
        Log.d("TAG", "getGoogleApiClient: settingupclient");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }
        return googleApiClient;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAG", "Starting Service");
        mGoogleApiClient.connect();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
        Intent intent = new Intent();
        intent.setAction("coordinatesLoaded");
        Bundle bundle = new Bundle();
        bundle.putDouble(getString(R.string.lat), location.getLatitude());
        bundle.putDouble(getString(R.string.longitude), location.getLongitude());
        intent.putExtra("bundle", bundle);
        sendBroadcast(intent);
        Log.d("STOP", "Service stopped");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("TAG", "onBind: binding");
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("TAG", "onConnected: serviceconnected");
        setupLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "ConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    public void getLocation(Location location) {

        if(location != null) {
            Log.d("TAG", "getLocation: locationnotnull");
            this.location = location;
            this.stopSelf();
        }
    }

    public void setupLocation(GoogleApiClient client){
        location = null;
        Log.d("TAG", "getLocation: gettinglocationfromservice");

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission
                (getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("TAG", "getLocation: permissionsnotworking");
        } else if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Log.d("TAG", "getLocation: got the right permissions");
        }

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);

        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, new LocationListener() {
            @Override
            public void onLocationChanged(Location locations) {
                Log.d("TAG", "onLocationChanged: "+locations.getLatitude());
                getLocation(locations);
            }

        });
    }
}
