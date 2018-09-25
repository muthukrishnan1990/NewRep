package bss.bbs.com.teslaclone;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;

import bss.bbs.com.teslaclone.Fragments.CalendarFragment;
import bss.bbs.com.teslaclone.Fragments.CarcompareFragment;
import bss.bbs.com.teslaclone.Fragments.ContactusFragment;
import bss.bbs.com.teslaclone.Fragments.DealersFragment;
import bss.bbs.com.teslaclone.Fragments.FavoritesFragment;
import bss.bbs.com.teslaclone.Fragments.FilterFragment;
import bss.bbs.com.teslaclone.Fragments.HomeFragment;
import bss.bbs.com.teslaclone.Fragments.JoinusFragment;
import bss.bbs.com.teslaclone.Fragments.ListingFragment;
import bss.bbs.com.teslaclone.Fragments.SellyourcarFragment;
import bss.bbs.com.teslaclone.Fragments.SettingsFragment;
import bss.bbs.com.teslaclone.Residemenu.ResideMenu;
import bss.bbs.com.teslaclone.Residemenu.ResideMenuItem;

import static java.lang.String.valueOf;

public class MainActivity extends FragmentActivity implements View.OnTouchListener {
    String msg = "Android : ";

    private ResideMenu resideMenu;
    private MainActivity mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemFilter;
    private ResideMenuItem itemDealers;
    private ResideMenuItem itemSellyourcar;
    private ResideMenuItem itemCarcomparison;
    private ResideMenuItem itemJoinus;
    private ResideMenuItem itemContactUs;
    private ResideMenuItem itemListing;
    private ResideMenuItem itemFavorites;
    private ResideMenuItem itemSettings;
    private ResideMenuItem itemCalendar;

    SharedPreferences pref;
    double latitude, langitute;
    String zipcode;
    int Radius;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.e(msg, "The onCreate() event");

        /** Automatically showing keyboard hidden */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mContext = this;
        setUpMenu();
        changeFragment(new HomeFragment(mContext));


    }

    /**
     * Called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.e(msg, "The onStart() event");
    }

    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(msg, "The onResume() event");
    }

    /**
     * Called when another activity is taking focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.e(msg, "The onPause() event");
    }

    /**
     * Called when the activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.e(msg, "The onStop() event");
    }

    /**
     * Called just before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(msg, "The onDestroy() event");
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        //set background of menu
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setScaleValue(0.5f);

        // create menu items;
        itemHome = new ResideMenuItem(this, R.drawable.showroom_icon, "Showroom");
        itemFilter = new ResideMenuItem(this, R.drawable.search_icon, "Car filter");
        itemCarcomparison = new ResideMenuItem(this, R.drawable.compare_icon, "Car comparison");
        itemDealers = new ResideMenuItem(this, R.drawable.dealer_icon, "Dealers");
        itemFavorites      = new ResideMenuItem(this, R.drawable.favorites_icon, "Favorites");
        itemJoinus = new ResideMenuItem(this, R.drawable.joinus, "Join Us");
        itemContactUs = new ResideMenuItem(this, R.drawable.contact_us, "Contact Us");
        itemSettings       = new ResideMenuItem(this, R.drawable.settings_icon, "Settings");


        itemHome.setOnTouchListener(this);
        itemFilter.setOnTouchListener(this);
        itemCarcomparison.setOnTouchListener(this);
        itemDealers.setOnTouchListener(this);
        itemFavorites.setOnTouchListener(this);
        itemJoinus.setOnTouchListener(this);
        itemContactUs.setOnTouchListener(this);
        itemSettings.setOnTouchListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemFilter, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCarcomparison, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemDealers, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemFavorites, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemJoinus, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemContactUs, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);


        findViewById(R.id.title_bar_left_menu).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                InputMethodManager imm2 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(resideMenu.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                return false;
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }


    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenuï¼Ÿ
    public ResideMenu getResideMenu() {
        return resideMenu;
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (view == itemHome) {
            itemHome.setBackgroundColor(Color.parseColor("#A9A9A9"));
            itemFilter.setBackgroundColor(Color.parseColor("#00000000"));
            itemDealers.setBackgroundColor(Color.parseColor("#00000000"));
            itemCarcomparison.setBackgroundColor(Color.parseColor("#00000000"));
            itemFavorites.setBackgroundColor(Color.parseColor("#00000000"));
            itemJoinus.setBackgroundColor(Color.parseColor("#00000000"));
            itemContactUs.setBackgroundColor(Color.parseColor("#00000000"));
            itemSettings.setBackgroundColor(Color.parseColor("#00000000"));
            changeFragment(new HomeFragment(mContext));
        } else if (view == itemFilter) {
            itemHome.setBackgroundColor(Color.parseColor("#00000000"));
            itemFilter.setBackgroundColor(Color.parseColor("#A9A9A9"));
            itemDealers.setBackgroundColor(Color.parseColor("#00000000"));
            itemCarcomparison.setBackgroundColor(Color.parseColor("#00000000"));
            itemFavorites.setBackgroundColor(Color.parseColor("#00000000"));
            itemJoinus.setBackgroundColor(Color.parseColor("#00000000"));
            itemContactUs.setBackgroundColor(Color.parseColor("#00000000"));
            itemSettings.setBackgroundColor(Color.parseColor("#00000000"));
            changeFragment(new FilterFragment(mContext));
        }  else if (view == itemCarcomparison) {
            itemHome.setBackgroundColor(Color.parseColor("#00000000"));
            itemFilter.setBackgroundColor(Color.parseColor("#00000000"));
            itemDealers.setBackgroundColor(Color.parseColor("#00000000"));
            itemCarcomparison.setBackgroundColor(Color.parseColor("#A9A9A9"));
            itemFavorites.setBackgroundColor(Color.parseColor("#00000000"));
            itemJoinus.setBackgroundColor(Color.parseColor("#00000000"));
            itemContactUs.setBackgroundColor(Color.parseColor("#00000000"));
            itemSettings.setBackgroundColor(Color.parseColor("#00000000"));
            changeFragment(new CarcompareFragment(mContext));
        } else if (view == itemDealers) {
            itemHome.setBackgroundColor(Color.parseColor("#00000000"));
            itemFilter.setBackgroundColor(Color.parseColor("#00000000"));
            itemDealers.setBackgroundColor(Color.parseColor("#A9A9A9"));
            itemCarcomparison.setBackgroundColor(Color.parseColor("#00000000"));
            itemFavorites.setBackgroundColor(Color.parseColor("#00000000"));
            itemJoinus.setBackgroundColor(Color.parseColor("#00000000"));
            itemContactUs.setBackgroundColor(Color.parseColor("#00000000"));
            itemSettings.setBackgroundColor(Color.parseColor("#00000000"));
            changeFragment(new DealersFragment(mContext));
        }else if (view == itemFavorites){
            itemHome.setBackgroundColor(Color.parseColor("#00000000"));
            itemFilter.setBackgroundColor(Color.parseColor("#00000000"));
            itemDealers.setBackgroundColor(Color.parseColor("#00000000"));
            itemCarcomparison.setBackgroundColor(Color.parseColor("#00000000"));
            itemFavorites.setBackgroundColor(Color.parseColor("#A9A9A9"));
            itemJoinus.setBackgroundColor(Color.parseColor("#00000000"));
            itemContactUs.setBackgroundColor(Color.parseColor("#00000000"));
            itemSettings.setBackgroundColor(Color.parseColor("#00000000"));
            changeFragment(new FavoritesFragment(mContext));
        }else if (view == itemJoinus) {
            itemHome.setBackgroundColor(Color.parseColor("#00000000"));
            itemFilter.setBackgroundColor(Color.parseColor("#00000000"));
            itemDealers.setBackgroundColor(Color.parseColor("#00000000"));
            itemCarcomparison.setBackgroundColor(Color.parseColor("#00000000"));
            itemFavorites.setBackgroundColor(Color.parseColor("#00000000"));
            itemJoinus.setBackgroundColor(Color.parseColor("#A9A9A9"));
            itemContactUs.setBackgroundColor(Color.parseColor("#00000000"));
            itemSettings.setBackgroundColor(Color.parseColor("#00000000"));
            changeFragment(new JoinusFragment(mContext));
        } else if (view == itemContactUs) {
            itemHome.setBackgroundColor(Color.parseColor("#00000000"));
            itemFilter.setBackgroundColor(Color.parseColor("#00000000"));
            itemDealers.setBackgroundColor(Color.parseColor("#00000000"));
            itemCarcomparison.setBackgroundColor(Color.parseColor("#00000000"));
            itemFavorites.setBackgroundColor(Color.parseColor("#00000000"));
            itemJoinus.setBackgroundColor(Color.parseColor("#00000000"));
            itemContactUs.setBackgroundColor(Color.parseColor("#A9A9A9"));
            itemSettings.setBackgroundColor(Color.parseColor("#00000000"));
            changeFragment(new ContactusFragment(mContext));
        }else if (view == itemSettings){
            itemHome.setBackgroundColor(Color.parseColor("#00000000"));
            itemFilter.setBackgroundColor(Color.parseColor("#00000000"));
            itemDealers.setBackgroundColor(Color.parseColor("#00000000"));
            itemCarcomparison.setBackgroundColor(Color.parseColor("#00000000"));
            itemFavorites.setBackgroundColor(Color.parseColor("#00000000"));
            itemJoinus.setBackgroundColor(Color.parseColor("#00000000"));
            itemContactUs.setBackgroundColor(Color.parseColor("#00000000"));
            itemSettings.setBackgroundColor(Color.parseColor("#A9A9A9"));
            changeFragment(new SettingsFragment(mContext));
        }
        resideMenu.closeMenu();
        return false;
    }

}