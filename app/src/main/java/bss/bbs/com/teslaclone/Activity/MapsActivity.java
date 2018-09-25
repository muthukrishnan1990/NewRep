package bss.bbs.com.teslaclone.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import bss.bbs.com.teslaclone.R;


/**
 * Created by Anbu on 07-Nov-17.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    GoogleMap googleMap;
    String lati,longi;
    String address,city,state,zipcode,phone;
    Button leftbutton;
    TextView addresstextView,phonetextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        if (!intent.equals(null)){
      //  if(!intent.getStringExtra("Lat").equals(null) && !intent.getStringExtra("Lan").equals(null)){
            lati = intent.getStringExtra("Lat");
            Log.e("Lat",intent.getStringExtra("Lat"));
            longi = intent.getStringExtra("Lan");
            Log.e("Lat",intent.getStringExtra("Lan"));
            address = intent.getStringExtra("Address");
            city = intent.getStringExtra("DealerCity");
            state = intent.getStringExtra("DealerState");
            zipcode = intent.getStringExtra("Postalcode");
            phone = intent.getStringExtra("phoneno");
            initMap();
        }

        leftbutton = (Button) findViewById(R.id.title_bar_left_menu);
        addresstextView = (TextView) findViewById(R.id.address);
        phonetextView = (TextView) findViewById(R.id.phone);
        leftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.riht_to_left);

            }
        });
    }

    private void initMap() {

        SupportMapFragment supportMapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        supportMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap Map) {
        googleMap = Map;
        if (!lati.equals(null) && !longi.equals(null)) {
                LatLng latLng = new LatLng(Double.parseDouble(lati), Double.parseDouble(longi));
                addresstextView.setText(address + "\n" + city + "," + state + "," + zipcode);
                phonetextView.setText(phone);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(10).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.addMarker(new MarkerOptions().position(latLng));

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.riht_to_left);
    }
}

