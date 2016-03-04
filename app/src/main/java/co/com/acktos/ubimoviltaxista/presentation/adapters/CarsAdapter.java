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

/**
 * Created by Acktos on 2/24/16.
 */
public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.ViewHolder> {

    private List<Car> cars;
    private static OnRecyclerViewClickListener onRecyclerViewClickListener;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView carType;
        public TextView carPlate;
        public TextView carBrand;
        public ImageView carTypeImg;



        public ViewHolder(View itemView) {

            super(itemView);
            carType=(TextView)itemView.findViewById(R.id.txt_car_type);
            carPlate=(TextView)itemView.findViewById(R.id.txt_car_plate);
            carBrand=(TextView)itemView.findViewById(R.id.txt_car_brand);
            carTypeImg=(ImageView)itemView.findViewById(R.id.img_car);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            Log.i(Config.DEBUG_TAG, "click on car:" + this.getLayoutPosition());
            onRecyclerViewClickListener.onRecyclerViewClick(view, this.getLayoutPosition());

        }
    }

    public CarsAdapter(Context context,List<Car> cars,OnRecyclerViewClickListener onRecyclerViewClick){

        this.context=context;
        this.cars=cars;
        this.onRecyclerViewClickListener=onRecyclerViewClick;
    }


    @Override
    public int getItemCount() {
        return cars.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.car_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.carType.setText(cars.get(i).getType());
        viewHolder.carPlate.setText(cars.get(i).getPlate());
        viewHolder.carBrand.setText(cars.get(i).getBrand());

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
