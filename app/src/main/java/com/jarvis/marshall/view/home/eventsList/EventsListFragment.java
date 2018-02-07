package com.jarvis.marshall.view.home.eventsList;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.EventDA;
import com.jarvis.marshall.model.Event;
import com.jarvis.marshall.view.home.groups.HomeFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsListFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private EventDA eventDA;
    private String tag,groupKey;

    public EventsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle!=null){
            groupKey = bundle.getString("groupKey");
        }

        FragmentManager fm = getActivity().getSupportFragmentManager();
        tag = fm.getBackStackEntryAt(
                fm.getBackStackEntryCount() - 1).getName();

        eventDA = new EventDA();
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
        /*
        //Event event = new Event();
        //eventDA.createNewEvent(event);
        CreateEventFragment createEventFragment = new CreateEventFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_anim,R.anim.stay_anim,R.anim.stay_anim,R.anim.exit_anim);
        //ft.add(R.id.main_framelayout, eventsListFragment, "EventsListFragment");
        ft.replace(R.id.main_framelayout, createEventFragment,tag);
        ft.addToBackStack(tag);
        ft.commit();*/
        CreateEventActivity createEventActivity = new CreateEventActivity();
        Intent intent = new Intent(getContext(),createEventActivity.getClass());
        intent.putExtra("groupKey",groupKey);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.enter_anim,R.anim.stay_anim);
    }

    public void loadEventsListRecyclerView(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dialog);
        progressDialog.show();
        progressDialog.setMessage("Loading events..");

        final ArrayList<Event> eventArrayList = new ArrayList<>();
        final EventsListAdapter adapter = new EventsListAdapter(getContext(),eventArrayList,progressDialog);
        recyclerView.setAdapter(adapter);

        eventDA.getAllEvents(groupKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue()!=null){
                    int num = 1;
                    String date=null, description=null, endTime=null, groupKey=null, key=null, name=null, startTime=null,status=null,venue=null;
                    ArrayList<String> eventMembers = new ArrayList<>();
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        switch (num) {
                            case (1):
                                date = ds.getValue().toString();
                                break;
                            case (2):
                                description = ds.getValue().toString();
                                break;
                            case (3):
                                endTime = ds.getValue().toString();
                                break;
                            case (4):
                                for(DataSnapshot ds2: ds.getChildren()){
                                    eventMembers.add(ds2.getKey()+":"+ds2.getValue());
                                }
                                break;
                            case (5):
                                groupKey = ds.getValue().toString();
                                break;
                            case (6):
                                key = ds.getValue().toString();
                                break;
                            case (7):
                                name = ds.getValue().toString();
                                break;
                            case (8):
                                startTime = ds.getValue().toString();
                                break;
                            case (9):
                                status = ds.getValue().toString();
                                break;
                            case (10):
                                venue = ds.getValue().toString();
                                Event event = new Event(name,date,startTime,endTime,venue,description,status,key,groupKey);
                                event.setEventMembers(eventMembers);
                                eventArrayList.add(event);
                                adapter.notifyItemInserted(eventArrayList.size() - 1);
                                break;
                        }
                        if(num <= 10)
                            num++;
                        else
                            num = 1;
                    }
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        progressDialog.dismiss();
    }
}
