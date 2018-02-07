package com.jarvis.marshall.view.home.eventsList;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.EventDA;
import com.jarvis.marshall.model.Event;

import java.util.ArrayList;

/**
 * Created by Jarvis on 02/02/2018.
 */

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.ListHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Event> eventArrayList;
    private ProgressDialog progressDialog;
    private EventDA eventDA;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private FirebaseAuth mAuth;
    public EventsListAdapter(Context context, ArrayList<Event> eventArrayList, ProgressDialog progressDialog){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.eventArrayList = eventArrayList;
        this.progressDialog = progressDialog;
        viewBinderHelper.setOpenOnlyOne(true);
        eventDA = new EventDA();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.viewholder_event_names, parent, false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, int position) {
        Event event = eventArrayList.get(position);
        viewBinderHelper.bind(holder.swipeRevealLayout,event.getKey());

        eventDA.getSpecificEvent(event.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    int num = 1;
                    String date=null, description=null, endTime=null, groupKey=null, key=null, name=null, startTime=null,status=null,venue=null;
                    ArrayList<String> eventMembers = new ArrayList<>();
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        switch (num) {
                            case (1):
                                holder.date.setText(ds.getValue().toString());
                                break;
                            case (3):
                                holder.endTime.setText(ds.getValue().toString());
                                break;
                            case (4):
                                for(DataSnapshot ds2: ds.getChildren()){
                                    if(ds2.getKey().equals(mAuth.getCurrentUser().getUid()))
                                        holder.userPosition.setText(ds2.getValue().toString());
                                    else {
                                        holder.check.setVisibility(View.INVISIBLE);
                                        holder.userPosition.setVisibility(View.INVISIBLE);
                                    }
                                }
                                break;
                            case (7):
                                holder.eventName.setText(ds.getValue().toString());
                                break;
                            case (8):
                                holder.startTime.setText(ds.getValue().toString());
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
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        progressDialog.dismiss();
    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder{
        private TextView eventName,date,startTime,endTime,userPosition,progress;
        private ImageView check;
        private SwipeRevealLayout swipeRevealLayout;
        public ListHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.vh_event_name);
            date = itemView.findViewById(R.id.vh_event_date);
            startTime = itemView.findViewById(R.id.vh_event_startTime);
            endTime = itemView.findViewById(R.id.vh_event_endTime);
            userPosition = itemView.findViewById(R.id.vh_event_userStatus);
            progress = itemView.findViewById(R.id.vh_event_progress);
            check = itemView.findViewById(R.id.vh_event_checkImage);
            swipeRevealLayout = itemView.findViewById(R.id.vh_events_list_swipe_reveal_layout);
        }
    }
}
