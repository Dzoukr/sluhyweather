package cz.dzoukr.sluhyweather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz.dzoukr.sluhyweather.R;

/**
 * Created by Roman on 14-Aug-15.
 */
public class RainFragment extends Fragment {

    public RainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_rain, container, false);
        return rootView;
    }

}
