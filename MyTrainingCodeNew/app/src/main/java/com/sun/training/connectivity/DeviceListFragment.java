package com.sun.training.connectivity;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sun.training.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceListFragment extends Fragment {


    public DeviceListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_list, container, false);
    }

    public void updateThisDevice(Parcelable device) {
        Log.d("wifi_p2p", "update device:" + device);

    }


}
