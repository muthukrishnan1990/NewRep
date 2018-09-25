package bss.bbs.com.teslaclone.Activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import bss.bbs.com.teslaclone.R;


/**
 * Created by Anbu on 24-Nov-17.
 */

public class TermOfUse extends FragmentActivity {
    Context context;
    WebView termsofuse;
    Button leftbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_of_use);
        termsofuse=(WebView) findViewById(R.id.webView);

        leftbutton = (Button) findViewById(R.id.title_bar_left_menu);

        leftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.riht_to_left);

            }
        });


        termsofuse.getSettings().setJavaScriptEnabled(true);
        termsofuse.loadUrl("http://ec2-52-35-98-183.us-west-2.compute.amazonaws.com/findyourcarapp_api/public/api/index/terms-of-use");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.riht_to_left);
    }
}
