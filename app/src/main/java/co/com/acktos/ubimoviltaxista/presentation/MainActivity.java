package co.com.acktos.ubimoviltaxista.presentation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

import co.com.acktos.ubimoviltaxista.R;
import co.com.acktos.ubimoviltaxista.android.DividerItemDecoration;
import co.com.acktos.ubimoviltaxista.app.Config;
import co.com.acktos.ubimoviltaxista.app.MaterialSimpleListAdapter;
import co.com.acktos.ubimoviltaxista.app.MaterialSimpleListItem;
import co.com.acktos.ubimoviltaxista.controllers.ServicesController;
import co.com.acktos.ubimoviltaxista.gcm.RegistrationIntentService;
import co.com.acktos.ubimoviltaxista.models.Service;
import co.com.acktos.ubimoviltaxista.presentation.adapters.CarsAdapter;
import co.com.acktos.ubimoviltaxista.presentation.adapters.ServicesAdapter;
import co.com.acktos.ubimoviltaxista.receivers.AlarmReceiver;

public class MainActivity extends AppCompatActivity implements ServicesAdapter.OnRecyclerViewClickListener {

    //Constants
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    //Android Utils
    private BroadcastReceiver registrationBroadcastReceiver;


    //Attributes
    private boolean isGcmRegisterInProgress=false;
    private List<Service> mServices;

    //Components
    ServicesController servicesController;



    //UI References
    private RecyclerView servicesRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Components
        servicesController=new ServicesController(this);


        //Initialize UI
        servicesRecyclerView = (RecyclerView) findViewById(R.id.recycler_services);
        servicesRecyclerView.setHasFixedSize(true);
        //servicesRecyclerView.addItemDecoration(new DividerItemDecoration(this, null));
        //servicesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(this);
        servicesRecyclerView.setLayoutManager(layoutManager);

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            isGcmRegisterInProgress=true;
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        AlarmReceiver alarmReceiver=new AlarmReceiver(this);
        alarmReceiver.setAlarm();





    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();


        getServices();

        setupBroadCastReceiver();


        if (checkPlayServices()) {

            if(!isGcmRegisterInProgress){
                // Start IntentService to register this application with GCM.
                isGcmRegisterInProgress=true;
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }



    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void getServices(){

        servicesController.getServices(new Response.Listener<List<Service>>() {
            @Override
            public void onResponse(List<Service> services) {

                if(services!=null){
                    mServices = services;
                    Log.i(Config.DEBUG_TAG, "on response get cars:" + services.size());
                    recyclerAdapter = new ServicesAdapter(MainActivity.this, services, MainActivity.this);
                    servicesRecyclerView.setAdapter(recyclerAdapter);
                }else{
                    Log.i(Config.DEBUG_TAG,"(getServices-MainActivity) services is null");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    private void setupBroadCastReceiver(){

        registrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                /*mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    mInformationTextView.setText(getString(R.string.token_error_message));
                }*/

                Log.i(Config.DEBUG_TAG,"OnReceiveBroadcast");
            }
        };
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(Config.DEBUG_TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRecyclerViewClick(View v, int position) {

        if(mServices!=null){

            showStatesList(mServices.get(position));

        }else{
            Log.i(Config.DEBUG_TAG,"(OnRecyclerViewClick-MainActivity) services is null");
        }

    }

    private void showStatesList(final Service service){

        int resourceArrayStates=0;
        int index=0;

        if(service.getState().equals(Config.STATE_ACCEPTED)){
            index=3;
            resourceArrayStates=R.array.states_accepted;
        }
        if(service.getState().equals(Config.STATE_ON_THE_WAY)){
            index=4;
            resourceArrayStates=R.array.states_on_the_way;
        }
        if(service.getState().equals(Config.STATE_ARRIVED)){
            index=5;
            resourceArrayStates=R.array.states_arrived;
        }
        if(service.getState().equals(Config.STATE_ON_BOARD) || service.getState().equals(Config.STATE_COMPLETED)){
            index=6;
            resourceArrayStates=R.array.states_on_board;
        }

        final int addIndexItem=index;
        new MaterialDialog.Builder(this)
                .title(R.string.update_state)
                .items(resourceArrayStates)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        Log.i(Config.DEBUG_TAG,"(ShowStatesList) selected item:"+which);

                        updateStateOnBackend(service.getId(),which+addIndexItem);
                        return true;
                    }
                })
                .positiveText(R.string.accept)
                .show();
    }

    private void updateStateOnBackend(String serviceId,int state){

        final Snackbar mSnackBar= Snackbar
                .make(
                        servicesRecyclerView,
                        R.string.msg_update_service_state_failed,
                        Snackbar.LENGTH_LONG);


        mSnackBar.setAction(R.string.accept, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnackBar.dismiss();

            }
        });

        mSnackBar.setAction(R.string.accept, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnackBar.dismiss();

            }
        });

        servicesController.updateServiceState(
                serviceId,
                String.valueOf(state),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {

                        if (result.equals(Config.SUCCESS_CODE)) {

                            getServices();
                            mSnackBar.setText(R.string.msg_update_service_state_success);
                            mSnackBar.show();

                        } else {

                            mSnackBar.setText(R.string.msg_update_service_state_failed);
                            mSnackBar.show();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        mSnackBar.setText(R.string.msg_update_service_state_failed);
                        mSnackBar.show();
                    }
                });
    }





}
