package co.com.acktos.ubimoviltaxista.presentation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import co.com.acktos.ubimoviltaxista.R;
import co.com.acktos.ubimoviltaxista.android.DividerItemDecoration;
import co.com.acktos.ubimoviltaxista.app.Config;
import co.com.acktos.ubimoviltaxista.controllers.CarsController;
import co.com.acktos.ubimoviltaxista.models.Car;
import co.com.acktos.ubimoviltaxista.presentation.adapters.CarsAdapter;

public class CarsActivity extends AppCompatActivity implements CarsAdapter.OnRecyclerViewClickListener{


    //Attributes
    List<Car> cars;

    //Components
    CarsController carsControllers;

    //UI References
    private RecyclerView carsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);


        //Initialize UI
        carsRecyclerView = (RecyclerView) findViewById(R.id.recycler_cars);
        carsRecyclerView.setHasFixedSize(true);
        carsRecyclerView.addItemDecoration(new DividerItemDecoration(this, null));
        carsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(this);
        carsRecyclerView.setLayoutManager(layoutManager);



        //Initialize components
        carsControllers=new CarsController(this);

        carsControllers.getCars(new Response.Listener<List<Car>>() {
            @Override
            public void onResponse(List<Car> carsList) {

                Log.i(Config.DEBUG_TAG, "on response get cars:"+carsList.size());
                recyclerAdapter = new CarsAdapter(CarsActivity.this,carsList,CarsActivity.this);
                carsRecyclerView.setAdapter(recyclerAdapter);
                cars=carsList;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });


    }


    private void launchServicesActivity(){

        Intent i=new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public void onRecyclerViewClick(View v, final int position) {

        final Car car=cars.get(position);

        new MaterialDialog.Builder(this)
                .content(R.string.msg_confirm_set_car)
                .positiveText(R.string.accept)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {

                        carsControllers.setCar(car.getId(), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {

                                launchServicesActivity();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {


                            }
                        });

                    }
                })
                .show();
    }
}
