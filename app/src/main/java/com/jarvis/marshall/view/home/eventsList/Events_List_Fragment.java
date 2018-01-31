package com.jarvis.marshall.view.home.eventsList;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jarvis.marshall.R;
import com.jarvis.marshall.model.Group;

/**
 * A simple {@link Fragment} subclass.
 */
public class Events_List_Fragment extends Fragment {


    public Events_List_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events__list, container, false);
    }

}
