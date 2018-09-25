package bss.bbs.com.teslaclone.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;

import bss.bbs.com.teslaclone.Activity.CarDeatils;
import bss.bbs.com.teslaclone.R;

/**
 * Created by sairaj on 23/08/17.
 */

public class SellingPriceAdapter extends RecyclerView.Adapter<SellingPriceAdapter.ViewHolder> {

    ArrayList<String> alName;
    ArrayList<Integer> alImage;
    Context context;

    ArrayList<String> VIN = new ArrayList<>();
    ArrayList<String> yearList = new ArrayList<>();
    ArrayList<String> makeList = new ArrayList<>();
    ArrayList<String> modelList = new ArrayList<>();
    ArrayList<String> msrpList = new ArrayList<>();
    ArrayList<String> offerList = new ArrayList<>();
    ArrayList<String> priceList = new ArrayList<>();
    ArrayList<String> milesList = new ArrayList<>();
    ArrayList<String> stockList = new ArrayList<>();
    ArrayList<String> bannerImageUrl = new ArrayList<>();
    ArrayList<String> Location = new ArrayList<>();
    ArrayList<String> DealerID = new ArrayList<>();
    ArrayList<String> ExteriorColor = new ArrayList<>();
    ArrayList<String> InteriorColor = new ArrayList<>();

    public SellingPriceAdapter(Context context, ArrayList<String> vin, ArrayList<String> make, ArrayList<String> model,  ArrayList<String> year, ArrayList<String> offer, ArrayList<String> price,  ArrayList<String> msrp,ArrayList<String> bannerimae, ArrayList<String> location, ArrayList<String> dealerID, ArrayList<String> miles, ArrayList<String> stock, ArrayList<String> exterior,ArrayList<String> interior) {
        super();
        this.context = context;
        //this.alName = alName;
       // this.alImage = alImage;
        this.VIN = vin;
        this.makeList = make;
        this.modelList = model;
        this.yearList = year;
        this.offerList = offer;
        this.priceList = price;
        this.msrpList=msrp;
        this.bannerImageUrl = bannerimae;
        this.Location = location;
        this.milesList = miles;
        this.stockList = stock;
        this.DealerID = dealerID;
        this.ExteriorColor = exterior;
        this.InteriorColor = interior;
    }

    /*@Override
    public int getItemCount() {
        return bannerImageUrl.size();
    }*/


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.selling_price, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.tvSpecies.setText(yearList.get(i)+" "+makeList.get(i)+" "+modelList.get(i));
        //viewHolder.sellingPrice.setText(priceList.get(i));
        viewHolder.sellinglocation.setText(Location.get(i));
        //viewHolder.imgThumbnail.setImageResource(bannerImageUrl.get(i));
        double msrp =Double.parseDouble(msrpList.get(i));
        double offer =Double.parseDouble(offerList.get(i));
        NumberFormat defaultFormat1 = NumberFormat.getInstance();
        double percentage = Math.ceil((offer / msrp) * 100);
        viewHolder.offerprice.setText("Offer: "+defaultFormat1.format(percentage)+"%");
        viewHolder.msrpprice.setText(Html.fromHtml("<sup>$</sup>")+defaultFormat1.format(msrp));
        strikeThroughText(viewHolder.msrpprice);
        if (!priceList.get(i).equalsIgnoreCase("0.00")) {

            double num2 =Double.parseDouble(priceList.get(i));
            NumberFormat defaultFormat = NumberFormat.getInstance();
            viewHolder.sellingPrice.setText(Html.fromHtml("<sup>$</sup>")+defaultFormat.format(num2));

        }else{

            viewHolder.sellingPrice.setText("Call For Price");

        }

        viewHolder.sellingcolor.setText(ExteriorColor.get(i));

        if((bannerImageUrl.get(i)).equals("") || (bannerImageUrl.get(i)).equals(null)){
            Picasso.with(context).load(R.drawable.logo).fit().into(viewHolder.imgThumbnail);
        }else {
            Picasso.with(context).load(bannerImageUrl.get(i)).placeholder(R.drawable.logo).fit().into(viewHolder.imgThumbnail);
        }
       // Picasso.with(context).load(bannerImageUrl.get(i)).placeholder(R.drawable.logo).fit().into(viewHolder.imgThumbnail);

        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    //Toast.makeText(context, "#" + position + " - " + alName.get(position) + " (Long click)", Toast.LENGTH_SHORT).show();
                    //context.startActivity(new Intent(context, CarDeatils.class));
                } else {
                    //Toast.makeText(context, "#" + position + " - " + VIN.get(position), Toast.LENGTH_SHORT).show();
                    //context.startActivity(new Intent(context, CarDeatils.class));

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

    public int getItemCount() {
        return VIN.size();
    }
    private void strikeThroughText(TextView price){
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imgThumbnail;
        public TextView tvSpecies,sellingPrice,sellinglocation,sellingcolor,offerprice,msrpprice;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tvSpecies = (TextView) itemView.findViewById(R.id.tv_species);
            sellingPrice = (TextView) itemView.findViewById(R.id.selling_Price);
            msrpprice = (TextView) itemView.findViewById(R.id.msrpprice);
            offerprice = (TextView) itemView.findViewById(R.id.offerprice);
            sellinglocation = (TextView) itemView.findViewById(R.id.location);
            sellingcolor=(TextView) itemView.findViewById(R.id.colorText);
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
