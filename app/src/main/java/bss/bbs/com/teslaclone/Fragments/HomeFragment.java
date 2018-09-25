package bss.bbs.com.teslaclone.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import bss.bbs.com.teslaclone.Adapter.SellingPriceAdapter;
import bss.bbs.com.teslaclone.Adapter.TrendingCarsAdapter;
import bss.bbs.com.teslaclone.Adapter.UpcomingCarAdapter;
import bss.bbs.com.teslaclone.R;
import bss.bbs.com.teslaclone.Residemenu.ResideMenu;
import bss.bbs.com.teslaclone.Singleton.AppController;
import bss.bbs.com.teslaclone.Singleton.Constants;
import bss.bbs.com.teslaclone.SplashScreen;
import bss.bbs.com.teslaclone.network.VolleyCustomPostRequest;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.String.valueOf;


@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment implements View.OnClickListener,LocationListener{

    private View parentView;
    private ResideMenu resideMenu;

    ArrayList<String> alName;
    ArrayList<Integer> alImage;
    ArrayList<Integer> compareImage;

    RecyclerView SellingRecyclerView;
    RecyclerView UpcomingRecyclerView;
    RecyclerView TrendingRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter SellingAdapter;
    RecyclerView.Adapter UpcomingAdapter;
    RecyclerView.Adapter TrendingAdapter;
    Context context;
    SharedPreferences pref;
    EditText EditZip;
    String address;
    double latitude,longitude;
    String Latitude="",Longitude="",Radius="";
    Button sellingbutton,upcomingbutton,comparebutton,Search;
    Dialog dialog;
    LinearLayout mainlayout;

    ArrayList<String> SellingVIN = new ArrayList<>();
    ArrayList<String> SellingyearList = new ArrayList<>();
    ArrayList<String> SellingmakeList = new ArrayList<>();
    ArrayList<String> SellingmodelList = new ArrayList<>();
    ArrayList<String> SellingOfferpriceList = new ArrayList<>();
    ArrayList<String> SellingmsrpList = new ArrayList<>();
    ArrayList<String> SellingpriceList = new ArrayList<>();
    ArrayList<String> SellingmilesList = new ArrayList<>();
    ArrayList<String> SellingstockList = new ArrayList<>();
    ArrayList<String> SellingbannerImageUrl = new ArrayList<>();
    ArrayList<String> SellingLocation = new ArrayList<>();
    ArrayList<String> SellingDealerID = new ArrayList<>();
    ArrayList<String> SellingExteriorColor = new ArrayList<>();
    ArrayList<String> SellingInteriorColor = new ArrayList<>();

    ArrayList<String> UpcomingVIN = new ArrayList<>();
    ArrayList<String> UpcomingyearList = new ArrayList<>();
    ArrayList<String> UpcomingmakeList = new ArrayList<>();
    ArrayList<String> UpcomingmodelList = new ArrayList<>();
    ArrayList<String> UpcomingpriceList = new ArrayList<>();
    ArrayList<String> UpcomingmilesList = new ArrayList<>();
    ArrayList<String> UpcomingstockList = new ArrayList<>();
    ArrayList<String> UpcomingbannerImageUrl = new ArrayList<>();
    ArrayList<String> UpcomingLocation = new ArrayList<>();
    ArrayList<String> UpcomingDealerID = new ArrayList<>();
    ArrayList<String> UpcomingExteriorColor = new ArrayList<>();
    ArrayList<String> UpcomingInteriorColor = new ArrayList<>();

    ArrayList<JSONObject> ObjectList1 = new ArrayList<>();
    ArrayList<JSONObject> ObjectList2 = new ArrayList<>();
    boolean isGPS = false;
    boolean isNetwork = false;
    String zipcode;
    private LocationManager locationManager;
    private Location location;
    private final int REQUEST_LOCATION = 200;
    private static final String TAG = "GPS";
    public HomeFragment(Context con) {
        // Required empty public constructor
        context = con;
        pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.home, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        locationManager = (LocationManager)getActivity().getSystemService(Service.LOCATION_SERVICE);
       if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 2, this);
            if (locationManager != null) {
                  location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (location != null) {
                updateUI(location);
            }
        }

        alName = new ArrayList<>(Arrays.asList("BMW", "Acura", "Ford", "Honda"));
        alImage = new ArrayList<>(Arrays.asList(R.drawable.list_car1, R.drawable.list_car2, R.drawable.list_car3, R.drawable.list_car4));
        compareImage = new ArrayList<>(Arrays.asList(R.drawable.list_car1, R.drawable.list_car2));
        setUpViews(parentView);
        loadRandomData();
        TextView titleview = (TextView) getActivity().findViewById(R.id.titleid);
        titleview.setText("Showroom");
        return parentView;
    }

    private void setUpViews(View view) {

        EditZip   = (EditText)view.findViewById(R.id.Zipcode);
        EditZip.setText(zipcode);
        Search = (Button) view.findViewById(R.id.search);
        mainlayout=(LinearLayout)view.findViewById(R.id.mainlayout);
        sellingbutton = (Button) view.findViewById(R.id.selling_button);
        upcomingbutton = (Button) view.findViewById(R.id.upcoming_button);
        comparebutton = (Button) view.findViewById(R.id.Trending_button);
        Search.setOnClickListener(this);
        sellingbutton.setOnClickListener(this);
        upcomingbutton.setOnClickListener(this);
        comparebutton.setOnClickListener(this);

        //Selling Price
        // Calling the RecyclerView
        SellingRecyclerView = (RecyclerView) view.findViewById(R.id.selling_recycler);
        SellingRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        SellingRecyclerView.setLayoutManager(mLayoutManager);

        //SellingAdapter = new SellingPriceAdapter(context, alName, alImage);
        /*SellingAdapter = new SellingPriceAdapter(context, SellingVIN, SellingmakeList, SellingmodelList, SellingyearList, SellingpriceList, SellingbannerImageUrl, SellingLocation, SellingDealerID, SellingmilesList, SellingstockList, SellingExteriorColor, SellingInteriorColor);
        SellingRecyclerView.setAdapter(SellingAdapter);*/


        //Upcoming Price
        // Calling the RecyclerView
        UpcomingRecyclerView = (RecyclerView)view.findViewById(R.id.upcoming_recycler);
        UpcomingRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        UpcomingRecyclerView.setLayoutManager(mLayoutManager);

        /*UpcomingAdapter = new UpcomingCarAdapter(context, UpcomingVIN, UpcomingmakeList, UpcomingmodelList, UpcomingyearList, UpcomingpriceList, UpcomingbannerImageUrl, UpcomingLocation, UpcomingDealerID, UpcomingmilesList, UpcomingstockList, SellingExteriorColor, SellingInteriorColor);
        UpcomingRecyclerView.setAdapter(UpcomingAdapter);*/

        //Trending Price
        // Calling the RecyclerView
        TrendingRecyclerView = (RecyclerView) view.findViewById(R.id.Trending_recycler);
        TrendingRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        TrendingRecyclerView.setLayoutManager(mLayoutManager);

       /* TrendingAdapter = new TrendingCarsAdapter(context, ObjectList1,ObjectList2);
        TrendingRecyclerView.setAdapter(TrendingAdapter);*/



    }
    public void loadRandomData() {


        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
       mainlayout.setAlpha(0.4f);
        popupwindow();


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
            VolleyCustomPostRequest jsonObjReq = new VolleyCustomPostRequest(Request.Method.POST,Constants.Showroom_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("showroom", response.toString());
                JSONObject json = null;
                try {
                    json = new JSONObject(response.toString());
                    if(json.has("showroom data")){
                        mainlayout.setAlpha(1.0f);
                        dialog.hide();
                        showDialogmessage("","No Data found in your location.Can you goto non-location.");
                        //showGPSDisabledAlertToUser();

                    }else {
                        getRandomCars(response.toString());
                        mainlayout.setAlpha(1.0f);
                        dialog.hide();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
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
        jsonObjReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjReq, "json_obj_req");
    }
    private void getRandomCars(String CarsResponse) {

        if (CarsResponse.equals(null)) {
            Log.e("Parse All CARS Response", "null");
        } else {

            JSONObject json = null, SellingPrice = null, UploadCars = null, TrendingCars = null;

            try {
                if(!CarsResponse.equals(null)){
                json = new JSONObject(CarsResponse);

                if (json.has("Best Selling Price")) {
                    JSONArray sellingarray = json.getJSONArray("Best Selling Price");

                    Log.e("Selling", valueOf(sellingarray.length()));
                    for (int i = 0; i < sellingarray.length(); i++) {
                        try {
                            JSONObject jsonobject = sellingarray.getJSONObject(i);
                            SellingVIN.add(jsonobject.getString("VIN"));
                            SellingbannerImageUrl.add(jsonobject.getString("Banner_Image"));
                            SellingmakeList.add(jsonobject.getString("Make"));
                            SellingmodelList.add(jsonobject.getString("Model"));
                            SellingyearList.add(jsonobject.getString("Year"));
                            SellingOfferpriceList.add(jsonobject.getString("Offer_Price"));
                            SellingpriceList.add(jsonobject.getString("List_Price"));
                            SellingmsrpList.add(jsonobject.getString("MSRP"));
                            SellingmilesList.add(jsonobject.getString("Mileage"));
                            SellingstockList.add(jsonobject.getString("Stock_Type"));
                            SellingLocation.add(jsonobject.getString("City"));
                            SellingDealerID.add(jsonobject.getString("Dealer_ID"));
                            SellingExteriorColor.add(jsonobject.getString("Exterior_Color"));
                            SellingInteriorColor.add(jsonobject.getString("Interior_Color"));
                            // Log.e("invidual_object", jsonobject.getString("City"));
                            SellingAdapter = new SellingPriceAdapter(context, SellingVIN, SellingmakeList, SellingmodelList, SellingyearList, SellingOfferpriceList, SellingpriceList, SellingmsrpList,SellingbannerImageUrl, SellingLocation, SellingDealerID, SellingmilesList, SellingstockList, SellingExteriorColor, SellingInteriorColor);
                            SellingRecyclerView.setAdapter(SellingAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (json.has("Upcoming cars")) {
                    JSONArray upcomingarray = json.getJSONArray("Upcoming cars");

                    Log.e("Upcoming", valueOf(upcomingarray.length()));
                    for (int i = 0; i < upcomingarray.length(); i++) {
                        try {
                            JSONObject upcomingobject = upcomingarray.getJSONObject(i);
                            UpcomingVIN.add(upcomingobject.getString("VIN"));
                            UpcomingbannerImageUrl.add(upcomingobject.getString("Banner_Image"));
                            UpcomingmakeList.add(upcomingobject.getString("Make"));
                            UpcomingmodelList.add(upcomingobject.getString("Model"));
                            UpcomingyearList.add(upcomingobject.getString("Year"));
                            UpcomingpriceList.add(upcomingobject.getString("List_Price"));
                            UpcomingmilesList.add(upcomingobject.getString("Mileage"));
                            UpcomingstockList.add(upcomingobject.getString("Stock_Type"));
                            UpcomingLocation.add(upcomingobject.getString("City"));
                            UpcomingDealerID.add(upcomingobject.getString("Dealer_ID"));
                            UpcomingExteriorColor.add(upcomingobject.getString("Exterior_Color"));
                            UpcomingInteriorColor.add(upcomingobject.getString("Interior_Color"));
                            // Log.e("invidual_object", jsonobject.getString("City"));

                            UpcomingAdapter = new UpcomingCarAdapter(context, UpcomingVIN, UpcomingmakeList, UpcomingmodelList, UpcomingyearList, UpcomingpriceList, UpcomingbannerImageUrl, UpcomingLocation, UpcomingDealerID, UpcomingmilesList, UpcomingstockList, SellingExteriorColor, SellingInteriorColor);
                            UpcomingRecyclerView.setAdapter(UpcomingAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (json.has("Car Comparision")) {
                    JSONArray comparearray = json.getJSONArray("Car Comparision");

                    Log.e("Comparison", valueOf(comparearray.length()));
                    for (int i = 0; i < comparearray.length(); i++) {

                        JSONArray individualarray = comparearray.getJSONArray(i);
                        //Log.e("Indiarray",individualarray.toString());
                        ObjectList1.add(individualarray.getJSONObject(0));
                        ObjectList2.add(individualarray.getJSONObject(1));
                        TrendingAdapter = new TrendingCarsAdapter(context, ObjectList1, ObjectList2);
                        TrendingRecyclerView.setAdapter(TrendingAdapter);

                    }

                    //Log.e("Indiarray",ObjectList1.toString());
                }

            }
        }
            catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
    @Override
    public void onStop() {
        super.onStop();
        locationManager.removeUpdates(this);

    }
    @Override
    public void onClick(View v) {

        //FragmentManager fm = getS

        if(v == sellingbutton){

            try {
                try {
                    Log.e("click","yes");
                    FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    Fragment fr = new FilterFragment(context);
                    ft.replace(R.id.main_fragment, fr);
                    ft.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(v == upcomingbutton){

            try {
                try {
                    Log.e("click","no");
                    FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    Fragment fr = new FilterFragment(context);
                    ft.replace(R.id.main_fragment, fr);
                    ft.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(v == Search){

            try {
                try {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(EditZip.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);
                        Geocoder geocode = new Geocoder(context);

                        address = EditZip.getText().toString();
                        Log.e("address",address);
                    if (address.equals(null) || address.equals(""))
                    {
                        showDialogmessage("","Please Enter the zipcode...");
                    }else{
                        geocodeAddress(address, geocode);
                        Log.e("click", "no");
                        int Radius = 100;
                        Bundle bundle = new Bundle();
                        bundle.putDouble("Lat", latitude);
                        bundle.putDouble("Long", longitude);
                        Log.e("lat", valueOf(longitude));
                        bundle.putInt("radius", Radius);
                        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                        Fragment fr = new FilterFragment(context);
                        fr.setArguments(bundle);
                        ft.replace(R.id.main_fragment, fr);
                        ft.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(v == comparebutton){

            try {
                try {
                    Log.e("click","no");
                    FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    Fragment fr = new CarcompareFragment(context);
                    ft.replace(R.id.main_fragment, fr);
                    ft.addToBackStack(null);
                    ft.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

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
           //Log.e("lat",valueOf(latitude));
          longitude = address.getLongitude();
           // Log.e("lat",valueOf(longitude));
        }

    }
    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");

        Latitude = valueOf(loc.getLatitude());
        Longitude =valueOf(loc.getLongitude());
        Radius ="200";
        Geocoder geocoder;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List < Address > addressList = geocoder.getFromLocation(Double.parseDouble(Latitude),Double.parseDouble(Longitude) , 1);  // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if(addressList != null ) {
                Address address = addressList.get(0);
                zipcode = address.getPostalCode();
            }else{
                Log.e("Tag",valueOf(addressList));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public  void popupwindow(){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dataloading_layout);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
   /* public void showGPSDisabledAlertToUser() {
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
                    Latitude=""; Longitude=""; Radius="";
                    loadRandomData();
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
    }*/
    public void showDialogmessage(String title,String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_custom);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        final TextView messagecontent = (TextView) dialog.findViewById(R.id.messagealert);
        final Button okbutton =(Button) dialog.findViewById(R.id.okbutton);
        messagecontent.setText(message);
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Latitude!=""&& Longitude!=""){
                    Latitude=""; Longitude=""; Radius="";
                    loadRandomData();
                }
                    dialog.dismiss();

            }
        });
        dialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
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
