package co.com.acktos.ubimoviltaxista.presentation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.PowerManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import co.com.acktos.ubimoviltaxista.R;
import co.com.acktos.ubimoviltaxista.app.Config;
import co.com.acktos.ubimoviltaxista.services.AcceptRequestService;

public class WakeUpRequestActivity extends AppCompatActivity {


    //UI References

    private Button btnAcceptService;
    private CoordinatorLayout coordinatorLayout;
    private TextView lblDistance;

    //CONSTANTS
    private static final int WAKELOCK_TIMEOUT = 30 * 1000;


    //ANDROID UTILS
    private PowerManager.WakeLock mWakeLock=null;


    //ATTRIBUTES
    String serviceId;
    String distance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupWakeLock();
        setContentView(R.layout.activity_wake_up_request);

        //Initialize UI
        btnAcceptService=(Button)findViewById(R.id.btn_accept_service);
        coordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinator_wake_up);
        lblDistance=(TextView) findViewById(R.id.lbl_distance);

        //Get intent data

        Bundle args=getIntent().getExtras();

        if(args!=null){

            serviceId=args.getString(Config.KEY_ID);
            distance=args.getString(Config.KEY_DISTANCE);

            Log.i(Config.DEBUG_TAG,"service id:"+serviceId);
            Log.i(Config.DEBUG_TAG,"distance:"+distance);

            lblDistance.setText(distance+" Meters");

        }else{
            Log.i(Config.DEBUG_TAG,"args is null en WakeUpRequestActivity");
        }


        btnAcceptService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                disableBtn(true);

                Intent acceptServiceIntent=new Intent(WakeUpRequestActivity.this, AcceptRequestService.class);
                acceptServiceIntent.putExtra(Config.KEY_SERVICE,serviceId);
                startService(acceptServiceIntent);


            }
        });
    }


    private void disableBtn(boolean disable){

        if(disable){

            btnAcceptService.setEnabled(false);
            btnAcceptService.setText(R.string.loading);

        }else{
            btnAcceptService.setEnabled(true);
            btnAcceptService.setText(R.string.accept);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(Config.DEBUG_TAG, "entry onResume in WakeupRequestActivity");

        // Set the window to keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        // Acquire wakelock
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (mWakeLock == null) {
            mWakeLock = pm.newWakeLock((
                            PowerManager.FULL_WAKE_LOCK |
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                            PowerManager.ACQUIRE_CAUSES_WAKEUP),
                            "wakeLockRequestActivity");
        }

        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
            Log.i(Config.DEBUG_TAG, "Wakelock aquired!!");
        }


        // Register broadCast
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver, new IntentFilter(Config.BROADCAST_ACTION));

        // Register finish broadcast

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageFinish, new IntentFilter(Config.BROADCAST_ACTION_FINISH));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageFinish);

        Log.i(Config.DEBUG_TAG, "unregister broadcast");
    }


    private void setupWakeLock(){


        //Ensure wakelock release
        Runnable releaseWakelock = new Runnable() {

            @Override
            public void run() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

                if (mWakeLock != null && mWakeLock.isHeld()) {
                    mWakeLock.release();
                }
            }
        };

        new Handler().postDelayed(releaseWakelock, WAKELOCK_TIMEOUT);

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract data included in the Intent
            String action = intent.getAction();
            Log.i(Config.DEBUG_TAG, "Action received:" + action);
            disableBtn(false);

            String code=intent.getStringExtra(Config.KEY_RESPONSE);

            Log.i(Config.DEBUG_TAG, "Action received code:" + code);

            if(code.equals(Config.SUCCESS_CODE)){

                Snackbar
                        .make(coordinatorLayout, R.string.msg_assign_service_success, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.accept, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent i=new Intent(WakeUpRequestActivity.this,MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                                startActivity(i);
                            }
                        })
                        .show();

            }else if(code.equals(Config.SERVICE_ALREADY_TAKEN)){
                final Snackbar failedSnackbar= Snackbar
                        .make(coordinatorLayout, R.string.msg_assign_service_already_taken, Snackbar.LENGTH_LONG);

                failedSnackbar.setAction(R.string.accept, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        failedSnackbar.dismiss();

                    }
                });
                failedSnackbar.show();
            }
            else{

                final Snackbar failedSnackbar= Snackbar
                        .make(coordinatorLayout, R.string.msg_assign_service_failed, Snackbar.LENGTH_LONG);

                failedSnackbar.setAction(R.string.accept, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                failedSnackbar.dismiss();

                            }
                        });
                failedSnackbar.show();

            }

        }
    };



    private BroadcastReceiver mMessageFinish = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract data included in the Intent
            String action = intent.getAction();
            Log.i(Config.DEBUG_TAG, "Action received:" + action);
            disableBtn(false);

            String code=intent.getStringExtra(Config.KEY_RESPONSE);

            if(code.equals(Config.FINISH_WAKE_UP_CODE)){

               finish();

            }

        }
    };


}
