package co.com.acktos.ubimoviltaxista.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import co.com.acktos.ubimoviltaxista.R;
import co.com.acktos.ubimoviltaxista.android.Encrypt;
import co.com.acktos.ubimoviltaxista.android.InternalStorage;
import co.com.acktos.ubimoviltaxista.app.AppController;
import co.com.acktos.ubimoviltaxista.app.Config;
import co.com.acktos.ubimoviltaxista.models.Driver;
import co.com.acktos.ubimoviltaxista.models.ServerResponse;

/**
 * Created by Acktos on 2/23/16.
 */
public class DriversController {

    private Context context;
    private InternalStorage internalStorage;
    Gson gson;

    //Constants
    public final static String TAG_DRIVER_LOGIN="driver_login";
    public final static String TAG_ASSIGN_SERVICE="assign_service";
    public final static String TAG_ADD_CREDIT="add_credit";

    public DriversController(Context context) {
        this.context = context;
        internalStorage = new InternalStorage(this.context);
        gson=new Gson();
        Firebase.setAndroidContext(context);

    }

    /**
     * Get driver authentication from server.
     * @param email
     * @param pswrd
     * @param responseListener
     * @param errorListener
     */
    public void driverLogin(
            final String email,
            final String pswrd,
            final Response.Listener<String> responseListener,
            final Response.ErrorListener errorListener) {


        StringRequest jsonObjReq = new StringRequest(

                Request.Method.POST,
                Config.API.DRIVER_LOGIN.getUrl(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d(Config.DEBUG_TAG, response.toString());

                        ServerResponse serverResponse = new ServerResponse(response);

                        if (serverResponse.getCode().equals(Config.SUCCESS_CODE)) {

                            try {


                                JSONObject jsonResponse=new JSONObject(response);
                                Driver driver=gson.fromJson(
                                        jsonResponse.getJSONObject(Config.KEY_FIELDS).toString(),
                                        Driver.class);

                                Log.i(Config.DEBUG_TAG,"driver:"+driver);

                                saveDriverProfile(driver);
                                responseListener.onResponse(Config.SUCCESS_CODE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i(Config.DEBUG_TAG, "jsonException:"+e.getMessage());
                            }

                        } else {
                            responseListener.onResponse(Config.FAILED_CODE);
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d(Config.DEBUG_TAG, "volley error:" + error.getMessage());
                        errorListener.onErrorResponse(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_EMAIL, email);
                params.put(Config.KEY_PASSWORD, pswrd);
                params.put(Config.KEY_ENCRYPT, Encrypt.md5(email + pswrd + Config.TOKEN));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, TAG_DRIVER_LOGIN);
    }


    /**
     * save profile into internal storage
     */
    private void saveDriverProfile(Driver driver) {

        Log.i(Config.DEBUG_TAG,"driver:"+gson.toJson(driver));
        internalStorage.saveFile(Config.FILE_DRIVER_PROFILE, gson.toJson(driver));

    }

    /**
     * Get user profile from internal storage
     *
     * @return User
     */
    public Driver getDriver() {

        Driver driver = null;

        if (internalStorage.isFileExists(Config.FILE_DRIVER_PROFILE)) {


            String profileString = internalStorage.readFile(Config.FILE_DRIVER_PROFILE);

            Log.i(Config.DEBUG_TAG, "profile: " + profileString);
            driver = gson.fromJson(profileString,Driver.class);

        }
        return driver;
    }


    /**
     * Send request to backend to accept the incoming service.
     * @param serviceId
     * @param responseListener
     * @param errorListener
     */
    public void assignService(
            final String serviceId,
            final Response.Listener<String> responseListener,
            final Response.ErrorListener errorListener) {


        final Driver driver=getDriver();

        if(driver!=null){
            StringRequest jsonObjReq = new StringRequest(

                    Request.Method.POST,
                    Config.API.ASSIGN_SERVICE.getUrl(),
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            Log.d(Config.DEBUG_TAG, response.toString());

                            ServerResponse serverResponse = new ServerResponse(response);

                            Log.i(Config.DEBUG_TAG,"assign service response:"+response);

                            if (serverResponse.getCode().equals(Config.SUCCESS_CODE)) {

                                responseListener.onResponse(Config.SUCCESS_CODE);


                            }else if(serverResponse.getCode().equals(Config.SERVICE_ALREADY_TAKEN)){

                                responseListener.onResponse(Config.SERVICE_ALREADY_TAKEN);

                            } else {
                                responseListener.onResponse(Config.FAILED_CODE);
                            }

                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d(Config.DEBUG_TAG, "volley error assign service:" + error.getMessage());
                            errorListener.onErrorResponse(error);
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    //Log.i(Config.DEBUG_TAG, "id:" + driver.getId());
                    //Log.i(Config.DEBUG_TAG, "car:" + carId);
                    //Log.i(Config.DEBUG_TAG, "service:" + "1");

                    Map<String, String> params = new HashMap<>();
                    params.put(Config.KEY_ID, driver.getId());
                    params.put(Config.KEY_SERVICE, serviceId);
                    params.put(Config.KEY_ENCRYPT, Encrypt.md5(driver.getId()+ serviceId + Config.TOKEN));

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, TAG_ASSIGN_SERVICE);

        }else{
            Log.i(Config.DEBUG_TAG,"Driver is null in assign driver-DriversController");
        }

    }

    public void addCredit(final String code,
                          final Response.Listener<String> responseListener,
                          final Response.ErrorListener errorListener){

        final Driver driver=getDriver();


        StringRequest jsonObjReq = new StringRequest(

                Request.Method.POST,
                Config.API.DRIVER_LOGIN.getUrl(),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d(Config.DEBUG_TAG,"sendAlarm response" + response.toString());

                        ServerResponse serverResponse = new ServerResponse(response);

                        if (serverResponse.getCode().equals(Config.SUCCESS_CODE)) {


                            responseListener.onResponse(Config.SUCCESS_CODE);


                        } else {
                            responseListener.onResponse(Config.FAILED_CODE);
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d(Config.DEBUG_TAG, "volley error add credit:" + error.getMessage());
                        errorListener.onErrorResponse(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Config.KEY_DRIVER_ID, driver.getId());
                params.put(Config.KEY_CODE, code);
                params.put(Config.KEY_ENCRYPT, Encrypt.md5(driver.getId()+code + Config.TOKEN));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, TAG_ADD_CREDIT);


    }

    public void sendAlarm(){

        final Driver driver=getDriver();
        Firebase driversRef=new Firebase("https://ubimovil.firebaseio.com/"+Config.TABLE_ALARMS);
        driversRef.child(Config.KEY_DRIVER+driver.getId())
                .child(Config.KEY_PANIC)
                .setValue("1", new Firebase.CompletionListener() {

                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {

                        if (firebaseError != null) {
                            Toast.makeText(
                                    context,context.getString(R.string.msg_error_occurred),Toast.LENGTH_LONG).show();
                        } else {
                            Log.i(Config.DEBUG_TAG, "onComplete firebase panic: Panic message don't could sent");

                            Toast.makeText(
                                    context, context.getString(R.string.msg_alarm_acive), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void registerGCMIdOnBackend(final String registerId,
                                       final Response.Listener<String> responseListener,
                                       final Response.ErrorListener errorListener) {


        final Driver driver=getDriver();


        if(driver!=null){
            StringRequest jsonObjReq = new StringRequest(

                    Request.Method.POST,
                    Config.API.REGISTER_GCM_ID.getUrl(),
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            Log.d(Config.DEBUG_TAG, response.toString());

                            ServerResponse serverResponse = new ServerResponse(response);

                            Log.i(Config.DEBUG_TAG,"register gcm id:"+response);

                            if (serverResponse.getCode().equals(Config.SUCCESS_CODE)) {

                                responseListener.onResponse(Config.SUCCESS_CODE);


                            } else {
                                responseListener.onResponse(Config.FAILED_CODE);
                            }

                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d(Config.DEBUG_TAG, "volley error register gcm id:" + error.getMessage());
                            errorListener.onErrorResponse(error);
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {

                    //Log.i(Config.DEBUG_TAG, "id:" + driver.getId());
                    //Log.i(Config.DEBUG_TAG, "car:" + carId);
                    //Log.i(Config.DEBUG_TAG, "service:" + "1");

                    Map<String, String> params = new HashMap<>();
                    params.put(Config.KEY_DRIVER_ID, driver.getId());
                    params.put(Config.KEY_GCM_ID, registerId);
                    params.put(Config.KEY_ENCRYPT, Encrypt.md5(driver.getId()+ registerId + Config.TOKEN));

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, TAG_ASSIGN_SERVICE);

        }else{
            Log.i(Config.DEBUG_TAG,"Driver is null in assign driver-DriversController");
        }

    }


}
