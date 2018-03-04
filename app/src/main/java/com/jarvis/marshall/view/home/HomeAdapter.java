package com.jarvis.marshall.view.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.MainActivity;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.GroupDA;
import com.jarvis.marshall.dataAccess.UserDA;
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
    private String userPosition,groupKey,groupPosition;
    private LayoutInflater inflater;
    private GroupDA groupDA;
    private UserDA userDA;
    private ProgressDialog progressDialog;
    private AlertDialog optionsDialog;
    private FirebaseAuth mAuth;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    public HomeAdapter(Context context, ArrayList<Group> groupList, ProgressDialog progressDialog){
        this.context = context;
        this.groupList = groupList;
        inflater = LayoutInflater.from(context);
        groupDA = new GroupDA();
        userDA = new UserDA();
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
    public void onBindViewHolder(final ListHolder holder, final int position) {
        final Group group = groupList.get(position);
        viewBinderHelper.bind(holder.swipeRevealLayout,group.getKey());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewEventsList(group.getKey());
            }
        });

        holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog(group.getGroupName());
                groupKey = group.getKey();
                groupPosition = String.valueOf(position);
                return true;
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
        groupDA.getGroup(group.getKey()).addChildEventListener(new ChildEventListener() {
            int ctr = 1;
            ArrayList<String> groupMembers;
            String groupName;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (ctr == 2) {
                    int numOfMembers = (int) dataSnapshot.getChildrenCount();
                    holder.numOfMembers.setText(String.valueOf(numOfMembers) + " Joined");
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        if(dataSnapshot1.getKey().equals(mAuth.getCurrentUser().getUid())){
                            if(dataSnapshot1.getValue().equals("Member")){
                                holder.check.setVisibility(View.INVISIBLE);
                                holder.userPosition.setVisibility(View.INVISIBLE);
                                holder.userPosition.setText("Member");
                            } else {
                                holder.userPosition.setText("Admin");
                            }
                        }
                    }
                }
                else if (ctr == 3)
                    holder.groupName.setText(dataSnapshot.getValue().toString());
                if (ctr < 5)
                    ctr++;
                else
                    ctr = 1;
                /*int ctr = 1;
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
                                    holder.userPosition.setText("Member");
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
                }*/
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
        /*groupDA.getGroup(group.getKey()).addValueEventListener(new ValueEventListener() {
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
                                    holder.userPosition.setText("Member");
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
        });*/
        userPosition = holder.userPosition.getText().toString();
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

    public void showDialog(String name){
        MainActivity mainActivity = (MainActivity) context;
        LayoutInflater inflater = mainActivity.getLayoutInflater();
        final View view2 = inflater.inflate(R.layout.dialog_group_options, null);
        optionsDialog = new AlertDialog.Builder(context)
                .setView(view2)
                .create();
        Button viewMembers, delete, editName, viewReports;
        TextView groupName;
        viewMembers = view2.findViewById(R.id.dgGroupSettings_members);
        delete = view2.findViewById(R.id.dgGroupSettings_delete);
        editName  = view2.findViewById(R.id.dgGroupSettings_editName);
        viewReports = view2.findViewById(R.id.dgGroupSettings_report);
        groupName = view2.findViewById(R.id.dgGroupSettings_groupName);

        viewMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteGroup();
            }
        });
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        groupName.setText(name);

        optionsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                groupKey = "";
                groupPosition = "";
            }
        });

        optionsDialog.show();
    }

    private void deleteGroup(){
        final String key = groupKey, position = groupPosition;
        optionsDialog.dismiss();
        AlertDialog.Builder dg = new AlertDialog.Builder(context);
        dg.setMessage(key);
        dg.show();
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage("Are you sure you want to delete this group?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        groupDA.deleteGroup(key);
                        userDA.deleteGroup(mAuth.getCurrentUser().getUid(),key);
                        groupList.remove(Integer.parseInt(position));
                        notifyItemRemoved(Integer.parseInt(position));
                        notifyItemRangeChanged(Integer.parseInt(position), groupList.size());


                    }
                }) //Set to null. We override the onclick
                .setNegativeButton("No", null)
                .create();
        dialog.show();
    }

    public void viewEventsList(String groupKey){
        MainActivity mainActivity = (MainActivity) context;
        EventsListFragment eventsListFragment = new EventsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("groupKey",groupKey);
        bundle.putString("userPosition",userPosition);
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




}
