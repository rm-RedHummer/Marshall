package com.jarvis.marshall.view.home.event;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.EventDA;
import com.jarvis.marshall.view.home.createEvent.CreateEventActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    private View view;
    private String eventKey,userPosition;
    private TextView title, date, time, venue,description;
    private FloatingActionButton editFab;
    private ArrayList<String> dataList,membersList;


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if(bundle!=null){
            eventKey = bundle.getString("eventKey");
            userPosition = bundle.getString("userPosition");
        }
        final Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        dataList = new ArrayList<>();
        view = inflater.inflate(R.layout.fragment_details, container, false);
        wireViews();
        EventDA eventDA = new EventDA();
        eventDA.getSpecificEvent(eventKey).addValueEventListener(new ValueEventListener() {
            Integer ctr = 0;
            String tempTime;
            @Override
            public void onDataChange(DataSnapshot ds) {

                for(DataSnapshot dataSnapshot: ds.getChildren()){
                    if(dataSnapshot.getKey().equals("date")){
                        dataList.add(dataSnapshot.getValue().toString());
                        date.setText(processDate(dataSnapshot.getValue().toString()));
                    } else if(dataSnapshot.getKey().equals("description")){
                        dataList.add(dataSnapshot.getValue().toString());
                        description.setText(dataSnapshot.getValue().toString());
                    } else if(dataSnapshot.getKey().equals("endTime")){
                        dataList.add(dataSnapshot.getValue().toString());
                        tempTime = dataSnapshot.getValue().toString();
                    } else if(dataSnapshot.getKey().equals("eventMembers")){
                        membersList = new ArrayList<>();
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            membersList.add(dataSnapshot1.getKey()+":"+dataSnapshot1.getValue());
                        }
                    } else if(dataSnapshot.getKey().equals("groupKey")){
                        dataList.add(dataSnapshot.getValue().toString());
                    } else if(dataSnapshot.getKey().equals("key")){
                        dataList.add(dataSnapshot.getValue().toString());
                    } else if(dataSnapshot.getKey().equals("name")){
                        dataList.add(dataSnapshot.getValue().toString());
                        title.setText(dataSnapshot.getValue().toString());
                        toolbar.setTitle(dataSnapshot.getValue().toString());
                    } else if(dataSnapshot.getKey().equals("startTime")){
                        dataList.add(dataSnapshot.getValue().toString());
                        time.setText(processTime(dataSnapshot.getValue().toString())+" - "+processTime(tempTime));
                    } else if(dataSnapshot.getKey().equals("venue")){
                        dataList.add(dataSnapshot.getValue().toString());
                        venue.setText(dataSnapshot.getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity();

            }
        });

        return view;
    }

    public void wireViews(){
        title = view.findViewById(R.id.fragDetails_title);
        description = view.findViewById(R.id.fragDetails_description);
        date = view.findViewById(R.id.fragDetails_date);
        time = view.findViewById(R.id.fragDetails_time);
        venue = view.findViewById(R.id.fragDetails_venue);
        editFab = view.findViewById(R.id.fragDetails_fab);

        if(userPosition.equals("None")||userPosition.equals("Member"))
            editFab.setVisibility(View.GONE);
    }

    public void changeActivity(){
        CreateEventActivity createEventActivity = new CreateEventActivity();
        Intent intent = new Intent(getContext(),createEventActivity.getClass());
        intent.putExtra("groupKey",dataList.get(3));
        intent.putExtra("eventArrayList",dataList);
        intent.putExtra("membersList",membersList);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.enter_anim,R.anim.stay_anim);
    }

    private String processDate(String date){
        String newDate="";
        String[] splitDate = date.split("/");
        String[] month = {"Jan","Feb","March","April","May","June","July","Aug","Sept","Oct","Nov","Dec"};
        for(int i = 0; i <12; i++){
            if(splitDate[0].equals(String.valueOf(i)))
                newDate = month[i]+" "+splitDate[1]+", "+"20"+splitDate[2].substring(splitDate[2].length()-2);
        }
        return newDate;
    }

    private String processTime(String time){
        String newTime="",postTime;
        String[] timeArray = time.split(":");
        int hour = Integer.parseInt(timeArray[0]);
        int minute  = Integer.parseInt(timeArray[1]);
        if(hour >11)
            postTime = "pm";
        else
            postTime = "am";

        if(hour>12){
            hour = hour - 12;
        } else if(hour == 0)
            hour = 12;

        if(minute<10)
            timeArray[1] = "0"+timeArray[1];
        newTime = String.valueOf(hour)+":"+timeArray[1]+postTime;
        return newTime;
    }

}
