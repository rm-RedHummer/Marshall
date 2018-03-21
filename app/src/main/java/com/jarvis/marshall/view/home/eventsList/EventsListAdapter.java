package com.jarvis.marshall.view.home.eventsList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.MainActivity;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.EventDA;
import com.jarvis.marshall.model.Event;
import com.jarvis.marshall.view.home.event.EventMainFragment;
import com.jarvis.marshall.view.home.members.MembersFragment;

import java.util.ArrayList;
import java.util.Map;

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
    private String userPosition;
    public EventsListAdapter(Context context, ArrayList<Event> eventArrayList, ProgressDialog progressDialog,String userPosition){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.eventArrayList = eventArrayList;
        this.progressDialog = progressDialog;
        viewBinderHelper.setOpenOnlyOne(true);
        eventDA = new EventDA();
        mAuth = FirebaseAuth.getInstance();
        this.userPosition = userPosition;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.viewholder_event_names, parent, false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, final int position) {
        final Event event = eventArrayList.get(position);
        viewBinderHelper.bind(holder.swipeRevealLayout,event.getKey());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userPos = "";
                boolean isUserMember=false;
                if(!userPosition.equals("Admin")){
                    for(Object key: event.getEventMembers().keySet()){
                        if(key.toString().equals(mAuth.getCurrentUser().getUid().toString())) {
                            userPos = event.getEventMembers().get(key.toString()).toString();
                            isUserMember = true;
                        }
                    }
                    if(isUserMember==false)
                        userPos = "None";
                } else
                    userPos = "Admin";
                viewEvent(event.getKey(),userPos);
            }
        });

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String userPos = "";
                boolean isUserMember=false;
                if(!userPosition.equals("Admin")){
                    for(Object key: event.getEventMembers().keySet()){
                        if(key.toString().equals(mAuth.getCurrentUser().getUid().toString())) {
                            userPos = event.getEventMembers().get(key.toString()).toString();
                            isUserMember = true;
                        }
                    }
                    if(isUserMember==false)
                        userPos = "None";
                } else
                    userPos = "Admin";

                showDialog(event.getName(),event.getKey(),position,userPos);
                return false;
            }
        });

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
                                holder.date.setText(processDate(ds.getValue().toString()));
                                break;
                            case (3):
                                holder.endTime.setText(processTime(ds.getValue().toString()));
                                break;
                            case (4):
                                for(DataSnapshot ds2: ds.getChildren()){
                                    if(userPosition.equals("Admin")){
                                        holder.check.setVisibility(View.VISIBLE);
                                        holder.userPosition.setVisibility(View.VISIBLE);
                                        holder.userPosition.setText("Admin");
                                    }
                                    else if(ds2.getKey().equals(mAuth.getCurrentUser().getUid())) {
                                        holder.check.setVisibility(View.VISIBLE);
                                        holder.userPosition.setVisibility(View.VISIBLE);
                                        holder.userPosition.setText(ds2.getValue().toString());
                                    }
                                }
                                break;
                            case (7):
                                holder.eventName.setText(ds.getValue().toString());
                                break;
                            case (8):
                                holder.startTime.setText(processTime(ds.getValue().toString()));
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

    public void viewEvent(String eventKey,String userPos){
        MainActivity mainActivity = (MainActivity) context;
        EventMainFragment eventMainFragment = new EventMainFragment();
        FragmentTransaction ft = mainActivity.getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("eventKey",eventKey);
        bundle.putString("userPosition",userPos);
        eventMainFragment.setArguments(bundle);

        //ft.add(R.id.main_framelayout, eventsListFragment, "EventsListFragment");
        ft.setCustomAnimations(R.anim.enter_anim,R.anim.stay_anim,R.anim.stay_anim,R.anim.exit_anim);
        ft.replace(R.id.main_framelayout, eventMainFragment,"EventMainFragment");
        ft.addToBackStack("EventMainFragment");
        ft.commit();
    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder{
        private TextView eventName,date,startTime,endTime,userPosition,progress;
        private ImageView check;
        private SwipeRevealLayout swipeRevealLayout;
        private ConstraintLayout layout;
        public ListHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.vh_event_name);
            date = itemView.findViewById(R.id.vh_event_date);
            startTime = itemView.findViewById(R.id.vh_event_startTime);
            endTime = itemView.findViewById(R.id.vh_event_endTime);
            userPosition = itemView.findViewById(R.id.vh_event_userStatus);
            check = itemView.findViewById(R.id.vh_event_checkImage);
            swipeRevealLayout = itemView.findViewById(R.id.vh_events_list_swipe_reveal_layout);
            layout = itemView.findViewById(R.id.vh_e
                    vents_list_constraint);
        }
    }

    private void showDialog(String eventName, final String eventKey, final int position, String userPos){
        MainActivity mainActivity = (MainActivity) context;
        LayoutInflater inflater = mainActivity.getLayoutInflater();
        final View view2 = inflater.inflate(R.layout.dialog_events_options, null);
        final AlertDialog optionsDialog = new AlertDialog.Builder(context)
                .setView(view2)
                .create();

        TextView title = view2.findViewById(R.id.dgEventSettings_eventName);
        Button viewMembers = view2.findViewById(R.id.dgEventSettings_members);
        Button reports = view2.findViewById(R.id.dgEventSettings_report);
        Button delete = view2.findViewById(R.id.dgEventSettings_delete);
        Button edit = view2.findViewById(R.id.dgEventSettings_editEvent);
        ImageView membersLine = view2.findViewById(R.id.dgEventSettings_lineMembers);
        ImageView reportsLine  = view2.findViewById(R.id.dgEventSettings_lineReports);
        ImageView editLine  = view2.findViewById(R.id.dgEventSettings_lineEditName);
        title.setText(eventName);

        reports.setVisibility(View.GONE);
        reportsLine.setVisibility(View.GONE);
        if(userPos.equals("Member")){
            delete.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            membersLine.setVisibility(View.GONE);
            editLine.setVisibility(View.GONE);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEvent(eventKey,position,optionsDialog);
            }
        });

        viewMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new MembersFragment(),eventKey,String.valueOf(position));
                optionsDialog.dismiss();
            }
        });

        optionsDialog.show();
    }

    private void changeFragment(Fragment fragment, String key, String userPosition){
        MainActivity mainActivity = (MainActivity) context;
        Bundle bundle = new Bundle();
        bundle.putString("eventKey",key);
        bundle.putString("userPosition",userPosition);
        fragment.setArguments(bundle);

        FragmentTransaction ft = mainActivity.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_anim,R.anim.stay_anim,R.anim.stay_anim,R.anim.exit_anim);
        ft.replace(R.id.main_framelayout, fragment, key);
        ft.addToBackStack(key);
        ft.commit();
    }

    private void deleteEvent(final String eventKey, final int position, AlertDialog dg){
        dg.dismiss();

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eventDA.deleteEvent(eventKey);
                        eventArrayList.remove(position);
                        notifyItemRemoved(position);

                        //notifyItemRangeChanged(Integer.parseInt(position), groupList.size());


                    }
                }) //Set to null. We override the onclick
                .setNegativeButton("No", null)
                .create();
        dialog.show();
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
