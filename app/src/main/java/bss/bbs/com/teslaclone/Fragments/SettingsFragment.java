package bss.bbs.com.teslaclone.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import bss.bbs.com.teslaclone.Activity.AboutUsActivity;
import bss.bbs.com.teslaclone.Activity.PrivacyPolicy;
import bss.bbs.com.teslaclone.Activity.TermOfUse;
import bss.bbs.com.teslaclone.R;

/**
 * Created by sairaj on 21/08/17.
 */

@SuppressLint("ValidFragment")
public class SettingsFragment extends Fragment implements View.OnClickListener{
    View parentView;
    Context context;
    TextView Termsofusetext,privacypolicy,AboutusReport;
    private ImageView switchOff;
    private ImageView switchOn;
    public SettingsFragment(Context con) {
        context = con;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.settings, container, false);

        TextView titleview = (TextView) getActivity().findViewById(R.id.titleid);
        titleview.setText("Settings");
        setUpViews(parentView);
        return parentView;
    }
    public void setUpViews(View view){

        Termsofusetext = (TextView)view.findViewById(R.id.terms_of_use);
        privacypolicy = (TextView)view.findViewById(R.id.privcy_policy);
        AboutusReport = (TextView)view.findViewById(R.id.AboutusReport);

       /* switchOn = (ImageView)view.findViewById( R.id.switch_on4 );
        switchOff= (ImageView)view.findViewById( R.id.switch_off4);*/

        Termsofusetext.setOnClickListener(this);
        privacypolicy.setOnClickListener(this);
        AboutusReport.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.terms_of_use:
                Intent i1=new Intent(getActivity(), TermOfUse.class);
                startActivity(i1);
                ((Activity) context).overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.privcy_policy:
                Intent i2=new Intent(getActivity(), PrivacyPolicy.class);
                context.startActivity(i2);
                ((Activity) context).overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
            case R.id.AboutusReport:
                Intent i3=new Intent(getActivity(), AboutUsActivity.class);
                context.startActivity(i3);
                ((Activity) context).overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                break;
        }
    }
}
