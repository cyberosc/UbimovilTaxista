package co.com.acktos.ubimoviltaxista.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import co.com.acktos.ubimoviltaxista.app.Config;

public class BootCompletedReceiver extends BroadcastReceiver {

    AlarmReceiver alarmReceiver;

    public BootCompletedReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(Config.DEBUG_TAG, "boot completed");
        //Toast.makeText(context,"boot completed ubimovil",Toast.LENGTH_LONG).show();

        alarmReceiver=new AlarmReceiver(context);

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            Log.i(Config.DEBUG_TAG, "boot completed 2");
            alarmReceiver.setAlarm();
        }
    }
}
