package bss.bbs.com.teslaclone.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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
//import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bss.bbs.com.teslaclone.Adapter.CustomFavCountryListAdapter;
import bss.bbs.com.teslaclone.Adapter.EndlessScrollListener;
import bss.bbs.com.teslaclone.Adapter.FilterDetailAdapter;
import bss.bbs.com.teslaclone.R;
import bss.bbs.com.teslaclone.Singleton.AppController;
import bss.bbs.com.teslaclone.Singleton.Constants;
import bss.bbs.com.teslaclone.Singleton.RangeSeekBar;
import bss.bbs.com.teslaclone.SplashScreen;
import bss.bbs.com.teslaclone.network.VolleyCustomPostRequest;
import bss.bbs.com.teslaclone.utils.OnDialogListClickListener;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.String.valueOf;



@SuppressLint("ValidFragment")
public class FilterFragment extends Fragment implements View.OnClickListener,LocationListener{

    private View parentView;
    Context context;
    RecyclerView recyclerView,makeRecycler,modelRecycler,priceRecycler,conditionRecycler,transmissionRecycler,trimRecycler,fuelRecycler,driveRecycler;
    RelativeLayout filterbutton,sortbutton;
    ArrayList<String> Make = new ArrayList<>();
    ArrayList<String> Model = new ArrayList<>();
    ArrayList<String> Year = new ArrayList<>();
    ArrayList<String> Price = new ArrayList<>();
    ArrayList<Integer> Banner_Image = new ArrayList<>();
   // RecyclerView.Adapter FilterAdapter;
    FilterDetailAdapter FilterAdapter;
    SharedPreferences pref;
    private EndlessScrollListener scrollListener;

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

    TextView MakeText,ModelText,YearText,PriceText,ConditionText,TransmissionText,MileageText,TrimText,FuelText,DriveText,year_Textview,ZipcodeText;
    Button cancel,search,submit,reset,zipsubmit,zipreset,internetbtn;
    EditText minEdit,maxEdit,zipEdit,radiusEdit;
    String Makestring = "All",Modelstring = "All",Yearstring = "All",Pricestring = "All",MinYear,MaxYear,MinMileage = "",MaxMileage = "",Sortlistdata="",Latitude="",Longitude="",Radius="",Zipcodedata="",Radiusdata="",Conditonstring="All",Tranmissionstring="All",Fuelstring="All",Drivestring="",Trimstring="All";
    ArrayList<String> Makefilterlist = new ArrayList<>();
    ArrayList<String> Modelfilterlist = new ArrayList<>();
    ArrayList<String> Yearfilterlist = new ArrayList<>();
    ArrayList<String> Pricefilterlist = new ArrayList<>();
    ArrayList<String> Conditionfilterlist = new ArrayList<>();
    ArrayList<String> Trimfilterlist = new ArrayList<>();
    ArrayList<String> Transmissionfilterlist = new ArrayList<>();
    ArrayList<String> Fuelfilterlist = new ArrayList<>();
    ArrayList<String> Drivefilterlist = new ArrayList<>();
    Dialog dialog;
    double latitude,longitude;

    LinearLayoutManager layoutManager;
    LinearLayout internetconn,loading,nodatafound,yearLayout,mileageLayout,Pricelow,Pricehigh,Yearold,Yearnew,Milesmin,Milesmax,zipcodeLayout;
    CustomFavCountryListAdapter customFavCountryListAdapter;
    RangeSeekBar rangeSeekBar;
    ImageView Pricelowimage,Pricehighimage,Yearoldimage,Yearnewimage,Milesminimage,Milesmaximage;
    TextView Pricelowtext,Pricehightext,Yearoldtext,Yearnewtext,Milesmintext,Milesmaxtext;

    private LocationManager locationManager;
    private Location location;
    private final int REQUEST_LOCATION = 200;
    private static final String TAG = "GPS";
    int currentCarCount = 0;
   // int totalcount=30;



    public static final String[] YEARS = new String[]{
            "1970","1971","1972","1973","1974","1975","1976","1977","1978","1979",
            "1980","1981","1982","1983","1984","1985","1986","1987","1988","1989",
            "1990","1991","1992","1993","1994","1995","1996","1997","1998","1999",
            "2000","2001","2002","2003","2004","2005","2006","2007","2008","2009",
            "2010","2011","2012","2013","2014","2015","2016","2017","2018"};

    public static final String[] PRICE = new String[]{
            "All",
            "Below $5,000","$1-$5,999",
            "$6,000-$6,999", "$7,000-$7,999","$8,000-$8,999","$9,000-$9,999",
            "$10,000-$15,999","$16,000-$19,999","$20,000-$29,999","$30,000-$39,999",
            "$40,000-$49,999","$50,000-$74,999","$75,000-$99,999","Over $100,000"
    };

    public static final String[] CONDTION = new String[]{
            "All","New","Used"};
    public static final String[] TRANSMISSION = new String[]{
            "All","Automatic","Manual"};

    public FilterFragment(Context con) {
        context = con;
        pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.filter, container, false);
        TextView titleview = (TextView) getActivity().findViewById(R.id.titleid);
        titleview.setText("Car Filter");
       locationManager = (LocationManager)getActivity().getSystemService(Service.LOCATION_SERVICE);
//        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 2, this);
            if (locationManager != null) {
                // gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (location != null) {
                updateUI(location);
            }
        }
        edmundsData(AppController.getInstance().EdmundsResponse);
        initViews(parentView);
        setUpViews(parentView);
        return parentView;
    }

    private void setUpViews(View view) {

        Make = new ArrayList<>(Arrays.asList("BMW", "Acura"));
        Model = new ArrayList<>(Arrays.asList("235i", "ILX"));
        Year = new ArrayList<>(Arrays.asList("2000", "2005"));
        Price = new ArrayList<>(Arrays.asList("$4000", "$5000"));
        Banner_Image = new ArrayList<>(Arrays.asList(R.drawable.list_car4, R.drawable.list_car3));
        filterbutton = (RelativeLayout) view.findViewById(R.id.filter_button);
        sortbutton = (RelativeLayout) view.findViewById(R.id.sort_button);
        loading = (LinearLayout) view.findViewById(R.id.Loading);
        nodatafound = (LinearLayout) view.findViewById(R.id.Nodata);
        internetconn = (LinearLayout) view.findViewById(R.id.internetconnect);
        internetbtn=(Button) view.findViewById(R.id.internetbtn);
        internetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadData(currentCarCount);
            }
        });

        filterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterPopupWindow(context);
            }
        });
        sortbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortPopupWindow(context);
            }
        });

            Bundle args = getArguments();
            if(args != null) {
                double Lat = args.getDouble("Lat");
                Latitude = valueOf(Lat);
                double Long = args.getDouble("Long");
                Longitude = valueOf(Long);
                int Radiusdata = args.getInt("radius");
                Radius = valueOf(Radiusdata);
            }

        loadData(currentCarCount);

    }

    private void initViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.filter_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

    }

    public void loadData(final int currentCarCount) {


        if(currentCarCount==0) {
            recyclerView.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
            nodatafound.setVisibility(View.GONE);
            internetconn.setVisibility(View.GONE);
        }

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        Map<String, String> params = new HashMap<String, String>();

        if( Latitude!=null && Latitude!=""){
            Log.e("lat",Latitude);
            params.put("latitude", Latitude);
        }else{
            params.put("latitude", "");
        }
        if( Longitude!=null && Longitude!="" ) {
            Log.e("lat",Longitude);
            params.put("longitude", Longitude);
        }else {
            params.put("longitude", "");
        }
        if( Radius!=null && Radius!=""){
            Log.e("lat",Radius);
            params.put("radius", Radius);
        }else {
            params.put("radius", "");
        }
        if(!Makestring.equals("All")){
            params.put("make", Makestring);
        }else {
            params.put("make", "");
            Log.e("Make", "");
        }

        if(!Modelstring.equals("All")){
            params.put("model", Modelstring);

        }else {
            params.put("model", "");
            Log.e("Model","");
        }

        if(!Yearstring.equals("All")){
            params.put("year", Yearstring);

        }else {
            params.put("year", "");
            Log.e("Year","");
        }

        if (MinYear !=null && MaxYear !=null){
            params.put("YearBetween", MinYear+"-"+MaxYear);
        }else {
            params.put("YearBetween", "");
        }

        if (MinMileage !="" && MaxMileage !=""){
            params.put("mileage_range", MinMileage+ "-" +MinMileage);
        }else {
            params.put("mileage_range", "");
        }

        if(!Pricestring.equals("All")){
            params.put("price", Pricestring);

        }else {
            params.put("price", "");
            Log.e("Price","");
        }
        if (Sortlistdata!=null && Sortlistdata !="" ){
            params.put("Sorting", Sortlistdata);
        }else {
            params.put("Sorting", "");
        }
        if(String.valueOf(currentCarCount)!="" && String.valueOf(currentCarCount)!=null){
            params.put("count", String.valueOf(currentCarCount));
        }else {
            params.put("count", "");
        }


        VolleyCustomPostRequest jsObjRequest = new VolleyCustomPostRequest(Request.Method.POST,Constants.carfilter_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                // TODO Auto-generated method stub
                    Log.e("Response", response.toString());
                   if(currentCarCount==0) {
                        storeResponsedata(response.toString());
                    }else{
                       loadmorecar(response.toString());
                    }
                    recyclerView.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                    nodatafound.setVisibility(View.GONE);
                    internetconn.setVisibility(View.GONE);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

                NetworkResponse response = error.networkResponse;
                Log.e("Error", error.toString());


                if (error instanceof TimeoutError ) {

                    recyclerView.setVisibility(View.GONE);
                    nodatafound.setVisibility(View.GONE);
                    internetconn.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);

                } else if (error instanceof AuthFailureError) {
                    //TODO
                    recyclerView.setVisibility(View.GONE);
                    nodatafound.setVisibility(View.GONE);
                    internetconn.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                } else if (error instanceof ServerError) {
                    //TODO
                    recyclerView.setVisibility(View.GONE);
                    nodatafound.setVisibility(View.GONE);
                    internetconn.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                } else if (error instanceof NetworkError) {
                    //TODO+
                    recyclerView.setVisibility(View.GONE);
                    internetconn.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                    nodatafound.setVisibility(View.GONE);
                }
                else if (error instanceof ParseError) {
                    //TODO
                    if(currentCarCount==0){
                    recyclerView.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    internetconn.setVisibility(View.GONE);
                    nodatafound.setVisibility(View.VISIBLE);
                    if(!Latitude.equals(null)&& !Longitude.equals(null) && Makestring.equals("All")){
                        showGPSDisabledAlertToUser();
                    }else{
                        showChangeLangDialog("","No Data Found...");
                    }
                    }else{
                        recyclerView.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        internetconn.setVisibility(View.GONE);
                        nodatafound.setVisibility(View.GONE);
                    }
                }
            }
        });

        jsObjRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsObjRequest, "tag_json_obj");


    }

    public void storeResponsedata(String reponse){

        VIN.clear();
        bannerImageUrl.clear();
        makeList.clear();
        modelList.clear();
        yearList.clear();
        priceList.clear();
        milesList.clear();
        stockList.clear();
        Location.clear();
        DealerID.clear();
        ExteriorColor.clear();
        InteriorColor.clear();

        JSONObject json = null, SellingPrice = null, UploadCars=null, TrendingCars=null;

        try {
            if (!reponse.equals(null)){
                json = new JSONObject(reponse);

            if (json.has("Filter_cars")) {
                JSONArray sellingarray = json.getJSONArray("Filter_cars");

                Log.e("filter", String.valueOf(sellingarray.length()));
                for (int i = 0; i < sellingarray.length(); i++) {
                    try {
                        JSONObject jsonobject = sellingarray.getJSONObject(i);
                        VIN.add(jsonobject.getString("VIN"));
                        bannerImageUrl.add(jsonobject.getString("Banner_Image"));
                        makeList.add(jsonobject.getString("Make"));
                        modelList.add(jsonobject.getString("Model"));
                        yearList.add(jsonobject.getString("Year"));
                        priceList.add(jsonobject.getString("List_Price"));
                        milesList.add(jsonobject.getString("Mileage"));
                        stockList.add(jsonobject.getString("Stock_Type"));
                        Location.add(jsonobject.getString("City"));
                        DealerID.add(jsonobject.getString("Dealer_ID"));
                        ExteriorColor.add(jsonobject.getString("Exterior_Color"));
                        InteriorColor.add(jsonobject.getString("Interior_Color"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                FilterAdapter = new FilterDetailAdapter(context, VIN, makeList, modelList, yearList, priceList, bannerImageUrl, Location, DealerID, milesList, stockList, ExteriorColor, InteriorColor, recyclerView);
                recyclerView.setAdapter(FilterAdapter);
                recyclerView.addOnScrollListener(new EndlessScrollListener(layoutManager) {
                    @Override
                    public void onLoadMore(int current_page) {

                        bannerImageUrl.add(null);
                        VIN.add(null);
                        bannerImageUrl.remove(bannerImageUrl.size() - 1);
                        VIN.remove(VIN.size() - 1);
                        FilterAdapter.notifyItemRemoved(bannerImageUrl.size());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                currentCarCount = currentCarCount + 30;
                                loadData(currentCarCount);
                            }
                        }, 100);
                    }
                });
            }
        }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void loadmorecar(String reponse){

        JSONObject json = null, SellingPrice = null, UploadCars=null, TrendingCars=null;

        try {
            if (!reponse.equals(null)) {
                json = new JSONObject(reponse);

                if (json.has("Filter_cars")) {
                    JSONArray sellingarray = json.getJSONArray("Filter_cars");
                    VIN.remove(VIN.size() - 1);
                    FilterAdapter.notifyItemRemoved(VIN.size());
                    Log.e("filter", String.valueOf(sellingarray.length()));
                    for (int i = 0; i < sellingarray.length(); i++) {
                        try {
                            JSONObject jsonobject = sellingarray.getJSONObject(i);

                            VIN.add(jsonobject.getString("VIN"));
                            bannerImageUrl.add(jsonobject.getString("Banner_Image"));
                            makeList.add(jsonobject.getString("Make"));
                            modelList.add(jsonobject.getString("Model"));
                            yearList.add(jsonobject.getString("Year"));
                            priceList.add(jsonobject.getString("List_Price"));
                            milesList.add(jsonobject.getString("Mileage"));
                            stockList.add(jsonobject.getString("Stock_Type"));
                            Location.add(jsonobject.getString("City"));
                            DealerID.add(jsonobject.getString("Dealer_ID"));
                            ExteriorColor.add(jsonobject.getString("Exterior_Color"));
                            InteriorColor.add(jsonobject.getString("Interior_Color"));
                            FilterAdapter.notifyItemInserted(VIN.size());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    FilterAdapter.setLoaded();

                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void filterPopupWindow(Context con) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filterlist_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        MakeText = (TextView) dialog.findViewById(R.id.Make_id);
        ModelText = (TextView) dialog.findViewById(R.id.Model_id);
        YearText = (TextView) dialog.findViewById(R.id.Year_id);
        PriceText = (TextView) dialog.findViewById(R.id.Price_id);
        ConditionText = (TextView) dialog.findViewById(R.id.Condition_id);
        TransmissionText = (TextView) dialog.findViewById(R.id.Transmission_id);
        MileageText = (TextView) dialog.findViewById(R.id.Mileage_id);
        TrimText = (TextView) dialog.findViewById(R.id.Trim_id);
        FuelText = (TextView) dialog.findViewById(R.id.Fuel_id);
        DriveText = (TextView) dialog.findViewById(R.id.Drivetrain_id);
        ZipcodeText = (TextView) dialog.findViewById(R.id.zipcode_id);
        cancel = (Button) dialog.findViewById(R.id.cancel_id);
        search = (Button) dialog.findViewById(R.id.search_id);
        makeRecycler = (RecyclerView) dialog.findViewById(R.id.makelist);
        modelRecycler = (RecyclerView) dialog.findViewById(R.id.modellist);
        yearLayout = (LinearLayout) dialog.findViewById(R.id.year_layout);
        priceRecycler = (RecyclerView) dialog.findViewById(R.id.pricelist);
        conditionRecycler = (RecyclerView) dialog.findViewById(R.id.conditionlist);
        transmissionRecycler = (RecyclerView) dialog.findViewById(R.id.transmissionlist);
        mileageLayout = (LinearLayout) dialog.findViewById(R.id.mileage_layout);
        trimRecycler = (RecyclerView) dialog.findViewById(R.id.trimlist);
        fuelRecycler = (RecyclerView) dialog.findViewById(R.id.fuellist);
        driveRecycler = (RecyclerView) dialog.findViewById(R.id.drivetrainlist);
        year_Textview = (TextView) dialog.findViewById(R.id.year_textview);
        minEdit = (EditText) dialog.findViewById(R.id.min_mileage);
        maxEdit = (EditText) dialog.findViewById(R.id.max_mileage);
        zipcodeLayout = (LinearLayout) dialog.findViewById(R.id.zipcode_layout);
        zipEdit = (EditText) dialog.findViewById(R.id.zipcodesearch);
        radiusEdit = (EditText) dialog.findViewById(R.id.radiussearch);
        submit = (Button) dialog.findViewById(R.id.submit);
        reset = (Button) dialog.findViewById(R.id.reset);
        zipsubmit = (Button) dialog.findViewById(R.id.zipsubmit);
        zipreset = (Button) dialog.findViewById(R.id.zipreset);

        MakeText.setOnClickListener(this);
        ModelText.setOnClickListener(this);
        YearText.setOnClickListener(this);
        PriceText.setOnClickListener(this);
        ConditionText.setOnClickListener(this);
        TransmissionText.setOnClickListener(this);
        MileageText.setOnClickListener(this);
        TrimText.setOnClickListener(this);
        FuelText.setOnClickListener(this);
        DriveText.setOnClickListener(this);
        ZipcodeText.setOnClickListener(this);
        submit.setOnClickListener(this);
        reset.setOnClickListener(this);
        zipsubmit.setOnClickListener(this);
        zipreset.setOnClickListener(this);
        cancel.setOnClickListener(this);
        search.setOnClickListener(this);

        customFavCountryListAdapter = new CustomFavCountryListAdapter(Makefilterlist,makeRecycler, FilterFragment.this.getActivity(), onDialogListClickListener,Makestring);
        makeRecycler.setAdapter(customFavCountryListAdapter);
        makeRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        customFavCountryListAdapter.notifyDataSetChanged();

        rangeSeekBar = new RangeSeekBar<Integer>(getActivity());
        year_Textview.setText(Arrays.asList(YEARS).get(0) +" to "+Arrays.asList(YEARS).get(Arrays.asList(YEARS).size()-1));
        rangeSeekBar.setRangeValues(Integer.parseInt(Arrays.asList(YEARS).get(0)), Integer.parseInt(Arrays.asList(YEARS).get(Arrays.asList(YEARS).size()-1)));

        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                Log.e("CRC =>", String.valueOf(minValue+" , "+ maxValue));
                MinYear = String.valueOf(minValue);
                MaxYear = String.valueOf(maxValue);
            }
        });

        rangeSeekBar.setNotifyWhileDragging(true);
        yearLayout.addView(rangeSeekBar);

        dialog.show();

    }

   public void sortPopupWindow(Context con) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sortfilterlayout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

       Pricelowtext =(TextView)dialog.findViewById(R.id.pricelow_id);
       Pricehightext =(TextView)dialog.findViewById(R.id.pricehigh_id);
       Yearoldtext =(TextView)dialog.findViewById(R.id.Yearold_id);
       Yearnewtext =(TextView)dialog.findViewById(R.id.Yearnew_id);
       Milesmintext =(TextView)dialog.findViewById(R.id.milesmin_id);
       Milesmaxtext =(TextView)dialog.findViewById(R.id.milesmax_id);


       Pricelowimage=(ImageView)dialog.findViewById(R.id.pricelowimage);
       Pricehighimage=(ImageView)dialog.findViewById(R.id.pricehigh);
       Yearoldimage=(ImageView)dialog.findViewById(R.id.yearold);
       Yearnewimage=(ImageView)dialog.findViewById(R.id.yearnew);
       Milesminimage=(ImageView)dialog.findViewById(R.id.milesmin);
       Milesmaximage=(ImageView)dialog.findViewById(R.id.milesmax);

       Pricelow = (LinearLayout) dialog.findViewById(R.id.Pricelowtohigh);
       Pricehigh = (LinearLayout) dialog.findViewById(R.id.Pricehightolow);
       Yearold = (LinearLayout) dialog.findViewById(R.id.Yearoldtonew);
       Yearnew = (LinearLayout) dialog.findViewById(R.id.yearnewtolow);
       Milesmin = (LinearLayout) dialog.findViewById(R.id.Milesmintomax);
       Milesmax = (LinearLayout) dialog.findViewById(R.id.Milesmaxtomin);
       cancel = (Button) dialog.findViewById(R.id.cancel_id);
       search = (Button) dialog.findViewById(R.id.search_id);

       Pricelow.setOnClickListener(this);
       Pricehigh.setOnClickListener(this);
       Yearold.setOnClickListener(this);
       Yearnew.setOnClickListener(this);
       Milesmin.setOnClickListener(this);
       Milesmax.setOnClickListener(this);
       cancel.setOnClickListener(this);
       search.setOnClickListener(this);

       dialog.show();
    }

    //private String filtermake;
    private OnDialogListClickListener onDialogListClickListener = new OnDialogListClickListener () {
        @Override
        public void onItemClick(String makelist, RecyclerView recyclerView) {
            switch (recyclerView.getId()) {
                case R.id.makelist:
                    Makestring = makelist;
                    Modelstring="";
                    Pricestring="";
                    MinYear=null;
                    MaxYear=null;
                    MinMileage = "";
                    MaxMileage = "";
                    Sortlistdata="";
                    Conditonstring="All";
                    Tranmissionstring="All";
                    Fuelstring="All";
                    Drivestring="";
                    Trimstring="All";
                    break;
                case R.id.modellist:
                    Modelstring = makelist;
                    Log.e("Modelstring",Modelstring);
                    break;
                case R.id.pricelist:
                    Pricestring = makelist;
                    break;
            }

        }
    };
    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");

        Latitude = valueOf(loc.getLatitude());
        Longitude =valueOf(loc.getLongitude());
        Radius="100";
    }

    @Override
    public void onStop() {
        super.onStop();
        locationManager.removeUpdates(this);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.Make_id:
                customFavCountryListAdapter = new CustomFavCountryListAdapter(Makefilterlist,makeRecycler, FilterFragment.this.getActivity(), onDialogListClickListener,Makestring);
                makeRecycler.setAdapter(customFavCountryListAdapter);
                makeRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                customFavCountryListAdapter.notifyDataSetChanged();
                InputMethodManager imm3 = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(maxEdit.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                makeRecycler.setVisibility(View.VISIBLE);
                modelRecycler.setVisibility(View.GONE);
                yearLayout.setVisibility(View.GONE);
                priceRecycler.setVisibility(View.GONE);
                conditionRecycler.setVisibility(View.GONE);
                transmissionRecycler.setVisibility(View.GONE);
                mileageLayout.setVisibility(View.GONE);
                trimRecycler.setVisibility(View.GONE);
                fuelRecycler.setVisibility(View.GONE);
                driveRecycler.setVisibility(View.GONE);
                zipcodeLayout.setVisibility(View.GONE);
                MakeText.setBackgroundColor(Color.parseColor("#a9a9a9"));
                ModelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                YearText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                PriceText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ConditionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TransmissionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                MileageText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TrimText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                FuelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                DriveText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ZipcodeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.Model_id:
                Modelfilterlist.clear();
                if(Makestring.equals("All")){
                    Modelfilterlist.add("Select Make First");
                }else{
                    fillAllModel(Makestring);
                }

                customFavCountryListAdapter = new CustomFavCountryListAdapter(Modelfilterlist,modelRecycler, FilterFragment.this.getActivity(), onDialogListClickListener,Modelstring);
                modelRecycler.setAdapter(customFavCountryListAdapter);
                modelRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                customFavCountryListAdapter.notifyDataSetChanged();
                InputMethodManager imm4 = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm4.hideSoftInputFromWindow(maxEdit.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                modelRecycler.setVisibility(View.VISIBLE);
                makeRecycler.setVisibility(View.GONE);
                yearLayout.setVisibility(View.GONE);
                priceRecycler.setVisibility(View.GONE);
                conditionRecycler.setVisibility(View.GONE);
                transmissionRecycler.setVisibility(View.GONE);
                mileageLayout.setVisibility(View.GONE);
                trimRecycler.setVisibility(View.GONE);
                fuelRecycler.setVisibility(View.GONE);
                driveRecycler.setVisibility(View.GONE);
                zipcodeLayout.setVisibility(View.GONE);
                MakeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ModelText.setBackgroundColor(Color.parseColor("#a9a9a9"));
                YearText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                PriceText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ConditionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TransmissionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                MileageText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TrimText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                FuelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                DriveText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ZipcodeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.Year_id:
                Yearfilterlist.clear();
                if(Makestring.equals("All")){
                    if (Arrays.asList(YEARS) != null && !Arrays.asList(YEARS).isEmpty()) {

                        //Log.e("First and last",Arrays.asList(YEARS).get(0) +" , "+Arrays.asList(YEARS).get(Arrays.asList(YEARS).size()-1));
                        year_Textview.setText(Arrays.asList(YEARS).get(0) +" to "+Arrays.asList(YEARS).get(Arrays.asList(YEARS).size()-1));
                        rangeSeekBar.setRangeValues(Integer.parseInt(Arrays.asList(YEARS).get(0)), Integer.parseInt(Arrays.asList(YEARS).get(Arrays.asList(YEARS).size()-1)));
                    }

                }else{

                    if(Modelstring.equals("All")){
                        fillMakeYear(Makestring);
                        if (Yearfilterlist != null && !Yearfilterlist.isEmpty()) {
                            //Log.e("First and last",Yearfilterlist.get(0) +" , "+Yearfilterlist.get(Yearfilterlist.size()-1));
                            year_Textview.setText(Yearfilterlist.get(0) +" to "+Yearfilterlist.get(Yearfilterlist.size()-1));
                            rangeSeekBar.setRangeValues(Integer.parseInt(Yearfilterlist.get(0)), Integer.parseInt(Yearfilterlist.get(Yearfilterlist.size()-1)));
                        }

                    }else{
                        fillMakeModelYearear(Makestring,Modelstring);
                        if (Yearfilterlist != null && !Yearfilterlist.isEmpty()) {
                            //Log.e("First and last",Yearfilterlist.get(0) +" , "+Yearfilterlist.get(Yearfilterlist.size()-1));
                            year_Textview.setText(Yearfilterlist.get(0) +" to "+Yearfilterlist.get(Yearfilterlist.size()-1));
                            rangeSeekBar.setRangeValues(Integer.parseInt(Yearfilterlist.get(0)), Integer.parseInt(Yearfilterlist.get(Yearfilterlist.size()-1)));
                        }
                    }


                }
                InputMethodManager immManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                immManager.hideSoftInputFromWindow(maxEdit.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                makeRecycler.setVisibility(View.GONE);
                modelRecycler.setVisibility(View.GONE);
                yearLayout.setVisibility(View.VISIBLE);
                priceRecycler.setVisibility(View.GONE);
                conditionRecycler.setVisibility(View.GONE);
                transmissionRecycler.setVisibility(View.GONE);
                mileageLayout.setVisibility(View.GONE);
                trimRecycler.setVisibility(View.GONE);
                fuelRecycler.setVisibility(View.GONE);
                driveRecycler.setVisibility(View.GONE);
                zipcodeLayout.setVisibility(View.GONE);
                MakeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ModelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                YearText.setBackgroundColor(Color.parseColor("#a9a9a9"));
                PriceText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ConditionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TransmissionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                MileageText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TrimText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                FuelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                DriveText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ZipcodeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.Price_id:
                Pricefilterlist = new ArrayList<String>(Arrays.asList(PRICE));

                customFavCountryListAdapter = new CustomFavCountryListAdapter(Pricefilterlist,priceRecycler, FilterFragment.this.getActivity(), onDialogListClickListener,Pricestring);
                priceRecycler.setAdapter(customFavCountryListAdapter);
                priceRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                customFavCountryListAdapter.notifyDataSetChanged();
                InputMethodManager imm5 = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm5.hideSoftInputFromWindow(maxEdit.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                makeRecycler.setVisibility(View.GONE);
                modelRecycler.setVisibility(View.GONE);
                yearLayout.setVisibility(View.GONE);
                priceRecycler.setVisibility(View.VISIBLE);
                conditionRecycler.setVisibility(View.GONE);
                transmissionRecycler.setVisibility(View.GONE);
                mileageLayout.setVisibility(View.GONE);
                trimRecycler.setVisibility(View.GONE);
                fuelRecycler.setVisibility(View.GONE);
                driveRecycler.setVisibility(View.GONE);
                zipcodeLayout.setVisibility(View.GONE);
                MakeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ModelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                YearText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                PriceText.setBackgroundColor(Color.parseColor("#a9a9a9"));
                ConditionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TransmissionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                MileageText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TrimText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                FuelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                DriveText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ZipcodeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.Condition_id:
                Conditionfilterlist = new ArrayList<String>(Arrays.asList(CONDTION));

                customFavCountryListAdapter = new CustomFavCountryListAdapter(Conditionfilterlist,conditionRecycler, FilterFragment.this.getActivity(), onDialogListClickListener,Conditonstring);
                conditionRecycler.setAdapter(customFavCountryListAdapter);
                conditionRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                customFavCountryListAdapter.notifyDataSetChanged();
                InputMethodManager imm6 = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm6.hideSoftInputFromWindow(maxEdit.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                makeRecycler.setVisibility(View.GONE);
                modelRecycler.setVisibility(View.GONE);
                yearLayout.setVisibility(View.GONE);
                priceRecycler.setVisibility(View.GONE);
                conditionRecycler.setVisibility(View.VISIBLE);
                transmissionRecycler.setVisibility(View.GONE);
                mileageLayout.setVisibility(View.GONE);
                trimRecycler.setVisibility(View.GONE);
                fuelRecycler.setVisibility(View.GONE);
                driveRecycler.setVisibility(View.GONE);
                zipcodeLayout.setVisibility(View.GONE);
                MakeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ModelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                YearText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                PriceText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ConditionText.setBackgroundColor(Color.parseColor("#a9a9a9"));
                TransmissionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                MileageText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TrimText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                FuelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                DriveText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ZipcodeText.setBackgroundColor(Color.parseColor("#FFFFFF"));

                break;
            case R.id.Transmission_id:
                Transmissionfilterlist = new ArrayList<String>(Arrays.asList(TRANSMISSION));

                customFavCountryListAdapter = new CustomFavCountryListAdapter(Transmissionfilterlist,transmissionRecycler, FilterFragment.this.getActivity(), onDialogListClickListener,Tranmissionstring);
                transmissionRecycler.setAdapter(customFavCountryListAdapter);
                transmissionRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                customFavCountryListAdapter.notifyDataSetChanged();
                InputMethodManager imm7 = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm7.hideSoftInputFromWindow(maxEdit.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                makeRecycler.setVisibility(View.GONE);
                modelRecycler.setVisibility(View.GONE);
                yearLayout.setVisibility(View.GONE);
                priceRecycler.setVisibility(View.GONE);
                conditionRecycler.setVisibility(View.GONE);
                transmissionRecycler.setVisibility(View.VISIBLE);
                mileageLayout.setVisibility(View.GONE);
                trimRecycler.setVisibility(View.GONE);
                fuelRecycler.setVisibility(View.GONE);
                driveRecycler.setVisibility(View.GONE);
                zipcodeLayout.setVisibility(View.GONE);
                MakeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ModelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                YearText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                PriceText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ConditionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TransmissionText.setBackgroundColor(Color.parseColor("#a9a9a9"));
                MileageText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TrimText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                FuelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                DriveText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ZipcodeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.Mileage_id:
                makeRecycler.setVisibility(View.GONE);
                modelRecycler.setVisibility(View.GONE);
                yearLayout.setVisibility(View.GONE);
                priceRecycler.setVisibility(View.GONE);
                conditionRecycler.setVisibility(View.GONE);
                transmissionRecycler.setVisibility(View.GONE);
                mileageLayout.setVisibility(View.VISIBLE);
                trimRecycler.setVisibility(View.GONE);
                fuelRecycler.setVisibility(View.GONE);
                driveRecycler.setVisibility(View.GONE);
                zipcodeLayout.setVisibility(View.GONE);
                MakeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ModelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                YearText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                PriceText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ConditionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TransmissionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                MileageText.setBackgroundColor(Color.parseColor("#a9a9a9"));
                TrimText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                FuelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                DriveText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ZipcodeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.zipcode_id:
                makeRecycler.setVisibility(View.GONE);
                modelRecycler.setVisibility(View.GONE);
                yearLayout.setVisibility(View.GONE);
                priceRecycler.setVisibility(View.GONE);
                conditionRecycler.setVisibility(View.GONE);
                transmissionRecycler.setVisibility(View.GONE);
                mileageLayout.setVisibility(View.GONE);
                trimRecycler.setVisibility(View.GONE);
                fuelRecycler.setVisibility(View.GONE);
                driveRecycler.setVisibility(View.GONE);
                zipcodeLayout.setVisibility(View.VISIBLE);
                MakeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ModelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                YearText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                PriceText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ConditionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TransmissionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                MileageText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TrimText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                FuelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                DriveText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ZipcodeText.setBackgroundColor(Color.parseColor("#a9a9a9"));
                break;
            case R.id.Trim_id:
                Trimfilterlist.clear();
                Trimfilterlist.add("Not Available");

                customFavCountryListAdapter = new CustomFavCountryListAdapter(Trimfilterlist,trimRecycler, FilterFragment.this.getActivity(), onDialogListClickListener,Trimstring);
                trimRecycler.setAdapter(customFavCountryListAdapter);
                trimRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                customFavCountryListAdapter.notifyDataSetChanged();
                InputMethodManager imm8 = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm8.hideSoftInputFromWindow(maxEdit.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                makeRecycler.setVisibility(View.GONE);
                modelRecycler.setVisibility(View.GONE);
                yearLayout.setVisibility(View.GONE);
                priceRecycler.setVisibility(View.GONE);
                conditionRecycler.setVisibility(View.GONE);
                transmissionRecycler.setVisibility(View.GONE);
                mileageLayout.setVisibility(View.GONE);
                trimRecycler.setVisibility(View.VISIBLE);
                fuelRecycler.setVisibility(View.GONE);
                driveRecycler.setVisibility(View.GONE);
                zipcodeLayout.setVisibility(View.GONE);
                MakeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ModelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                YearText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                PriceText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ConditionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TransmissionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                MileageText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TrimText.setBackgroundColor(Color.parseColor("#a9a9a9"));
                FuelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                DriveText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ZipcodeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.Fuel_id:
                Fuelfilterlist.clear();
                Fuelfilterlist.add("Not Available");

                customFavCountryListAdapter = new CustomFavCountryListAdapter(Fuelfilterlist,fuelRecycler, FilterFragment.this.getActivity(), onDialogListClickListener,Fuelstring);
                fuelRecycler.setAdapter(customFavCountryListAdapter);
                fuelRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                customFavCountryListAdapter.notifyDataSetChanged();
                InputMethodManager imm9 = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm9.hideSoftInputFromWindow(maxEdit.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                makeRecycler.setVisibility(View.GONE);
                modelRecycler.setVisibility(View.GONE);
                yearLayout.setVisibility(View.GONE);
                priceRecycler.setVisibility(View.GONE);
                conditionRecycler.setVisibility(View.GONE);
                transmissionRecycler.setVisibility(View.GONE);
                mileageLayout.setVisibility(View.GONE);
                trimRecycler.setVisibility(View.GONE);
                fuelRecycler.setVisibility(View.VISIBLE);
                driveRecycler.setVisibility(View.GONE);
                zipcodeLayout.setVisibility(View.GONE);
                MakeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ModelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                YearText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                PriceText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ConditionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TransmissionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                MileageText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TrimText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                FuelText.setBackgroundColor(Color.parseColor("#a9a9a9"));
                DriveText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ZipcodeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.Drivetrain_id:
                Drivefilterlist.clear();
                Drivefilterlist.add("Not Available");

                customFavCountryListAdapter = new CustomFavCountryListAdapter(Drivefilterlist,driveRecycler, FilterFragment.this.getActivity(), onDialogListClickListener,Drivestring);
                driveRecycler.setAdapter(customFavCountryListAdapter);
                driveRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                customFavCountryListAdapter.notifyDataSetChanged();
                InputMethodManager im = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(maxEdit.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                makeRecycler.setVisibility(View.GONE);
                modelRecycler.setVisibility(View.GONE);
                yearLayout.setVisibility(View.GONE);
                priceRecycler.setVisibility(View.GONE);
                conditionRecycler.setVisibility(View.GONE);
                transmissionRecycler.setVisibility(View.GONE);
                mileageLayout.setVisibility(View.GONE);
                trimRecycler.setVisibility(View.GONE);
                fuelRecycler.setVisibility(View.GONE);
                driveRecycler.setVisibility(View.VISIBLE);
                zipcodeLayout.setVisibility(View.GONE);
                MakeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ModelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                YearText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                PriceText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                ConditionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TransmissionText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                MileageText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                TrimText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                FuelText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                DriveText.setBackgroundColor(Color.parseColor("#a9a9a9"));
                ZipcodeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.cancel_id:
                dialog.dismiss();
                break;
            case R.id.submit:
                MinMileage = minEdit.getText().toString();
                MaxMileage = maxEdit.getText().toString();
                Log.e("Min",MinMileage+ "-" +MaxMileage);
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(maxEdit.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                break;
            case R.id.zipsubmit:
                Zipcodedata = zipEdit.getText().toString();
                Radiusdata = radiusEdit.getText().toString();
                if(!Zipcodedata.equals("") && !Radiusdata.equals("")){
                    Log.e("zip",Zipcodedata+ "-" +Radiusdata);
                    InputMethodManager imm1 = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm1.hideSoftInputFromWindow(radiusEdit.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    Geocoder geocode = new Geocoder(context);
                    geocodeAddress(Zipcodedata,geocode);
                    Latitude = valueOf(latitude);
                    Longitude = valueOf(longitude);
                    Radius = Radiusdata;
                }else{
                    showChangeLangDialog("Uh oh!","Enter the Zipcode & Radius");
                }

                break;
            case R.id.Pricelowtohigh:
                Sortlistdata ="LowesttoHighest";
                Pricelowimage.setVisibility(View.VISIBLE);
                Pricehighimage.setVisibility(View.INVISIBLE);
                Yearoldimage.setVisibility(View.INVISIBLE);
                Yearnewimage.setVisibility(View.INVISIBLE);
                Milesminimage.setVisibility(View.INVISIBLE);
                Milesmaximage.setVisibility(View.INVISIBLE);
                Pricelowtext.setTextColor(Color.parseColor("#D32F2F"));
                Pricehightext.setTextColor(Color.parseColor("#000000"));
                Yearoldtext.setTextColor(Color.parseColor("#000000"));
                Yearnewtext.setTextColor(Color.parseColor("#000000"));
                Milesmintext.setTextColor(Color.parseColor("#000000"));
                Milesmaxtext.setTextColor(Color.parseColor("#000000"));
                break;

            case R.id.Pricehightolow:
                Sortlistdata ="HighesttoLowest";
                Pricelowimage.setVisibility(View.INVISIBLE);
                Pricehighimage.setVisibility(View.VISIBLE);
                Yearoldimage.setVisibility(View.INVISIBLE);
                Yearnewimage.setVisibility(View.INVISIBLE);
                Milesminimage.setVisibility(View.INVISIBLE);
                Milesmaximage.setVisibility(View.INVISIBLE);
                Pricelowtext.setTextColor(Color.parseColor("#000000"));
                Pricehightext.setTextColor(Color.parseColor("#D32F2F"));
                Yearoldtext.setTextColor(Color.parseColor("#000000"));
                Yearnewtext.setTextColor(Color.parseColor("#000000"));
                Milesmintext.setTextColor(Color.parseColor("#000000"));
                Milesmaxtext.setTextColor(Color.parseColor("#000000"));
                break;

            case R.id.Yearoldtonew:
                Sortlistdata ="OldesttoNewest";
                Pricelowimage.setVisibility(View.INVISIBLE);
                Pricehighimage.setVisibility(View.INVISIBLE);
                Yearoldimage.setVisibility(View.VISIBLE);
                Yearnewimage.setVisibility(View.INVISIBLE);
                Milesminimage.setVisibility(View.INVISIBLE);
                Milesmaximage.setVisibility(View.INVISIBLE);
                Pricelowtext.setTextColor(Color.parseColor("#000000"));
                Pricehightext.setTextColor(Color.parseColor("#000000"));
                Yearoldtext.setTextColor(Color.parseColor("#D32F2F"));
                Yearnewtext.setTextColor(Color.parseColor("#000000"));
                Milesmintext.setTextColor(Color.parseColor("#000000"));
                Milesmaxtext.setTextColor(Color.parseColor("#000000"));
                break;

            case R.id.yearnewtolow:
                Sortlistdata ="NewesttoOldest";
                Pricelowimage.setVisibility(View.INVISIBLE);
                Pricehighimage.setVisibility(View.INVISIBLE);
                Yearoldimage.setVisibility(View.INVISIBLE);
                Yearnewimage.setVisibility(View.VISIBLE);
                Milesminimage.setVisibility(View.INVISIBLE);
                Milesmaximage.setVisibility(View.INVISIBLE);
                Pricelowtext.setTextColor(Color.parseColor("#000000"));
                Pricehightext.setTextColor(Color.parseColor("#000000"));
                Yearoldtext.setTextColor(Color.parseColor("#000000"));
                Yearnewtext.setTextColor(Color.parseColor("#D32F2F"));
                Milesmintext.setTextColor(Color.parseColor("#000000"));
                Milesmaxtext.setTextColor(Color.parseColor("#000000"));
                break;

            case R.id.Milesmintomax:
                Sortlistdata ="MintoMax";
                Pricelowimage.setVisibility(View.INVISIBLE);
                Pricehighimage.setVisibility(View.INVISIBLE);
                Yearoldimage.setVisibility(View.INVISIBLE);
                Yearnewimage.setVisibility(View.INVISIBLE);
                Milesminimage.setVisibility(View.VISIBLE);
                Milesmaximage.setVisibility(View.INVISIBLE);
                Pricelowtext.setTextColor(Color.parseColor("#000000"));
                Pricehightext.setTextColor(Color.parseColor("#000000"));
                Yearoldtext.setTextColor(Color.parseColor("#000000"));
                Yearnewtext.setTextColor(Color.parseColor("#000000"));
                Milesmintext.setTextColor(Color.parseColor("#D32F2F"));
                Milesmaxtext.setTextColor(Color.parseColor("#000000"));
                break;

            case R.id.Milesmaxtomin:
                Sortlistdata ="MaxtoMin";
                Pricelowimage.setVisibility(View.INVISIBLE);
                Pricehighimage.setVisibility(View.INVISIBLE);
                Yearoldimage.setVisibility(View.INVISIBLE);
                Yearnewimage.setVisibility(View.INVISIBLE);
                Milesminimage.setVisibility(View.INVISIBLE);
                Milesmaximage.setVisibility(View.VISIBLE);
                Pricelowtext.setTextColor(Color.parseColor("#000000"));
                Pricehightext.setTextColor(Color.parseColor("#000000"));
                Yearoldtext.setTextColor(Color.parseColor("#000000"));
                Yearnewtext.setTextColor(Color.parseColor("#000000"));
                Milesmintext.setTextColor(Color.parseColor("#000000"));
                Milesmaxtext.setTextColor(Color.parseColor("#D32F2F"));
                break;

            case R.id.reset:
                minEdit.setText("");
                maxEdit.setText("");
                MinMileage = "";
                MaxMileage = "";
                break;
            case R.id.zipreset:
                zipEdit.setText("");
                radiusEdit.setText("");
                Zipcodedata = "";
                Radiusdata = "";
                break;
            case R.id.search_id:
                currentCarCount=0;
                //totalcount=30;
                loadData(currentCarCount);
                dialog.dismiss();
                break;
        }
    }

    public void edmundsData(String edmundresponse) {
        //Log.e("EdmundsData",edmundresponse);

        JSONObject edmundjson = null;
        JSONArray makesArray;

        try {
            if(!edmundresponse.equals(null)){
            edmundjson = new JSONObject(edmundresponse);
            makesArray = edmundjson.getJSONArray("makes");
            //Log.e("EdmundsData",String .valueOf(makesArray.length()));
            Makefilterlist.add("All");
            for (int index = 0; index < makesArray.length(); index++) {
                JSONObject makes_obj = makesArray.getJSONObject(index);
                //String make_name = makes_obj.getString("name");
                if (!makes_obj.getString("name").equals("aston-martin") && !makes_obj.getString("name").equals("avanti") && !makes_obj.getString("name").equals("daewoo") && !makes_obj.getString("name").equals("fisker") && !makes_obj.getString("name").equals("isuzu") && !makes_obj.getString("name").equals("oldsmobile")) {
                    Makefilterlist.add(makes_obj.getString("name"));
                    Collections.sort(Makefilterlist);
                }

            }
        }
            //Log.e("Arraylength",MakeList.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    private void fillAllModel(String make) {

        String edmundresponse = AppController.getInstance().EdmundsResponse;
        JSONObject edmundjson = null;
        JSONArray makesArray;

        try {
            if(!edmundresponse.equals(null)){
            edmundjson = new JSONObject(edmundresponse);
            makesArray = edmundjson.getJSONArray("makes");
            //Log.e("EdmundsData",String .valueOf(makesArray.length()));
            Modelfilterlist.add("All");
            for (int index = 0; index < makesArray.length(); index++) {
                JSONObject makes_obj = makesArray.getJSONObject(index);
                //String make_name = makes_obj.getString("name");
                //Makefilterlist.add(makes_obj.getString("name"));
                if (make.equals(makes_obj.getString("name"))) {
                    JSONArray modelarray = makes_obj.getJSONArray("models");

                    for (int i = 0; i < modelarray.length(); i++) {
                        JSONObject indmodel = modelarray.getJSONObject(i);
                        Modelfilterlist.add(indmodel.getString("name"));
                        Collections.sort(Modelfilterlist);
                    }
                }

            }
        }
            //Log.e("Arraylength",MakeList.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void fillMakeModelYearear(String make,String model) {

        String edmundresponse = AppController.getInstance().EdmundsResponse;
        JSONObject edmundjson = null;
        JSONArray makesArray;

        try {
            if(!edmundresponse.equals(null)){
            edmundjson = new JSONObject(edmundresponse);
            makesArray = edmundjson.getJSONArray("makes");
            //Log.e("EdmundsData",String .valueOf(makesArray.length()));
            for (int index = 0; index < makesArray.length(); index++) {
                JSONObject makes_obj = makesArray.getJSONObject(index);
                //String make_name = makes_obj.getString("name");
                //Makefilterlist.add(makes_obj.getString("name"));
                if (make.equals(makes_obj.getString("name"))) {
                    JSONArray modelarray = makes_obj.getJSONArray("models");
                    for (int i = 0; i < modelarray.length(); i++) {
                        JSONObject indmodel = modelarray.getJSONObject(i);

                        if (model.equals(indmodel.getString("name"))) {

                            JSONArray yeararray = indmodel.getJSONArray("year");

                            for (int k = 0; k < yeararray.length(); k++) {
                                JSONObject indyear = yeararray.getJSONObject(k);
                                Yearfilterlist.add(indyear.getString("year"));
                            }

                        }

                    }
                }

            }
        }
            //Log.e("Arraylength",MakeList.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void fillMakeYear(String make) {

        String edmundresponse = AppController.getInstance().EdmundsResponse;
        JSONObject edmundjson = null;
        JSONArray makesArray;

        try {
            if(!edmundresponse.equals(null)) {
                edmundjson = new JSONObject(edmundresponse);
                makesArray = edmundjson.getJSONArray("makes");
                //Log.e("EdmundsData",String .valueOf(makesArray.length()));
                for (int index = 0; index < makesArray.length(); index++) {
                    JSONObject makes_obj = makesArray.getJSONObject(index);
                    //String make_name = makes_obj.getString("name");
                    //Makefilterlist.add(makes_obj.getString("name"));
                    if (make.equals(makes_obj.getString("name"))) {
                        JSONArray modelarray = makes_obj.getJSONArray("models");

                        for (int i = 0; i < modelarray.length(); i++) {
                            JSONObject indmodel = modelarray.getJSONObject(i);
                            JSONArray yeararray = indmodel.getJSONArray("year");
                            for (int k = 0; k < yeararray.length(); k++) {
                                JSONObject indyear = yeararray.getJSONObject(k);
                                Yearfilterlist.add(indyear.getString("year"));
                            }
                        }
                    }

                }
            }
            //Log.e("Arraylength",MakeList.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void geocodeAddress(String addressStr, Geocoder gc) {

        Address address = null;
        List<Address> addressList = null;
        try {
            if (!TextUtils.isEmpty(addressStr)) {
                addressList = gc.getFromLocationName(addressStr, 5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != addressList && addressList.size() > 0) {
            address = addressList.get(0);
        }
        if (null != address && address.hasLatitude()
                && address.hasLongitude()) {
            latitude = address.getLatitude();
            longitude = address.getLongitude();
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

    public void showGPSDisabledAlertToUser() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alert);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        final TextView messagecontent = (TextView) dialog.findViewById(R.id.messagealert);
        final TextView textcenter = (TextView) dialog.findViewById(R.id.textcentermessage);
        final Button okbutton =(Button) dialog.findViewById(R.id.okbutton);
        final Button cancelbutton =(Button) dialog.findViewById(R.id.cancelbutton);
        textcenter.setText("");
        messagecontent.setText("No Data found in your location.Can you goto non-location?");
        okbutton.setText("Ok");
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Latitude!=""&& Longitude!=""){
                    Latitude=""; Longitude=""; Radius="";currentCarCount=0;
                    loadData(currentCarCount);
                }
                dialog.dismiss();
            }
        });
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    public void onLocationChanged(android.location.Location location) {
        updateUI(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        if (s.equals(LocationManager.GPS_PROVIDER)) {
            //showGPSDisabledAlertToUser();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }
}
