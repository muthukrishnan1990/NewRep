package bss.bbs.com.teslaclone.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import bss.bbs.com.teslaclone.R;

/**
 * Created by sairaj on 15/09/17.
 */

public class TableLayoutAdapter extends RecyclerView.Adapter<TableLayoutAdapter.ViewHolder> {

    Context context;
    ArrayList<String> RetriveList1 = new ArrayList<>();
    JSONObject jsonoject =null;
    String identify;

    public TableLayoutAdapter(Context context, JSONObject object,String string) {

        super();
        this.context = context;
       // this.RetriveList1 = tableData;
        this.jsonoject = object;
        this.identify = string;

    }

    @Override
    public TableLayoutAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.table_layout, viewGroup, false);
        TableLayoutAdapter.ViewHolder viewHolder = new TableLayoutAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TableLayoutAdapter.ViewHolder viewHolder, int i) {


        if(jsonoject != null) {
            try {
                Iterator iterator = jsonoject.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    RetriveList1.add(key);
                }
                for (int index = 0; index < RetriveList1.size(); index++) {

                    TableRow.LayoutParams Headerowparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    TableRow headerRow = new TableRow(context);
                    headerRow.setBackgroundColor(Color.DKGRAY);
                    headerRow.setLayoutParams(Headerowparams);
                    TextView textview = new TextView(context);
                    textview.setLayoutParams(Headerowparams);
                    textview.setText(RetriveList1.get(index).replace("_", " "));
                    textview.setTextColor(Color.WHITE);
                    textview.setTextSize(20);
                    textview.setTypeface(null, Typeface.BOLD);
                    headerRow.addView(textview);

                    viewHolder.mainTable.addView(headerRow);

                    JSONObject issue = jsonoject.getJSONObject(RetriveList1.get(index));
                    Iterator seconditer = issue.keys();
                    while (seconditer.hasNext()) {
                        String newkey = (String) seconditer.next();
                        String data = issue.getString(newkey);
                        String finalkey;
                        if(identify.equals("CarDetail")){
                            String removeline = newkey.replace("_", " ");
                            finalkey = removeline.substring(0, 1).toUpperCase() + removeline.substring(1).toLowerCase();
                        }else {
                            finalkey = newkey;
                        }

                        TableRow innerrow = new TableRow(context);
                        TableLayout.LayoutParams layoutRow = new TableLayout.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
                        layoutRow.setMargins(0, 5, 0, 5);
                        innerrow.setLayoutParams(layoutRow);

                        innerrow.setGravity(Gravity.CENTER);
                        innerrow.setWeightSum(1f);

                        TextView innertext = new TextView(context);
                        //TableRow.LayoutParams layoutHistory = new TableRow.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
                        TableRow.LayoutParams frontHistory = new TableRow.LayoutParams(0, RecyclerView.LayoutParams.WRAP_CONTENT, 0.4f);
                        innertext.setLayoutParams(frontHistory);
                        innertext.setText(finalkey);
                        innertext.setTextSize(15);
                        innertext.setGravity(Gravity.CENTER);
                        TextView dotted = new TextView(context);
                        TableRow.LayoutParams centerHistory = new TableRow.LayoutParams(0, RecyclerView.LayoutParams.WRAP_CONTENT, 0.2f);
                        dotted.setLayoutParams(centerHistory);
                        dotted.setText(":");
                        dotted.setTextSize(15);
                        dotted.setGravity(Gravity.CENTER);
                        TextView value = new TextView(context);
                        TableRow.LayoutParams lastHistory = new TableRow.LayoutParams(0, RecyclerView.LayoutParams.WRAP_CONTENT, 0.4f);
                        value.setLayoutParams(lastHistory);
                        value.setText(data);
                        value.setTextSize(15);
                        value.setGravity(Gravity.CENTER);
                        innerrow.addView(innertext);
                        innerrow.addView(dotted);
                        innerrow.addView(value);
                        viewHolder.mainTable.addView(innerrow);
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            TableRow innerrow = new TableRow(context);
            TableLayout.LayoutParams layoutRow = new TableLayout.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            layoutRow.setMargins(0, 5, 0, 5);
            innerrow.setLayoutParams(layoutRow);

            innerrow.setGravity(Gravity.CENTER);
            innerrow.setWeightSum(1f);

            TextView innertext = new TextView(context);
            TableRow.LayoutParams frontHistory = new TableRow.LayoutParams(0, RecyclerView.LayoutParams.WRAP_CONTENT, 0.4f);
            innertext.setLayoutParams(frontHistory);
            innertext.setText("Not Available");
            innertext.setTextSize(15);
            innertext.setGravity(Gravity.CENTER);
            TextView dotted = new TextView(context);
            TableRow.LayoutParams centerHistory = new TableRow.LayoutParams(0, RecyclerView.LayoutParams.WRAP_CONTENT, 0.2f);
            dotted.setLayoutParams(centerHistory);
            dotted.setText(":");
            dotted.setTextSize(15);
            dotted.setGravity(Gravity.CENTER);
            TextView value = new TextView(context);
            TableRow.LayoutParams lastHistory = new TableRow.LayoutParams(0, RecyclerView.LayoutParams.WRAP_CONTENT, 0.4f);
            value.setLayoutParams(lastHistory);
            value.setText("Not Available");
            value.setTextSize(15);
            value.setGravity(Gravity.CENTER);
            innerrow.addView(innertext);
            innerrow.addView(dotted);
            innerrow.addView(value);
            viewHolder.mainTable.addView(innerrow);
        }

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TableLayout mainTable;


        public ViewHolder(View itemView) {
            super(itemView);
            mainTable = (TableLayout) itemView.findViewById(R.id.main_table);

        }


    }
}
