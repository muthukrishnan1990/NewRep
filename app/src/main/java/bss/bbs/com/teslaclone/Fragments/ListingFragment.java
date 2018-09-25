package bss.bbs.com.teslaclone.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bss.bbs.com.teslaclone.R;

/**
 * Created by sairaj on 21/08/17.
 */

public class ListingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.listing, container, false);
    }
}
