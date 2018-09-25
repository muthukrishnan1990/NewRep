package bss.bbs.com.teslaclone.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

/**
 * Created by Anbu on 17-Nov-17.
 */

@SuppressLint("ValidFragment")
public class ContactusFragment extends Fragment {
    Context context;
    EditText FirstName,Lastname,Email,PhoneNo,Comment;
    Button submit;
    String Firstname1,LastName1,EmailID,Phone1,Comment1;
    public ContactusFragment(Context con) {
        context = con;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentview = inflater.inflate(R.layout.contact_us, container, false);

        TextView titleview = (TextView) getActivity().findViewById(R.id.titleid);
        titleview.setText("Contact Us");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setUpViews(parentview);
         return parentview;
    }
    private void setUpViews(View view) {
        FirstName = (EditText)view.findViewById(R.id.firstnamelist);
        Lastname = (EditText)view.findViewById(R.id.lastnamelist);
        Email = (EditText)view.findViewById(R.id.emailnamelist);
        PhoneNo = (EditText)view.findViewById(R.id.phonenumber);
        Comment = (EditText)view.findViewById(R.id.commentdata);
        submit =(Button)view.findViewById(R.id.push_button1);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int socketTimeout = 30000; // 30 seconds. You can change it
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                Map<String, String> params = new HashMap<String, String>();

                Firstname1 = FirstName.getText().toString();
                LastName1 = Lastname.getText().toString();
                EmailID = Email.getText().toString();
                Phone1 = PhoneNo.getText().toString();
                Comment1= Comment.getText().toString();

                if (!validateFirstName(Firstname1)|| Firstname1=="" || Firstname1==null) {
                    showChangeLangDialog("","Enter the Firstname");
                } else if (!validateLastName(LastName1) || LastName1=="" || LastName1==null) {
                    showChangeLangDialog("","Enter the LastName");
                } else if (!isValidEmail(EmailID) || EmailID=="" || EmailID==null) {
                    showChangeLangDialog("","Enter the EmailID");
                }else if (!isValidPhone(Phone1) || Phone1=="" || Phone1==null) {
                    showChangeLangDialog("","Enter the Phone no");
                } else if (!validateLastName(Comment1) || Comment1=="" || Comment1==null) {
                    showChangeLangDialog("","Enter the Comment");
                }else {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(Comment.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    params.put("firstname", Firstname1);
                    params.put("lastname", LastName1);
                    params.put("email", EmailID);
                    params.put("phone", Phone1);
                    params.put("comments", Comment1);

                    VolleyCustomPostRequest jsObjRequest = new VolleyCustomPostRequest(Request.Method.POST, Constants.contactus_URL, params, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            // TODO Auto-generated method stub
                            Log.e("Response", response.toString());
                            showChangeLangDialog("Uh oh!","Mail Successfully sent.....");

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
        });


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