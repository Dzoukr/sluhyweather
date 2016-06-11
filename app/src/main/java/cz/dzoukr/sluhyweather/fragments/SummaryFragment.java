package cz.dzoukr.sluhyweather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz.dzoukr.sluhyweather.MainActivity;
import cz.dzoukr.sluhyweather.R;

/**
 * Created by Roman on 14-Aug-15.
 */
public class SummaryFragment extends Fragment {

    public SummaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_summary, container, false);
        return rootView;
    }
}
