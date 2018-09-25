package bss.bbs.com.teslaclone.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;

import bss.bbs.com.teslaclone.Activity.CarDeatils;
import bss.bbs.com.teslaclone.Activity.DealerDetails;
import bss.bbs.com.teslaclone.R;



public class FilterDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;

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
    private final int ITEM_VIEW_TYPE_BASIC = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private OnLoadMoreListener onLoadMoreListener;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private int previousTotal = 0;


    public FilterDetailAdapter(final Context context, ArrayList<String> vin, ArrayList<String> make, ArrayList<String> model, ArrayList<String> year, ArrayList<String> price, ArrayList<String> bannerimae, ArrayList<String> location, ArrayList<String> dealerID, ArrayList<String> miles, ArrayList<String> stock, ArrayList<String> exterior, ArrayList<String> interior, RecyclerView recyclerView) {

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
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
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
    public int getItemViewType(int position) {
        return VIN.get(position) == null ? VIEW_TYPE_LOADING : ITEM_VIEW_TYPE_BASIC;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        //RecyclerView.ViewHolder vh;
        if (i == ITEM_VIEW_TYPE_BASIC) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_item_layout, viewGroup, false);
            return new ViewHolder(view);


        }else if(i==VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_item, viewGroup, false);
            return new ProgressViewHolder(view);


        }
       /// View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_item_layout, viewGroup, false);
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder  viewHolder, int i) {
            if (viewHolder instanceof ViewHolder) {
                ViewHolder userViewHolder = (ViewHolder) viewHolder;
                userViewHolder.sectionMakeModel.setText(yearList.get(i) + " " + makeList.get(i) + "  " + modelList.get(i) + " ");
                userViewHolder.sectionLocation.setText(Location.get(i));

                if (!priceList.get(i).equalsIgnoreCase("0.00")) {

                    double num2 = Double.parseDouble(priceList.get(i));
                    NumberFormat defaultFormat = NumberFormat.getInstance();
                    userViewHolder.sectionPrice.setText(Html.fromHtml("<sup>$</sup>") + defaultFormat.format(num2));

                } else {

                    userViewHolder.sectionPrice.setText("Call For Price");

                }
                userViewHolder.stocktype.setText(stockList.get(i));
                if(!ExteriorColor.get(i).equalsIgnoreCase("")) {
                    userViewHolder.sectioncolor.setText(ExteriorColor.get(i));
                }else{
                    userViewHolder.sectioncolor.setText(InteriorColor.get(i));
                }
               // double miles = Double.parseDouble(milesList.get(i));
               // NumberFormat defaultFormat = NumberFormat.getInstance();
                if(!milesList.get(i).equalsIgnoreCase("0")){
//            viewHolder.tv_mile.setText(Mileage.get(i));
                    double miles = Double.parseDouble(milesList.get(i));
                    NumberFormat defaultFormat = NumberFormat.getInstance();
                    userViewHolder.mileagelist.setText(Html.fromHtml(defaultFormat.format(miles) + "" + "Miles"));
                }else {
                    userViewHolder.mileagelist.setText("Call for Mileage");
                }
               // userViewHolder.mileagelist.setText(Html.fromHtml(defaultFormat.format(miles) + "" + "Miles"));
                if((bannerImageUrl.get(i)).equals("") || (bannerImageUrl.get(i)).equals(null)){
                    Picasso.with(context).load(R.drawable.logo).fit().into(userViewHolder.img_banner);
                }else {
                    Picasso.with(context).load(bannerImageUrl.get(i)).placeholder(R.drawable.logo).fit().into(userViewHolder.img_banner);
                }
                //Picasso.with(context).load(bannerImageUrl.get(i)).placeholder(R.drawable.logo).fit().into(userViewHolder.img_banner);
                userViewHolder.setItemClickListener
                        (new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                if (isLongClick) {
                                } else {
                                    //Toast.makeText(context, " " + position + " - " + modelList.get(position), Toast.LENGTH_SHORT).show();
                                    //getCarDetailsPost(VIN.get(position),DealerID.get(position));
                                    Intent intent = new Intent(context, CarDeatils.class);
                                    intent.putExtra("Make", makeList.get(position));
                                    intent.putExtra("Model", modelList.get(position));
                                    intent.putExtra("Year", yearList.get(position));
                                    intent.putExtra("VIN", VIN.get(position));
                                    intent.putExtra("Dealer_ID", DealerID.get(position));
                                    intent.putExtra("Banne_Image", bannerImageUrl.get(position));
                                    intent.putExtra("Price", priceList.get(position));
                                    intent.putExtra("Mileage", milesList.get(position));
                                    intent.putExtra("Condition", stockList.get(position));
                                    intent.putExtra("Location", Location.get(position));
                                    intent.putExtra("Exterior", ExteriorColor.get(position));
                                    intent.putExtra("Interior", InteriorColor.get(position));
                                    context.startActivity(intent);
                                    ((Activity) context).overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                                }
                            }
                        });

            } else if(viewHolder instanceof ProgressViewHolder) {
                ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
            }
        }
     public void setLoaded() {
        isLoading = false;
    }
    public int getItemCount() {
         return VIN.size();
       // return  VIN == null ? 0 : VIN.size();
    }
    public  void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }


    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView sectionMakeModel,sectionPrice,sectionLocation,website,sectioncolor,stocktype,mileagelist;
        private ImageView img_banner;
        private ItemClickListener clickListener;

        public ViewHolder(View view) {
            super(view);

            sectionMakeModel = (TextView) view.findViewById(R.id.filter_makemodel);
            sectionPrice = (TextView) view.findViewById(R.id.filter_price);
            stocktype = (TextView) view.findViewById(R.id.stocktype);
            sectioncolor = (TextView) view.findViewById(R.id.colorlist);

            img_banner = (ImageView) view.findViewById(R.id.filter_image);
            sectionLocation = (TextView) view.findViewById(R.id.location);
            mileagelist= (TextView)view.findViewById(R.id.mileagelist);
            website= (TextView)view.findViewById(R.id.website);
            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1=new Intent(context, DealerDetails.class);
                    intent1.putExtra("Dealer_ID",DealerID.get(getAdapterPosition()));
                    context.startActivity(intent1);
                    ((Activity) context).overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                }
            });
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
    public  class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public ProgressViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.more_progress);
        }
    }
    public void getCarDetailsPost(String vin,String dealerID){

        Toast.makeText(context,vin+"\n"+dealerID,Toast.LENGTH_SHORT).show();

    }



}
