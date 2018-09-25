package bss.bbs.com.teslaclone.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bss.bbs.com.teslaclone.R;
import bss.bbs.com.teslaclone.Singleton.AppController;
import bss.bbs.com.teslaclone.Singleton.Constants;
import bss.bbs.com.teslaclone.network.VolleyCustomPostRequest;


@SuppressLint("ValidFragment")
public class JoinusFragment extends Fragment {

    Context context;
    SharedPreferences pref;
    View parentView;
    Button Submit;
    private AwesomeValidation awesomeValidation;
    String Dealername,Firstname,LastName,EmailID,Phone,Website,Inventory;
    EditText DealershipName,FirstName,Lastname,Email,PhoneNo,WebsiteURL,Inventory_Provide;
    public JoinusFragment(Context con) {
        context = con;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.signup_layout, container, false);

        TextView titleview = (TextView) getActivity().findViewById(R.id.titleid);
        titleview.setText("Join Us");
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setUpViews(parentView);
        return parentView;
    }
    private void setUpViews(View view) {
        DealershipName = (EditText)view.findViewById(R.id.Dealername);
        FirstName = (EditText)view.findViewById(R.id.Firstname);
        Lastname = (EditText)view.findViewById(R.id.Lastname);
        Email = (EditText)view.findViewById(R.id.Emailaddress);
        PhoneNo = (EditText)view.findViewById(R.id.PhoneNo);
        WebsiteURL = (EditText)view.findViewById(R.id.WebsiteUrl);
        Inventory_Provide = (EditText)view.findViewById(R.id.Inventoryprovide);
        Submit =(Button)view.findViewById(R.id.push_button);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });


    }

    public void loadData() {

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        Map<String, String> params = new HashMap<String, String>();

        Dealername = DealershipName.getText().toString();
        Firstname = FirstName.getText().toString();
        LastName = Lastname.getText().toString();
        EmailID = Email.getText().toString();
        Website = WebsiteURL.getText().toString();
        Phone = PhoneNo.getText().toString();
        Inventory= Inventory_Provide.getText().toString();
        if (!validateLastName(Dealername) || Dealername=="" || Dealername==null) {
            showChangeLangDialog("","Enter the Dealername");
        }else if (!validateFirstName(Firstname)|| Firstname=="" || Firstname==null) {
            showChangeLangDialog("","Enter the Firstname");
        } else if (!validateLastName(LastName) || LastName=="" || LastName==null) {
            showChangeLangDialog("","Enter the LastName");
        } else if (!isValidEmail(EmailID) || EmailID=="" || EmailID==null) {
            showChangeLangDialog("","Enter the EmailID");
        }else if (!isValidPhone(Phone) || Phone=="" || Phone==null) {
            showChangeLangDialog("","Enter the Phone no");
        } else if (!isValidUrl(Website) || Website=="" || Website==null) {
            showChangeLangDialog("","Enter the Website");
        }else if (!validateLastName(Inventory) || Inventory=="" || Inventory==null) {
            showChangeLangDialog("","Enter the Inventory");
        }else{
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(Inventory_Provide.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);
            params.put("dealershipname", Dealername);
            params.put("firstname", Firstname);
            params.put("lastname", LastName);
            params.put("email", EmailID);
            params.put("phone", Phone);
            params.put("websiteurl", Website);
            params.put("inventory", Inventory);


            VolleyCustomPostRequest jsObjRequest = new VolleyCustomPostRequest(Request.Method.POST, Constants.signup_URL, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    // TODO Auto-generated method stub
                    Log.e("Response", response.toString());

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
    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        if(m.matches())
            return true;
        else
            return false;
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

