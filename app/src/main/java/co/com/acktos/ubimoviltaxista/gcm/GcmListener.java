package co.com.acktos.ubimoviltaxista.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import co.com.acktos.ubimoviltaxista.presentation.MainActivity;
import co.com.acktos.ubimoviltaxista.app.Config;
import co.com.acktos.ubimoviltaxista.presentation.WakeUpRequestActivity;
import co.com.acktos.ubimoviltaxista.services.UpdateCurrentPositionService;

/**
 * Created by Acktos on 2/10/16.
 */
public class GcmListener extends GcmListenerService {


    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.d(Config.DEBUG_TAG, "From: " + from);
        Log.d(Config.DEBUG_TAG, "data: " + data.toString());


        if(data.containsKey(Config.COLLAPSE_KEY) && data.containsKey(Config.KEY_SERVICE)){

            try {
                String collapseKey = data.getString(Config.COLLAPSE_KEY);
                String serviceId = data.getString(Config.KEY_SERVICE);


                // Handle driver_select message
                if (collapseKey.equals(Config.COLLAPSE_KEY_DRIVER_SELECT)) {

                    if(data.containsKey(Config.KEY_DISTANCE)){

                        String distance=data.getString(Config.KEY_DISTANCE);
                        launchWakeUpRequestActivity(serviceId,distance);


                    }else{
                        Log.e(Config.DEBUG_TAG, "This message don't have distance key");
                    }

                }

                // Handle distance_request message

                if(collapseKey.equals(Config.COLLAPSE_KEY_DISTANCE_REQUEST)){

                    updateFirebaseLocation(serviceId);

                }

                //Remove WakeUp Screen
                if(collapseKey.equals(Config.COLLAPSE_KEY_FINISH)){

                    sendBroadcastServerResponse(Config.FINISH_WAKE_UP_CODE);
                }


            }catch (NullPointerException e){
                Log.e(Config.DEBUG_TAG, "Exception in GCMListener");
            }

        }else{
            Log.d(Config.DEBUG_TAG, "this message don't have collapse key or serviceId");
        }


        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        sendNotification(from);
    }


    private void launchWakeUpRequestActivity(String serviceId, String distance){

        Intent wakeUpIntent=new Intent(this, WakeUpRequestActivity.class);
        wakeUpIntent.putExtra(Config.KEY_ID, serviceId);
        wakeUpIntent.putExtra(Config.KEY_DISTANCE, distance);
        wakeUpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(wakeUpIntent);
    }

    private void updateFirebaseLocation(String serviceId){

        Log.i(Config.DEBUG_TAG, "Saving request distance on Firebase...");
        Intent updateIntent=new Intent(this, UpdateCurrentPositionService.class);
        updateIntent.putExtra(Config.KEY_SERVICE, serviceId);
        startService(updateIntent);

    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void  sendBroadcastServerResponse(String code){

        Intent responseIntent = new Intent();
        responseIntent.setAction(Config.BROADCAST_ACTION_FINISH);
        responseIntent.putExtra(Config.KEY_RESPONSE, code);
        LocalBroadcastManager.getInstance(this).sendBroadcast(responseIntent);

        Log.i(Config.DEBUG_TAG, "send broadcast finish wakeup:" + code);

    }
}
