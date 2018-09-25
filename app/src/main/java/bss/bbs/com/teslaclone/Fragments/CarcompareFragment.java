package bss.bbs.com.teslaclone.Fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import bss.bbs.com.teslaclone.Adapter.CustomSpinnerAdapter;
import bss.bbs.com.teslaclone.Adapter.TableLayoutAdapter;
import bss.bbs.com.teslaclone.R;
import bss.bbs.com.teslaclone.Singleton.AppController;
import bss.bbs.com.teslaclone.Singleton.Constants;
import bss.bbs.com.teslaclone.network.VolleyCustomPostRequest;

import static bss.bbs.com.teslaclone.R.drawable.border_radius;



@SuppressLint("ValidFragment")
public class CarcompareFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    Context context;
    private View parentView;
    ImageView Firstimage,Secondimage;
    TextView Firstmake,Sceondmake,Firstmodel,Secondmodel,Popuptext,Basic_Text,Specification_Text,Feature_Text;
    Button comparebutton,cancel,search;
    LinearLayout firstlayout,secondlayout,Basicbutton,Specbutton,Featurebutton;
    String firstmake,secondmake,firstmodel,secondmodel,firstimage,firstextcolor,firstintcolor,secondimage,firstvin,secondvin,firstyear,secondyear,secondextcolor,secondintcolor,identifyPopUp;
    Dialog dialog;
    Spinner Makespinner,Modelspinner,Yearspinner;

    ArrayList<String> MakeList = new ArrayList<>();
    ArrayList<String> ModelList = new ArrayList<>();
    ArrayList<String> YearList = new ArrayList<>();
    CustomSpinnerAdapter<String> makeAdapter,modelAdapter,yearAdapter;
    String selectedmake,selectedmodel,selectedyear;
    JSONObject Firstcar,Secondcar,Firstcarsquash,Secondcarsquash = null;
    String FirstcarDeatil ="Not Send";
    String SecondcarDetail = "Not Send";
    JSONObject Exterior_Dimension,Steering_object,Suspension_object,Basic_Hole_Object,Spec_Hole_Object,Featuer_Hole_Object;
    RecyclerView TablelayoutRecycler;
    RecyclerView.LayoutManager Tablelayout;
    RecyclerView.Adapter TablelayoutAdapter;

    @SuppressLint("ValidFragment")
    public CarcompareFragment(Context con) {
        // Required empty public constructor
        context = con;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.compare, container, false);
        TextView titleview1 = (TextView) getActivity().findViewById(R.id.titleid);
        titleview1.setText("Car Comparison");
        setUpViews(parentView);

        edmundsData(AppController.getInstance().EdmundsResponse);

        return parentView;
    }

    private void edmundsData(String edmundsResponse) {
        Log.e("edmundsResponse",edmundsResponse);
        JSONObject edmundjson = null;
        JSONArray makesArray;

        try {
            if(!edmundsResponse.equals(null)){
            edmundjson = new JSONObject(edmundsResponse);
            makesArray = edmundjson.getJSONArray("makes");
            //Log.e("EdmundsData",String .valueOf(makesArray.length()));
            for (int index = 0; index < makesArray.length(); index++) {
                JSONObject makes_obj = makesArray.getJSONObject(index);
                //String make_name = makes_obj.getString("name");
                if (!makes_obj.getString("name").equals("aston-martin") && !makes_obj.getString("name").equals("avanti") && !makes_obj.getString("name").equals("daewoo") && !makes_obj.getString("name").equals("fisker") && !makes_obj.getString("name").equals("isuzu") && !makes_obj.getString("name").equals("oldsmobile")) {
                    MakeList.add(makes_obj.getString("name"));
                    Collections.sort(MakeList);
                }
            }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUpViews(View upViews) {

        Firstimage = (ImageView) upViews.findViewById(R.id.img_thumbnail1);
        Secondimage = (ImageView) upViews.findViewById(R.id.img_thumbnail2);
        Firstmake = (TextView) upViews.findViewById(R.id.Trending_make);
        Sceondmake = (TextView) upViews.findViewById(R.id.Trending_Secocnd_make);
        Firstmodel = (TextView) upViews.findViewById(R.id.Trending_model);
        Secondmodel = (TextView) upViews.findViewById(R.id.Trending_Secocnd_model);
        comparebutton = (Button) upViews.findViewById(R.id.Compare_Button);
        firstlayout = (LinearLayout) upViews.findViewById(R.id.first_layout);
        secondlayout = (LinearLayout) upViews.findViewById(R.id.second_layout);
        Basicbutton = (LinearLayout) upViews.findViewById(R.id.Basic_Button);
        Specbutton = (LinearLayout) upViews.findViewById(R.id.Spec_Button);
        Featurebutton = (LinearLayout) upViews.findViewById(R.id.Feature_Button);
        Basic_Text = (TextView) upViews.findViewById(R.id.Basic_Text);
        Specification_Text = (TextView) upViews.findViewById(R.id.Specification_Text);
        Feature_Text = (TextView) upViews.findViewById(R.id.Feature_Text);

        TablelayoutRecycler = (RecyclerView) upViews.findViewById(R.id.Table_recycler);
        Tablelayout = new LinearLayoutManager(context);
        TablelayoutRecycler.setLayoutManager(Tablelayout);

        comparebutton.setOnClickListener(this);
        firstlayout.setOnClickListener(this);
        secondlayout.setOnClickListener(this);
        Basicbutton.setOnClickListener(this);
        Specbutton.setOnClickListener(this);
        Featurebutton.setOnClickListener(this);

        Basicbutton.setBackgroundColor(Color.DKGRAY);
        Basic_Text.setTextColor(Color.WHITE);

        Bundle args = getArguments();

        if(args != null) {
            if (args.getString("Second_Vin") != null) {
                firstimage = args.getString("First_Image");
                firstmake = args.getString("First_Make");
                firstmodel = args.getString("First_Model");
                firstyear = args.getString("First_Year");
                firstvin = args.getString("First_Vin");
                firstextcolor = args.getString("First_Ext_Color");
                firstintcolor = args.getString("First_Int_Color");
                secondimage = args.getString("Second_Image");
                secondmake = args.getString("Second_Make");
                secondmodel = args.getString("Second_Model");
                secondyear = args.getString("Second_Year");
                secondvin = args.getString("Second_Vin");
                secondextcolor = args.getString("First_Ext_Color");
                secondintcolor = args.getString("First_Int_Color");

                Picasso.with(this.context).load(firstimage).placeholder(R.drawable.logo).fit().into(Firstimage);
                Picasso.with(this.context).load(secondimage).placeholder(R.drawable.logo).fit().into(Secondimage);
                Firstmake.setText(firstmake);
                Firstmake.setVisibility(View.VISIBLE);
                Sceondmake.setText(secondmake);
                Sceondmake.setVisibility(View.VISIBLE);
                Firstmodel.setText(firstmodel + " " + firstyear);
                Secondmodel.setText(secondmodel + " " + secondyear);
                comparebutton.setClickable(false);
                edmundsFirstVehicleDetail(firstvin);
                edmundsSecondVehicleDetail(secondvin);
                //Basic_Text.setBackgroundColor(Color.DKGRAY);
                Basicbutton.setBackgroundColor(Color.DKGRAY);
                Basic_Text.setTextColor(Color.WHITE);

            }else{
                firstimage = args.getString("First_Image");
                firstmake = args.getString("First_Make");
                firstmodel = args.getString("First_Model");
                firstyear = args.getString("First_Year");
                firstvin = args.getString("First_Vin");
                firstextcolor = args.getString("First_Ext_Color");
                firstintcolor = args.getString("First_Int_Color");

                Picasso.with(this.context).load(firstimage).placeholder(R.drawable.logo).fit().into(Firstimage);
                Firstmake.setText(firstmake);
                Firstmake.setVisibility(View.VISIBLE);
                Firstmodel.setText(firstmodel + " " + firstyear);
                comparebutton.setClickable(false);
                edmundsFirstVehicleDetail(firstvin);
                Basicbutton.setBackgroundColor(Color.DKGRAY);
                Basic_Text.setTextColor(Color.WHITE);
            }
        }
    }

    private void filterPopupWindow(String data) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.comparepopup_layout);
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        Popuptext = (TextView) dialog.findViewById(R.id.Popup_Text);
        Makespinner = (Spinner) dialog.findViewById(R.id.Make_spinner);
        Modelspinner = (Spinner) dialog.findViewById(R.id.Model_spinner);
        Yearspinner = (Spinner) dialog.findViewById(R.id.Year_spinner);
        cancel = (Button) dialog.findViewById(R.id.cancel_id);
        search = (Button) dialog.findViewById(R.id.search_id);

        if(data.equals("First")){
            identifyPopUp = data;
            Popuptext.setText("Select first car to compare");
        }else{
            identifyPopUp = data;
            Popuptext.setText("Select second car to compare");
        }

        cancel.setOnClickListener(this);
        search.setOnClickListener(this);
        Makespinner.setOnItemSelectedListener(this);
        Modelspinner.setOnItemSelectedListener(this);
        Yearspinner.setOnItemSelectedListener(this);

        //Log.e("Arraylength",MakeList.toString());
        makeAdapter = new CustomSpinnerAdapter<String>(context, R.layout.custom_dealer_spinner_item);
        makeAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        makeAdapter.add("Select Make");
        makeAdapter.addAll(MakeList);
        Makespinner.setAdapter(makeAdapter);
        Makespinner.setSelection(0);

        modelAdapter = new CustomSpinnerAdapter<String>(context, R.layout.custom_dealer_spinner_item);
        modelAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        modelAdapter.add("Select Model");
        Modelspinner.setAdapter(modelAdapter);
        Modelspinner.setSelection(0);

        yearAdapter = new CustomSpinnerAdapter<String>(context, R.layout.custom_dealer_spinner_item);
        yearAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        yearAdapter.add("Select Year");
        Yearspinner.setAdapter(yearAdapter);
        Yearspinner.setSelection(0);

        dialog.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.Compare_Button:
                FirstcarDeatil ="Not Send";
                SecondcarDetail = "Not Send";
                edmundsFirstVehicleDetail(firstvin);
                edmundsSecondVehicleDetail(secondvin);
                comparebutton.setClickable(false);
                comparebutton.setBackgroundColor(getResources().getColor(R.color.Darkgray));
                break;
            case R.id.first_layout:
                selectedmake="";
                selectedyear="";
                selectedmodel="";
                filterPopupWindow("First");
                break;
            case R.id.second_layout:
                selectedmake="";
                selectedyear="";
                selectedmodel="";
                filterPopupWindow("Second");
                break;
            case R.id.cancel_id:
                dialog.dismiss();
                break;
            case R.id.search_id:
                if(!selectedmake.equals("")&& !selectedmodel.equals("")&& !selectedyear.equals("")) {
                    //dialog.dismiss();
                    getMathchCar();
                }else{
                    getMathchCar();
                }
                break;
            case R.id.Basic_Button:
                Basicbutton.setBackgroundColor(Color.DKGRAY);
                Specbutton.setBackground(getResources().getDrawable(R.drawable.border_radius));
                Featurebutton.setBackground(getResources().getDrawable(R.drawable.border_radius));
                Basic_Text.setTextColor(Color.WHITE);
                Specification_Text.setTextColor(Color.GRAY);
                Feature_Text.setTextColor(Color.GRAY);
                TablelayoutAdapter = new TableLayoutAdapter(context,Basic_Hole_Object,"Compare");
                TablelayoutRecycler.setAdapter(TablelayoutAdapter);
                break;
            case R.id.Spec_Button:
//                Basic_Text.setBackgroundColor(Color.WHITE);
//                Specification_Text.setBackgroundColor(Color.DKGRAY);
//                Feature_Text.setBackgroundColor(Color.WHITE);
                Basicbutton.setBackground(getResources().getDrawable(R.drawable.border_radius));
                Specbutton.setBackgroundColor(Color.DKGRAY);
                Featurebutton.setBackground(getResources().getDrawable(R.drawable.border_radius));
                Basic_Text.setTextColor(Color.GRAY);
                Specification_Text.setTextColor(Color.WHITE);
                Feature_Text.setTextColor(Color.GRAY);
                TablelayoutAdapter = new TableLayoutAdapter(context,Spec_Hole_Object,"Compare");
                TablelayoutRecycler.setAdapter(TablelayoutAdapter);
                break;
            case R.id.Feature_Button:
//                Basic_Text.setBackgroundColor(Color.WHITE);
//                Specification_Text.setBackgroundColor(Color.WHITE);
//                Feature_Text.setBackgroundColor(Color.DKGRAY);
                Basicbutton.setBackground(getResources().getDrawable(R.drawable.border_radius));
                Specbutton.setBackground(getResources().getDrawable(R.drawable.border_radius));
                Featurebutton.setBackgroundColor(Color.DKGRAY);
                Basic_Text.setTextColor(Color.GRAY);
                Specification_Text.setTextColor(Color.GRAY);
                Feature_Text.setTextColor(Color.WHITE);
                TablelayoutAdapter = new TableLayoutAdapter(context,Featuer_Hole_Object,"Compare");
                TablelayoutRecycler.setAdapter(TablelayoutAdapter);
                break;
        }

    }

    private void edmundsFirstVehicleDetail(String vin) {
        //Toast.makeText(context, "Button should work!", Toast.LENGTH_SHORT).show();

        Map<String, String> params = new HashMap<String, String>();
        String URL = Constants.Edmundsvin_Detail + vin + "&" + Constants.Edmunds_Key;


        VolleyCustomPostRequest jsObjRequest = new VolleyCustomPostRequest(URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub

                //FirstcarDeatil = "Available";
                try {
                   if(!response.toString().equals(null)) {
                       Firstcar = new JSONObject(response.toString());
                       JSONArray FirstHoleder = Firstcar.getJSONArray("styleHolder");
                       for (int i = 0; i < FirstHoleder.length(); i++) {
                           JSONObject Firststyleobject = FirstHoleder.optJSONObject(i);
                           JSONArray Firstsquishvin = Firststyleobject.getJSONArray("squishVins");
                           JSONObject Firstvinobject = Firstsquishvin.getJSONObject(0);
                           String FirstSqashVIN = Firstvinobject.getString("squishVin");
                           Log.e("Squashidfirst", FirstSqashVIN);
                           edumandFirtSquashVIN(FirstSqashVIN);
                       }
                   }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //parseEdundsRespnseData();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

                NetworkResponse response = error.networkResponse;
                Log.e("Vin_Error", error.toString());
                FirstcarDeatil = "Not Available";
                parseEdundsRespnseData();

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

    private void edmundsSecondVehicleDetail(String vin) {
        //Toast.makeText(context, "Button should work!", Toast.LENGTH_SHORT).show();

        Map<String, String> params = new HashMap<String, String>();
        String URL = Constants.Edmundsvin_Detail + vin + "&" + Constants.Edmunds_Key;


        VolleyCustomPostRequest jsObjRequest = new VolleyCustomPostRequest(URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub
               // SecondcarDetail = "Available";
                try {
                    if(!response.toString().equals(null)){
                        Secondcar = new JSONObject(response.toString());
                    JSONArray SecondHolder = Secondcar.getJSONArray("styleHolder");
                    for (int i = 0; i < SecondHolder.length(); i++) {
                        JSONObject Secondstyleobject = SecondHolder.optJSONObject(i);
                        JSONArray Secondsquishvin = Secondstyleobject.getJSONArray("squishVins");
                        JSONObject Secondvinobject = Secondsquishvin.getJSONObject(0);
                        String SecondSquashVIN = Secondvinobject.getString("squishVin");
                        Log.e("Squashidsecond", SecondSquashVIN);
                        edumandSecondSquashVIN(SecondSquashVIN);
                    }
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //parseEdundsRespnseData();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

                NetworkResponse response = error.networkResponse;
                Log.e("Vin_Error", error.toString());
                SecondcarDetail = "Not Available";
                parseEdundsRespnseData();

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

    private void parseEdundsRespnseData() {
        Basic_Hole_Object = new JSONObject();
        Spec_Hole_Object = new JSONObject();
        Featuer_Hole_Object = new JSONObject();
        JSONObject Basics = null,OverallInfo = null,Sellerinfo = null;
        Exterior_Dimension = new JSONObject();
        Steering_object = new JSONObject();
        Suspension_object = new JSONObject();
        if(FirstcarDeatil.equals("Available") && SecondcarDetail.equals("Available")){

            try {

                JSONArray FirstHoleder = Firstcar.getJSONArray("styleHolder");
                JSONArray SecondHolder = Secondcar.getJSONArray("styleHolder");
                JSONObject Firststyleobject = FirstHoleder.optJSONObject(0);
                JSONObject Secondstyleobject = SecondHolder.optJSONObject(0);

                //********************* Trim *********************//
                JSONObject FirstTrimobject = Firststyleobject.getJSONObject("trim");
                JSONObject SecondTrimobject = Secondstyleobject.getJSONObject("trim");

                //********************* Body Type *********************//
                JSONObject Firstcatagories = Firststyleobject.getJSONObject("categories");
                JSONArray Firstvehicletype = Firstcatagories.getJSONArray("Vehicle Style");

                JSONObject Secondcatagories = Secondstyleobject.getJSONObject("categories");
                JSONArray Secondvehicletype = Secondcatagories.getJSONArray("Vehicle Style");

                    //********************* attributeGroups *********************//
                    JSONObject Firstattributgroup = Firststyleobject.getJSONObject("attributeGroups");
                    JSONObject Secondattributgroup = Secondstyleobject.getJSONObject("attributeGroups");

                    //**EXTERIOR DIMENSION//
                    JSONObject FirstExteriordimension = Firstattributgroup.getJSONObject("EXTERIOR_DIMENSIONS");
                    JSONObject SecondExteriordimension = Secondattributgroup.getJSONObject("EXTERIOR_DIMENSIONS");
                    JSONObject Firstattribute = FirstExteriordimension.getJSONObject("attributes");
                    JSONObject Secondattribute = SecondExteriordimension.getJSONObject("attributes");
                    Iterator iterator = Firstattribute.keys();
                    while(iterator.hasNext()){
                        String key = (String)iterator.next();
                        String Firstvalue,Secondvalue;
                        JSONObject Firstattrobject = Firstattribute.getJSONObject(key);
                        Firstvalue = Firstattrobject.getString("value");
                        if(Secondattribute.has(key)){
                            JSONObject Secondattrobject = Secondattribute.getJSONObject(key);
                            Secondvalue = Secondattrobject.getString("value");
                        }else{
                            Secondvalue = "Not Available";
                        }

                        Exterior_Dimension.put(Firstvalue,Secondvalue);
                        Log.e("Exterior_Dimension",Exterior_Dimension.toString());
                    }

                    //**STEERING//
                    JSONObject FirstSTEERING = Firstattributgroup.getJSONObject("STEERING");
                    JSONObject SecondSTEERING = Secondattributgroup.getJSONObject("STEERING");

                    JSONObject FirstSTEERING_attributes = FirstSTEERING.getJSONObject("attributes");
                    JSONObject SecondSTEERING_attributes = SecondSTEERING.getJSONObject("attributes");
                    JSONObject FirstPOWER_STEERING = FirstSTEERING_attributes.getJSONObject("POWER_STEERING");
                    JSONObject SecondPOWER_STEERING = SecondSTEERING_attributes.getJSONObject("POWER_STEERING");
                    Steering_object.put(FirstPOWER_STEERING.getString("value"),SecondPOWER_STEERING.getString("value"));
                    Log.e("Steering_object",Steering_object.toString());

                    //**SUSPENSION//
                    JSONObject FirstSUSPENSION = Firstattributgroup.getJSONObject("SUSPENSION");
                    JSONObject SecondSUSPENSION = Secondattributgroup.getJSONObject("SUSPENSION");

                    JSONObject FirstSUSPENSION_attributes = FirstSUSPENSION.getJSONObject("attributes");
                    JSONObject SecondSUSPENSION_attributes = SecondSUSPENSION.getJSONObject("attributes");
                    Iterator iterator2 = FirstSUSPENSION_attributes.keys();
                    while(iterator2.hasNext()){

                        String key = (String)iterator2.next();
                        String Firstvalue,Secondvalue;
                        JSONObject FirstSUSPENSION_attrobject = FirstSUSPENSION_attributes.getJSONObject(key);
                        Firstvalue = FirstSUSPENSION_attrobject.getString("value");
                        if(SecondSUSPENSION_attributes.has(key)){
                            JSONObject SecondSUSPENSION_attrobject = SecondSUSPENSION_attributes.getJSONObject(key);
                            Secondvalue = SecondSUSPENSION_attrobject.getString("value");
                        }else{
                            Secondvalue = "Not Available";
                        }

                        Suspension_object.put(Firstvalue,Secondvalue);
                        Log.e("Suspension_object",Suspension_object.toString());
                    }


                //********************* FEATURE DATAS *********************//
                //**MISC>_EXTERIOR//
                JSONObject Miscvalue = new JSONObject();
                if(Firstattributgroup.has("MISC._EXTERIOR_FEATURES") && Secondattributgroup.has("MISC._EXTERIOR_FEATURES")) {
                    JSONObject FirstMISC = Firstattributgroup.getJSONObject("MISC._EXTERIOR_FEATURES");
                    JSONObject SecondMISC = Secondattributgroup.getJSONObject("MISC._EXTERIOR_FEATURES");
                    String Misc_Title = FirstMISC.getString("name");
                    JSONObject FirstMISC_attributes = FirstMISC.getJSONObject("attributes");
                    JSONObject SecondMISC_attributes = SecondMISC.getJSONObject("attributes");
                    Iterator iterator3 = FirstMISC_attributes.keys();
                    while (iterator3.hasNext()) {

                        String key = (String) iterator3.next();
                        String Firstvalue,Secondvalue;
                        JSONObject FirstMISC_attrobject = FirstMISC_attributes.getJSONObject(key);
                        Firstvalue = FirstMISC_attrobject.getString("value");
                        if(SecondMISC_attributes.has(key)){
                            JSONObject SecondtMISC_attrobject = SecondMISC_attributes.getJSONObject(key);
                            Secondvalue = SecondtMISC_attrobject.getString("value");
                        }else{
                            Secondvalue = "Not Available";
                        }
                        //String Suspensionname = MISC_attrobject.getString("name");
                        //String Suspensiovalue = MISC_attrobject.getString("value");
                        Miscvalue.put(Firstvalue, Secondvalue);

                    }
                    Featuer_Hole_Object.put(Misc_Title,Miscvalue);
                }

                //**CONSUMER_GENERIC_FEATURE//
                JSONObject Consumer = new JSONObject();
                if(Firstattributgroup.has("CONSUMER_GENERIC_FEATURE") && Secondattributgroup.has("CONSUMER_GENERIC_FEATURE")) {
                    JSONObject FirstCONSUMER = Firstattributgroup.getJSONObject("CONSUMER_GENERIC_FEATURE");
                    JSONObject SecondCONSUMER = Secondattributgroup.getJSONObject("CONSUMER_GENERIC_FEATURE");
                    String CONSUMER_Title = FirstCONSUMER.getString("name");

                    JSONObject FirstCONSUMER_attributes = FirstCONSUMER.getJSONObject("attributes");
                    JSONObject SecondCONSUMER_attributes = SecondCONSUMER.getJSONObject("attributes");
                    Iterator iterator4 = FirstCONSUMER_attributes.keys();
                    while (iterator4.hasNext()) {

                        String key = (String) iterator4.next();
                        String Firstvalue,Secondvalue;
                        JSONObject FirstCONSUMER_attrobject = FirstCONSUMER_attributes.getJSONObject(key);
                        Firstvalue = FirstCONSUMER_attrobject.getString("value");
                        if(SecondCONSUMER_attributes.has(key)){
                            JSONObject SecondCONSUMER_attrobject = SecondCONSUMER_attributes.getJSONObject(key);
                            Secondvalue = SecondCONSUMER_attrobject.getString("value");
                        }else{
                            Secondvalue = "Not Available";
                        }
                        //String Suspensionname = CONSUMER_attrobject.getString("name");
                        //String Suspensiovalue = CONSUMER_attrobject.getString("value");
                        Consumer.put(Firstvalue, Secondvalue);

                    }
                    Featuer_Hole_Object.put(CONSUMER_Title,Consumer);
                }

                //**MISC._INTERIOR_FEATURES//
                JSONObject INTERIOR = new JSONObject();
                if(Firstattributgroup.has("MISC._INTERIOR_FEATURES") && Secondattributgroup.has("MISC._INTERIOR_FEATURES")) {
                    JSONObject FirstMisc_INTERIOR = Firstattributgroup.getJSONObject("MISC._INTERIOR_FEATURES");
                    JSONObject SecondtMisc_INTERIOR = Firstattributgroup.getJSONObject("MISC._INTERIOR_FEATURES");
                    String INTERIOR_Title = FirstMisc_INTERIOR.getString("name");

                    JSONObject FirstINTERIOR_attributes = FirstMisc_INTERIOR.getJSONObject("attributes");
                    JSONObject SecondINTERIOR_attributes = SecondtMisc_INTERIOR.getJSONObject("attributes");
                    Iterator iterator5 = FirstINTERIOR_attributes.keys();
                    while (iterator5.hasNext()) {

                        String key = (String) iterator5.next();
                        String Firstvalue,Secondvalue;
                        JSONObject FirstINTERIORattrobject = FirstINTERIOR_attributes.getJSONObject(key);
                        Firstvalue = FirstINTERIORattrobject.getString("value");
                        if(SecondINTERIOR_attributes.has(key)){
                            JSONObject SecondINTERIORattrobject = SecondINTERIOR_attributes.getJSONObject(key);
                            Secondvalue = SecondINTERIORattrobject.getString("value");
                        }else{
                            Secondvalue = "Not Available";
                        }
                        //String Suspensionname = INTERIORattrobject.getString("name");
                        //String Suspensiovalue = INTERIORattrobject.getString("value");
                        INTERIOR.put(Firstvalue, Secondvalue);

                    }
                    Featuer_Hole_Object.put(INTERIOR_Title,INTERIOR);
                }

                //**AIR_CONDITIONING//
                JSONObject AIR_CONDITIONING = new JSONObject();
                if(Firstattributgroup.has("AIR_CONDITIONING") && Secondattributgroup.has("AIR_CONDITIONING")) {
                    JSONObject FirstAir_con = Firstattributgroup.getJSONObject("AIR_CONDITIONING");
                    JSONObject SecondAir_con = Firstattributgroup.getJSONObject("AIR_CONDITIONING");
                    String AIR_Title = FirstAir_con.getString("name");

                    JSONObject FirstAir_attributes = FirstAir_con.getJSONObject("attributes");
                    JSONObject SecondAir_attributes = SecondAir_con.getJSONObject("attributes");
                    Iterator iterator6 = FirstAir_attributes.keys();
                    while (iterator6.hasNext()) {

                        String key = (String) iterator6.next();
                        String Firstvalue,Secondvalue;
                        JSONObject FirstAir_attrobject = FirstAir_attributes.getJSONObject(key);
                        Firstvalue = FirstAir_attrobject.getString("value");
                        if(SecondAir_attributes.has(key)){
                            JSONObject SecondAir_attrobject = SecondAir_attributes.getJSONObject(key);
                            Secondvalue = SecondAir_attrobject.getString("value");
                        }else {
                            Secondvalue = "Not Available";
                        }
                        //String Suspensionname = Air_attrobject.getString("name");
                        //String Suspensiovalue = Air_attrobject.getString("value");
                        AIR_CONDITIONING.put(Firstvalue, Secondvalue);

                    }
                    Featuer_Hole_Object.put(AIR_Title,AIR_CONDITIONING);
                }

                //**STORAGE//
                JSONObject STORAGE = new JSONObject();
                if(Firstattributgroup.has("STORAGE") && Secondattributgroup.has("STORAGE")) {
                    JSONObject FirstSTORAGE_con = Firstattributgroup.getJSONObject("STORAGE");
                    JSONObject SecondSTORAGE_con = Secondattributgroup.getJSONObject("STORAGE");
                    String STORAGE_Title = FirstSTORAGE_con.getString("name");

                    JSONObject FirstSTORAGE_attributes = FirstSTORAGE_con.getJSONObject("attributes");
                    JSONObject SecondSTORAGE_attributes = SecondSTORAGE_con.getJSONObject("attributes");
                    Iterator iterator7 = FirstSTORAGE_attributes.keys();
                    while (iterator7.hasNext()) {

                        String key = (String) iterator7.next();
                        String Firstvalue,Secondvalue;
                        JSONObject FirstSTORAGE_attrobject = FirstSTORAGE_attributes.getJSONObject(key);
                        Firstvalue = FirstSTORAGE_attrobject.getString("value");
                        if(SecondSTORAGE_attributes.has(key)){
                            JSONObject SecondSTORAGE_attrobject = SecondSTORAGE_attributes.getJSONObject(key);
                            Secondvalue = SecondSTORAGE_attrobject.getString("value");
                        }else{
                            Secondvalue = "Not Available";
                        }
                        //String Suspensionname = STORAGE_attrobject.getString("name");
                        //String Suspensiovalue = STORAGE_attrobject.getString("value");
                        STORAGE.put(Firstvalue, Secondvalue);

                    }
                    Featuer_Hole_Object.put(STORAGE_Title,STORAGE);
                }

                //**MOBILE_CONNECTIVITY//
                JSONObject MOBILE_CONNECTIVITY = new JSONObject();
                if(Firstattributgroup.has("MOBILE_CONNECTIVITY") && Secondattributgroup.has("MOBILE_CONNECTIVITY")) {
                    JSONObject FirstMOBILE_con = Firstattributgroup.getJSONObject("MOBILE_CONNECTIVITY");
                    JSONObject SecondMOBILE_con = Secondattributgroup.getJSONObject("MOBILE_CONNECTIVITY");
                    String MOBILE_Title = FirstMOBILE_con.getString("name");

                    JSONObject FirstMOBILE_attributes = FirstMOBILE_con.getJSONObject("attributes");
                    JSONObject SecondMOBILE_attributes = SecondMOBILE_con.getJSONObject("attributes");
                    Iterator iterator8 = FirstMOBILE_attributes.keys();
                    while (iterator8.hasNext()) {

                        String key = (String) iterator8.next();
                        String Firstvalue,Secondvalue;
                        JSONObject FirstMOBILE_attrobject = FirstMOBILE_attributes.getJSONObject(key);
                        Firstvalue = FirstMOBILE_attrobject.getString("value");
                        if(SecondMOBILE_attributes.has(key)){
                            JSONObject SecondMOBILE_attrobject = SecondMOBILE_attributes.getJSONObject(key);
                            Secondvalue = SecondMOBILE_attrobject.getString("value");
                        }else{
                            Secondvalue = "Not Available";
                        }
                        //String Suspensionname = MOBILE_attrobject.getString("name");
                        //String Suspensiovalue = MOBILE_attrobject.getString("value");
                        MOBILE_CONNECTIVITY.put(Firstvalue, Secondvalue);

                    }
                    Featuer_Hole_Object.put(MOBILE_Title,MOBILE_CONNECTIVITY);
                }

                //**EXTERIOR_LIGHTS//
                JSONObject EXTERIOR_LIGHTS = new JSONObject();
                if(Firstattributgroup.has("EXTERIOR_LIGHTS") && Secondattributgroup.has("EXTERIOR_LIGHTS")) {
                    JSONObject FirstEXTERIOR_con = Firstattributgroup.getJSONObject("EXTERIOR_LIGHTS");
                    JSONObject SecondEXTERIOR_con = Secondattributgroup.getJSONObject("EXTERIOR_LIGHTS");
                    String EXTERIOR_Title = FirstEXTERIOR_con.getString("name");

                    JSONObject FirstEXTERIOR_attributes = FirstEXTERIOR_con.getJSONObject("attributes");
                    JSONObject SecondEXTERIOR_attributes = SecondEXTERIOR_con.getJSONObject("attributes");
                    Iterator iterator9 = FirstEXTERIOR_attributes.keys();
                    while (iterator9.hasNext()) {

                        String key = (String) iterator9.next();
                        String Firstvalue,Secondvalue;
                        JSONObject FirstEXTERIOR_attrobject = FirstEXTERIOR_attributes.getJSONObject(key);
                        Firstvalue = FirstEXTERIOR_attrobject.getString("value");
                        if(SecondEXTERIOR_attributes.has(key)){
                            JSONObject SecondEXTERIOR_attrobject = SecondEXTERIOR_attributes.getJSONObject(key);
                            Secondvalue = SecondEXTERIOR_attrobject.getString("value");
                        }else{
                            Secondvalue = "Not Available";
                        }
                        //String Suspensionname = EXTERIOR_attrobject.getString("name");
                        //String Suspensiovalue = EXTERIOR_attrobject.getString("value");
                        EXTERIOR_LIGHTS.put(Firstvalue, Secondvalue);

                    }
                    Featuer_Hole_Object.put(EXTERIOR_Title,EXTERIOR_LIGHTS);
                }

                //**POWER_OUTLETS//
                JSONObject POWER_OUTLETS = new JSONObject();
                if (Firstattributgroup.has("POWER_OUTLETS") && Secondattributgroup.has("POWER_OUTLETS")) {
                    JSONObject FirstPOWER_con = Firstattributgroup.getJSONObject("POWER_OUTLETS");
                    JSONObject SecondPOWER_con = Secondattributgroup.getJSONObject("POWER_OUTLETS");
                    String POWER_Title = FirstPOWER_con.getString("name");

                    JSONObject FirstPOWER_attributes = FirstPOWER_con.getJSONObject("attributes");
                    JSONObject SecondPOWER_attributes = SecondPOWER_con.getJSONObject("attributes");
                    Iterator iterator10 = FirstPOWER_attributes.keys();
                    while (iterator10.hasNext()) {

                        String key = (String) iterator10.next();
                        String Firstvalue,Secondvalue;
                        JSONObject FirstPOWER_attrobject = FirstPOWER_attributes.getJSONObject(key);
                        Firstvalue = FirstPOWER_attrobject.getString("value");
                        if(SecondPOWER_attributes.has(key)){
                            JSONObject SecondPOWER_attrobject = SecondPOWER_attributes.getJSONObject(key);
                            Secondvalue = SecondPOWER_attrobject.getString("value");
                        }else{
                            Secondvalue = "Not Available";
                        }
                        //String Suspensionname = POWER_attrobject.getString("name");
                       // String Suspensiovalue = POWER_attrobject.getString("value");
                        POWER_OUTLETS.put(Firstvalue, Secondvalue);

                    }
                    Featuer_Hole_Object.put(POWER_Title,POWER_OUTLETS);
                }

                //**SECURITY//
                JSONObject SECURITY = new JSONObject();
                if(Firstattributgroup.has("SECURITY") && Secondattributgroup.has("SECURITY")) {
                    JSONObject FirstSECURITY_con = Firstattributgroup.getJSONObject("SECURITY");
                    JSONObject SecondSECURITY_con = Secondattributgroup.getJSONObject("SECURITY");
                    String SECURITY_Title = FirstSECURITY_con.getString("name");

                    JSONObject FirstSECURITY_attributes = FirstSECURITY_con.getJSONObject("attributes");
                    JSONObject SecondSECURITY_attributes = FirstSECURITY_con.getJSONObject("attributes");
                    Iterator iterator11 = FirstSECURITY_attributes.keys();
                    while (iterator11.hasNext()) {

                        String key = (String) iterator11.next();
                        String Firstvalue,Secondvalue;
                        JSONObject FirstSECURITY_attrobject = FirstSECURITY_attributes.getJSONObject(key);
                        Firstvalue = FirstSECURITY_attrobject.getString("value");
                        if(SecondSECURITY_attributes.has(key)){
                            JSONObject SecondSECURITY_attrobject = SecondSECURITY_attributes.getJSONObject(key);
                            Secondvalue = SecondSECURITY_attrobject.getString("value");
                        }else{
                            Secondvalue = "Not Available";
                        }
                        //String Suspensionname = SECURITY_attrobject.getString("name");
                        //String Suspensiovalue = SECURITY_attrobject.getString("value");
                        SECURITY.put(Firstvalue, Secondvalue);

                    }
                    Featuer_Hole_Object.put(SECURITY_Title,SECURITY);
                }

                //**INSTRUMENTATION//
                JSONObject INSTRUMENTATION = new JSONObject();
                if(Firstattributgroup.has("INSTRUMENTATION") && Secondattributgroup.has("INSTRUMENTATION")) {
                    JSONObject FirstINSTRUMENTATION_con = Firstattributgroup.getJSONObject("INSTRUMENTATION");
                    JSONObject SecondINSTRUMENTATION_con = Secondattributgroup.getJSONObject("INSTRUMENTATION");
                    String INSTRUMENTATION_Title = FirstINSTRUMENTATION_con.getString("name");

                    JSONObject FirstINSTRUMENTATION_attributes = FirstINSTRUMENTATION_con.getJSONObject("attributes");
                    JSONObject SecondINSTRUMENTATION_attributes = SecondINSTRUMENTATION_con.getJSONObject("attributes");
                    Iterator iterator12 = FirstINSTRUMENTATION_attributes.keys();
                    while (iterator12.hasNext()) {

                        String key = (String) iterator12.next();
                        String Firstvalue,Secondvalue;
                        JSONObject FirstINSTRUMENTATION_attrobject = FirstINSTRUMENTATION_attributes.getJSONObject(key);
                        Firstvalue = FirstINSTRUMENTATION_attrobject.getString("value");
                        if(SecondINSTRUMENTATION_attributes.has(key)){
                            JSONObject SecondINSTRUMENTATION_attrobject = SecondINSTRUMENTATION_attributes.getJSONObject(key);
                            Secondvalue = SecondINSTRUMENTATION_attrobject.getString("value");
                        }else{
                            Secondvalue = "Not Available";
                        }
                        //String Suspensionname = INSTRUMENTATION_attrobject.getString("name");
                        //String Suspensiovalue = INSTRUMENTATION_attrobject.getString("value");
                        INSTRUMENTATION.put(Firstvalue, Secondvalue);

                    }
                    Featuer_Hole_Object.put(INSTRUMENTATION_Title,INSTRUMENTATION);
                }

                //**SEATING_CONFIGURATION//
                JSONObject SEATING_CONFIGURATION = new JSONObject();
                if(Firstattributgroup.has("SEATING_CONFIGURATION") && Secondattributgroup.has("SEATING_CONFIGURATION")) {
                    JSONObject FirstSEATING_con = Firstattributgroup.getJSONObject("SEATING_CONFIGURATION");
                    JSONObject SecondSEATING_con = Secondattributgroup.getJSONObject("SEATING_CONFIGURATION");
                    String SEATING_Title = FirstSEATING_con.getString("name");

                    JSONObject FirstSEATING_attributes = FirstSEATING_con.getJSONObject("attributes");
                    JSONObject SecondSEATING_attributes = FirstSEATING_con.getJSONObject("attributes");
                    Iterator iterator13 = FirstSEATING_attributes.keys();
                    while (iterator13.hasNext()) {

                        String key = (String) iterator13.next();
                        String Firstvalue,Secondvalue;
                        JSONObject FirstSEATING_attrobject = FirstSEATING_attributes.getJSONObject(key);
                        Firstvalue = FirstSEATING_attrobject.getString("value");
                        if(SecondSEATING_attributes.has(key)){
                            JSONObject SecondSEATING_attrobject = SecondSEATING_attributes.getJSONObject(key);
                            Secondvalue = SecondSEATING_attrobject.getString("value");
                        }else{
                            Secondvalue = "Not Available";
                        }
                        //String Suspensionname = SEATING_attrobject.getString("name");
                        //String Suspensiovalue = SEATING_attrobject.getString("value");
                        SEATING_CONFIGURATION.put(Firstvalue, Secondvalue);

                    }
                    Featuer_Hole_Object.put(SEATING_Title,SEATING_CONFIGURATION);
                }

                //**WINDOWS//
                JSONObject WINDOWS = new JSONObject();
                if(Firstattributgroup.has("WINDOWS") && Secondattributgroup.has("WINDOWS")) {
                    JSONObject FirstWINDOWS_con = Firstattributgroup.getJSONObject("WINDOWS");
                    JSONObject SecondWINDOWS_con = Secondattributgroup.getJSONObject("WINDOWS");
                    String WINDOWS_Title = FirstWINDOWS_con.getString("name");

                    JSONObject FirstWINDOWS_attributes = FirstWINDOWS_con.getJSONObject("attributes");
                    JSONObject SecondWINDOWS_attributes = SecondWINDOWS_con.getJSONObject("attributes");
                    Iterator iterator14 = FirstWINDOWS_attributes.keys();
                    while (iterator14.hasNext()) {

                        String key = (String) iterator14.next();
                        String Firstvalue,Secondvalue;
                        JSONObject FirstWINDOWS_attrobject = FirstWINDOWS_attributes.getJSONObject(key);
                        Firstvalue = FirstWINDOWS_attrobject.getString("value");
                        if(SecondWINDOWS_attributes.has(key)){
                            JSONObject SecondWINDOWS_attrobject = SecondWINDOWS_attributes.getJSONObject(key);
                            Secondvalue = SecondWINDOWS_attrobject.getString("value");
                        }else{
                            Secondvalue = "Not Available";
                        }
                        //String Suspensionname = WINDOWS_attrobject.getString("name");
                        //String Suspensiovalue = WINDOWS_attrobject.getString("value");
                        WINDOWS.put(Firstvalue, Secondvalue);

                    }
                    Featuer_Hole_Object.put(WINDOWS_Title,WINDOWS);
                }


                    Basics = new JSONObject();
                    try {
                        Basics.put(firstmake,secondmake);
                        Basics.put(firstmodel,secondmodel);
                        Basics.put(FirstTrimobject.getString("name"),SecondTrimobject.getString("name"));
                        Basics.put(firstyear,secondyear);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    OverallInfo = new JSONObject();
                    try {
                        OverallInfo.put(firstextcolor,secondextcolor);
                        OverallInfo.put(firstintcolor,secondintcolor);
                        OverallInfo.put(Firststyleobject.getString("engineFuelType"),Secondstyleobject.getString("engineFuelType"));
                        OverallInfo.put(Firststyleobject.getString("transmissionType"),Secondstyleobject.getString("transmissionType"));
                        OverallInfo.put(Firststyleobject.getString("transmissionType"),Secondstyleobject.getString("transmissionType"));
                        OverallInfo.put(Firstvehicletype.getString(0),Secondvehicletype.getString(0));
                        OverallInfo.put(Firststyleobject.getString("engineType"),Secondstyleobject.getString("engineType"));
                        OverallInfo.put(Firststyleobject.getString("engineCompressorType"),Secondstyleobject.getString("engineCompressorType"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Sellerinfo = new JSONObject();
                    try {
                        Sellerinfo.put("Location","Not Available");
                        Sellerinfo.put("Registration number","Not available");
                        Sellerinfo.put("Registration state","Not available");
                        Sellerinfo.put("Miles driven","Not Available");
                        Sellerinfo.put("Seller type","Dealer");
                        Sellerinfo.put("Number of owners",1);
                        Sellerinfo.put("Condition","Not Available");
                        Sellerinfo.put("Chassis number","Not available");
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                try {

                    //********************* Engine *********************//
                    JSONObject FirstEngine = Firstcarsquash.getJSONObject("engine");
                    JSONObject Firstrpm = FirstEngine.getJSONObject("rpm");
                    JSONObject SecondEngine = Secondcarsquash.getJSONObject("engine");
                    JSONObject Secondrpm = SecondEngine.getJSONObject("rpm");

                    //Engine_object = new JSONObject();
                    JSONObject EngineObjects = new JSONObject();
                    EngineObjects.put(FirstEngine.getString("horsepower")+" @ RPM "+Firstrpm.getString("horsepower"),SecondEngine.getString("horsepower")+" @ RPM "+Secondrpm.getString("horsepower"));
                    EngineObjects.put(FirstEngine.getString("torque")+" @ RPM "+ Firstrpm.getString("torque"),SecondEngine.getString("torque")+" @ RPM "+ Secondrpm.getString("torque"));
                    EngineObjects.put(FirstEngine.getString("displacement"),SecondEngine.getString("displacement"));
                    EngineObjects.put(FirstEngine.getString("cylinder"),SecondEngine.getString("cylinder"));
                    EngineObjects.put(FirstEngine.getString("totalValves"),SecondEngine.getString("totalValves"));

                    //********************* Transmission *********************//
                    JSONObject FirstTrans = Firstcarsquash.getJSONObject("transmission");
                    JSONObject SecondTrans = Secondcarsquash.getJSONObject("transmission");

                    JSONObject Transmission = new JSONObject();
                    Transmission.put(FirstTrans.getString("transmissionType"),SecondTrans.getString("transmissionType"));
                    Transmission.put(FirstTrans.getString("numberOfSpeeds"),SecondTrans.getString("numberOfSpeeds"));

                    Spec_Hole_Object.put("Exterior_Dimension",Exterior_Dimension);
                    Spec_Hole_Object.put("Engine",EngineObjects);
                    Spec_Hole_Object.put("Transmission",Transmission);
                    Spec_Hole_Object.put("Steering",Steering_object);
                    Spec_Hole_Object.put("Suspension",Suspension_object);
                    Log.e("Specificatio",Spec_Hole_Object.toString());


                }catch (JSONException e) {

                    e.printStackTrace();
                }
                //Basic_Hole_Object = new JSONObject();
                Basic_Hole_Object.put("Basic", Basics);
                Basic_Hole_Object.put("Overall_info", OverallInfo);
                Basic_Hole_Object.put("Seller_info", Sellerinfo);
                TablelayoutAdapter = new TableLayoutAdapter(context,Basic_Hole_Object,"Compare");
                TablelayoutRecycler.setAdapter(TablelayoutAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else if(FirstcarDeatil.equals("Available") && SecondcarDetail.equals("Not Available")){

            try {
                JSONArray FirstHoleder = Firstcar.getJSONArray("styleHolder");
                    JSONObject Firststyleobject = FirstHoleder.optJSONObject(0);

                    //********************* Trim *********************//
                    JSONObject FirstTrimobject = Firststyleobject.getJSONObject("trim");

                    //********************* Body Type *********************//
                    JSONObject Firstcatagories = Firststyleobject.getJSONObject("categories");
                    JSONArray Firstvehicletype = Firstcatagories.getJSONArray("Vehicle Style");

                    //********************* attributeGroups *********************//
                    JSONObject Firstattributgroup = Firststyleobject.getJSONObject("attributeGroups");

                    //**EXTERIOR DIMENSION//
                    JSONObject FirstExteriordimension = Firstattributgroup.getJSONObject("EXTERIOR_DIMENSIONS");
                    JSONObject Firstattribute = FirstExteriordimension.getJSONObject("attributes");
                    Iterator iterator = Firstattribute.keys();
                    while(iterator.hasNext()){

                        String key = (String)iterator.next();
                        JSONObject Firstattrobject = Firstattribute.getJSONObject(key);
                        Exterior_Dimension.put(Firstattrobject.getString("value"),"Not Available");

                    }

                    //**STEERING//
                    JSONObject FirstSTEERING = Firstattributgroup.getJSONObject("STEERING");

                    JSONObject FirstSTEERING_attributes = FirstSTEERING.getJSONObject("attributes");
                    JSONObject FirstPOWER_STEERING = FirstSTEERING_attributes.getJSONObject("POWER_STEERING");
                    Steering_object.put(FirstPOWER_STEERING.getString("value"),"Not Available");

                    //**SUSPENSION//
                    JSONObject FirstSUSPENSION = Firstattributgroup.getJSONObject("SUSPENSION");

                    JSONObject FirstSUSPENSION_attributes = FirstSUSPENSION.getJSONObject("attributes");
                    Iterator iterator2 = FirstSUSPENSION_attributes.keys();
                    while(iterator2.hasNext()){

                        String key = (String)iterator2.next();
                        JSONObject FirstSUSPENSION_attrobject = FirstSUSPENSION_attributes.getJSONObject(key);
                        Suspension_object.put(FirstSUSPENSION_attrobject.getString("value"),"Not Available");

                    }

                //********************* FEATURE DATAS *********************//
                //**MISC>_EXTERIOR//
                JSONObject Miscvalue = new JSONObject();
                if(Firstattributgroup.has("MISC._EXTERIOR_FEATURES")) {
                    JSONObject MISC = Firstattributgroup.getJSONObject("MISC._EXTERIOR_FEATURES");
                    String Misc_Title = MISC.getString("name");

                    JSONObject MISC_attributes = MISC.getJSONObject("attributes");
                    Iterator iterator3 = MISC_attributes.keys();
                    while (iterator3.hasNext()) {

                        String key = (String) iterator3.next();
                        JSONObject MISC_attrobject = MISC_attributes.getJSONObject(key);
                        //String Suspensionname = MISC_attrobject.getString("name");
                        String Suspensiovalue = MISC_attrobject.getString("value");
                        Miscvalue.put(Suspensiovalue, "Not Available");

                    }
                    Featuer_Hole_Object.put(Misc_Title,Miscvalue);
                }

                //**CONSUMER_GENERIC_FEATURE//
                JSONObject Consumer = new JSONObject();
                if(Firstattributgroup.has("CONSUMER_GENERIC_FEATURE")) {
                    JSONObject CONSUMER = Firstattributgroup.getJSONObject("CONSUMER_GENERIC_FEATURE");
                    String CONSUMER_Title = CONSUMER.getString("name");

                    JSONObject CONSUMER_attributes = CONSUMER.getJSONObject("attributes");
                    Iterator iterator4 = CONSUMER_attributes.keys();
                    while (iterator4.hasNext()) {

                        String key = (String) iterator4.next();
                        JSONObject CONSUMER_attrobject = CONSUMER_attributes.getJSONObject(key);
                        //String Suspensionname = CONSUMER_attrobject.getString("name");
                        String Suspensiovalue = CONSUMER_attrobject.getString("value");
                        Consumer.put(Suspensiovalue, "Not Available");

                    }
                    Featuer_Hole_Object.put(CONSUMER_Title,Consumer);
                }

                //**MISC._INTERIOR_FEATURES//
                JSONObject INTERIOR = new JSONObject();
                if(Firstattributgroup.has("MISC._INTERIOR_FEATURES")) {
                    JSONObject Misc_INTERIOR = Firstattributgroup.getJSONObject("MISC._INTERIOR_FEATURES");
                    String INTERIOR_Title = Misc_INTERIOR.getString("name");

                    JSONObject INTERIOR_attributes = Misc_INTERIOR.getJSONObject("attributes");
                    Iterator iterator5 = INTERIOR_attributes.keys();
                    while (iterator5.hasNext()) {

                        String key = (String) iterator5.next();
                        JSONObject INTERIORattrobject = INTERIOR_attributes.getJSONObject(key);
                        //String Suspensionname = INTERIORattrobject.getString("name");
                        String Suspensiovalue = INTERIORattrobject.getString("value");
                        INTERIOR.put(Suspensiovalue, "Not Available");

                    }
                    Featuer_Hole_Object.put(INTERIOR_Title,INTERIOR);
                }

                //**AIR_CONDITIONING//
                JSONObject AIR_CONDITIONING = new JSONObject();
                if(Firstattributgroup.has("AIR_CONDITIONING")) {
                    JSONObject Air_con = Firstattributgroup.getJSONObject("AIR_CONDITIONING");
                    String AIR_Title = Air_con.getString("name");

                    JSONObject Air_attributes = Air_con.getJSONObject("attributes");
                    Iterator iterator6 = Air_attributes.keys();
                    while (iterator6.hasNext()) {

                        String key = (String) iterator6.next();
                        JSONObject Air_attrobject = Air_attributes.getJSONObject(key);
                        //String Suspensionname = Air_attrobject.getString("name");
                        String Suspensiovalue = Air_attrobject.getString("value");
                        AIR_CONDITIONING.put(Suspensiovalue, "Not Available");

                    }
                    Featuer_Hole_Object.put(AIR_Title,AIR_CONDITIONING);
                }

                //**STORAGE//
                JSONObject STORAGE = new JSONObject();
                if(Firstattributgroup.has("STORAGE")) {
                    JSONObject STORAGE_con = Firstattributgroup.getJSONObject("STORAGE");
                    String STORAGE_Title = STORAGE_con.getString("name");

                    JSONObject STORAGE_attributes = STORAGE_con.getJSONObject("attributes");
                    Iterator iterator7 = STORAGE_attributes.keys();
                    while (iterator7.hasNext()) {

                        String key = (String) iterator7.next();
                        JSONObject STORAGE_attrobject = STORAGE_attributes.getJSONObject(key);
                        //String Suspensionname = STORAGE_attrobject.getString("name");
                        String Suspensiovalue = STORAGE_attrobject.getString("value");
                        STORAGE.put(Suspensiovalue, "Not Available");

                    }
                    Featuer_Hole_Object.put(STORAGE_Title,STORAGE);
                }

                //**MOBILE_CONNECTIVITY//
                JSONObject MOBILE_CONNECTIVITY = new JSONObject();
                if(Firstattributgroup.has("MOBILE_CONNECTIVITY")) {
                    JSONObject MOBILE_con = Firstattributgroup.getJSONObject("MOBILE_CONNECTIVITY");
                    String MOBILE_Title = MOBILE_con.getString("name");

                    JSONObject MOBILE_attributes = MOBILE_con.getJSONObject("attributes");
                    Iterator iterator8 = MOBILE_attributes.keys();
                    while (iterator8.hasNext()) {

                        String key = (String) iterator8.next();
                        JSONObject MOBILE_attrobject = MOBILE_attributes.getJSONObject(key);
                        //String Suspensionname = MOBILE_attrobject.getString("name");
                        String Suspensiovalue = MOBILE_attrobject.getString("value");
                        MOBILE_CONNECTIVITY.put(Suspensiovalue, "Not Available");

                    }
                    Featuer_Hole_Object.put(MOBILE_Title,MOBILE_CONNECTIVITY);
                }

                //**EXTERIOR_LIGHTS//
                JSONObject EXTERIOR_LIGHTS = new JSONObject();
                if(Firstattributgroup.has("EXTERIOR_LIGHTS")) {
                    JSONObject EXTERIOR_con = Firstattributgroup.getJSONObject("EXTERIOR_LIGHTS");
                    String EXTERIOR_Title = EXTERIOR_con.getString("name");

                    JSONObject EXTERIOR_attributes = EXTERIOR_con.getJSONObject("attributes");
                    Iterator iterator9 = EXTERIOR_attributes.keys();
                    while (iterator9.hasNext()) {

                        String key = (String) iterator9.next();
                        JSONObject EXTERIOR_attrobject = EXTERIOR_attributes.getJSONObject(key);
                        //String Suspensionname = EXTERIOR_attrobject.getString("name");
                        String Suspensiovalue = EXTERIOR_attrobject.getString("value");
                        EXTERIOR_LIGHTS.put(Suspensiovalue, "Not Available");

                    }
                    Featuer_Hole_Object.put(EXTERIOR_Title,EXTERIOR_LIGHTS);
                }

                //**POWER_OUTLETS//
                JSONObject POWER_OUTLETS = new JSONObject();
                if (Firstattributgroup.has("POWER_OUTLETS")) {
                    JSONObject POWER_con = Firstattributgroup.getJSONObject("POWER_OUTLETS");
                    String POWER_Title = POWER_con.getString("name");

                    JSONObject POWER_attributes = POWER_con.getJSONObject("attributes");
                    Iterator iterator10 = POWER_attributes.keys();
                    while (iterator10.hasNext()) {

                        String key = (String) iterator10.next();
                        JSONObject POWER_attrobject = POWER_attributes.getJSONObject(key);
                        //String Suspensionname = POWER_attrobject.getString("name");
                        String Suspensiovalue = POWER_attrobject.getString("value");
                        POWER_OUTLETS.put(Suspensiovalue, "Not Available");

                    }
                    Featuer_Hole_Object.put(POWER_Title,POWER_OUTLETS);
                }

                //**SECURITY//
                JSONObject SECURITY = new JSONObject();
                if(Firstattributgroup.has("SECURITY")) {
                    JSONObject SECURITY_con = Firstattributgroup.getJSONObject("SECURITY");
                    String SECURITY_Title = SECURITY_con.getString("name");

                    JSONObject SECURITY_attributes = SECURITY_con.getJSONObject("attributes");
                    Iterator iterator11 = SECURITY_attributes.keys();
                    while (iterator11.hasNext()) {

                        String key = (String) iterator11.next();
                        JSONObject SECURITY_attrobject = SECURITY_attributes.getJSONObject(key);
                        //String Suspensionname = SECURITY_attrobject.getString("name");
                        String Suspensiovalue = SECURITY_attrobject.getString("value");
                        SECURITY.put(Suspensiovalue, "Not Available");

                    }
                    Featuer_Hole_Object.put(SECURITY_Title,SECURITY);
                }

                //**INSTRUMENTATION//
                JSONObject INSTRUMENTATION = new JSONObject();
                if(Firstattributgroup.has("INSTRUMENTATION")) {
                    JSONObject INSTRUMENTATION_con = Firstattributgroup.getJSONObject("INSTRUMENTATION");
                    String INSTRUMENTATION_Title = INSTRUMENTATION_con.getString("name");

                    JSONObject INSTRUMENTATION_attributes = INSTRUMENTATION_con.getJSONObject("attributes");
                    Iterator iterator12 = INSTRUMENTATION_attributes.keys();
                    while (iterator12.hasNext()) {

                        String key = (String) iterator12.next();
                        JSONObject INSTRUMENTATION_attrobject = INSTRUMENTATION_attributes.getJSONObject(key);
                        //String Suspensionname = INSTRUMENTATION_attrobject.getString("name");
                        String Suspensiovalue = INSTRUMENTATION_attrobject.getString("value");
                        INSTRUMENTATION.put(Suspensiovalue, "Not Available");

                    }
                    Featuer_Hole_Object.put(INSTRUMENTATION_Title,INSTRUMENTATION);
                }

                //**SEATING_CONFIGURATION//
                JSONObject SEATING_CONFIGURATION = new JSONObject();
                if(Firstattributgroup.has("SEATING_CONFIGURATION")) {
                    JSONObject SEATING_con = Firstattributgroup.getJSONObject("SEATING_CONFIGURATION");
                    String SEATING_Title = SEATING_con.getString("name");

                    JSONObject SEATING_attributes = SEATING_con.getJSONObject("attributes");
                    Iterator iterator13 = SEATING_attributes.keys();
                    while (iterator13.hasNext()) {

                        String key = (String) iterator13.next();
                        JSONObject SEATING_attrobject = SEATING_attributes.getJSONObject(key);
                        //String Suspensionname = SEATING_attrobject.getString("name");
                        String Suspensiovalue = SEATING_attrobject.getString("value");
                        SEATING_CONFIGURATION.put(Suspensiovalue, "Not Available");

                    }
                    Featuer_Hole_Object.put(SEATING_Title,SEATING_CONFIGURATION);
                }

                //**WINDOWS//
                JSONObject WINDOWS = new JSONObject();
                if(Firstattributgroup.has("WINDOWS")) {
                    JSONObject WINDOWS_con = Firstattributgroup.getJSONObject("WINDOWS");
                    String WINDOWS_Title = WINDOWS_con.getString("name");

                    JSONObject WINDOWS_attributes = WINDOWS_con.getJSONObject("attributes");
                    Iterator iterator14 = WINDOWS_attributes.keys();
                    while (iterator14.hasNext()) {

                        String key = (String) iterator14.next();
                        JSONObject WINDOWS_attrobject = WINDOWS_attributes.getJSONObject(key);
                        //String Suspensionname = WINDOWS_attrobject.getString("name");
                        String Suspensiovalue = WINDOWS_attrobject.getString("value");
                        WINDOWS.put(Suspensiovalue, "Not Available");

                    }
                    Featuer_Hole_Object.put(WINDOWS_Title,WINDOWS);
                }



                    Basics = new JSONObject();
                    Basics.put(firstmake,secondmake);
                    Basics.put(firstmodel,secondmodel);
                    Basics.put(FirstTrimobject.getString("name"),"Not Available");
                    Basics.put(firstyear,secondyear);

                    OverallInfo = new JSONObject();
                    OverallInfo.put(firstextcolor,secondextcolor);
                    OverallInfo.put(firstintcolor,secondintcolor);
                    OverallInfo.put(Firststyleobject.getString("engineFuelType"),"Not Availble");
                    OverallInfo.put(Firststyleobject.getString("transmissionType"),"Not Availble");
                    OverallInfo.put(Firststyleobject.getString("transmissionType"),"Not Availble");
                    OverallInfo.put(Firstvehicletype.getString(0),"Not Availble");
                    OverallInfo.put(Firststyleobject.getString("engineType"),"Not Availble");
                    OverallInfo.put(Firststyleobject.getString("engineCompressorType"),"Not Availble");

                    Sellerinfo = new JSONObject();
                    Sellerinfo.put("Location","Not Available");
                    Sellerinfo.put("Registration number","Not available");
                    Sellerinfo.put("Registration state","Not available");
                    Sellerinfo.put("Miles driven","Not Available");
                    Sellerinfo.put("Seller type","Dealer");
                    Sellerinfo.put("Number of owners",1);
                    Sellerinfo.put("Condition","Not Available");
                    Sellerinfo.put("Chassis number","Not available");

                try {

                    //********************* Engine *********************//
                    JSONObject FirstEngine = Firstcarsquash.getJSONObject("engine");
                    JSONObject Firstrpm = FirstEngine.getJSONObject("rpm");

                    //Engine_object = new JSONObject();
                    JSONObject EngineObjects = new JSONObject();
                    EngineObjects.put(FirstEngine.getString("horsepower")+" @ RPM "+Firstrpm.getString("horsepower"),"Not Available");
                    EngineObjects.put(FirstEngine.getString("torque")+" @ RPM "+ Firstrpm.getString("torque"),"Not Available");
                    EngineObjects.put(FirstEngine.getString("displacement"),"Not Available");
                    EngineObjects.put(FirstEngine.getString("cylinder"),"Not Available");
                    EngineObjects.put(FirstEngine.getString("totalValves"),"Not Available");

                    //********************* Transmission *********************//
                    JSONObject FirstTrans = Firstcarsquash.getJSONObject("transmission");

                    JSONObject Transmission = new JSONObject();
                    Transmission.put(FirstTrans.getString("transmissionType"),"Not Available");
                    Transmission.put(FirstTrans.getString("numberOfSpeeds"),"Not Available");

                    Spec_Hole_Object.put("Exterior_Dimension",Exterior_Dimension);
                    Spec_Hole_Object.put("Engine",EngineObjects);
                    Spec_Hole_Object.put("Transmission",Transmission);
                    Spec_Hole_Object.put("Steering",Steering_object);
                    Spec_Hole_Object.put("Suspension",Suspension_object);

                }catch (JSONException e) {

                    e.printStackTrace();
                }

                Basic_Hole_Object.put("Basic", Basics);
                Basic_Hole_Object.put("Overall_info", OverallInfo);
                Basic_Hole_Object.put("Seller_info", Sellerinfo);
                TablelayoutAdapter = new TableLayoutAdapter(context,Basic_Hole_Object,"Compare");
                TablelayoutRecycler.setAdapter(TablelayoutAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if(FirstcarDeatil.equals("Not Available") && SecondcarDetail.equals("Available")){

            try {
                JSONArray SecondHolder = Secondcar.getJSONArray("styleHolder");
                    JSONObject Secondstyleobject = SecondHolder.optJSONObject(0);

                    //********************* Trim *********************//
                    JSONObject SecondTrimobject = Secondstyleobject.getJSONObject("trim");

                    //********************* Body Type *********************//
                    JSONObject Secondcatagories = Secondstyleobject.getJSONObject("categories");
                    JSONArray Secondvehicletype = Secondcatagories.getJSONArray("Vehicle Style");

                    //********************* attributeGroups *********************//
                    JSONObject Secondattributgroup = Secondstyleobject.getJSONObject("attributeGroups");

                    //**EXTERIOR DIMENSION//
                    JSONObject SecondExteriordimension = Secondattributgroup.getJSONObject("EXTERIOR_DIMENSIONS");
                    JSONObject Secondattribute = SecondExteriordimension.getJSONObject("attributes");
                    Iterator iterator = Secondattribute.keys();
                    while(iterator.hasNext()){

                        String key = (String)iterator.next();
                        JSONObject Secondattrobject = Secondattribute.getJSONObject(key);
                        Exterior_Dimension.put("Not Available",Secondattrobject.getString("value"));

                    }

                    //**STEERING//
                    JSONObject SecondSTEERING = Secondattributgroup.getJSONObject("STEERING");

                    JSONObject SecondSTEERING_attributes = SecondSTEERING.getJSONObject("attributes");
                    JSONObject SecondPOWER_STEERING = SecondSTEERING_attributes.getJSONObject("POWER_STEERING");
                    Steering_object.put("Not Available",SecondPOWER_STEERING.getString("value"));

                    //**SUSPENSION//
                    JSONObject SecondSUSPENSION = Secondattributgroup.getJSONObject("SUSPENSION");

                    JSONObject SecondSUSPENSION_attributes = SecondSUSPENSION.getJSONObject("attributes");
                    Iterator iterator2 = SecondSUSPENSION_attributes.keys();
                    while(iterator2.hasNext()){

                        String key = (String)iterator2.next();
                        JSONObject SecondSUSPENSION_attrobject = SecondSUSPENSION_attributes.getJSONObject(key);
                        Suspension_object.put("Not Available",SecondSUSPENSION_attrobject.getString("value"));

                    }

                //********************* FEATURE DATAS *********************//
                //**MISC>_EXTERIOR//
                JSONObject Miscvalue = new JSONObject();
                if(Secondattributgroup.has("MISC._EXTERIOR_FEATURES")) {
                    JSONObject MISC = Secondattributgroup.getJSONObject("MISC._EXTERIOR_FEATURES");
                    String Misc_Title = MISC.getString("name");

                    JSONObject MISC_attributes = MISC.getJSONObject("attributes");
                    Iterator iterator3 = MISC_attributes.keys();
                    while (iterator3.hasNext()) {

                        String key = (String) iterator3.next();
                        JSONObject MISC_attrobject = MISC_attributes.getJSONObject(key);
                        //String Suspensionname = MISC_attrobject.getString("name");
                        String Suspensiovalue = MISC_attrobject.getString("value");
                        Miscvalue.put("Not Available", Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(Misc_Title,Miscvalue);
                }

                //**CONSUMER_GENERIC_FEATURE//
                JSONObject Consumer = new JSONObject();
                if(Secondattributgroup.has("CONSUMER_GENERIC_FEATURE")) {
                    JSONObject CONSUMER = Secondattributgroup.getJSONObject("CONSUMER_GENERIC_FEATURE");
                    String CONSUMER_Title = CONSUMER.getString("name");

                    JSONObject CONSUMER_attributes = CONSUMER.getJSONObject("attributes");
                    Iterator iterator4 = CONSUMER_attributes.keys();
                    while (iterator4.hasNext()) {

                        String key = (String) iterator4.next();
                        JSONObject CONSUMER_attrobject = CONSUMER_attributes.getJSONObject(key);
                        //String Suspensionname = CONSUMER_attrobject.getString("name");
                        String Suspensiovalue = CONSUMER_attrobject.getString("value");
                        Consumer.put("Not Available", Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(CONSUMER_Title,Consumer);
                }

                //**MISC._INTERIOR_FEATURES//
                JSONObject INTERIOR = new JSONObject();
                if(Secondattributgroup.has("MISC._INTERIOR_FEATURES")) {
                    JSONObject Misc_INTERIOR = Secondattributgroup.getJSONObject("MISC._INTERIOR_FEATURES");
                    String INTERIOR_Title = Misc_INTERIOR.getString("name");

                    JSONObject INTERIOR_attributes = Misc_INTERIOR.getJSONObject("attributes");
                    Iterator iterator5 = INTERIOR_attributes.keys();
                    while (iterator5.hasNext()) {

                        String key = (String) iterator5.next();
                        JSONObject INTERIORattrobject = INTERIOR_attributes.getJSONObject(key);
                        //String Suspensionname = INTERIORattrobject.getString("name");
                        String Suspensiovalue = INTERIORattrobject.getString("value");
                        INTERIOR.put("Not Available", Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(INTERIOR_Title,INTERIOR);
                }

                //**AIR_CONDITIONING//
                JSONObject AIR_CONDITIONING = new JSONObject();
                if(Secondattributgroup.has("AIR_CONDITIONING")) {
                    JSONObject Air_con = Secondattributgroup.getJSONObject("AIR_CONDITIONING");
                    String AIR_Title = Air_con.getString("name");

                    JSONObject Air_attributes = Air_con.getJSONObject("attributes");
                    Iterator iterator6 = Air_attributes.keys();
                    while (iterator6.hasNext()) {

                        String key = (String) iterator6.next();
                        JSONObject Air_attrobject = Air_attributes.getJSONObject(key);
                        //String Suspensionname = Air_attrobject.getString("name");
                        String Suspensiovalue = Air_attrobject.getString("value");
                        AIR_CONDITIONING.put("Not Available", Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(AIR_Title,AIR_CONDITIONING);
                }

                //**STORAGE//
                JSONObject STORAGE = new JSONObject();
                if(Secondattributgroup.has("STORAGE")) {
                    JSONObject STORAGE_con = Secondattributgroup.getJSONObject("STORAGE");
                    String STORAGE_Title = STORAGE_con.getString("name");

                    JSONObject STORAGE_attributes = STORAGE_con.getJSONObject("attributes");
                    Iterator iterator7 = STORAGE_attributes.keys();
                    while (iterator7.hasNext()) {

                        String key = (String) iterator7.next();
                        JSONObject STORAGE_attrobject = STORAGE_attributes.getJSONObject(key);
                        //String Suspensionname = STORAGE_attrobject.getString("name");
                        String Suspensiovalue = STORAGE_attrobject.getString("value");
                        STORAGE.put("Not Available", Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(STORAGE_Title,STORAGE);
                }

                //**MOBILE_CONNECTIVITY//
                JSONObject MOBILE_CONNECTIVITY = new JSONObject();
                if(Secondattributgroup.has("MOBILE_CONNECTIVITY")) {
                    JSONObject MOBILE_con = Secondattributgroup.getJSONObject("MOBILE_CONNECTIVITY");
                    String MOBILE_Title = MOBILE_con.getString("name");

                    JSONObject MOBILE_attributes = MOBILE_con.getJSONObject("attributes");
                    Iterator iterator8 = MOBILE_attributes.keys();
                    while (iterator8.hasNext()) {

                        String key = (String) iterator8.next();
                        JSONObject MOBILE_attrobject = MOBILE_attributes.getJSONObject(key);
                        //String Suspensionname = MOBILE_attrobject.getString("name");
                        String Suspensiovalue = MOBILE_attrobject.getString("value");
                        MOBILE_CONNECTIVITY.put("Not Available", Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(MOBILE_Title,MOBILE_CONNECTIVITY);
                }

                //**EXTERIOR_LIGHTS//
                JSONObject EXTERIOR_LIGHTS = new JSONObject();
                if(Secondattributgroup.has("EXTERIOR_LIGHTS")) {
                    JSONObject EXTERIOR_con = Secondattributgroup.getJSONObject("EXTERIOR_LIGHTS");
                    String EXTERIOR_Title = EXTERIOR_con.getString("name");

                    JSONObject EXTERIOR_attributes = EXTERIOR_con.getJSONObject("attributes");
                    Iterator iterator9 = EXTERIOR_attributes.keys();
                    while (iterator9.hasNext()) {

                        String key = (String) iterator9.next();
                        JSONObject EXTERIOR_attrobject = EXTERIOR_attributes.getJSONObject(key);
                        //String Suspensionname = EXTERIOR_attrobject.getString("name");
                        String Suspensiovalue = EXTERIOR_attrobject.getString("value");
                        EXTERIOR_LIGHTS.put("Not Available", Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(EXTERIOR_Title,EXTERIOR_LIGHTS);
                }

                //**POWER_OUTLETS//
                JSONObject POWER_OUTLETS = new JSONObject();
                if (Secondattributgroup.has("POWER_OUTLETS")) {
                    JSONObject POWER_con = Secondattributgroup.getJSONObject("POWER_OUTLETS");
                    String POWER_Title = POWER_con.getString("name");

                    JSONObject POWER_attributes = POWER_con.getJSONObject("attributes");
                    Iterator iterator10 = POWER_attributes.keys();
                    while (iterator10.hasNext()) {

                        String key = (String) iterator10.next();
                        JSONObject POWER_attrobject = POWER_attributes.getJSONObject(key);
                        //String Suspensionname = POWER_attrobject.getString("name");
                        String Suspensiovalue = POWER_attrobject.getString("value");
                        POWER_OUTLETS.put("Not Available", Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(POWER_Title,POWER_OUTLETS);
                }

                //**SECURITY//
                JSONObject SECURITY = new JSONObject();
                if(Secondattributgroup.has("SECURITY")) {
                    JSONObject SECURITY_con = Secondattributgroup.getJSONObject("SECURITY");
                    String SECURITY_Title = SECURITY_con.getString("name");

                    JSONObject SECURITY_attributes = SECURITY_con.getJSONObject("attributes");
                    Iterator iterator11 = SECURITY_attributes.keys();
                    while (iterator11.hasNext()) {

                        String key = (String) iterator11.next();
                        JSONObject SECURITY_attrobject = SECURITY_attributes.getJSONObject(key);
                        //String Suspensionname = SECURITY_attrobject.getString("name");
                        String Suspensiovalue = SECURITY_attrobject.getString("value");
                        SECURITY.put("Not Available", Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(SECURITY_Title,SECURITY);
                }

                //**INSTRUMENTATION//
                JSONObject INSTRUMENTATION = new JSONObject();
                if(Secondattributgroup.has("INSTRUMENTATION")) {
                    JSONObject INSTRUMENTATION_con = Secondattributgroup.getJSONObject("INSTRUMENTATION");
                    String INSTRUMENTATION_Title = INSTRUMENTATION_con.getString("name");

                    JSONObject INSTRUMENTATION_attributes = INSTRUMENTATION_con.getJSONObject("attributes");
                    Iterator iterator12 = INSTRUMENTATION_attributes.keys();
                    while (iterator12.hasNext()) {

                        String key = (String) iterator12.next();
                        JSONObject INSTRUMENTATION_attrobject = INSTRUMENTATION_attributes.getJSONObject(key);
                        //String Suspensionname = INSTRUMENTATION_attrobject.getString("name");
                        String Suspensiovalue = INSTRUMENTATION_attrobject.getString("value");
                        INSTRUMENTATION.put("Not Available", Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(INSTRUMENTATION_Title,INSTRUMENTATION);
                }

                //**SEATING_CONFIGURATION//
                JSONObject SEATING_CONFIGURATION = new JSONObject();
                if(Secondattributgroup.has("SEATING_CONFIGURATION")) {
                    JSONObject SEATING_con = Secondattributgroup.getJSONObject("SEATING_CONFIGURATION");
                    String SEATING_Title = SEATING_con.getString("name");

                    JSONObject SEATING_attributes = SEATING_con.getJSONObject("attributes");
                    Iterator iterator13 = SEATING_attributes.keys();
                    while (iterator13.hasNext()) {

                        String key = (String) iterator13.next();
                        JSONObject SEATING_attrobject = SEATING_attributes.getJSONObject(key);
                        //String Suspensionname = SEATING_attrobject.getString("name");
                        String Suspensiovalue = SEATING_attrobject.getString("value");
                        SEATING_CONFIGURATION.put("Not Available", Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(SEATING_Title,SEATING_CONFIGURATION);
                }

                //**WINDOWS//
                JSONObject WINDOWS = new JSONObject();
                if(Secondattributgroup.has("WINDOWS")) {
                    JSONObject WINDOWS_con = Secondattributgroup.getJSONObject("WINDOWS");
                    String WINDOWS_Title = WINDOWS_con.getString("name");

                    JSONObject WINDOWS_attributes = WINDOWS_con.getJSONObject("attributes");
                    Iterator iterator14 = WINDOWS_attributes.keys();
                    while (iterator14.hasNext()) {

                        String key = (String) iterator14.next();
                        JSONObject WINDOWS_attrobject = WINDOWS_attributes.getJSONObject(key);
                        //String Suspensionname = WINDOWS_attrobject.getString("name");
                        String Suspensiovalue = WINDOWS_attrobject.getString("value");
                        WINDOWS.put("Not Available", Suspensiovalue);

                    }
                    Featuer_Hole_Object.put(WINDOWS_Title,WINDOWS);
                }



                    Basics = new JSONObject();
                    Basics.put(firstmake,secondmake);
                    Basics.put(firstmodel,secondmodel);
                    Basics.put("Not Available",SecondTrimobject.getString("name"));
                    Basics.put(firstyear,secondyear);

                    OverallInfo = new JSONObject();
                    OverallInfo.put(firstextcolor,secondextcolor);
                    OverallInfo.put(firstintcolor,secondintcolor);
                    OverallInfo.put("Not Available",Secondstyleobject.getString("engineFuelType"));
                    OverallInfo.put("Not Available",Secondstyleobject.getString("transmissionType"));
                    OverallInfo.put("Not Available",Secondstyleobject.getString("transmissionType"));
                    OverallInfo.put("Not Available",Secondvehicletype.getString(0));
                    OverallInfo.put("Not Available",Secondstyleobject.getString("engineType"));
                    OverallInfo.put("Not Available",Secondstyleobject.getString("engineCompressorType"));

                    Sellerinfo = new JSONObject();
                    Sellerinfo.put("Location","Not Available");
                    Sellerinfo.put("Registration number","Not available");
                    Sellerinfo.put("Registration state","Not available");
                    Sellerinfo.put("Miles driven","Not Available");
                    Sellerinfo.put("Seller type","Dealer");
                    Sellerinfo.put("Number of owners",1);
                    Sellerinfo.put("Condition","Not Available");
                    Sellerinfo.put("Chassis number","Not available");

                try {

                    //********************* Engine *********************//
                    JSONObject SecondEngine = Secondcarsquash.getJSONObject("engine");
                    JSONObject Secondrpm = SecondEngine.getJSONObject("rpm");

                    //Engine_object = new JSONObject();
                    JSONObject EngineObjects = new JSONObject();
                    EngineObjects.put("Not Available",SecondEngine.getString("horsepower")+" @ RPM "+Secondrpm.getString("horsepower"));
                    EngineObjects.put("Not Available",SecondEngine.getString("torque")+" @ RPM "+ Secondrpm.getString("torque"));
                    EngineObjects.put("Not Available",SecondEngine.getString("displacement"));
                    EngineObjects.put("Not Available",SecondEngine.getString("cylinder"));
                    EngineObjects.put("Not Available",SecondEngine.getString("totalValves"));

                    //********************* Transmission *********************//
                    JSONObject SecondTrans = Secondcarsquash.getJSONObject("transmission");

                    //Trans_object = new JSONObject();
                    JSONObject Transmission = new JSONObject();
                    Transmission.put("Not Available",SecondTrans.getString("transmissionType"));
                    Transmission.put("Not Available",SecondTrans.getString("numberOfSpeeds"));

                    Spec_Hole_Object = new JSONObject();
                    Spec_Hole_Object.put("Exterior_Dimension",Exterior_Dimension);
                    Spec_Hole_Object.put("Engine",EngineObjects);
                    Spec_Hole_Object.put("Transmission",Transmission);
                    Spec_Hole_Object.put("Steering",Steering_object);
                    Spec_Hole_Object.put("Suspension",Suspension_object);
                    Log.e("Specificatio",Spec_Hole_Object.toString());


                }catch (JSONException e) {

                    e.printStackTrace();
                }

                Basic_Hole_Object = new JSONObject();
                Basic_Hole_Object.put("Basic", Basics);
                Basic_Hole_Object.put("Overall_info", OverallInfo);
                Basic_Hole_Object.put("Seller_info", Sellerinfo);
                TablelayoutAdapter = new TableLayoutAdapter(context,Basic_Hole_Object,"Compare");
                TablelayoutRecycler.setAdapter(TablelayoutAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else if(FirstcarDeatil.equals("Not Available") && SecondcarDetail.equals("Not Available")){

            Basic_Text.setBackgroundColor(Color.DKGRAY);
            Basic_Text.setTextColor(Color.WHITE);
            TablelayoutAdapter = new TableLayoutAdapter(context,Basic_Hole_Object,"Compare");
            TablelayoutRecycler.setAdapter(TablelayoutAdapter);

        }

    }

    private void edumandSecondSquashVIN(String secondSquashVIN) {

        Map<String, String> params = new HashMap<String, String>();
        String URL = Constants.Edmundssquashvin_Detail+secondSquashVIN+"?"+Constants.Edmunds_Key;
        //Log.e("URL",URL);

        VolleyCustomPostRequest jsObjRequest = new VolleyCustomPostRequest(URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                // TODO Auto-generated method stub
                SecondcarDetail = "Available";
                try {
                    Secondcarsquash = new JSONObject(response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                parseEdundsRespnseData();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

                NetworkResponse response = error.networkResponse;
                Log.e("Squash Error", error.toString());
                SecondcarDetail = "Not Available";
                parseEdundsRespnseData();

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

    private void edumandFirtSquashVIN(String firstSqashVIN) {

        Map<String, String> params = new HashMap<String, String>();
        String URL = Constants.Edmundssquashvin_Detail+firstSqashVIN+"?"+Constants.Edmunds_Key;
        //Log.e("URL",URL);

        VolleyCustomPostRequest jsObjRequest = new VolleyCustomPostRequest(URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                // TODO Auto-generated method stub
                FirstcarDeatil = "Available";
                try {
                    Firstcarsquash = new JSONObject(response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                parseEdundsRespnseData();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

                NetworkResponse response = error.networkResponse;
                Log.e("Squash Error", error.toString());
                FirstcarDeatil = "Not Available";
                parseEdundsRespnseData();

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

    private void getMathchCar() {

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        if(selectedmake !=null && selectedmodel !=null && selectedyear !=null && selectedmake !="" && selectedmodel !="" && selectedyear !=""){

            Map<String, String> params = new HashMap<String, String>();
            params.put("make", selectedmake);
            params.put("model", selectedmodel);
            params.put("year", selectedyear);

            VolleyCustomPostRequest jsObjRequest = new VolleyCustomPostRequest(Request.Method.POST, Constants.getmatch_URL, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    // TODO Auto-generated method stub
                   // Log.e("Response",response.toString());
                    dialog.dismiss();
                    storeResponsedata(response.toString());

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub

                    NetworkResponse response = error.networkResponse;
                    showChangeLangDialog("Sorry!","No data found.Please try again...");
                    //Toast.makeText(getActivity(),"Car data not found.Please try again",Toast.LENGTH_SHORT).show();
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

        }else{
            showChangeLangDialog("","Please Select Make,Model,Year");
        }
    }

    private void storeResponsedata(String response) {
        //Log.e("getmatch",response);
        JSONObject json = null;

        try {
            if(!response.equals(null)){
            json = new JSONObject(response);
            String getMake = json.getString("Make");
            String getModel = json.getString("Model");
            String getYear = json.getString("Year");
            String getImage = json.getString("Banner_Image");
            String getVin = json.getString("VIN");
            String getExtcolor = json.getString("Exterior_Color");
            String getIntcolor = json.getString("Interior_Color");
            Log.e("VIN", getVin);

            if (identifyPopUp.equals("First")) {

                firstmake = getMake;
                firstmodel = getModel;
                firstyear = getYear;
                firstimage = getImage;
                firstvin = getVin;
                firstextcolor = getExtcolor;
                firstintcolor = getIntcolor;

                Picasso.with(this.context).load(firstimage).placeholder(R.drawable.logo).fit().into(Firstimage);
                Firstmake.setText(firstmake);
                Firstmake.setVisibility(View.VISIBLE);
                Firstmodel.setText(firstmodel + " " + firstyear);

            } else {

                secondmake = getMake;
                secondmodel = getModel;
                secondyear = getYear;
                secondimage = getImage;
                secondvin = getVin;
                secondextcolor = getExtcolor;
                secondintcolor = getIntcolor;

                Picasso.with(this.context).load(secondimage).placeholder(R.drawable.logo).fit().into(Secondimage);
                Sceondmake.setText(secondmake);
                Sceondmake.setVisibility(View.VISIBLE);
                Secondmodel.setText(secondmodel + " " + secondyear);

            }

            compareCars();

        }
    }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void compareCars() {

        if(firstvin != null && secondvin != null){

            comparebutton.setBackgroundColor(Color.BLUE);
            comparebutton.setClickable(true);

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;
        int sp_id = spinner.getId();
        Log.e("GetID", sp_id + "");
        String Make,Model,Year;
        switch (sp_id) {
            case R.id.Make_spinner:
                if(position == 0){
                    Make = "";
                    Log.e("Selectmake",Make);
                }else{
                    Make = MakeList.get(position-1);
                    selectedmake = Make;
                    fillAllModel(Make);
                    Log.e("Select Make",Make);
                    YearList.clear();
                    yearAdapter = new CustomSpinnerAdapter<String>(context, R.layout.custom_dealer_spinner_item);
                    yearAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
                    yearAdapter.add("Select Year");
                    yearAdapter.addAll(YearList);
                    Yearspinner.setAdapter(yearAdapter);
                    Yearspinner.setSelection(0);
                }
                break;
            case R.id.Model_spinner:
                if(position == 0){
                    Model = "";
                    Log.e("Selectmodel",Model);
                }else{
                    Model = ModelList.get(position-1);
                    selectedmodel = Model;
                    fillAllYear(selectedmake,Model);
                    Log.e("Select model",Model);
                }
                break;
            case R.id.Year_spinner:
                if(position == 0){
                    Year = "";
                    Log.e("Select year",Year);
                }else{
                    Year = YearList.get(position-1);
                    selectedyear = Year;
                    Log.e("Select year",Year);
                }
                break;
        }

    }

    private void fillAllYear(String selectedmake, String model) {

        YearList.clear();
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
                if (selectedmake.equals(makes_obj.getString("name"))) {
                    JSONArray modelarray = makes_obj.getJSONArray("models");
                    for (int i = 0; i < modelarray.length(); i++) {
                        JSONObject indmodel = modelarray.getJSONObject(i);

                        if (model.equals(indmodel.getString("name"))) {

                            JSONArray yeararray = indmodel.getJSONArray("year");

                            for (int k = 0; k < yeararray.length(); k++) {
                                JSONObject indyear = yeararray.getJSONObject(k);
                                YearList.add(indyear.getString("year"));
                                Collections.sort(YearList);
                            }

                        }

                    }
                }
            }
            //Log.e("Arraylength",MakeList.toString());
            yearAdapter = new CustomSpinnerAdapter<String>(context, R.layout.custom_dealer_spinner_item);
            yearAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
            yearAdapter.add("Select Year");
            yearAdapter.addAll(YearList);
            Yearspinner.setAdapter(yearAdapter);
            Yearspinner.setSelection(0);
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAllModel(String make) {

        Log.e("Make",make);
        ModelList.clear();
       // YearList.clear();
        String edmundresponse = AppController.getInstance().EdmundsResponse;
        JSONObject edmundjson = null;
        JSONArray makesArray;

        try {
            if (!edmundresponse.equals(null)){
                edmundjson = new JSONObject(edmundresponse);
            makesArray = edmundjson.getJSONArray("makes");
            //Log.e("EdmundsData",String .valueOf(makesArray));
            for (int index = 0; index < makesArray.length(); index++) {
                try {
                    JSONObject makes_obj = makesArray.getJSONObject(index);

                    if (make.equals(makes_obj.getString("name"))) {
                        JSONArray modelarray = makes_obj.getJSONArray("models");
                        //Log.e("EdmundsData", String.valueOf(modelarray));
                        for (int i = 0; i < modelarray.length(); i++) {
                            JSONObject indmodel = modelarray.getJSONObject(i);
                            ModelList.add(indmodel.getString("name"));
                            Collections.sort(ModelList);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Log.e("Arraylength",ModelList.toString());
            modelAdapter = new CustomSpinnerAdapter<String>(context, R.layout.custom_dealer_spinner_item);
            modelAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
            modelAdapter.add("Select Model");
            modelAdapter.addAll(ModelList);
            Modelspinner.setAdapter(modelAdapter);
            Modelspinner.setSelection(0);
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
