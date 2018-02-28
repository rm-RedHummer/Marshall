package com.jarvis.marshall.view.home.event;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.EventDA;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    private View view;
    private String eventKey;
    private TextView title, date, time, venue,description;
    private FloatingActionButton editFab;

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
        }

        view = inflater.inflate(R.layout.fragment_details, container, false);
        wireViews();
        EventDA eventDA = new EventDA();
        eventDA.getSpecificEvent(eventKey).addValueEventListener(new ValueEventListener() {
            Integer ctr = 0;
            String tempTime;
            @Override
            public void onDataChange(DataSnapshot ds) {
                for(DataSnapshot dataSnapshot: ds.getChildren()){
                    switch (ctr){
                        case(0):
                            date.setText(dataSnapshot.getValue().toString());
                            break;
                        case(1):
                            description.setText(dataSnapshot.getValue().toString());
                            break;
                        case(2):
                            tempTime = dataSnapshot.getValue().toString();
                            break;
                        case(6):
                            title.setText(dataSnapshot.getValue().toString());
                            break;
                        case(7):
                            time.setText(dataSnapshot.getValue().toString()+" - "+tempTime);
                            break;
                        case(9):
                            venue.setText(dataSnapshot.getValue().toString());
                            break;
                    }
                    if(ctr < 10)
                        ctr++;
                    else
                        ctr=0;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
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
    }

}
