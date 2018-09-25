package bss.bbs.com.teslaclone.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bss.bbs.com.teslaclone.Activity.CarDeatils;
import bss.bbs.com.teslaclone.R;

/**
 * Created by sairaj on 08/09/17.
 */

public class DealerCarListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Integer> alImage;
    ArrayList<String> alName;
    Context context;

    ArrayList<String> VIN = new ArrayList<>();
    ArrayList<String> yearList = new ArrayList<>();
    ArrayList<String> makeList = new ArrayList<>();
    ArrayList<String> modelList = new ArrayList<>();
    ArrayList<String> priceList = new ArrayList<>();
    ArrayList<String> milesList = new ArrayList<>();
    ArrayList<String> stockList = new ArrayList<>();
    ArrayList<String> bannerImageUrl = new ArrayList<>();
    //ArrayList<String> Location = new ArrayList<>();
    ArrayList<String> DealerID = new ArrayList<>();
    ArrayList<String> ExteriorColor = new ArrayList<>();
    ArrayList<String> InteriorColor = new ArrayList<>();
    String Location;
    private final int ITEM_VIEW_TYPE_BASIC = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private OnLoadMoreListener onLoadMoreListener;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private int previousTotal = 0;


    public DealerCarListAdapter(Context context, ArrayList<String> vin, ArrayList<String> make, ArrayList<String> model,  ArrayList<String> year, ArrayList<String> price,  ArrayList<String> bannerimae, String location, ArrayList<String> dealerID, ArrayList<String> miles, ArrayList<String> stock, ArrayList<String> exterior,ArrayList<String> interior,RecyclerView recyclerView) {

        this.context = context;
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
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager  gridLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = gridLayoutManager.getItemCount();
                    lastVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == ITEM_VIEW_TYPE_BASIC) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.dealer_list_cars, viewGroup, false);
            return new ViewHolder(itemView);
        }else if(i==VIEW_TYPE_LOADING){
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_loading_item, viewGroup, false);
            return new ProgressViewHolder(itemView);
        }
        //DealerCarListAdapter.ViewHolder viewHolder = new DealerCarListAdapter.ViewHolder(v);
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return VIN.get(position) == null ? VIEW_TYPE_LOADING : ITEM_VIEW_TYPE_BASIC;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder userViewHolder = (ViewHolder) viewHolder;

            userViewHolder.make.setText(makeList.get(i));
            userViewHolder.model.setText(modelList.get(i));
            userViewHolder.year.setText(yearList.get(i));
            if((bannerImageUrl.get(i)).equals("") || (bannerImageUrl.get(i)).equals(null)){
                Picasso.with(context).load(R.drawable.logo).fit().into(userViewHolder.imgThumbnail);
            }else {
                Picasso.with(context).load(bannerImageUrl.get(i)).placeholder(R.drawable.logo).fit().into(userViewHolder.imgThumbnail);
            }
          //  Picasso.with(context).load(bannerImageUrl.get(i)).placeholder(R.drawable.logo).fit().into(userViewHolder.imgThumbnail);

            userViewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (isLongClick) {
                        //Toast.makeText(context, "#" + position + " - " + alName.get(position) + " (Long click)", Toast.LENGTH_SHORT).show();
                        //context.startActivity(new Intent(context, CarDeatils.class));
                    } else {
                        //Toast.makeText(context, "#" + position + " - " + alImage.get(position), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, CarDeatils.class);
                        intent.putExtra("Make", makeList.get(position));
                        intent.putExtra("Model", modelList.get(position));
                        intent.putExtra("Year", yearList.get(position));
                        intent.putExtra("VIN", VIN.get(position));
                        intent.putExtra("Dealer_ID", DealerID.get(position));
                        intent.putExtra("Banne_Image", bannerImageUrl.get(position));
                        intent.putExtra("Price", priceList.get(position));
                        Log.e("Mileage", milesList.get(position));
                        intent.putExtra("Mileage", milesList.get(position));
                        intent.putExtra("Condition", stockList.get(position));
                        intent.putExtra("Location", Location);
                        intent.putExtra("Exterior", ExteriorColor.get(position));
                        intent.putExtra("Interior", InteriorColor.get(position));
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);

                    }
                }
            });
        }else if(viewHolder instanceof ProgressViewHolder) {
            ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
        }

    }

    public int getItemCount() {
        return VIN.size();
       // return  VIN == null ? 0 : VIN.size();
    }
    public  void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }
    public void setLoaded() {
        isLoading = false;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imgThumbnail;
        public TextView make,model,year;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            make = (TextView) itemView.findViewById(R.id.Make);
            model = (TextView) itemView.findViewById(R.id.Model);
            year = (TextView) itemView.findViewById(R.id.Year);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.section_car_image);
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
    public  class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.more_progress);
        }
    }
}
