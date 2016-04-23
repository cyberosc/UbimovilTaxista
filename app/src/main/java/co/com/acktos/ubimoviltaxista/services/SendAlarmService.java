package co.com.acktos.ubimoviltaxista.services;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import co.com.acktos.ubimoviltaxista.R;
import co.com.acktos.ubimoviltaxista.app.Config;
import co.com.acktos.ubimoviltaxista.controllers.DriversController;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class SendAlarmService extends IntentService {


    DriversController driversController;

    public SendAlarmService() {
        super("SendAlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        driversController=new DriversController(this);

        driversController.sendAlarm();

    }


}
