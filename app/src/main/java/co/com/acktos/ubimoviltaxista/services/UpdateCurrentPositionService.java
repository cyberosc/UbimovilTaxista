package co.com.acktos.ubimoviltaxista.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;
import java.util.Map;

import co.com.acktos.ubimoviltaxista.app.Config;
import co.com.acktos.ubimoviltaxista.controllers.DriversController;
import co.com.acktos.ubimoviltaxista.models.Driver;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class UpdateCurrentPositionService extends IntentService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{


    //Android Utils
    private GoogleApiClient mGoogleApiClient;// Google client to interact with Google API


    //ATTRIBUTES
    private Location mLastLocation;
    private String serviceId=null;
    private Driver driver;


    //Firebase
    Firebase driversRef;
    Firebase servicesRef;

    //Components
    DriversController driversController;

    public UpdateCurrentPositionService() {

        super("UpdateCurrentPositionService");


    }

    @Override
    protected void onHandleIntent(Intent intent)  {

        Log.i(Config.DEBUG_TAG,"Entry on UpdateCurrentPositionService");

        driversController=new DriversController(this);
        driver=driversController.getDriver();

        Firebase.setAndroidContext(this); //Initialize Firebase
        driversRef = new Firebase("https://ubimovil.firebaseio.com/drivers");


        //get service id
        Bundle extras= intent.getExtras();

        if(extras!=null){

            serviceId=extras.getString(Config.KEY_SERVICE);
        }


        if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)== ConnectionResult.SUCCESS){
            buildGoogleApiClient();
            if (mGoogleApiClient != null) {

                mGoogleApiClient.connect();

            }else{
                Log.i(Config.DEBUG_TAG,"googleApi client is null in UpdateCurrentPositionService");
            }

        }else{

            Log.i(Config.DEBUG_TAG,"Google play service is not available in updateCurrentPositionService" );
        }
    }

    private void getLocation() {

        Log.i(Config.DEBUG_TAG, "Entry to getLocation");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location != null) {

            mLastLocation=location;
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            Log.i(Config.DEBUG_TAG, "coordinates:" + latitude + ", " + longitude);

        } else {

            Log.i(Config.DEBUG_TAG, "Couldn't get the location. Make sure location is enabled on the device");
        }
    }




    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(Config.DEBUG_TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());

    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        Log.i(Config.DEBUG_TAG, "Connected with Google API client");
        getLocation();
        savePositionOnFirebase();

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Log.i(Config.DEBUG_TAG, "The connection with googleApiClient was suspended");
        mGoogleApiClient.connect();
    }


    // FIREBASE METHODS

    public void savePositionOnFirebase(){

        String driverId=driver.getId();

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();


        Map<String, String> driverMap = new HashMap<String, String>();

        if(mLastLocation!=null){


        driverMap.put(Config.KEY_LAST_LOCATION, mLastLocation.getLatitude()+","+mLastLocation.getLongitude());
        driverMap.put(Config.KEY_CITY, "Bogota");
        driverMap.put(Config.KEY_UPDATE_DATE, ts);


        if(serviceId!=null){

            Log.i(Config.DEBUG_TAG,"Saving request distance on Firebase, service id:"+serviceId);

            driverMap.put(Config.KEY_LAST_EVENT,Config.COLLAPSE_KEY_DISTANCE_REQUEST);
            servicesRef = new Firebase("https://ubimovil.firebaseio.com/"+Config.TABLE_SERVICES);
            servicesRef.child("service"+serviceId).child(Config.TABLE_DRIVERS).child("driver"+driverId).setValue(driverMap);

            Log.i(Config.DEBUG_TAG,"Save request distance on Firebase");

        }else{

            driversRef.child("driver"+driverId).setValue(driverMap);

        }

        }else{

            Log.i(Config.DEBUG_TAG, "savePositionOnFirebase: mLastLocation is null");
        }

    }


}
