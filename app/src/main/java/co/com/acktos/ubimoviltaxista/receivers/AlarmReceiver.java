package co.com.acktos.ubimoviltaxista.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.util.Log;

import co.com.acktos.ubimoviltaxista.services.UpdateCurrentPositionService;
import co.com.acktos.ubimoviltaxista.app.Config;

/**
 * This receiver is notified for Android system to handle periodic alarms.
 */
public class AlarmReceiver extends BroadcastReceiver {


    // Android Utils
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    //Attributes
    private Context context;

    public AlarmReceiver(){}

    public AlarmReceiver(Context context) {
        this.context=context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(Config.DEBUG_TAG, "Alarm broadcast is notified");
        Intent service = new Intent(context, UpdateCurrentPositionService.class);
        context.startService(service);

    }


    public void setAlarm(){

        alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i=new Intent(context,AlarmReceiver.class);
        alarmIntent= PendingIntent.getBroadcast(context, Config.REQUEST_ALARM_PENDING, i, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                Config.MINIMUM_ALARM_INTERVAL, Config.MINIMUM_ALARM_INTERVAL, alarmIntent);

        activeBootCompletedReceiver(true);

    }

    public void cancelAlarm(){

        if (alarmManager!= null) {
            alarmManager.cancel(alarmIntent);
        }

        activeBootCompletedReceiver(false);
    }

    /**
     * This method actives/des-actives bootCompleteReceiver, because this is disable in the manifest by default
     */
    private void activeBootCompletedReceiver(boolean turn){

        int state;
        if(turn)state=PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
        else state=PackageManager.COMPONENT_ENABLED_STATE_DISABLED;

        ComponentName receiver = new ComponentName(context, BootCompletedReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver, state, PackageManager.DONT_KILL_APP);

    }
}
