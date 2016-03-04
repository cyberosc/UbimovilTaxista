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

/**
 * Created by Acktos on 2/24/16.
 */
public class CarsController {

    private Context context;
    private DriversController driversController;
    private Driver driver;
    private Gson gson;

    //Volley Request Tags
    private final static String TAG_GET_DRIVERS="get_drivers";
    private final static String TAG_SET_CAR="set_car";

    public CarsController(Context context){

        this.context=context;
        driversController=new DriversController(context);
        driver=driversController.getDriver();
        gson=new Gson();
    }

    public void getCars(
            final Response.Listener<List<Car>> responseListener,
            final Response.ErrorListener errorListener){


        final String driverId;
        driverId=driver.getId();

        Log.i(Config.DEBUG_TAG, "driver id:" + driverId);

        StringRequest stringReq = new StringRequest(

                Request.Method.POST,
                Config.API.GET_CARS.getUrl(),
                new Response.Listener<String>() {

                    List<Car> cars=null;

                    @Override
                    public void onResponse(String response) {

                        Log.d(Config.DEBUG_TAG, "get cars:"+response);

                        ServerResponse serverResponse=new ServerResponse(response);

                        if(serverResponse.getCode().equals(Config.SUCCESS_CODE)){

                            try {

                                cars=new ArrayList<>();

                                JSONObject carsObject=new JSONObject(response);

                                Type listType = new TypeToken<List<Car>>(){}.getType();
                                cars=gson.fromJson(
                                        carsObject.getJSONArray(Config.KEY_FIELDS).toString(),
                                        listType);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        responseListener.onResponse(cars);

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d(Config.DEBUG_TAG, "volley error:"+error.getMessage());
                        errorListener.onErrorResponse(error);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(Config.KEY_ID, driverId);
                params.put(Config.KEY_ENCRYPT, Encrypt.md5(driverId + Config.TOKEN));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringReq, TAG_GET_DRIVERS);

    }


    public void setCar(final String carId,
                       final Response.Listener<String> responseListener,
                       final Response.ErrorListener errorListener){

        final String driverId;
        driverId=driver.getId();

        Log.i(Config.DEBUG_TAG, "driver id:" + driverId);

        StringRequest stringReq = new StringRequest(

                Request.Method.POST,
                Config.API.SET_CAR.getUrl(),
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        Log.d(Config.DEBUG_TAG, "set cars:"+response);

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

                        Log.d(Config.DEBUG_TAG, "volley error set car:"+error.getMessage());
                        errorListener.onErrorResponse(error);
                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(Config.KEY_ID, driverId);
                params.put(Config.KEY_CAR, carId);
                params.put(Config.KEY_ENCRYPT, Encrypt.md5(driverId + carId+ Config.TOKEN));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringReq, TAG_SET_CAR);
    }
}
