package bss.bbs.com.teslaclone.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import bss.bbs.com.teslaclone.R;


/**
 * Created by Anbu on 28-Nov-17.
 */

public class PrivacyPolicy extends FragmentActivity{
    Context context;
    WebView Privacypolicy;
    Button leftbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacypolicy);
        Privacypolicy=(WebView) findViewById(R.id.webView);

        leftbutton = (Button) findViewById(R.id.title_bar_left_menu);

        leftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.riht_to_left);

            }
        });


        Privacypolicy.getSettings().setJavaScriptEnabled(true);
        Privacypolicy.loadUrl("http://ec2-52-35-98-183.us-west-2.compute.amazonaws.com/findyourcarapp_api/public/api/index/privacy-policy");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.riht_to_left);
    }
}

