package co.com.acktos.ubimoviltaxista.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import co.com.acktos.ubimoviltaxista.app.Config;
import co.com.acktos.ubimoviltaxista.controllers.DriversController;


public class AcceptRequestService extends IntentService {


    //Components
    DriversController driversController;


    public AcceptRequestService() {

        super("AcceptRequestService");
        driversController=new DriversController(this);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            Bundle args=intent.getExtras();

            if(args!=null){

                if(args.containsKey(Config.KEY_SERVICE)){

                    String serviceId=args.getString(Config.KEY_SERVICE);

                    driversController.assignService(serviceId, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String resultCode) {

                           sendBroadcastServerResponse(resultCode);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                            sendBroadcastServerResponse(Config.ERROR_CODE);

                        }
                    });

                } else Log.i(Config.DEBUG_TAG, "service Id is null in acceptRequestService");

            }else Log.i(Config.DEBUG_TAG, "args is null in acceptRequestService");



        }
    }



    private void  sendBroadcastServerResponse(String code){

        Intent responseIntent = new Intent();
        responseIntent.setAction(Config.BROADCAST_ACTION);
        responseIntent.putExtra(Config.KEY_RESPONSE, code);
        LocalBroadcastManager.getInstance(this).sendBroadcast(responseIntent);

        Log.i(Config.DEBUG_TAG,"send broadcast  response:"+code);

    }


}
