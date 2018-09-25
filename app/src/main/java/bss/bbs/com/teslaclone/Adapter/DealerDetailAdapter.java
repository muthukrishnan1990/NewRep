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

import bss.bbs.com.teslaclone.Activity.DealerDetails;
import bss.bbs.com.teslaclone.R;

/**
 * Created by sairaj on 30/08/17.
 */

public class DealerDetailAdapter extends RecyclerView.Adapter<DealerDetailAdapter.ViewHolder> {

    private Context context;

    ArrayList<String> Dealername = new ArrayList<>();
    ArrayList<String> Location = new ArrayList<>();
    ArrayList<String> Banner_Image = new ArrayList<>();
    ArrayList<String> Dealer_ID = new ArrayList<>();

    public DealerDetailAdapter(Context context, ArrayList<String> dealername, ArrayList<String> location, ArrayList<String> banner_image, ArrayList<String> dealerID ) {

        this.Dealername = dealername;
        this.Location = location;
        this.Banner_Image = banner_image;
        this.Dealer_ID = dealerID;
        this.context = context;
    }

    @Override
    public DealerDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dealer_item_layout, viewGroup, false);
        return new DealerDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DealerDetailAdapter.ViewHolder viewHolder, int i) {

        viewHolder.sectionDealername.setText(Dealername.get(i));
        viewHolder.sectionLocation.setText(Location.get(i));

        if((Banner_Image.get(i)).equals("") || (Banner_Image.get(i)).equals(null)){
            Picasso.with(context).load(R.drawable.logo).fit().into(viewHolder.img_banner);
        }else {
            Picasso.with(context).load(Banner_Image.get(i)).placeholder(R.drawable.logo).fit().into(viewHolder.img_banner);
        }
      //  Picasso.with(context).load(Banner_Image.get(i)).placeholder(R.drawable.logo).fit().into(viewHolder.img_banner);
        (viewHolder).setItemClickListener
                (new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if (isLongClick) {
                        } else {
                            //Toast.makeText(context, " " + position + " - " + Dealername.get(position), Toast.LENGTH_SHORT).show();

                            Intent intent= new Intent(context, DealerDetails.class);
                            intent.putExtra("Dealer_ID",Dealer_ID.get(position));
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        }
                    }
                });
    }

    public int getItemCount() {
        return Banner_Image.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView sectionDealername,sectionLocation;
        private ImageView img_banner;
        private ItemClickListener clickListener;

        public ViewHolder(View view) {
            super(view);

            sectionDealername = (TextView) view.findViewById(R.id.dealer_name);
            sectionLocation = (TextView) view.findViewById(R.id.dealer_location);

            img_banner = (ImageView) view.findViewById(R.id.dealer_image);
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
