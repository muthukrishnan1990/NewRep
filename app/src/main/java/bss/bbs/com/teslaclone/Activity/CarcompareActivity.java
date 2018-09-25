package bss.bbs.com.teslaclone.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import bss.bbs.com.teslaclone.Fragments.CarcompareFragment;
import bss.bbs.com.teslaclone.Fragments.FilterFragment;
import bss.bbs.com.teslaclone.MainActivity;
import bss.bbs.com.teslaclone.R;

/**
 * Created by Anbu on 10-Nov-17.
 */

public class CarcompareActivity extends FragmentActivity {
    private CarcompareActivity mContext;
    Button leftbutton;
    String BannerImage1,Exterior1,Interior1,Make1,Model1,Year1,VIN1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carcompare_activity);
        mContext = this;
        leftbutton = (Button) findViewById(R.id.title_bar_left_menu);
        Intent intent = getIntent();
        if(!intent.equals(null)) {
            Make1 = intent.getStringExtra("Make");
            Model1 = intent.getStringExtra("Model");
            Year1 = intent.getStringExtra("Year");
            BannerImage1 = intent.getStringExtra("Bannerimage");
            VIN1 = intent.getStringExtra("Vin");
            Exterior1 = intent.getStringExtra("Exterior");
            Interior1 = intent.getStringExtra("Interior");
            targetfragment();
        }
        leftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.riht_to_left);

            }
        });

    }
    public void targetfragment(){
        Bundle bundle = new Bundle();
        bundle.putString("First_Make",Make1);
        bundle.putString("First_Model",Model1);
        bundle.putString("First_Year",Year1);
        bundle.putString("First_Image",BannerImage1);
        bundle.putString("First_Vin",VIN1);
        bundle.putString("First_Ext_Color",Exterior1);
        bundle.putString("First_Int_Color",Interior1);
        CarcompareFragment frag = new CarcompareFragment(mContext);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        frag.setArguments(bundle);
        transaction.add(R.id.carcompare,frag,"Test Fragment");
        transaction.commit();

    }

}
