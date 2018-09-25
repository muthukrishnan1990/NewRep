package bss.bbs.com.teslaclone;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import bss.bbs.com.teslaclone.Singleton.AppController;
import bss.bbs.com.teslaclone.Singleton.Constants;
import bss.bbs.com.teslaclone.network.VolleyCustomPostRequest;

import static java.lang.String.valueOf;

public class SplashScreen extends Activity implements LocationListener{

    Context context = this;
    SharedPreferences pref;
    double tvLatitude,tvLongitude;
    int Radius;
    String postalCode;
    Button internetbtn;
    ProgressBar loadingdata;

    private LocationManager locationManager;
    private Location location;
    private final int REQUEST_LOCATION = 200;
    private static final String TAG = "GPS";
    private static final int SPLASH_DISPLAY_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        internetbtn=(Button)findViewById(R.id.internetbtn);
        loadingdata=(ProgressBar)findViewById(R.id.loadingdata);
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
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
        } else {
            showGPSDisabledAlertToUser();
        }
        pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
        edmundsData();
        cityListData();
        //loadRandomData();

          SharedPreferences.Editor editor = pref.edit();

        // Saving long
       /* editor.putString("lat", valueOf(tvLatitude));
        editor.putString("long", valueOf(tvLongitude));
        editor.putString("radius",valueOf(Radius));
        editor.putString("zip", postalCode); */// Saving string
        editor.commit();
        internetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingdata.setVisibility(View.VISIBLE);
                internetbtn.setVisibility(View.GONE);
                edmundsData();
                cityListData();
                //loadRandomData();
            }
        });

    }
    /** Called when the activity is about to become visible. */
    @Override
    protected void onStart() {
        super.onStart();

    }

    /** Called when the activity has become visible. */
    @Override
    protected void onResume() {
        super.onResume();

    }

    /** Called when another activity is taking focus. */
    @Override
    protected void onPause() {
        super.onPause();

    }

    /** Called when the activity is no longer visible. */
    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(this);

    }

    /** Called just before the activity is destroyed. */


    /*public void loadRandomData() {

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constants.Showroom_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                SharedPreferences.Editor editor = pref.edit();
                editor.putString("ShowroomResponse", response.toString());
                editor.commit();

                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
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
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("latitude", "");
                params.put("longitude", "");
                params.put("radius", "");

                return params;
            }

        };

        jsonObjReq.setRetryPolicy(policy);

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, "json_obj_req");

    }*/

    public void edmundsData() {

        int socketTimeout = 60000; // 60 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        Map<String, String> params = new HashMap<String, String>();
        //params.put("VIN", vin);
        //params.put("Dealer_ID", dealerID);


        VolleyCustomPostRequest jsObjRequest = new VolleyCustomPostRequest(Constants.Edumandsmakelist_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                // TODO Auto-generated method stub
                String  respo = response.toString();
                Log.e("Thump",respo);
                AppController.getInstance().EdmundsResponse = response.toString();
                //Log.e("Edmunds",AppController.getInstance().EdmundsResponse);
                //storeResponsedata(respo);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

                NetworkResponse response = error.networkResponse;
                Log.e("Error", error.toString());


                if (error instanceof TimeoutError ) {
                    loadingdata.setVisibility(View.GONE);
                } else if (error instanceof AuthFailureError) {
                    loadingdata.setVisibility(View.GONE);
                } else if (error instanceof ServerError) {
                    loadingdata.setVisibility(View.GONE);
                } else if (error instanceof NetworkError) {
                    showDialogmessage("","No Internet Connection..");
                    internetbtn.setVisibility(View.VISIBLE);
                    loadingdata.setVisibility(View.GONE);
                } else if (error instanceof ParseError) {
                    loadingdata.setVisibility(View.GONE);
                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-API-KEY", "dk0KlY8Mpu3uxKOqb8Cu95BGg3NoASgM8K8ffoBf");
                return headers;
            }
        };

        jsObjRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsObjRequest, "tag_json_obj");

    }

    public void cityListData() {

        Map<String, String> params = new HashMap<String, String>();


        JsonArrayRequest req = new JsonArrayRequest(Constants.Citylist_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.e("Response",response.toString());
                        AppController.getInstance().CityList = response.toString();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {

                                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                                startActivity(i);
                                finish();

                            }
                        }, SPLASH_DISPLAY_TIME);
                       /* Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(i);
                        finish();*/

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());

            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, "json_array_req");

    }

    @Override
    public void onLocationChanged(Location location) {
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

    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");

        tvLatitude= loc.getLatitude();
        tvLongitude=loc.getLongitude();
        Radius =100;
        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List < Address > addressList = geocoder.getFromLocation(tvLatitude, tvLongitude, 1);  // Here 1 represent max location result to returned, by documents it recommended 1 to 5
           if(addressList != null ) {
               Address address = addressList.get(0);
               postalCode = address.getPostalCode();
           }else{
               Log.e("Tag",valueOf(addressList));
           }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
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
        messagecontent.setText("GPS is disabled in your device. Would you like to enable it?");
        okbutton.setText("Settings");
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
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
}
