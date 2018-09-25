package bss.bbs.com.teslaclone.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import bss.bbs.com.teslaclone.R;



public class FilterListDataAdapter extends BaseAdapter {

    Context con;
    ArrayList<String> Listdata;
    LayoutInflater inflater;

    public FilterListDataAdapter(Context context, ArrayList<String> makefilterlist) {

        // TODO Auto-generated constructor stub
        this.con = context;
        this.Listdata = makefilterlist;
        inflater = (LayoutInflater) con
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Listdata.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressLint({ "ViewHolder", "InflateParams" })
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        convertView = inflater.inflate(R.layout.make_list_item, null);
        TextView t1 = (TextView) convertView.findViewById(R.id.name);


        t1.setText(Listdata.get(position));

        return convertView;
    }
}
