package bss.bbs.com.teslaclone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bss.bbs.com.teslaclone.Activity.CarDeatils;
import bss.bbs.com.teslaclone.R;

/**
 * Created by sairaj on 08/09/17.
 */

public class ThumpImageAdapter extends RecyclerView.Adapter<ThumpImageAdapter.ViewHolder> {

    Context context;
    ArrayList<String> Thumpimages = new ArrayList<>();
    ImageView BannerImage ;

    public ThumpImageAdapter(Context context, ArrayList<String> alImage,ImageView bannerimage) {

        this.context = context;
         this.Thumpimages = alImage;
        this.BannerImage = bannerimage;
    }

    @Override
    public ThumpImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.car_detail_item, viewGroup, false);
        ThumpImageAdapter.ViewHolder viewHolder = new ThumpImageAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ThumpImageAdapter.ViewHolder viewHolder, int i) {


        Picasso.with(context).load(Thumpimages.get(i)).placeholder(R.drawable.logo).fit().into(viewHolder.imgThumbnail);

        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                    //Toast.makeText(context, "#" + position + " - " + alName.get(position) + " (Long click)", Toast.LENGTH_SHORT).show();
                    //context.startActivity(new Intent(context, CarDeatils.class));
                } else {
                    Picasso.with(context).load(Thumpimages.get(position)).fit().into(BannerImage);
                    //Toast.makeText(context, "#" + position + " - " + Thumpimages.get(position), Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    public int getItemCount() {
        return Thumpimages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imgThumbnail;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.Slide_image);
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
