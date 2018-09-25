package bss.bbs.com.teslaclone.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bss.bbs.com.teslaclone.Fragments.CarcompareFragment;
import bss.bbs.com.teslaclone.R;

/**
 * Created by sairaj on 25/08/17.
 */

public class TrendingCarsAdapter extends RecyclerView.Adapter<TrendingCarsAdapter.ViewHolder> {

    ArrayList<String> alName;
    ArrayList<Integer> alImage;
    Context context;

    ArrayList<JSONObject> RetriveList1 = new ArrayList<>();
    ArrayList<JSONObject> RetriveList2 = new ArrayList<>();

    public TrendingCarsAdapter(Context context, ArrayList<JSONObject> list1, ArrayList<JSONObject> list2) {

        super();
        this.context = context;
        this.alName = alName;
        this.alImage = alImage;

        this.RetriveList1 = list1;
        this.RetriveList2 = list2;
    }

    @Override
    public TrendingCarsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trending_price, viewGroup, false);
        TrendingCarsAdapter.ViewHolder viewHolder = new TrendingCarsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrendingCarsAdapter.ViewHolder viewHolder, final int i) {
        //viewHolder.tvSpecies.setText(alName.get(i));
        //viewHolder.imgThumbnail1.setImageResource(alImage.get(i));
        //viewHolder.imgThumbnail2.setImageResource(alImage.get(1));
        //Picasso.with(context).load(alImage.get(0)).placeholder(R.drawable.list_car4).fit().into(viewHolder.imgThumbnail1);
        //Picasso.with(context).load(alImage.get(1)).placeholder(R.drawable.list_car4).fit().into(viewHolder.imgThumbnail2);
        try{
            viewHolder.Make1.setText(RetriveList1.get(i).getString("Make"));
            viewHolder.Make2.setText(RetriveList2.get(i).getString("Make"));
            viewHolder.Model1.setText(RetriveList1.get(i).getString("Model"));
            viewHolder.Model2.setText(RetriveList2.get(i).getString("Model"));
            if((RetriveList1.get(i)).equals("") || (RetriveList1.get(i)).equals(null)){
                Picasso.with(context).load(R.drawable.logo).fit().into(viewHolder.imgThumbnail1);
            }else {
                Picasso.with(context).load(RetriveList1.get(i).getString("Banner_Image")).placeholder(R.drawable.logo).fit().into(viewHolder.imgThumbnail1);
            }
           // Picasso.with(context).load(RetriveList1.get(i).getString("Banner_Image")).placeholder(R.drawable.logo).fit().into(viewHolder.imgThumbnail1);
            if((RetriveList2.get(i)).equals("") || (RetriveList2.get(i)).equals(null)){
                Picasso.with(context).load(R.drawable.logo).fit().into(viewHolder.imgThumbnail2);
            }else {
                Picasso.with(context).load(RetriveList2.get(i).getString("Banner_Image")).placeholder(R.drawable.logo).fit().into(viewHolder.imgThumbnail2);
            }
           // Picasso.with(context).load(RetriveList2.get(i).getString("Banner_Image")).placeholder(R.drawable.logo).fit().into(viewHolder.imgThumbnail2);

        }catch (JSONException e) {
            e.printStackTrace();
        }


        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    //Toast.makeText(context, "#" + position + " - " + alName.get(position) + " (Long click)", Toast.LENGTH_SHORT).show();
                    //context.startActivity(new Intent(context, MainActivity.class));
                } else {
                    //Toast.makeText(context, "#" + position + " - " + alName.get(position), Toast.LENGTH_SHORT).show();
                    try{
                        Bundle args = new Bundle();
                        //args.putString("bannerImage", bannerimage);
                        //args.putString("response", respo);
                        args.putString("First_Image", RetriveList1.get(i).getString("Banner_Image"));
                        args.putString("First_Vin", RetriveList1.get(i).getString("VIN"));
                        args.putString("First_Make", RetriveList1.get(i).getString("Make"));
                        args.putString("First_Model", RetriveList1.get(i).getString("Model"));
                        args.putString("First_Year", RetriveList1.get(i).getString("Year"));
                        args.putString("First_Ext_Color", RetriveList1.get(i).getString("Exterior_Color"));
                        args.putString("First_Int_Color", RetriveList1.get(i).getString("Interior_Color"));
                        args.putString("Second_Image", RetriveList2.get(i).getString("Banner_Image"));
                        args.putString("Second_Vin", RetriveList2.get(i).getString("VIN"));
                        args.putString("Second_Make", RetriveList2.get(i).getString("Make"));
                        args.putString("Second_Model", RetriveList2.get(i).getString("Model"));
                        args.putString("Second_Year", RetriveList2.get(i).getString("Year"));
                        args.putString("Second_Ext_Color", RetriveList1.get(i).getString("Exterior_Color"));
                        args.putString("Second_Int_Color", RetriveList1.get(i).getString("Interior_Color"));
                        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                        Fragment fr = new CarcompareFragment(context);
                        fr.setArguments(args);
                        ft.replace(R.id.main_fragment, fr);
                        ft.addToBackStack(null);
                        ft.commit();
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return RetriveList1.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imgThumbnail1,imgThumbnail2;
        public TextView Make1,Make2,Model1,Model2;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail1 = (ImageView) itemView.findViewById(R.id.img_thumbnail1);
            imgThumbnail2 = (ImageView) itemView.findViewById(R.id.img_thumbnail2);
            Make1 = (TextView) itemView.findViewById(R.id.Trending_make);
            Make2 = (TextView) itemView.findViewById(R.id.Trending_Secocnd_make);
            Model1 = (TextView) itemView.findViewById(R.id.Trending_model);
            Model2 = (TextView) itemView.findViewById(R.id.Trending_Secocnd_model);
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
