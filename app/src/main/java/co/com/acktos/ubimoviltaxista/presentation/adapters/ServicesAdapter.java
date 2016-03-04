package co.com.acktos.ubimoviltaxista.presentation.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.com.acktos.ubimoviltaxista.R;
import co.com.acktos.ubimoviltaxista.app.Config;
import co.com.acktos.ubimoviltaxista.models.Car;
import co.com.acktos.ubimoviltaxista.models.Service;

/**
 * Created by Acktos on 2/24/16.
 */
public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    private List<Service> services;
    private static OnRecyclerViewClickListener onRecyclerViewClickListener;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView serviceId;
        public TextView time;
        public TextView pickup;
        public TextView client;
        public TextView state;



        public ViewHolder(View itemView) {

            super(itemView);
            serviceId=(TextView)itemView.findViewById(R.id.lbl_service_id);
            time=(TextView)itemView.findViewById(R.id.lbl_time);
            pickup=(TextView)itemView.findViewById(R.id.lbl_pickup);
            client=(TextView)itemView.findViewById(R.id.lbl_user);
            state=(TextView)itemView.findViewById(R.id.lbl_service_state);


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            Log.i(Config.DEBUG_TAG, "click on service:" + this.getLayoutPosition());
            onRecyclerViewClickListener.onRecyclerViewClick(view, this.getLayoutPosition());

        }
    }

    public ServicesAdapter(Context context,List<Service> services,OnRecyclerViewClickListener onRecyclerViewClick){

        this.context=context;
        this.services=services;
        this.onRecyclerViewClickListener=onRecyclerViewClick;
    }


    @Override
    public int getItemCount() {
        return services.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.service_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.serviceId.setText("#"+services.get(i).getId());
        viewHolder.time.setText(services.get(i).getTime());
        viewHolder.pickup.setText(services.get(i).getPickup());
        viewHolder.client.setText(services.get(i).getClient());
        viewHolder.state.setText(services.get(i).getState());

        /*Picasso.with(context)
                .load(cars.get(i).getTypeImg())
                .placeholder(R.drawable.taxi_vip)
                .into(viewHolder.carTypeImg);*/
    }

    public interface OnRecyclerViewClickListener
    {

        void onRecyclerViewClick(View v, int position);
    }


}
