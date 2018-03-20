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
import android.support.v4.app.Fragment;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.MainActivity;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.GroupDA;
import com.jarvis.marshall.dataAccess.UserDA;
import com.jarvis.marshall.model.Group;
import com.jarvis.marshall.view.home.eventsList.EventsListFragment;
import com.jarvis.marshall.view.home.groups.HomeFragment;
import com.jarvis.marshall.view.home.members.MembersFragment;

import java.util.ArrayList;

/**
 * Created by Jarvis on 03/01/2018.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ListHolder>{
    private Context context;
    private ArrayList<Group> groupList;
    private String groupKey,groupPosition;
    private LayoutInflater inflater;
    private GroupDA groupDA;
    private UserDA userDA;
    private ProgressDialog progressDialog;
    private AlertDialog optionsDialog;
    private FirebaseAuth mAuth;
    private boolean isDeleted;

    public HomeAdapter(Context context, ArrayList<Group> groupList, ProgressDialog progressDialog){
        this.context = context;
        this.groupList = groupList;
        inflater = LayoutInflater.from(context);
        groupDA = new GroupDA();
        userDA = new UserDA();
        this.progressDialog = progressDialog;

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

        final ArrayList<String> groupMembers = group.getGroupMembers();
        final ArrayList<String> userPos = new ArrayList<>();

        for(int i = 0; i < groupMembers.size(); i++){
            String[] split = groupMembers.get(i).split(":");
            if(split[0].equals(mAuth.getCurrentUser().getUid()))
                userPos.add(split[1]);
        }



        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new EventsListFragment(),group.getKey(),userPos.get(0));
                //viewEventsList(group.getKey());
            }
        });

        holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog(group.getGroupName(),userPos);
                groupKey = group.getKey();
                groupPosition = String.valueOf(position);
                return true;
            }
        });


        /*groupDA.getGroup(group.getKey()).addChildEventListener(new ChildEventListener() {
            int ctr = 1;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(ctr == 1)
                    holder.groupCode.setText(dataSnapshot.getValue().toString());
                else if (ctr == 2) {
                    int numOfMembers = (int) dataSnapshot.getChildrenCount();
                    holder.numOfMembers.setText(String.valueOf(numOfMembers) + " Joined");
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        if(dataSnapshot1.getKey().equals(mAuth.getCurrentUser().getUid())){

                            if(dataSnapshot1.getValue().equals("Member")){
                                holder.check.setVisibility(View.INVISIBLE);
                                holder.userPositionTextView.setVisibility(View.INVISIBLE);
                                holder.userPositionTextView.setText("Member");
                            } else {
                                holder.userPositionTextView.setText("Admin");

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
        });*/
        groupDA.getGroup(group.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {
                int ctr = 1;
                ArrayList<String> groupMembers;
                String groupName;
                for(DataSnapshot dataSnapshot: ds.getChildren()){
                    if(ctr == 1)
                        holder.groupCode.setText(dataSnapshot.getValue().toString());
                    else if (ctr == 2) {
                        int numOfMembers = (int) dataSnapshot.getChildrenCount();
                        holder.numOfMembers.setText(String.valueOf(numOfMembers) + " Joined");
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            if(dataSnapshot1.getKey().equals(mAuth.getCurrentUser().getUid())){

                                if(dataSnapshot1.getValue().equals("Member")){
                                    holder.check.setVisibility(View.INVISIBLE);
                                    holder.userPositionTextView.setVisibility(View.INVISIBLE);
                                    holder.userPositionTextView.setText("Member");
                                } else {
                                    holder.userPositionTextView.setText("Admin");

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

    @Override
    public int getItemCount() { return groupList.size(); }

    public boolean getIsDeleted(){ return isDeleted; };

    public void setIsDeleted(){ this.isDeleted = false; }

    public class ListHolder extends RecyclerView.ViewHolder {
        private TextView groupName, numOfMembers,userPositionTextView,groupCode;
        private ConstraintLayout constraintLayout;
        private ImageView check;
        public ListHolder(View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.vh_groups_title);
            numOfMembers = itemView.findViewById(R.id.vh_groups_members);
            constraintLayout = itemView.findViewById(R.id.vh_group_constraint);
            check = itemView.findViewById(R.id.vh_group_checkImage);
            userPositionTextView = itemView.findViewById(R.id.vh_group_userStatus);
            groupCode  = itemView.findViewById(R.id.vh_group_groupCode);
        }
    }

    public void showDialog(String name,ArrayList<String> userPos){
        final MainActivity mainActivity = (MainActivity) context;
        LayoutInflater inflater = mainActivity.getLayoutInflater();
        final View view2 = inflater.inflate(R.layout.dialog_group_options, null);
        optionsDialog = new AlertDialog.Builder(context)
                .setView(view2)
                .create();
        Button viewMembers, delete, editName, viewReports;
        ImageView lineMembers, lineReports, lineEditName;
        TextView groupName;
        viewMembers = view2.findViewById(R.id.dgGroupSettings_members);
        delete = view2.findViewById(R.id.dgGroupSettings_delete);
        editName  = view2.findViewById(R.id.dgGroupSettings_editName);
        viewReports = view2.findViewById(R.id.dgGroupSettings_report);
        groupName = view2.findViewById(R.id.dgGroupSettings_groupName);
        lineEditName = view2.findViewById(R.id.dgGroupSettings_lineEditName);
        lineReports = view2.findViewById(R.id.dgGroupSettings_lineReports);
        lineMembers = view2.findViewById(R.id.dgGroupSettings_lineMembers);

        viewReports.setVisibility(View.GONE);
        lineReports.setVisibility(View.GONE);
        if(!userPos.get(0).equals("Admin") ){ // F T
            delete.setVisibility(View.GONE);
            editName.setVisibility(View.GONE);

            lineEditName.setVisibility(View.GONE);
            lineMembers.setVisibility(View.GONE);
        }

        viewMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new MembersFragment(), groupKey, "0");
                optionsDialog.dismiss();
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
                editGroupName(mainActivity);
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

    private void changeFragment(Fragment fragment, String key,String userPosition){
        MainActivity mainActivity = (MainActivity) context;
        Bundle bundle = new Bundle();
        bundle.putString("groupKey",key);
        bundle.putString("userPosition",userPosition);
        fragment.setArguments(bundle);

        FragmentTransaction ft = mainActivity.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_anim,R.anim.stay_anim,R.anim.stay_anim,R.anim.exit_anim);
        ft.replace(R.id.main_framelayout, fragment, key);
        ft.addToBackStack(key);
        ft.commit();
    }

    private void deleteGroup(){
        final String key = groupKey, position = groupPosition;
        optionsDialog.dismiss();

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage("Are you sure you want to delete this group?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isDeleted = true;
                        groupDA.deleteGroup(key);
                        userDA.deleteGroup(mAuth.getCurrentUser().getUid(),key);
                        groupList.remove(Integer.parseInt(position));
                        notifyItemRemoved(Integer.parseInt(position));

                        //notifyItemRangeChanged(Integer.parseInt(position), groupList.size());


                    }
                }) //Set to null. We override the onclick
                .setNegativeButton("No", null)
                .create();
        dialog.show();
    }


    public void clearBackStack(){
        MainActivity mainActivity = (MainActivity) context;
        final FragmentManager fm = mainActivity.getSupportFragmentManager();
    }

    public void editGroupName(final Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        final View view2 = inflater.inflate(R.layout.dialog_add_group,null);
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view2)
                .setTitle("Enter new name of the group.")
                .setPositiveButton("Confirm", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final EditText groupName = view2.findViewById(R.id.editText_group_name);
                        if(groupName.getText().toString().isEmpty()){
                            Snackbar.make(view, "Please enter group name.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else {
                            groupDA.checkGroupNames(groupName.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() != null) {
                                        Snackbar.make(view, "Group name is already taken.", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    } else {
                                        groupDA.changeGroupName(groupKey,groupName.getText().toString());
                                        Snackbar.make(view, "The name of the group is successfully changed.", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();

                                        optionsDialog.dismiss();
                                        dialog.dismiss();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                });
            }
        });
        dialog.show();
    }



}
