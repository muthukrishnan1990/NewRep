package bss.bbs.com.teslaclone.Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bss.bbs.com.teslaclone.Activity.CarDeatils;
import bss.bbs.com.teslaclone.R;
import bss.bbs.com.teslaclone.Singleton.AppController;
import bss.bbs.com.teslaclone.Singleton.Constants;
import bss.bbs.com.teslaclone.network.VolleyCustomPostRequest;

/**
 * Created by Anbu on 08-Dec-17.
 */

public class CarFavouritesAdapter extends RecyclerView.Adapter<CarFavouritesAdapter.ViewHolder> {
    ArrayList<String> VIN = new ArrayList<>();
    ArrayList<String> DealerID = new ArrayList<>();
    ArrayList<String> Banner_Image = new ArrayList<>();
    ArrayList<String> Make = new ArrayList<>();
    ArrayList<String> Model = new ArrayList<>();
    ArrayList<String> Price = new ArrayList<>();
    ArrayList<String> Mileage = new ArrayList<>();
    ArrayList<String> Year = new ArrayList<>();
    ArrayList<String> Exterior = new ArrayList<>();
    ArrayList<String> Interior = new ArrayList<>();
    ArrayList<String> Condition = new ArrayList<>();
    ArrayList<String> Location = new ArrayList<>();
    private Context context;
    Button edit;
    RelativeLayout viewForeground;
    public CarFavouritesAdapter(Context context, ArrayList<String> VIN_UID2,ArrayList<String> Dealerid, ArrayList<String> Make2,
                                ArrayList<String> Model2,ArrayList<String> Mileage2,ArrayList<String> Banner_Image2,ArrayList<String> Price2,ArrayList<String> Year2,ArrayList<String> exterior,ArrayList<String> interior,ArrayList<String> condition,ArrayList<String> location) {
        this.VIN = VIN_UID2;
        this.DealerID = Dealerid;
        this.Banner_Image = Banner_Image2;
        this.Make = Make2;
        this.Model = Model2;
        this.Price = Price2;
        this.Mileage = Mileage2;
        this.Year = Year2;
        this.Exterior = exterior;
        this.Interior = interior;
        this.Condition = condition;
        this.Location = location;
        this.context = context;
        FragmentManager fm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cars_favorites, viewGroup, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(CarFavouritesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.sectionMakeModel.setText(Year.get(0) + " " + Make.get(0) + " " + Model.get(0) + " ");
//        viewHolder.cardealername.setText(Dealer.get(0));

        if(!Mileage.get(i).equalsIgnoreCase("0")){
//            viewHolder.tv_mile.setText(Mileage.get(i));
            double num =Double.parseDouble(Mileage.get(i));
            NumberFormat defaultFormat = NumberFormat.getInstance();
            viewHolder.tv_mile.setText(defaultFormat.format(num)+" miles");
        }else {
            viewHolder.tv_mile.setText("Call for Mileage");
        }
        if((Banner_Image.get(i)).equals("") || (Banner_Image.get(i)).equals(null)){
            Picasso.with(context).load(R.drawable.logo).fit().into(viewHolder.img_banner);
        }else {
            Picasso.with(context).load(Banner_Image.get(i)).placeholder(R.drawable.logo).fit().into(viewHolder.img_banner);
        }
        (viewHolder).setItemClickListener
                (new ItemClickListener() {

                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if (isLongClick) {
                        } else {
                            Intent intent = new Intent(context, CarDeatils.class);
                            intent.putExtra("Make", Make.get(position));
                            intent.putExtra("Model", Model.get(position));
                            intent.putExtra("Year", Year.get(position));
                            intent.putExtra("VIN", VIN.get(position));
                            intent.putExtra("Dealer_ID", DealerID.get(position));
                            intent.putExtra("Banne_Image", Banner_Image.get(position));
                            intent.putExtra("Price", Price.get(position));
                             intent.putExtra("Mileage", Mileage.get(position));
                            intent.putExtra("Condition", Condition.get(position));
                            intent.putExtra("Location", Location.get(position));
                            intent.putExtra("Exterior", Exterior.get(position));
                            intent.putExtra("Interior", Interior.get(position));
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
//                            Toast.makeText(context, " " + position + " - " + Model.get(position), Toast.LENGTH_SHORT).show();
                           // getCarDetailsPost(VIN.get(position),Banner_Image.get(position));
                        }
                    }
                });

       if (!Price.get(i).equalsIgnoreCase("0.00")) {

            double num2 =Double.parseDouble(Price.get(i));
            NumberFormat defaultFormat = NumberFormat.getInstance();
            viewHolder.tv_price.setText(Html.fromHtml("Price:"+"<sup>$</sup>")+defaultFormat.format(num2));

        }else{

            viewHolder.tv_price.setText("Call For Price");

        }
        viewHolder.tv_location.setText("Location:"+Location.get(i));
        viewHolder.tv_stocktype.setText("StockType:"+Condition.get(i));

    }


    public int getItemCount() {
        return VIN.size();
    }
    public void removeItem(int position) {
        //VIN.remove(position);
        DBAdapter db = new DBAdapter(context);
        db.deleteQuery("DELETE FROM myfavorites WHERE VIN='" + VIN.remove(position) + "'");
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, VIN.size());
        /*DBAdapter db = new DBAdapter(context);
        db.deleteQuery("DELETE FROM myfavorites WHERE VIN='" + VIN.remove(position) + "'");*/
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView tv_year, tv_make, tv_stocktype,tv_model,tv_location, tv_price,sectionMakeModel,cardealername,Dealer, tv_mile;
        private ImageView img_banner;
        private ItemClickListener clickListener;

        public ViewHolder(View view) {
            super(view);
            viewForeground=(RelativeLayout)view.findViewById(R.id.view_foreground);
            sectionMakeModel = (TextView) view.findViewById(R.id.section_make_model);
            tv_price = (TextView) view.findViewById(R.id.price);
            tv_mile = (TextView) view.findViewById(R.id.mile);
            tv_location=(TextView) view.findViewById(R.id.Location);
            tv_stocktype=(TextView) view.findViewById(R.id.stocktype);
            img_banner = (ImageView) view.findViewById(R.id.image);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {

            clickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }
}
