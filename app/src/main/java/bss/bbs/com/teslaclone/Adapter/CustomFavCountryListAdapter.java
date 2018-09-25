package bss.bbs.com.teslaclone.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bss.bbs.com.teslaclone.R;
import bss.bbs.com.teslaclone.utils.OnDialogListClickListener;

/**
 * Created by sairaj on 19/09/17.
 */

public class CustomFavCountryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> mCountryList;
    Context mContext;
    int position;
    RecyclerView recyclerView;
    String selecteditem;
    //Country cnt;

    OnDialogListClickListener mlistener;
    int selectedPosition=-1;
    public CustomFavCountryListAdapter(ArrayList<String> makefilterlist, RecyclerView makeRecycler, FragmentActivity activity, OnDialogListClickListener onDialogListClickListener,String selecteditem) {

        this.mCountryList = makefilterlist;
        this.mContext = activity;
        this.mlistener = onDialogListClickListener;
        this.recyclerView=makeRecycler;
        this.selecteditem=selecteditem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.make_list_item,
                parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, final int position) {
        ItemViewHolder holder = (ItemViewHolder) rawHolder;

        holder.tv_fav_con_name.setText(mCountryList.get(position));
        /*Log.e("List",mCountryList.get(position));*/
       if(selecteditem.equals(mCountryList.get(position))) {
           holder.ticked.setVisibility(View.VISIBLE);
       }else {
           holder.ticked.setVisibility(View.INVISIBLE);
       }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition=position;
               selecteditem = mCountryList.get(position);
                mlistener.onItemClick(mCountryList.get(position),recyclerView);
                notifyDataSetChanged();

            }
        });
        holder.itemView.setTag(mCountryList.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mCountryList.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_fav_con_name;
        ImageView ticked;
        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_fav_con_name = (TextView) itemView.findViewById(R.id.name);
            ticked=(ImageView)itemView.findViewById(R.id.ticked);
        }
    }
}
