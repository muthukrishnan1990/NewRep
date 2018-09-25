package bss.bbs.com.teslaclone.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import bss.bbs.com.teslaclone.Adapter.DBAdapter;
import bss.bbs.com.teslaclone.Adapter.TableLayoutAdapter;
import bss.bbs.com.teslaclone.Adapter.ThumpImageAdapter;
import bss.bbs.com.teslaclone.R;
import bss.bbs.com.teslaclone.Singleton.AppController;
import bss.bbs.com.teslaclone.Singleton.Constants;
import bss.bbs.com.teslaclone.network.VolleyCustomPostRequest;



public class CarDeatils extends FragmentActivity implements View.OnClickListener {

    Context context = this;
    SharedPreferences pref;
    ArrayList<Integer> alImage;
    ArrayList<String> favorite_vin = new ArrayList<String>();
    RecyclerView thump_image,TablelayoutRecycler;
    RecyclerView.LayoutManager mLayoutManager,Tablelayout;
    RecyclerView.Adapter Thumplist,TablelayoutAdapter,Carcomparelist;
    Button leftbutton,Comparecar_Button,Dealerdeatil_Button;
    ImageView bannerimage;
    TextView Emptyview,Loading,MakeModel_ID,Price_ID,Location_ID,Rating_Text,Trans_ID,KM_ID,Condition_ID,Basic_Text,Specification_Text,Feature_Text;
    RatingBar ratingbar;
    JSONObject Basic,Overall_info,Seller_info,Exterior_Dimension,Steering_object,Suspension_object,Basic_Hole_Object,Spec_Hole_Object,Featuer_Hole_Object;

    ArrayList<String> Thumpimages = new ArrayList<>();
    ArrayList<JSONObject> TableData = new ArrayList<>();
    LinearLayout Basicbutton,Specbutton,Featurebutton,RatingLayout;
    String Image_Count,VIN,DealerID,BannerImage,Price,Condition,Mileage,Location,Exterior,Interior,Make,Model,Year,Trim,MakeNiceName,ModelNiceName,Trans_type,Squashid,Fueltype,Bodytype,Enginetype,Enginecompressor;
    ImageView favorate,favorate1;
    DBAdapter db;
    String vinid,selectedvin="";
    /*public CarDeatils(Context con) {
        // Required empty public constructor
        context = con;
        pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_detail);

        db = new DBAdapter(context);

        Intent intent = getIntent();
        if (!intent.equals(null)) {
            Make = intent.getStringExtra("Make");
            Model = intent.getStringExtra("Model");
            Year = intent.getStringExtra("Year");
            VIN = intent.getStringExtra("VIN");
            DealerID = intent.getStringExtra("Dealer_ID");
            BannerImage = intent.getStringExtra("Banne_Image");
            Mileage = intent.getStringExtra("Mileage");
            Price = intent.getStringExtra("Price");
            Condition = intent.getStringExtra("Condition");
            Location = intent.getStringExtra("Location");
            Exterior = intent.getStringExtra("Exterior");
            Interior = intent.getStringExtra("Interior");
        }
        edmundsVehicleDetail(VIN);
        loadData(VIN,DealerID);

        setUpViews();

        Cursor c = db.fetchQuery("SELECT * FROM myfavorites");
        int count = c.getCount();
        while (c.moveToNext()) {
            selectedvin=c.getString(c.getColumnIndex("VIN"));
            Log.e("selectedvin",selectedvin);
            if(selectedvin.equals(VIN)){
                favorate1.setVisibility(View.VISIBLE);
                favorate.setVisibility(View.GONE);
            }
        }


    }

    private void setUpViews() {

        leftbutton = (Button) findViewById(R.id.title_bar_left_menu);

        leftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.riht_to_left);

            }
        });

        bannerimage = (ImageView) findViewById(R.id.Car_banner);
        Picasso.with(context).load(BannerImage).placeholder(R.drawable.logo).fit().into(bannerimage);

        Emptyview = (TextView) findViewById(R.id.Empty_Text);
        Loading = (TextView) findViewById(R.id.Loading_Text);
        MakeModel_ID = (TextView) findViewById(R.id.Make_Model_Year);
        Price_ID = (TextView) findViewById(R.id.Price_id);
        Location_ID = (TextView) findViewById(R.id.location);
        Rating_Text = (TextView) findViewById(R.id.Rating_id);
        Trans_ID = (TextView) findViewById(R.id.Trans_text);
        KM_ID = (TextView) findViewById(R.id.KM_text);
        Condition_ID = (TextView) findViewById(R.id.Condition_text);
        RatingLayout = (LinearLayout) findViewById(R.id.Rating_layout);
        ratingbar = (RatingBar) findViewById(R.id.Rating_Star);

        favorate=(ImageView) findViewById(R.id.favorate_icon);
        favorate1=(ImageView) findViewById(R.id.favorate_icon1);

        Comparecar_Button = (Button) findViewById(R.id.Comparecar_Button);
        Dealerdeatil_Button = (Button) findViewById(R.id.Dealerdetail_Button);

        Basicbutton = (LinearLayout) findViewById(R.id.Basic_Button);
        Specbutton = (LinearLayout) findViewById(R.id.Spec_Button);
        Featurebutton = (LinearLayout) findViewById(R.id.Feature_Button);
        Basic_Text = (TextView) findViewById(R.id.Basic_Text);
        Specification_Text = (TextView) findViewById(R.id.Specification_Text);
        Feature_Text = (TextView) findViewById(R.id.Feature_Text);

        MakeModel_ID.setText(Make+" "+Model+" "+Year);

        Comparecar_Button.setOnClickListener(this);
        Dealerdeatil_Button.setOnClickListener(this);
        Basicbutton.setOnClickListener(this);
        Specbutton.setOnClickListener(this);
        Featurebutton.setOnClickListener(this);
        favorate.setOnClickListener(this);
        favorate1.setOnClickListener(this);

        TablelayoutRecycler = (RecyclerView) findViewById(R.id.Table_recycler);
        Tablelayout = new LinearLayoutManager(context);
        TablelayoutRecycler.setLayoutManager(Tablelayout);



        if(Location != null){

            Location_ID.setText(Location);

        }

        if(Condition != null){

            Condition_ID.setText(Condition);

        }

        if(Mileage != null){

          //  KM_ID.setText(Mileage);
           /* double num =Double.parseDouble(Mileage);
            NumberFormat defaultFormat = NumberFormat.getInstance();
            KM_ID.setText(defaultFormat.format(num));*/
            if(!Mileage.equalsIgnoreCase("0")){
//            viewHolder.tv_mile.setText(Mileage.get(i));
                double num =Double.parseDouble(Mileage);
                NumberFormat defaultFormat = NumberFormat.getInstance();
                KM_ID.setText(defaultFormat.format(num));
            }else {
                KM_ID.setText("Call for Mileage");
            }


        }

        if (!Price.equalsIgnoreCase("0.00")) {

            double num2 =Double.parseDouble(Price);
            NumberFormat defaultFormat = NumberFormat.getInstance();
            Price_ID.setText(Html.fromHtml("<sup>$</sup>")+defaultFormat.format(num2));

        }else{

            Price_ID.setText("Call For Price");

        }

        //Selling Price
        // Calling the RecyclerView
        thump_image = (RecyclerView) findViewById(R.id.Thump_recycler);
        thump_image.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        thump_image.setLayoutManager(mLayoutManager);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.riht_to_left);
    }

    public void edmundsVehicleDetail(final String vin){

        Map<String, String> params = new HashMap<String, String>();
        String URL = Constants.Edmundsvin_Detail+vin+"&"+Constants.Edmunds_Key;

        VolleyCustomPostRequest jsObjRequest = new VolleyCustomPostRequest(URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                // TODO Auto-generated method stub
                parseSpecRespnseData(response.toString());

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

                NetworkResponse response = error.networkResponse;
                Log.e("Vin_Error", error.toString());


                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO+
                } else if (error instanceof ParseError) {
                    //TODO
                }
            }
        });

        //jsObjRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsObjRequest, "tag_json_obj");

    }

    private void parseSpecRespnseData(String specResponse) {

        JSONObject json = null ;
        try {
            if(!specResponse.equals(null)){
            json = new JSONObject(specResponse);

            JSONArray styleHolder = json.getJSONArray("styleHolder");
            Exterior_Dimension = new JSONObject();
            Steering_object = new JSONObject();
            Suspension_object = new JSONObject();

            for (int i = 0; i < styleHolder.length(); i++) {
                JSONObject styleobject = styleHolder.optJSONObject(i);

                Make = styleobject.getString("makeName");
                MakeNiceName = styleobject.getString("makeNiceName");
                Model = styleobject.getString("modelName");
                ModelNiceName = styleobject.getString("modelNiceName");
                Year = styleobject.getString("year");
                Trans_type = styleobject.getString("transmissionType");
                Enginetype = styleobject.getString("engineType");
                Enginecompressor = styleobject.getString("engineCompressorType");
                Fueltype = styleobject.getString("engineFuelType");

                //********************* squishVins *********************//

                JSONArray squishvin = styleobject.getJSONArray("squishVins");
                JSONObject vinobject = squishvin.getJSONObject(0);
                Squashid = vinobject.getString("squishVin");
                Log.e("Squashid", Squashid);
                //********************* Trim *********************//
                JSONObject Trimobject = styleobject.getJSONObject("trim");
                Trim = Trimobject.getString("name");

                //********************* Body Type *********************//
                JSONObject catagories = styleobject.getJSONObject("categories");
                JSONArray vehicletype = catagories.getJSONArray("Vehicle Style");
                Bodytype = vehicletype.getString(0);

                //********************* attributeGroups *********************//
                JSONObject attributgroup = styleobject.getJSONObject("attributeGroups");

                //**EXTERIOR DIMENSION//
                JSONObject Exteriordimension = attributgroup.getJSONObject("EXTERIOR_DIMENSIONS");
                JSONObject attribute = Exteriordimension.getJSONObject("attributes");
                Iterator iterator = attribute.keys();
                while (iterator.hasNext()) {

                    String key = (String) iterator.next();
                    JSONObject attrobject = attribute.getJSONObject(key);
                    String name = attrobject.getString("name");
                    String value = attrobject.getString("value");
                    Exterior_Dimension.put(name, value);

                }

                //**STEERING//
                JSONObject STEERING = attributgroup.getJSONObject("STEERING");

                JSONObject STEERING_attributes = STEERING.getJSONObject("attributes");
                JSONObject POWER_STEERING = STEERING_attributes.getJSONObject("POWER_STEERING");
                Steering_object.put(POWER_STEERING.getString("name"), POWER_STEERING.getString("value"));

                //**SUSPENSION//
                JSONObject SUSPENSION = attributgroup.getJSONObject("SUSPENSION");

                JSONObject SUSPENSION_attributes = SUSPENSION.getJSONObject("attributes");
                Iterator iterator2 = SUSPENSION_attributes.keys();
                while (iterator2.hasNext()) {

                    String key = (String) iterator2.next();
                    JSONObject SUSPENSION_attrobject = SUSPENSION_attributes.getJSONObject(key);
                    String Suspensionname = SUSPENSION_attrobject.getString("name");
                    String Suspensiovalue = SUSPENSION_attrobject.getString("value");
                    Suspension_object.put(Suspensionname, Suspensiovalue);

                }

                //********************* FEATURE DATAS *********************//
                Featuer_Hole_Object = new JSONObject();
                //**MISC>_EXTERIOR//
                JSONObject Miscvalue = new JSONObject();
                if (attributgroup.has("MISC._EXTERIOR_FEATURES")) {
                    JSONObject MISC = attributgroup.getJSONObject("MISC._EXTERIOR_FEATURES");
                    String Misc_Title = MISC.getString("name");

                    JSONObject MISC_attributes = MISC.getJSONObject("attributes");
                    Iterator iterator3 = MISC_attributes.keys();
                    while (iterator3.hasNext()) {

                        String key = (String) iterator3.next();
                        JSONObject MISC_attrobject = MISC_attributes.getJSONObject(key);
                        String Suspensionname = MISC_attrobject.getString("name");
                        String Suspensiovalue = MISC_attrobject.getString("value");
                        Miscvalue.put(Suspensionname, Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(Misc_Title, Miscvalue);
                }

                //**CONSUMER_GENERIC_FEATURE//
                JSONObject Consumer = new JSONObject();
                if (attributgroup.has("CONSUMER_GENERIC_FEATURE")) {
                    JSONObject CONSUMER = attributgroup.getJSONObject("CONSUMER_GENERIC_FEATURE");
                    String CONSUMER_Title = CONSUMER.getString("name");

                    JSONObject CONSUMER_attributes = CONSUMER.getJSONObject("attributes");
                    Iterator iterator4 = CONSUMER_attributes.keys();
                    while (iterator4.hasNext()) {

                        String key = (String) iterator4.next();
                        JSONObject CONSUMER_attrobject = CONSUMER_attributes.getJSONObject(key);
                        String Suspensionname = CONSUMER_attrobject.getString("name");
                        String Suspensiovalue = CONSUMER_attrobject.getString("value");
                        Consumer.put(Suspensionname, Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(CONSUMER_Title, Consumer);
                }

                //**MISC._INTERIOR_FEATURES//
                JSONObject INTERIOR = new JSONObject();
                if (attributgroup.has("MISC._INTERIOR_FEATURES")) {
                    JSONObject Misc_INTERIOR = attributgroup.getJSONObject("MISC._INTERIOR_FEATURES");
                    String INTERIOR_Title = Misc_INTERIOR.getString("name");

                    JSONObject INTERIOR_attributes = Misc_INTERIOR.getJSONObject("attributes");
                    Iterator iterator5 = INTERIOR_attributes.keys();
                    while (iterator5.hasNext()) {

                        String key = (String) iterator5.next();
                        JSONObject INTERIORattrobject = INTERIOR_attributes.getJSONObject(key);
                        String Suspensionname = INTERIORattrobject.getString("name");
                        String Suspensiovalue = INTERIORattrobject.getString("value");
                        INTERIOR.put(Suspensionname, Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(INTERIOR_Title, INTERIOR);
                }

                //**AIR_CONDITIONING//
                JSONObject AIR_CONDITIONING = new JSONObject();
                if (attributgroup.has("AIR_CONDITIONING")) {
                    JSONObject Air_con = attributgroup.getJSONObject("AIR_CONDITIONING");
                    String AIR_Title = Air_con.getString("name");

                    JSONObject Air_attributes = Air_con.getJSONObject("attributes");
                    Iterator iterator6 = Air_attributes.keys();
                    while (iterator6.hasNext()) {

                        String key = (String) iterator6.next();
                        JSONObject Air_attrobject = Air_attributes.getJSONObject(key);
                        String Suspensionname = Air_attrobject.getString("name");
                        String Suspensiovalue = Air_attrobject.getString("value");
                        AIR_CONDITIONING.put(Suspensionname, Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(AIR_Title, AIR_CONDITIONING);
                }

                //**STORAGE//
                JSONObject STORAGE = new JSONObject();
                if (attributgroup.has("STORAGE")) {
                    JSONObject STORAGE_con = attributgroup.getJSONObject("STORAGE");
                    String STORAGE_Title = STORAGE_con.getString("name");

                    JSONObject STORAGE_attributes = STORAGE_con.getJSONObject("attributes");
                    Iterator iterator7 = STORAGE_attributes.keys();
                    while (iterator7.hasNext()) {

                        String key = (String) iterator7.next();
                        JSONObject STORAGE_attrobject = STORAGE_attributes.getJSONObject(key);
                        String Suspensionname = STORAGE_attrobject.getString("name");
                        String Suspensiovalue = STORAGE_attrobject.getString("value");
                        STORAGE.put(Suspensionname, Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(STORAGE_Title, STORAGE);
                }

                //**MOBILE_CONNECTIVITY//
                JSONObject MOBILE_CONNECTIVITY = new JSONObject();
                if (attributgroup.has("MOBILE_CONNECTIVITY")) {
                    JSONObject MOBILE_con = attributgroup.getJSONObject("MOBILE_CONNECTIVITY");
                    String MOBILE_Title = MOBILE_con.getString("name");

                    JSONObject MOBILE_attributes = MOBILE_con.getJSONObject("attributes");
                    Iterator iterator8 = MOBILE_attributes.keys();
                    while (iterator8.hasNext()) {

                        String key = (String) iterator8.next();
                        JSONObject MOBILE_attrobject = MOBILE_attributes.getJSONObject(key);
                        String Suspensionname = MOBILE_attrobject.getString("name");
                        String Suspensiovalue = MOBILE_attrobject.getString("value");
                        MOBILE_CONNECTIVITY.put(Suspensionname, Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(MOBILE_Title, MOBILE_CONNECTIVITY);
                }

                //**EXTERIOR_LIGHTS//
                JSONObject EXTERIOR_LIGHTS = new JSONObject();
                if (attributgroup.has("EXTERIOR_LIGHTS")) {
                    JSONObject EXTERIOR_con = attributgroup.getJSONObject("EXTERIOR_LIGHTS");
                    String EXTERIOR_Title = EXTERIOR_con.getString("name");

                    JSONObject EXTERIOR_attributes = EXTERIOR_con.getJSONObject("attributes");
                    Iterator iterator9 = EXTERIOR_attributes.keys();
                    while (iterator9.hasNext()) {

                        String key = (String) iterator9.next();
                        JSONObject EXTERIOR_attrobject = EXTERIOR_attributes.getJSONObject(key);
                        String Suspensionname = EXTERIOR_attrobject.getString("name");
                        String Suspensiovalue = EXTERIOR_attrobject.getString("value");
                        EXTERIOR_LIGHTS.put(Suspensionname, Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(EXTERIOR_Title, EXTERIOR_LIGHTS);
                }

                //**POWER_OUTLETS//
                JSONObject POWER_OUTLETS = new JSONObject();
                if (attributgroup.has("POWER_OUTLETS")) {
                    JSONObject POWER_con = attributgroup.getJSONObject("POWER_OUTLETS");
                    String POWER_Title = POWER_con.getString("name");

                    JSONObject POWER_attributes = POWER_con.getJSONObject("attributes");
                    Iterator iterator10 = POWER_attributes.keys();
                    while (iterator10.hasNext()) {

                        String key = (String) iterator10.next();
                        JSONObject POWER_attrobject = POWER_attributes.getJSONObject(key);
                        String Suspensionname = POWER_attrobject.getString("name");
                        String Suspensiovalue = POWER_attrobject.getString("value");
                        POWER_OUTLETS.put(Suspensionname, Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(POWER_Title, POWER_OUTLETS);
                }

                //**SECURITY//
                JSONObject SECURITY = new JSONObject();
                if (attributgroup.has("SECURITY")) {
                    JSONObject SECURITY_con = attributgroup.getJSONObject("SECURITY");
                    String SECURITY_Title = SECURITY_con.getString("name");

                    JSONObject SECURITY_attributes = SECURITY_con.getJSONObject("attributes");
                    Iterator iterator11 = SECURITY_attributes.keys();
                    while (iterator11.hasNext()) {

                        String key = (String) iterator11.next();
                        JSONObject SECURITY_attrobject = SECURITY_attributes.getJSONObject(key);
                        String Suspensionname = SECURITY_attrobject.getString("name");
                        String Suspensiovalue = SECURITY_attrobject.getString("value");
                        SECURITY.put(Suspensionname, Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(SECURITY_Title, SECURITY);
                }

                //**INSTRUMENTATION//
                JSONObject INSTRUMENTATION = new JSONObject();
                if (attributgroup.has("INSTRUMENTATION")) {
                    JSONObject INSTRUMENTATION_con = attributgroup.getJSONObject("INSTRUMENTATION");
                    String INSTRUMENTATION_Title = INSTRUMENTATION_con.getString("name");

                    JSONObject INSTRUMENTATION_attributes = INSTRUMENTATION_con.getJSONObject("attributes");
                    Iterator iterator12 = INSTRUMENTATION_attributes.keys();
                    while (iterator12.hasNext()) {

                        String key = (String) iterator12.next();
                        JSONObject INSTRUMENTATION_attrobject = INSTRUMENTATION_attributes.getJSONObject(key);
                        String Suspensionname = INSTRUMENTATION_attrobject.getString("name");
                        String Suspensiovalue = INSTRUMENTATION_attrobject.getString("value");
                        INSTRUMENTATION.put(Suspensionname, Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(INSTRUMENTATION_Title, INSTRUMENTATION);
                }

                //**SEATING_CONFIGURATION//
                JSONObject SEATING_CONFIGURATION = new JSONObject();
                if (attributgroup.has("SEATING_CONFIGURATION")) {
                    JSONObject SEATING_con = attributgroup.getJSONObject("SEATING_CONFIGURATION");
                    String SEATING_Title = SEATING_con.getString("name");

                    JSONObject SEATING_attributes = SEATING_con.getJSONObject("attributes");
                    Iterator iterator13 = SEATING_attributes.keys();
                    while (iterator13.hasNext()) {

                        String key = (String) iterator13.next();
                        JSONObject SEATING_attrobject = SEATING_attributes.getJSONObject(key);
                        String Suspensionname = SEATING_attrobject.getString("name");
                        String Suspensiovalue = SEATING_attrobject.getString("value");
                        SEATING_CONFIGURATION.put(Suspensionname, Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(SEATING_Title, SEATING_CONFIGURATION);
                }

                //**WINDOWS//
                JSONObject WINDOWS = new JSONObject();
                if (attributgroup.has("WINDOWS")) {
                    JSONObject WINDOWS_con = attributgroup.getJSONObject("WINDOWS");
                    String WINDOWS_Title = WINDOWS_con.getString("name");

                    JSONObject WINDOWS_attributes = WINDOWS_con.getJSONObject("attributes");
                    Iterator iterator14 = WINDOWS_attributes.keys();
                    while (iterator14.hasNext()) {

                        String key = (String) iterator14.next();
                        JSONObject WINDOWS_attrobject = WINDOWS_attributes.getJSONObject(key);
                        String Suspensionname = WINDOWS_attrobject.getString("name");
                        String Suspensiovalue = WINDOWS_attrobject.getString("value");
                        WINDOWS.put(Suspensionname, Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(WINDOWS_Title, WINDOWS);
                }

                Log.e("Featuers", Featuer_Hole_Object.toString());
            }

            if (Trans_type != null) {
                Trans_ID.setText(Trans_type);
            }

            MakeModel_ID.setText(Make + " " + Model + " " + Trim + " " + Year);
            ratingData(MakeNiceName, Model, Year);
            createJson();
            requestSquashVin(Squashid);
        }
        }catch (JSONException e) {

            e.printStackTrace();
        }

    }

    private void ratingData(String make,String model,String year){

        Map<String, String> params = new HashMap<String, String>();
        String Replacemodel = model.toLowerCase().replace(" ","-");
        Log.e("ReplaceModel",Replacemodel);
        String URL = Constants.Edmundsrating_Detail+make+"/"+Replacemodel+"/"+year+"?"+Constants.Edmunds_Key;

        VolleyCustomPostRequest jsObjRequest = new VolleyCustomPostRequest(URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                // TODO Auto-generated method stub

                ratingRespnseData(response.toString());


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

                NetworkResponse response = error.networkResponse;
                Log.e("Error", error.toString());


                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO+
                } else if (error instanceof ParseError) {
                    //TODO
                }
            }
        });

        //jsObjRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsObjRequest, "tag_json_obj");

    }

    private void ratingRespnseData(String specResponse) {

        JSONObject json = null ;
        try {
            if (!specResponse.equals(null)){
                json = new JSONObject(specResponse);

            String rating = json.getString("averageRating");
            //float data= json.getString("averageRating");

            if (rating != null) {

                Rating_Text.setText(rating);
                ratingbar.setRating(Float.parseFloat(rating));
                LayerDrawable stars = (LayerDrawable) ratingbar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                RatingLayout.setVisibility(View.VISIBLE);

            }
        }
        }catch (JSONException e) {

            e.printStackTrace();
        }

    }

    private void createJson() throws JSONException {

        Basic = new JSONObject();
        try {

            Basic.put("Make",Make);
            Basic.put("Model",Model);
            Basic.put("Trim",Trim);
            Basic.put("Year",Year);


        }catch (JSONException e){
            e.printStackTrace();
        }

        Overall_info = new JSONObject();
        try {

            Overall_info.put("Exterior Color",Exterior);
            Overall_info.put("Interior Color",Interior);
            Overall_info.put("Fuel type",Fueltype);
            Overall_info.put("Transmission type",Trans_type);
            Overall_info.put("Body type",Bodytype);
            Overall_info.put("Engine type",Enginetype);
            Overall_info.put("Engine compressor type",Enginecompressor);

        }catch (JSONException e){
            e.printStackTrace();
        }

        Seller_info = new JSONObject();
        try {

            Seller_info.put("Location",Location);
            Seller_info.put("Registration number","Not available");
            Seller_info.put("Registration state","Not available");
            if(!Mileage.equalsIgnoreCase("0")){
                     Seller_info.put("Miles driven",Mileage);
            }else {
                Seller_info.put("Miles driven","Call for Mileage");
            }

            Seller_info.put("Seller type","Dealer");
            Seller_info.put("Number of owners",1);
            Seller_info.put("Condition",Condition);
            Seller_info.put("Chassis number","Not available");


        }catch (Exception e){
            e.printStackTrace();
        }

        Basic_Hole_Object = new JSONObject();
        Basic_Hole_Object.put("Basic", Basic);
        Basic_Hole_Object.put("Overall_info",Overall_info);
        Basic_Hole_Object.put("Seller_info",Seller_info);
        Log.e("Info",Basic_Hole_Object.toString());
        Basicbutton.setBackgroundColor(Color.DKGRAY);
        Basic_Text.setTextColor(Color.WHITE);
        TablelayoutAdapter = new TableLayoutAdapter(context,Basic_Hole_Object,"CarDetail");
        TablelayoutRecycler.setAdapter(TablelayoutAdapter);


    }

    private void requestSquashVin(String squashid) {

        Map<String, String> params = new HashMap<String, String>();
        String URL = Constants.Edmundssquashvin_Detail+squashid+"?"+Constants.Edmunds_Key;
       // Log.e("URL",URL);

        VolleyCustomPostRequest jsObjRequest = new VolleyCustomPostRequest(URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                // TODO Auto-generated method stub
                parseSquashRespnseData(response.toString());

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

                NetworkResponse response = error.networkResponse;
                Log.e("Squash Error", error.toString());


                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO+
                } else if (error instanceof ParseError) {
                    //TODO
                }
            }
        });

        //jsObjRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsObjRequest, "tag_json_obj");

    }

    private void parseSquashRespnseData(String response) {

        JSONObject json = null ;
        try {
            if (!response.equals(null)){
                json = new JSONObject(response);
            //********************* Engine *********************//
            JSONObject Engine = json.getJSONObject("engine");
            String power = Engine.getString("horsepower");
            String torgue = Engine.getString("torque");
            String displacement = Engine.getString("displacement");
            String cylinders = Engine.getString("cylinder");
            String valves = Engine.getString("totalValves");
            JSONObject rpm = Engine.getJSONObject("rpm");
            String horsepower = rpm.getString("horsepower");
            String torguepower = rpm.getString("torque");

            //Engine_object = new JSONObject();
            JSONObject EngineObjects = new JSONObject();
            EngineObjects.put("Engine power", power + " @ RPM " + horsepower);
            EngineObjects.put("Engine torgue", torgue + " @ RPM " + torguepower);
            EngineObjects.put("Engine displacement", displacement);
            EngineObjects.put("No.of cylinders", cylinders);
            EngineObjects.put("No.of valves", valves);

            //********************* Transmission *********************//
            JSONObject Trans = json.getJSONObject("transmission");
            String transtype = Trans.getString("transmissionType");
            String gears = Trans.getString("numberOfSpeeds");

            //Trans_object = new JSONObject();
            JSONObject Transmission = new JSONObject();
            Transmission.put("Transmission type", transtype);
            Transmission.put("No.of Gears", gears);

            Spec_Hole_Object = new JSONObject();
            Spec_Hole_Object.put("Exterior_Dimension", Exterior_Dimension);
            Spec_Hole_Object.put("Engine", EngineObjects);
            Spec_Hole_Object.put("Transmission", Transmission);
            Spec_Hole_Object.put("Steering", Steering_object);
            Spec_Hole_Object.put("Suspension", Suspension_object);
            Log.e("Specificatio", Spec_Hole_Object.toString());

        }
        }catch (JSONException e) {

            e.printStackTrace();
        }

    }

    public void loadData(final String vin, final String dealerID){

        int socketTimeout = 30000; // 30 seconds. You can change it
       RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);



        Map<String, String> params = new HashMap<String, String>();
        params.put("VIN", vin);
        //Log.e("Thump_VIN",vin);
        params.put("Dealer_ID", dealerID);


        VolleyCustomPostRequest jsObjRequest = new VolleyCustomPostRequest(Request.Method.POST, Constants.cardetail_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                // TODO Auto-generated method stub
                String  respo = response.toString();
                storeResponsedata(respo);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

                NetworkResponse response = error.networkResponse;
                Log.e("Error", error.toString());

                Emptyview.setVisibility(View.VISIBLE);
                thump_image.setVisibility(View.GONE);
                Loading.setVisibility(View.GONE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO+
                } else if (error instanceof ParseError) {
                    //TODO
                }
            }
        });

        jsObjRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsObjRequest, "tag_json_obj");

    }

    public void storeResponsedata(String response){

        JSONObject json = null, SellingPrice = null, UploadCars=null, TrendingCars=null;

        try {
            if (!response.equals(null)){
                json = new JSONObject(response);
            if (json.has("Thumb_Image_URL_Pattern")) {
                JSONArray sellingarray = json.getJSONArray("Thumb_Image_URL_Pattern");

                Log.e("thump_Image", String.valueOf(sellingarray.length()));
                for (int index = 0; index < sellingarray.length(); index++) {
                    Thumpimages.add(sellingarray.getString(index));
                }

                if (sellingarray.length() < 1) {

                    Emptyview.setVisibility(View.VISIBLE);
                    thump_image.setVisibility(View.GONE);
                    Loading.setVisibility(View.GONE);


                } else {

                    Thumplist = new ThumpImageAdapter(context, Thumpimages, bannerimage);
                    thump_image.setAdapter(Thumplist);
                    Emptyview.setVisibility(View.GONE);
                    thump_image.setVisibility(View.VISIBLE);
                    Loading.setVisibility(View.GONE);
                }

            } else {

                Log.e("Response", "No Data");
                Emptyview.setVisibility(View.VISIBLE);
                thump_image.setVisibility(View.GONE);
                Loading.setVisibility(View.GONE);
            }
        }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.Basic_Button:
                Basicbutton.setBackgroundColor(Color.DKGRAY);
                Specbutton.setBackground(getResources().getDrawable(R.drawable.border_radius));
                Featurebutton.setBackground(getResources().getDrawable(R.drawable.border_radius));
                Basic_Text.setTextColor(Color.WHITE);
                Specification_Text.setTextColor(Color.GRAY);
                Feature_Text.setTextColor(Color.GRAY);
                TablelayoutAdapter = new TableLayoutAdapter(context,Basic_Hole_Object,"CarDetail");
                TablelayoutRecycler.setAdapter(TablelayoutAdapter);
                break;
            case R.id.Spec_Button:
                Basicbutton.setBackground(getResources().getDrawable(R.drawable.border_radius));
                Specbutton.setBackgroundColor(Color.DKGRAY);
                Featurebutton.setBackground(getResources().getDrawable(R.drawable.border_radius));
                Basic_Text.setTextColor(Color.GRAY);
                Specification_Text.setTextColor(Color.WHITE);
                Feature_Text.setTextColor(Color.GRAY);
                TablelayoutAdapter = new TableLayoutAdapter(context,Spec_Hole_Object,"CarDetail");
                TablelayoutRecycler.setAdapter(TablelayoutAdapter);
                break;
            case R.id.Feature_Button:
                Basicbutton.setBackground(getResources().getDrawable(R.drawable.border_radius));
                Specbutton.setBackground(getResources().getDrawable(R.drawable.border_radius));
                Featurebutton.setBackgroundColor(Color.DKGRAY);
                Basic_Text.setTextColor(Color.GRAY);
                Specification_Text.setTextColor(Color.GRAY);
                Feature_Text.setTextColor(Color.WHITE);
                TablelayoutAdapter = new TableLayoutAdapter(context,Featuer_Hole_Object,"CarDetail");
                TablelayoutRecycler.setAdapter(TablelayoutAdapter);
                break;
            case R.id.Comparecar_Button:
                Intent intent1= new Intent(getApplicationContext(), CarcompareActivity.class);
                intent1.putExtra("Make",Make);
                intent1.putExtra("Model",Model);
                intent1.putExtra("Year",Year);
                intent1.putExtra("Bannerimage",BannerImage);
                intent1.putExtra("Vin",VIN);
                intent1.putExtra("Exterior",Exterior);
                intent1.putExtra("Interior",Interior);
                context.startActivity(intent1);
                ((Activity) context).overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.Dealerdetail_Button:
                Intent intent= new Intent(getApplicationContext(), DealerDetails.class);
                intent.putExtra("Dealer_ID",DealerID);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;

            case R.id.favorate_icon:
                favorate.setVisibility(View.GONE);
                favorate1.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(),"Favorate added",Toast.LENGTH_SHORT).show();
                favorite_vin.clear();
                Cursor c = db.fetchQuery("SELECT * FROM myfavorites");
                int count = c.getCount();
                if (count > 0) {
                    while (c.moveToNext()) {
                        vinid = c.getString(c.getColumnIndex("VIN"));
                        favorite_vin.add(vinid);
                    }
                }
                Log.e("Array vin", String.valueOf(favorite_vin));
                boolean isStringExists = favorite_vin.contains(VIN);
                if (!isStringExists == true) {
                    showChangeLangDialog("","Car added to favorites");
                   // Toast.makeText(context, "Car added to favorites", Toast.LENGTH_SHORT).show();

                    ContentValues cv_make = new ContentValues();
                    cv_make.put("VIN", VIN);
                    cv_make.put("Dealer_ID",DealerID);
                    cv_make.put("Make", Make);
                    cv_make.put("Model", Model);
                    cv_make.put("Year", Year);
                    cv_make.put("List_Price", Price);
                     cv_make.put("Mileage", Mileage);
                    cv_make.put("image_url_pattern", BannerImage);
                    cv_make.put("exterior", Exterior);
                    cv_make.put("interior", Interior);
                    cv_make.put("Condition", Condition);
                    cv_make.put("Location", Location);
                    db.insert(cv_make, "myfavorites");

                }
                break;

            case R.id.favorate_icon1:
                favorate.setVisibility(View.VISIBLE);
                favorate1.setVisibility(View.GONE);
                //Toast.makeText(getApplicationContext(),"Favorate removed",Toast.LENGTH_SHORT).show();
                db.deleteQuery("DELETE FROM myfavorites WHERE VIN='" + VIN + "'");
                //Toast.makeText(context, "Car removed from favorites", Toast.LENGTH_SHORT).show();
                showChangeLangDialog("","Car removed from favorites");
                break;

            default:
                break;
        }
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
