package bss.bbs.com.teslaclone.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bss.bbs.com.teslaclone.Adapter.CarFavouritesAdapter;
import bss.bbs.com.teslaclone.Adapter.DBAdapter;
import bss.bbs.com.teslaclone.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sairaj on 21/08/17.
 */

@SuppressLint("ValidFragment")
public class FavoritesFragment extends Fragment {
    ImageView header_logo_image,favo;
    TextView header_title,favotx,edit,edit1;
    Context context;

    RecyclerView recyclerView;


    ArrayList<String> VIN = new ArrayList<String>();
    ArrayList<String> DealerID = new ArrayList<>();
    ArrayList<String> Make = new ArrayList<String>();
    ArrayList<String> Model = new ArrayList<String>();
    ArrayList<String> image_url_pattern = new ArrayList<String>();
    ArrayList<String> Year = new ArrayList<String>();
    ArrayList<String> List_Price = new ArrayList<String>();
    ArrayList<String> Mileage = new ArrayList<String>();
    ArrayList<String> Exterior = new ArrayList<String>();
    ArrayList<String> Interior = new ArrayList<String>();
    ArrayList<String> Codition = new ArrayList<String>();
    ArrayList<String> Location = new ArrayList<String>();
    private CarFavouritesAdapter adapter;
    private Paint p = new Paint();
    // ArrayList<String> MSRP = new ArrayList<String>();
    DBAdapter db;
    RecyclerView recycl;

    public FavoritesFragment(Context con) {
        context=con;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBAdapter(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.favorites, container, false);
        TextView titleview = (TextView) getActivity().findViewById(R.id.titleid);
        titleview.setText("Favorites Car");
        db = new DBAdapter(context);
        setToolbar(view);
        initViews(view);
        return view;
    }
    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of LoginFragment");
        super.onResume();
        myfavourites();
    }
    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of HomeFragment");
        super.onPause();
    }
    private void myfavourites() {
        VIN.clear();
        DealerID.clear();
        Make.clear();
        Model.clear();
        image_url_pattern.clear();
        List_Price.clear();
        Mileage.clear();
        Year.clear();
        Exterior.clear();
        Interior.clear();
        Codition.clear();
        Location.clear();
       // MSRP.clear();

        Cursor c = db.fetchQuery("SELECT * FROM myfavorites");
        int count = c.getCount();
        if (count > 0) {
            while (c.moveToNext()) {
                VIN.add(c.getString(c.getColumnIndex("VIN")));
                DealerID.add(c.getString(c.getColumnIndex("Dealer_ID")));
                Make.add(c.getString(c.getColumnIndex("Make")));
                Model.add(c.getString(c.getColumnIndex("Model")));
                image_url_pattern.add(c.getString(c.getColumnIndex("image_url_pattern")));
                List_Price.add(c.getString(c.getColumnIndex("List_Price")));
                Mileage.add(c.getString(c.getColumnIndex("Mileage")));
                Year.add(c.getString(c.getColumnIndex("Year")));
                Exterior.add(c.getString(c.getColumnIndex("exterior")));
                Interior.add(c.getString(c.getColumnIndex("interior")));
                Codition.add(c.getString(c.getColumnIndex("Condition")));
                Location.add(c.getString(c.getColumnIndex("Location")));

            }

        }
         adapter = new CarFavouritesAdapter(context,VIN,DealerID,Make, Model,Mileage,image_url_pattern,List_Price,Year,Exterior,Interior,Codition,Location);
        Log.e("example",adapter.toString());
        recyclerView.setAdapter(adapter);
        if(VIN.size() > 0){

            favotx.setVisibility(View.GONE);
            favo.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
        initSwipe();
    }
        private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT){
                    adapter.removeItem(position);
                    showChangeLangDialog("","Car removed from favorites");
                }
            }
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.rubbish_bin);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);

                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private void setToolbar(final View view) {

        favo=(ImageView) view.findViewById(R.id.splash_ivicon);
        favotx=(TextView) view.findViewById(R.id.empty_favorites);
        //edit=(TextView)view.findViewById(R.id.edit);


        recycl=(RecyclerView)view.findViewById(R.id.favourites_result_lay_recycler);


    }

    private void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.favourites_result_lay_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
    }
    public void showChangeLangDialog(String title,String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_custom);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        final TextView messagecontent = (TextView) dialog.findViewById(R.id.messagealert);
        final TextView titlecontent = (TextView) dialog.findViewById(R.id.titlealert);
        final Button okbutton =(Button) dialog.findViewById(R.id.okbutton);
        titlecontent.setText(title);
        messagecontent.setText(message);
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
