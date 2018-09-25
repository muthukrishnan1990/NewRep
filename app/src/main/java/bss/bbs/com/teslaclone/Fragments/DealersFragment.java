package bss.bbs.com.teslaclone.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import bss.bbs.com.teslaclone.Adapter.CustomSpinnerAdapter;
import bss.bbs.com.teslaclone.Adapter.DealerDetailAdapter;
import bss.bbs.com.teslaclone.R;
import bss.bbs.com.teslaclone.Singleton.AppController;
import bss.bbs.com.teslaclone.Singleton.Constants;
import bss.bbs.com.teslaclone.network.VolleyCustomPostRequest;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.String.valueOf;

/**
 * Created by sairaj on 21/08/17.
 */

@SuppressLint("ValidFragment")
public class DealersFragment extends Fragment implements LocationListener,AdapterView.OnItemSelectedListener,View.OnClickListener{

    private View parentView;
    Context context;
    RecyclerView recyclerView;
    ArrayList<String> Dealername = new ArrayList<>();
    ArrayList<String> Location = new ArrayList<>();
    ArrayList<String> Banner_Image = new ArrayList<>();
    ArrayList<String> Dealer_ID = new ArrayList<>();

    RecyclerView.Adapter DealerAdapter;
    ArrayList<String> MakeList = new ArrayList<>();
    ArrayList<String> CityList = new ArrayList<>();


    Spinner SpinnerWithCity,SpinnerWithMake;
    Button Searchbutton,internetbtn;
    CustomSpinnerAdapter<String> makeAdapter, cityAdapter;
    String Make,City;
    LinearLayout loading,nodatafound,internetconn;
    private LocationManager locationManager;
    private Location location;
    private final int REQUEST_LOCATION = 200;
    private static final String TAG = "GPS";
    String Latitude="",Longitude="",Radius="";
    SharedPreferences pref;

    public DealersFragment(Context con) {
        context = con;
        pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.dealers, container, false);
        TextView titleview = (TextView) getActivity().findViewById(R.id.titleid);
        titleview.setText("Dealers");
        locationManager = (LocationManager)getActivity().getSystemService(Service.LOCATION_SERVICE);
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

        loadData(Make,City);
        edmundsData(AppController.getInstance().EdmundsResponse);
        cityListData(AppController.getInstance().CityList);
        initViews(parentView);
        setUpViews(parentView);

        SpinnerWithMake.setOnItemSelectedListener(this);
        SpinnerWithCity.setOnItemSelectedListener(this);
        Searchbutton.setOnClickListener(this);

        fillMakeSpinner();
        fillCitySpinner();

        return parentView;
    }

    private void setUpViews(View view) {
        SpinnerWithCity = (Spinner) view.findViewById(R.id.city_spinner);
        SpinnerWithMake = (Spinner) view.findViewById(R.id.make_spinner);
        Searchbutton = (Button) view.findViewById(R.id.search_button);
        loading = (LinearLayout) view.findViewById(R.id.Loading);
        nodatafound = (LinearLayout) view.findViewById(R.id.Nodata);
        internetconn = (LinearLayout) view.findViewById(R.id.internetconnect);
        internetbtn=(Button) view.findViewById(R.id.internetbtn);
        internetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData(Make,City);
            }
        });

    }

    private void initViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.Dealer_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

    }
    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");

        Latitude = valueOf(loc.getLatitude());
        Longitude =valueOf(loc.getLongitude());
        Radius="200";
    }
    @Override
    public void onStop() {
        super.onStop();
        locationManager.removeUpdates(this);

    }
    private void loadData(String make, String city){



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
       /* params.put("latitude", "");
        params.put("longitude", "");
        params.put("radius", "");*/
        if(make != null){
            params.put("make", make);
            Log.e("Make",make);
        }else {
            params.put("make", "");
            Log.e("Make", "");
        }

        if(city != null){
            params.put("city", city);
            Log.e("City",city);
        }else {
            params.put("city", "");
            Log.e("City","");
        }


        VolleyCustomPostRequest jsObjRequest = new VolleyCustomPostRequest(Request.Method.POST,Constants.dealerlist_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                // TODO Auto-generated method stub
               Log.e("newresult",response.toString());
                JSONObject json = null;
                try {
                    json = new JSONObject(response.toString());
                    if(json.has("showroom data")){
                        recyclerView.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        nodatafound.setVisibility(View.VISIBLE);
                        internetconn.setVisibility(View.GONE);
                        if(Latitude!=""&& Longitude!="") {
                            showGPSDisabledAlertToUser();
                        }else{
                            showDialogmessage("","No Dealers found...");
                        }
                        }else {
                        storeResponsedata(response.toString());
                        recyclerView.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        nodatafound.setVisibility(View.GONE);
                        internetconn.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                    loading.setVisibility(View.VISIBLE);
                    internetconn.setVisibility(View.GONE);
                } else if (error instanceof ServerError) {
                    //TODO
                    recyclerView.setVisibility(View.GONE);
                    nodatafound.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    internetconn.setVisibility(View.GONE);
                } else if (error instanceof NetworkError) {
                    //TODO+
                    recyclerView.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    internetconn.setVisibility(View.VISIBLE);
                } else if (error instanceof ParseError) {
                    //TODO
                    recyclerView.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    nodatafound.setVisibility(View.VISIBLE);
                    internetconn.setVisibility(View.GONE);
                }
            }
        });

        jsObjRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsObjRequest, "tag_json_obj");

    }

    public void storeResponsedata(String response){

        Banner_Image.clear();
        Location.clear();
        Dealername.clear();
        Dealer_ID.clear();

        JSONObject json = null, SellingPrice = null, UploadCars=null, TrendingCars=null;

        try {
            if (!response.equals(null)) {
                json = new JSONObject(response);

                if (json.has("Result")) {
                    JSONArray sellingarray = json.getJSONArray("Result");

                    // Log.e("Dealer", String.valueOf(sellingarray.length()));
                    for (int i = 0; i < sellingarray.length(); i++) {
                        try {
                            JSONObject jsonobject = sellingarray.getJSONObject(i);
                            Banner_Image.add(jsonobject.getString("Banner_Image"));
                            Location.add(jsonobject.getString("City"));
                            Dealername.add(jsonobject.getString("Dealer_Name"));
                            Dealer_ID.add(jsonobject.getString("Dealer_ID"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    DealerAdapter = new DealerDetailAdapter(context, Dealername, Location, Banner_Image, Dealer_ID);
                    recyclerView.setAdapter(DealerAdapter);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.search_button:
                if(City.equals("All")){
                    City="";
                }
                loadData(Make,City);
                break;
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;
        int sp_id = spinner.getId();
        Log.e("GetID", sp_id + "");
        switch (sp_id) {

            case R.id.make_spinner:

                if(position == 0){
                    Make = "";
                    Log.e("Select Make",Make);

                }else if(position == 1){

                    Make = "";
                    Log.e("Select Make",Make);

                }else{

                    Make = MakeList.get(position-2);
                    Log.e("Select Make",Make);

                }

                break;

            case R.id.city_spinner:

                if(position == 0){

                    City = "";
                    Log.e("Select City",City);

                }else{

                    City = CityList.get(position-1);
                    Log.e("Select City",City);

                }

                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void fillMakeSpinner() {

        makeAdapter = new CustomSpinnerAdapter<String>(context, R.layout.custom_spinner_item);
        makeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        makeAdapter.add("Select Make");
        makeAdapter.add("All");
        makeAdapter.addAll(MakeList);
        SpinnerWithMake.setAdapter(makeAdapter);
        SpinnerWithMake.setSelection(0);
    }

    private void fillCitySpinner() {

        cityAdapter = new CustomSpinnerAdapter<String>(context, R.layout.custom_spinner_item);
        cityAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        cityAdapter.add("Select City");
       // cityAdapter.add("All");
        cityAdapter.addAll(CityList);
        SpinnerWithCity.setAdapter(cityAdapter);
        SpinnerWithCity.setSelection(0);
    }

    public void edmundsData(String edmundresponse) {
        //Log.e("EdmundsData",edmundresponse);

        JSONObject edmundjson = null;
        JSONArray makesArray;

        try {
            if(edmundresponse!=null) {
                edmundjson = new JSONObject(edmundresponse);
                makesArray = edmundjson.getJSONArray("makes");
                //Log.e("EdmundsData",String .valueOf(makesArray.length()));
                for (int index = 0; index < makesArray.length(); index++) {
                    JSONObject makes_obj = makesArray.getJSONObject(index);
                    //String make_name = makes_obj.getString("name");
                    if(!makes_obj.getString("name").equals("aston-martin") && !makes_obj.getString("name").equals("avanti")&& !makes_obj.getString("name").equals("daewoo")&& !makes_obj.getString("name").equals("fisker")&& !makes_obj.getString("name").equals("isuzu") &&  !makes_obj.getString("name").equals("oldsmobile")) {
                    MakeList.add(makes_obj.getString("name"));
                    Collections.sort(MakeList);
                    }
                }
                //Log.e("Arraylength",MakeList.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void cityListData(String citydata) {
        //Log.e("EdmundsData",edmundresponse);

        JSONArray cityArray;
        try {
            if (!citydata.equals(null)) {
                cityArray = new JSONArray(citydata);
                CityList.add("All");
                for (int i = 0; i < cityArray.length(); i++) {

                    String nickname = cityArray.getString(i);
                    Log.e("nickname", nickname);
                    CityList.add(nickname);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        messagecontent.setText("No Dealers found in your location.Can you goto non-location?");
        okbutton.setText("Ok");
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Latitude!=""&& Longitude!=""){
                    Latitude=""; Longitude=""; Radius="";Make="";City="";
                    loadData(Make,City);
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
    public  void showDialogmessage(String title,String message) {
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
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onLocationChanged(android.location.Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

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
