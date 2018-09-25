package bss.bbs.com.teslaclone.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bss.bbs.com.teslaclone.Activity.CarDeatils;
import bss.bbs.com.teslaclone.R;

/**
 * Created by sairaj on 25/08/17.
 */

public class UpcomingCarAdapter extends RecyclerView.Adapter<UpcomingCarAdapter.ViewHolder> {
    ArrayList<String> alName;
    ArrayList<Integer> alImage;
    Context context;

    ArrayList<String> VIN = new ArrayList<>();
    ArrayList<String> yearList = new ArrayList<>();
    ArrayList<String> makeList = new ArrayList<>();
    ArrayList<String> modelList = new ArrayList<>();
    ArrayList<String> priceList = new ArrayList<>();
    ArrayList<String> milesList = new ArrayList<>();
    ArrayList<String> stockList = new ArrayList<>();
    ArrayList<String> bannerImageUrl = new ArrayList<>();
    ArrayList<String> Location = new ArrayList<>();
    ArrayList<String> DealerID = new ArrayList<>();
    ArrayList<String> ExteriorColor = new ArrayList<>();
    ArrayList<String> InteriorColor = new ArrayList<>();

    public UpcomingCarAdapter(Context context, ArrayList<String> vin, ArrayList<String> make, ArrayList<String> model,  ArrayList<String> year, ArrayList<String> price,  ArrayList<String> bannerimae, ArrayList<String> location, ArrayList<String> dealerID, ArrayList<String> miles, ArrayList<String> stock, ArrayList<String> exterior,ArrayList<String> interior) {
        super();
        this.context = context;
        this.alName = alName;
        this.alImage = alImage;

        this.VIN = vin;
        this.makeList = make;
        this.modelList = model;
        this.yearList = year;
        this.priceList = price;
        this.bannerImageUrl = bannerimae;
        this.Location = location;
        this.DealerID = dealerID;
        this.milesList = miles;
        this.stockList = stock;
        this.ExteriorColor = exterior;
        this.InteriorColor = interior;
    }

    @Override
    public UpcomingCarAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.upcoing_price, viewGroup, false);
        UpcomingCarAdapter.ViewHolder viewHolder = new UpcomingCarAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UpcomingCarAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tvSpecies.setText(makeList.get(i)+" "+modelList.get(i));
        viewHolder.Upcomingyear.setText("Expect Launch at "+yearList.get(i));
        viewHolder.Upcominglocation.setText(Location.get(i));
        viewHolder.Upcomingcolor.setText(ExteriorColor.get(i));

        if((bannerImageUrl.get(i)).equals("") || (bannerImageUrl.get(i)).equals(null)){
            Picasso.with(context).load(R.drawable.logo).fit().into(viewHolder.imgThumbnail);
        }else {
            Picasso.with(context).load(bannerImageUrl.get(i)).placeholder(R.drawable.logo).fit().into(viewHolder.imgThumbnail);
        }
        //Picasso.with(context).load(bannerImageUrl.get(i)).placeholder(R.drawable.logo).fit().into(viewHolder.imgThumbnail);

        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    //Toast.makeText(context, "#" + position + " - " + alName.get(position) + " (Long click)", Toast.LENGTH_SHORT).show();
                    //context.startActivity(new Intent(context, MainActivity.class));
                } else {
                    //Toast.makeText(context, "#" + position + " - " + VIN.get(position), Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(context, CarDeatils.class);
                    intent.putExtra("Make", makeList.get(position));
                    intent.putExtra("Model", modelList.get(position));
                    intent.putExtra("Year", yearList.get(position));
                    intent.putExtra("VIN",VIN.get(position));
                    intent.putExtra("Dealer_ID",DealerID.get(position));
                    intent.putExtra("Banne_Image",bannerImageUrl.get(position));
                    intent.putExtra("Price",priceList.get(position));
                    intent.putExtra("Mileage",milesList.get(position));
                    intent.putExtra("Condition",stockList.get(position));
                    intent.putExtra("Location",Location.get(position));
                    intent.putExtra("Exterior",ExteriorColor.get(position));
                    intent.putExtra("Interior",InteriorColor.get(position));
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return VIN.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imgThumbnail;
        public TextView tvSpecies,Upcomingyear,Upcominglocation,Upcomingcolor;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tvSpecies = (TextView) itemView.findViewById(R.id.upcoming_species);
            Upcomingyear = (TextView) itemView.findViewById(R.id.upcoming_year);
            Upcominglocation = (TextView) itemView.findViewById(R.id.location);
            Upcomingcolor = (TextView) itemView.findViewById(R.id.colorText);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
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