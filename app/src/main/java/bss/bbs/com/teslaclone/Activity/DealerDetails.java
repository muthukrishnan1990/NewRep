package bss.bbs.com.teslaclone.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
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
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bss.bbs.com.teslaclone.Adapter.CustomSpinnerAdapter;
import bss.bbs.com.teslaclone.Adapter.DealerCarListAdapter;
import bss.bbs.com.teslaclone.Adapter.EndlessScrollListener;
import bss.bbs.com.teslaclone.Adapter.OnLoadMoreListener;
import bss.bbs.com.teslaclone.R;
import bss.bbs.com.teslaclone.Singleton.AppController;
import bss.bbs.com.teslaclone.Singleton.Constants;
import bss.bbs.com.teslaclone.network.VolleyCustomPostRequest;



public class DealerDetails extends FragmentActivity implements AdapterView.OnItemSelectedListener{

    Context context = this;
    SharedPreferences pref;
    RecyclerView DealerRecycler;
    //RecyclerView.LayoutManager mLayoutManager;
    GridLayoutManager mLayoutManager;
    //RecyclerView.Adapter DealerList;
    DealerCarListAdapter DealerList;
    Button leftbutton,Submited;
    LinearLayout PhoneCall,MessageDealer,Location,mLinearlayout;
    Spinner SpinnerWithMake;
    ImageView bannerimage;
    Dialog dialog;
    String Makevalue;
    TextView dealername,dealerlocation;
    EditText FirstnameText,LastnameText,EmailText,PhoneText;
    String Firstnamedata,LastNamedata,EmailIDdata,Phonedata;
    CustomSpinnerAdapter<String> makeAdapter;
    boolean makedata = false;

    ArrayList<String> VIN = new ArrayList<>();
    ArrayList<String> yearList = new ArrayList<>();
    ArrayList<String> makeList = new ArrayList<>();
    ArrayList<String> modelList = new ArrayList<>();
    ArrayList<String> priceList = new ArrayList<>();
    ArrayList<String> milesList = new ArrayList<>();
    ArrayList<String> stockList = new ArrayList<>();
    ArrayList<String> bannerImageUrl = new ArrayList<>();
    //ArrayList<String> Location = new ArrayList<>();
    ArrayList<String> DealerID = new ArrayList<>();
    ArrayList<String> ExteriorColor = new ArrayList<>();
    ArrayList<String> InteriorColor = new ArrayList<>();


    ArrayList<String> MakeValueList = new ArrayList<>();
    int Count=0;
    //int totalcount=30;
    private PopupWindow mPopupWindow;
    ImageButton btnclose;
    CheckBox Emailchecked,Phonechecked;
    String Dealername,City,BannerImage,Dealer_ID,SelectedMake,Phone,Email,Lng,Lat,Address,DealerCity,State,Postal;
    String message="Dear Dealer," +"\n"+
            "\n"+ "      I am interested in your car list in FindYourCarApp.com." +
            "you can reach me using following contact information" +"\n"+"\n"+
            "Firstname:" +"\n"+
            "Lastname :" +"\n"+
            "Phone no :" +"\n"+
            "Email id :";
    String subject="I'm Interested in Your car";
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dealer_detail);

        Intent intent = getIntent();

//        Dealername = intent.getStringExtra("Dealer_Name");
//        City = intent.getStringExtra("Location");
//        BannerImage = intent.getStringExtra("Banner_Image");
        Dealer_ID = intent.getStringExtra("Dealer_ID");

        loadData(Dealer_ID,SelectedMake,Count);
        setUpViews();


    }

    private void setUpViews() {

        mLinearlayout= (LinearLayout) findViewById(R.id.Linearlayout);
        leftbutton = (Button) findViewById(R.id.title_bar_left_menu);
        PhoneCall = (LinearLayout) findViewById(R.id.Dealerphone);
        MessageDealer = (LinearLayout) findViewById(R.id.Dealermessage);
        Location = (LinearLayout) findViewById(R.id.DealerLocation);
        leftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.riht_to_left);

            }
        });
        PhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                showChangeLangDialog(Phone);
            }
        });
        MessageDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub

                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.email_layout);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                //dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                btnclose = (ImageButton)dialog.findViewById(R.id.ib_close);
                FirstnameText = (EditText)dialog.findViewById(R.id.editTextName);
                LastnameText = (EditText)dialog.findViewById(R.id.editTextlastname);
                EmailText = (EditText)dialog.findViewById(R.id.editTextEmail);
                PhoneText = (EditText)dialog.findViewById(R.id.editTextMobile);
                Emailchecked = (CheckBox)dialog.findViewById(R.id.checkbox_meat);
                Phonechecked = (CheckBox)dialog.findViewById(R.id.checkbox_cheese);
                Submited = (Button)dialog.findViewById(R.id.buttonSubmit);

                Submited.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view2) {
                        int socketTimeout = 30000; // 30 seconds. You can change it
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                        Map<String, String> params = new HashMap<String, String>();

                        Firstnamedata = FirstnameText.getText().toString();
                        LastNamedata = LastnameText.getText().toString();
                        EmailIDdata = EmailText.getText().toString();
                        Phonedata = PhoneText.getText().toString();

                        if (!validateFirstName(Firstnamedata)|| Firstnamedata=="" || Firstnamedata==null) {
                            // FirstName.setError("Invalid FirstName");
                            showDialogmessage("","Enter the Firstname");
                        } else if (!validateLastName(LastNamedata) || LastNamedata=="" || LastNamedata==null) {
                            // Lastname.setError("Invalid Lastname");
                            showDialogmessage("","Enter the LastName");
                        } else if (!isValidEmail(EmailIDdata) || EmailIDdata=="" || EmailIDdata==null) {
                            // Email.setError("Invalid Email");
                            showDialogmessage("","Enter the EmailID");
                        }else if (Phonedata=="" || Phonedata==null) {
                            //PhoneNo.setError("Invalid Phone no");
                            showDialogmessage("","Enter the Phone no");
                        } else {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(FirstnameText.getWindowToken(),
                                    InputMethodManager.RESULT_UNCHANGED_SHOWN);


                            if(Phonechecked.isChecked()&&Emailchecked.isChecked()) {
                                params.put("firstname", Firstnamedata);
                                params.put("lastname", LastNamedata);
                                params.put("email", EmailIDdata);
                                params.put("phone", Phonedata);
                                params.put("preference", "Phone,Email");
                            VolleyCustomPostRequest jsObjRequest = new VolleyCustomPostRequest(Request.Method.POST, Constants.dealerEmail_URL, params, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    // TODO Auto-generated method stub
                                    Log.e("Response", response.toString());
                                    showDialogmessage("", response.toString());
                                    dialog.dismiss();
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO Auto-generated method stub

                                    NetworkResponse response = error.networkResponse;
                                    Log.e("Error", error.toString());
                                    showDialogmessage("","Message sent Successfully...");
                                    dialog.dismiss();
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
                        else{
                            showDialogmessage("Mandatory","Please Click  Contact Preference");
                        }
                        }
                    }
                });
                btnclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                 dialog.show();
            }
        });
    Location.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {

       //  if(!Lng.equals(null) && !Lat.equals(null) ) {
         Intent i1 = new Intent(DealerDetails.this, MapsActivity.class);
         i1.putExtra("Lat", Lat);
         Log.e("latdealer",Lat);
         i1.putExtra("Lan", Lng);
         Log.e("Lngdealer",Lng);
         i1.putExtra("Address", Address);
         i1.putExtra("DealerCity", DealerCity);
         i1.putExtra("DealerState", State);
         i1.putExtra("Postalcode", Postal);
         i1.putExtra("phoneno", Phone);
         startActivity(i1);
         //  }
     }
 });

        dealername = (TextView) findViewById(R.id.Dealer_Name);
        dealerlocation = (TextView) findViewById(R.id.Dealer_Location);

        bannerimage = (ImageView) findViewById(R.id.Dealer_Banner);
//        Picasso.with(context).load(BannerImage).placeholder(R.drawable.logo).fit().into(bannerimage);

        SpinnerWithMake = (Spinner) findViewById(R.id.make_spinner);
        SpinnerWithMake.setOnItemSelectedListener(this);
        DealerRecycler = (RecyclerView) findViewById(R.id.dealer_recycler);
        DealerRecycler.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(context,2);
        DealerRecycler.setNestedScrollingEnabled(false);
        DealerRecycler.setLayoutManager(mLayoutManager);

    }
    public void  Phonecall(){
        if (!TextUtils.isEmpty((CharSequence) Phone)) {
            if (checkPermission(Manifest.permission.CALL_PHONE)) {
                String dial = "tel:" + Phone;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            } else {
                Toast.makeText(DealerDetails.this, "Permission Call Phone denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(DealerDetails.this, "Enter a phone number", Toast.LENGTH_SHORT).show();
        }
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            PhoneCall.setEnabled(true);
        } else {
            PhoneCall.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.riht_to_left);
    }

    public void loadData(final String dealname,String make,final int count){

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        Map<String, String> params = new HashMap<String, String>();
        params.put("Dealer_ID", dealname);
        if(make != null){
            params.put("Make", make);
        }else {
            params.put("Make", "");
        }
        if(String.valueOf(count)!="" && String.valueOf(count)!=null){
            params.put("count", String.valueOf(count));
        }else{
            params.put("count", "");
        }


        VolleyCustomPostRequest jsObjRequest = new VolleyCustomPostRequest(Request.Method.POST, Constants.dealerdetail_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub
                String  respo = response.toString();
               Log.e("Thump",respo);
               if(count==0) {
                   storeResponsedata(respo);
               }else{
                   Loadmoredata(respo);
               }

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
        DealerID.clear();
        milesList.clear();
        stockList.clear();
        ExteriorColor.clear();
        InteriorColor.clear();


        JSONObject json = null, SellingPrice = null, UploadCars=null, TrendingCars=null;

        try {
            if (!reponse.equals(null)) {
                json = new JSONObject(reponse);
                Dealername = json.getString("Dealer_Name");
                City = json.getString("City");
                BannerImage = json.getString("Banner_Image");
                Picasso.with(context).load(BannerImage).placeholder(R.drawable.logo).fit().into(bannerimage);
                dealername.setText(Dealername);
                dealerlocation.setText(City);

                if (json.has("Make list")) {
                    JSONArray Makelist = json.getJSONArray("Make list");
                    Log.e("Make_list", String.valueOf(Makelist));
                    for (int i = 0; i < Makelist.length(); i++) {
                        Makevalue = Makelist.getString(i);
                        if (!Makevalue.isEmpty()) {
                            MakeValueList.add(Makevalue);
                        }
                    }

                    if(!makedata) {
                        makeAdapter = new CustomSpinnerAdapter<String>(context, R.layout.custom_dealer_spinner_item);
                        makeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                        makeAdapter.add("Select Make");
                        makeAdapter.add("All");
                        makeAdapter.addAll(MakeValueList);
                        SpinnerWithMake.setAdapter(makeAdapter);
                        SpinnerWithMake.setSelection(0);
                    }

                }

                if (json.has("Result")) {
                    JSONArray sellingarray = json.getJSONArray("Result");

                    Log.e("Dealer_Car_List", String.valueOf(sellingarray.length()));
                    for (int i = 0; i < sellingarray.length(); i++) {
                        try {
                            JSONObject jsonobject = sellingarray.getJSONObject(i);
                            VIN.add(jsonobject.getString("VIN"));
                            bannerImageUrl.add(jsonobject.getString("Image_URL_Pattern"));
                            makeList.add(jsonobject.getString("Make"));
                            modelList.add(jsonobject.getString("Model"));
                            yearList.add(jsonobject.getString("Model_Year"));
                            priceList.add(jsonobject.getString("List_Price"));
                            DealerID.add(jsonobject.getString("Dealer_ID"));
                            milesList.add(jsonobject.getString("Odometer"));
                            //Log.e("Mileage",milesList.toString());
                            stockList.add(jsonobject.getString("Stock_Type"));
                            ExteriorColor.add(jsonobject.getString("Exterior_Base_Color"));
                            InteriorColor.add(jsonobject.getString("Interior_Base_Color"));
                            Phone = jsonobject.getString("Phone");
                            Email = jsonobject.getString("Email");
                            Lng = jsonobject.getString("Location_Longitude");
                            Lat = jsonobject.getString("Location_Latitude");
                            Address = jsonobject.getString("Address");
                            DealerCity = jsonobject.getString("City");
                            State = jsonobject.getString("State");
                            Postal = jsonobject.getString("PostalCode");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    DealerList = new DealerCarListAdapter(context, VIN, makeList, modelList, yearList, priceList, bannerImageUrl, City, DealerID, milesList, stockList, ExteriorColor, InteriorColor, DealerRecycler);
                    DealerRecycler.setAdapter(DealerList);
                    DealerRecycler.addOnScrollListener(new EndlessScrollListener(mLayoutManager) {
                        @Override
                        public void onLoadMore(int current_page) {
                            //add progress item
                            bannerImageUrl.add(null);
                            VIN.add(null);
                            bannerImageUrl.remove(bannerImageUrl.size() - 1);
                            VIN.remove(VIN.size() - 1);
                            DealerList.notifyItemRemoved(bannerImageUrl.size());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Count = Count + 30;
                                    loadData(Dealer_ID, SelectedMake, Count);
                                }
                            }, 100);
                        }
                    });
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }
    public void Loadmoredata(String reponse){

        JSONObject json = null, SellingPrice = null, UploadCars=null, TrendingCars=null;

        try {
            if (!reponse.equals(null)) {
                json = new JSONObject(reponse);
                Dealername = json.getString("Dealer_Name");
                City = json.getString("City");
                BannerImage = json.getString("Banner_Image");
                Picasso.with(context).load(BannerImage).placeholder(R.drawable.logo).fit().into(bannerimage);
                dealername.setText(Dealername);
                dealerlocation.setText(City);

                if (json.has("Result")) {
                    JSONArray sellingarray = json.getJSONArray("Result");
                    VIN.remove(VIN.size() - 1);
                    DealerList.notifyItemRemoved(VIN.size());
                    Log.e("Dealer_Car_List", String.valueOf(sellingarray.length()));
                    for (int i = 0; i < sellingarray.length(); i++) {
                        try {
                            JSONObject jsonobject = sellingarray.getJSONObject(i);

                            VIN.add(jsonobject.getString("VIN"));
                            bannerImageUrl.add(jsonobject.getString("Image_URL_Pattern"));
                            makeList.add(jsonobject.getString("Make"));
                            modelList.add(jsonobject.getString("Model"));
                            yearList.add(jsonobject.getString("Model_Year"));
                            priceList.add(jsonobject.getString("List_Price"));
                            DealerID.add(jsonobject.getString("Dealer_ID"));
                            milesList.add(jsonobject.getString("Odometer"));
                            //Log.e("Mileage",milesList.toString());
                            stockList.add(jsonobject.getString("Stock_Type"));
                            ExteriorColor.add(jsonobject.getString("Exterior_Base_Color"));
                            InteriorColor.add(jsonobject.getString("Interior_Base_Color"));
                            Phone = jsonobject.getString("Phone");
                            Email = jsonobject.getString("Email");
                            Lng = jsonobject.getString("Location_Longitude");
                            Lat = jsonobject.getString("Location_Latitude");
                            Address = jsonobject.getString("Address");
                            DealerCity = jsonobject.getString("City");
                            State = jsonobject.getString("State");
                            Postal = jsonobject.getString("PostalCode");

                            DealerList.notifyItemInserted(VIN.size());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //DealerList.notifyDataSetChanged();
                    DealerList.setLoaded();

                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
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
                        Count=0;
                        SelectedMake = "";
                        Log.e("Select Make", SelectedMake);
                        makedata = true;

                }else if(position == 1){
                    Count=0;
                    SelectedMake = "";
                    Log.e("Select Make",SelectedMake);
                    makedata = true;
                    loadData(Dealer_ID,SelectedMake,Count);


                }else{
                    Count=0;
                    SelectedMake = MakeValueList.get(position-2);
                    makedata = true;
                    loadData(Dealer_ID,SelectedMake,Count);
                    Log.e("Select Make",SelectedMake);


                }

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE :
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    PhoneCall.setEnabled(true);
                    Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
    public void showChangeLangDialog(String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alert);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        final TextView messagecontent = (TextView) dialog.findViewById(R.id.messagealert);
        final Button okbutton =(Button) dialog.findViewById(R.id.okbutton);
        final Button cancelbutton =(Button) dialog.findViewById(R.id.cancelbutton);
        messagecontent.setText(message);
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Phonecall();
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
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validateFirstName( String firstName )
    {
        return firstName.matches( "[A-Z][a-zA-Z]*" );
    }
    public static boolean validateLastName( String lastName )
    {
        return lastName.matches( "[a-zA-z]+([ '-][a-zA-Z]+)*" );
    }
    private boolean isValidPhone(String phone)
    {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone))
        {
            if(phone.length() < 6 || phone.length() > 13)
            {
                check = false;
            }
            else
            {
                check = true;
            }
        }else
        {
            check=false;
        }
        return check;
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

}
