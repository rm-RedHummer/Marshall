package com.jarvis.marshall.view.home.eventsList;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jarvis.marshall.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsListFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;

    public EventsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle!=null){
            AlertDialog.Builder dg = new AlertDialog.Builder(getContext());
            dg.setMessage(bundle.getString("groupKey"));
            dg.show();
        }
        view = inflater.inflate(R.layout.fragment_events_list, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Events");

        recyclerView = view.findViewById(R.id.recyclerview_events_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        loadEventsListRecyclerView();

        FloatingActionButton fab = view.findViewById(R.id.events_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent();
            }
        });

        return view;
    }

    public void addEvent(){

    }

    public void loadEventsListRecyclerView(){

    }
}
