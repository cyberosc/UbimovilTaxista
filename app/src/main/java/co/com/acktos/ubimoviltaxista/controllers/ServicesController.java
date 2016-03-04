package co.com.acktos.ubimoviltaxista.controllers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.com.acktos.ubimoviltaxista.android.Encrypt;
import co.com.acktos.ubimoviltaxista.app.AppController;
import co.com.acktos.ubimoviltaxista.app.Config;
import co.com.acktos.ubimoviltaxista.models.Car;
import co.com.acktos.ubimoviltaxista.models.Driver;
import co.com.acktos.ubimoviltaxista.models.ServerResponse;
import co.com.acktos.ubimoviltaxista.models.Service;

/**
 * Created by Acktos on 2/24/16.
 */
public class ServicesController {

    private Context context;
    private DriversController driversController;
    private Driver driver;
    private Gson gson;

    //Volley Request Tags
    private final static String TAG_GET_SERVICES="get_services";
    private final static String TAG_UPDATE_SERVICE_STATE="update_service_state";

    public ServicesController(Context context){

        this.context=context;
        driversController=new DriversController(context);
        driver=driversController.getDriver();
        gson=new Gson();
    }

    public void getServices(
            final Response.Listener<List<Service>> responseListener,
            final Response.ErrorListener errorListener){


        final String driverId;
        driverId=driver.getId();

        Log.i(Config.DEBUG_TAG, "(getServices) driver id:" + driverId);

        StringRequest stringReq = new StringRequest(

                Request.Method.POST,
                Config.API.GET_SERVICES.getUrl(),
                new Response.Listener<String>() {

                    List<Service> services=null;

                    @Override
                    public void onResponse(String response) {

                        Log.d(Config.DEBUG_TAG, " get services:"+response);

                        ServerResponse serverResponse=new ServerResponse(response);

                        if(serverResponse.getCode().equals(Config.SUCCESS_CODE)){

                            try {

                                services=new ArrayList<>();

                                JSONObject servicesObject=new JSONObject(response);

                                Type listType = new TypeToken<List<Service>>(){}.getType();
                                services=gson.fromJson(
                                        servicesObject.getJSONArray(Config.KEY_FIELDS).toString(),
                                        listType);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        responseListener.onResponse(services);

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d(Config.DEBUG_TAG, "(getServices) volley error:"+error.getMessage());
                        errorListener.onErrorResponse(error);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(Config.KEY_ID, "");
                params.put(Config.KEY_DRIVER, driverId);
                params.put(Config.KEY_ENCRYPT, Encrypt.md5(driverId + Config.TOKEN));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringReq, TAG_GET_SERVICES);

    }


    public void updateServiceState(
                        final String serviceId,
                        final String state,
                        final Response.Listener<String> responseListener,
                        final Response.ErrorListener errorListener){


        StringRequest stringReq = new StringRequest(

                Request.Method.POST,
                Config.API.UPDATE_SERVICE_STATE.getUrl(),
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        Log.d(Config.DEBUG_TAG, "update service state:"+response);

                        ServerResponse serverResponse=new ServerResponse(response);

                        if(serverResponse.getCode().equals(Config.SUCCESS_CODE)){

                            responseListener.onResponse(Config.SUCCESS_CODE);

                        }else{
                            responseListener.onResponse(Config.FAILED_CODE);
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d(Config.DEBUG_TAG, "volley error update service state:"+error.getMessage());
                        errorListener.onErrorResponse(error);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                Log.i(Config.DEBUG_TAG,"(updateServiceState) serviceId:"+serviceId);
                Log.i(Config.DEBUG_TAG,"(updateServiceState) state:"+state);

                params.put(Config.KEY_ID, serviceId);
                params.put(Config.KEY_STATE, state);
                params.put(Config.KEY_ENCRYPT, Encrypt.md5(serviceId + state+ Config.TOKEN));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringReq, TAG_UPDATE_SERVICE_STATE);
    }
}
