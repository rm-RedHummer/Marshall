package com.jarvis.marshall.view.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.MainActivity;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.GroupDA;
import com.jarvis.marshall.model.Group;
import com.jarvis.marshall.view.home.eventsList.EventsListFragment;
import com.jarvis.marshall.view.home.groups.HomeFragment;

import java.util.ArrayList;

/**
 * Created by Jarvis on 03/01/2018.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ListHolder>{
    private Context context;
    private ArrayList<Group> groupList;
    private LayoutInflater inflater;
    private GroupDA groupDA;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    public HomeAdapter(Context context, ArrayList<Group> groupList, ProgressDialog progressDialog){
        this.context = context;
        this.groupList = groupList;
        inflater = LayoutInflater.from(context);
        groupDA = new GroupDA();
        this.progressDialog = progressDialog;
        viewBinderHelper.setOpenOnlyOne(true);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.viewholder_groups, parent, false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, int position) {
        final Group group = groupList.get(position);
        viewBinderHelper.bind(holder.swipeRevealLayout,group.getKey());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewEventsList(group.getKey());
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dg = new AlertDialog.Builder(context);
                dg.setMessage("Edit is clicked");
                dg.show();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dg = new AlertDialog.Builder(context);
                dg.setMessage("Delete is clicked");
                dg.show();
            }
        });
        groupDA.getGroup(group.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int ctr = 1;
                ArrayList<String> groupMembers;
                String groupName;
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if (ctr == 2) {
                        int numOfMembers = (int) ds.getChildrenCount();
                        holder.numOfMembers.setText(String.valueOf(numOfMembers) + " Joined");
                        for(DataSnapshot dataSnapshot1:ds.getChildren()){
                            if(dataSnapshot1.getKey().equals(mAuth.getCurrentUser().getUid())){
                                if(dataSnapshot1.getValue().equals("Member")){
                                    holder.check.setVisibility(View.INVISIBLE);
                                    holder.userPosition.setVisibility(View.INVISIBLE);
                                } else {
                                    holder.userPosition.setText("Admin");
                                }
                            }
                        }
                    }
                    else if (ctr == 3)
                        holder.groupName.setText(ds.getValue().toString());
                    if (ctr < 5)
                        ctr++;
                    else
                        ctr = 1;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final int  width = Resources.getSystem().getDisplayMetrics().widthPixels;
        holder.constraintLayout.post(new Runnable() {
            @Override
            public void run() {
                int height = holder.constraintLayout.getHeight();

                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(width,height);
                holder.constraintLayout.setLayoutParams(layoutParams);
                //holder.constraintLayout.setMinWidth(width);
            }
        });
        progressDialog.dismiss();
    }

    public void viewEventsList(String groupKey){
        MainActivity mainActivity = (MainActivity) context;
        EventsListFragment eventsListFragment = new EventsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("groupKey",groupKey);
        eventsListFragment.setArguments(bundle);

        FragmentTransaction ft = mainActivity.getSupportFragmentManager().beginTransaction();
        //ft.add(R.id.main_framelayout, eventsListFragment, "EventsListFragment");
        ft.setCustomAnimations(R.anim.enter_anim,R.anim.stay_anim,R.anim.stay_anim,R.anim.exit_anim);
        ft.replace(R.id.main_framelayout, eventsListFragment,groupKey);
        ft.addToBackStack(groupKey);
        ft.commit();

    }
    public void clearBackStack(){
        MainActivity mainActivity = (MainActivity) context;
        final FragmentManager fm = mainActivity.getSupportFragmentManager();
    }

    @Override
    public int getItemCount() { return groupList.size(); }

    public class ListHolder extends RecyclerView.ViewHolder {
        private TextView groupName, numOfMembers,userPosition;
        private SwipeRevealLayout swipeRevealLayout;
        private Button deleteButton,editButton;
        private ConstraintLayout constraintLayout;
        private ImageView check;
        public ListHolder(View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.vh_groups_title);
            numOfMembers = itemView.findViewById(R.id.vh_groups_members);
            swipeRevealLayout = itemView.findViewById(R.id.vh_group_swipe_reveal_layout);
            deleteButton = itemView.findViewById(R.id.vh_group_leave_btn);
            editButton = itemView.findViewById(R.id.vh_group_edit_btn);
            constraintLayout = itemView.findViewById(R.id.vh_group_constraint);
            check = itemView.findViewById(R.id.vh_group_checkImage);
            userPosition = itemView.findViewById(R.id.vh_group_userStatus);
        }
    }


}
