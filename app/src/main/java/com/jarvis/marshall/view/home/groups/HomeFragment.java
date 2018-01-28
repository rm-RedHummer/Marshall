package com.jarvis.marshall.view.home.groups;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jarvis.marshall.MainActivity;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.GroupDA;
import com.jarvis.marshall.dataAccess.UserDA;
import com.jarvis.marshall.model.Group;
import com.jarvis.marshall.view.home.HomeAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private View view;
    private GroupDA groupDA;
    private UserDA userDA;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();
        groupDA = new GroupDA();
        userDA = new UserDA();

        recyclerView = view.findViewById(R.id.recyclerview_home);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        loadRecyclerView();

        FloatingActionButton fab = view.findViewById(R.id.home_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LayoutInflater inflater = getLayoutInflater();
                final View view2 = inflater.inflate(R.layout.dialog_add_group,null);
                final AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setView(view2)
                        .setTitle("Enter the name of the group")
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
                                groupDA.checkGroupNames(groupName.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null) {
                                            Snackbar.make(view, "The group name is already taken.", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                        } else {
                                            dialog.dismiss();
                                            ArrayList<String> groupMembers = new ArrayList<>();
                                            groupMembers.add(mAuth.getCurrentUser().getUid());
                                            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().getRoot().child("group");
                                            final String key = rootRef.push().getKey();
                                            final String groupCode = key.substring(key.length()-7,key.length()-1);
                                            final Group group = new Group(groupName.getText().toString(),groupMembers);
                                            group.setKey(key);
                                            group.setGroupCode(groupCode);
                                            /*groupDA.checkGroupCode(groupCode).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.getValue() != null) {
                                                        //groupDA.deleteGroup(key);
                                                        //String newKey = rootRef.push().getKey();
                                                        //group.setKey(newKey);

                                                    } else {
                                                        group.setGroupName("Yah yah");
                                                        //group.setKey(key);
                                                        //group.setGroupCode(groupCode);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });*/
                                            groupDA.createNewGroup(group);


                                            userDA.getGroupList(mAuth.getCurrentUser().getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.getValue()!=null) {
                                                        ArrayList<String> groupList = (ArrayList<String>) dataSnapshot.getValue();
                                                        groupList.add(key);
                                                        userDA.setGroupList(mAuth.getCurrentUser().getUid().toString(),groupList);

                                                    } else {
                                                        ArrayList<String> groupList = new ArrayList<String>();
                                                        groupList.add(key);
                                                        userDA.setGroupList(mAuth.getCurrentUser().getUid().toString(),groupList);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                            Snackbar.make(view, "The group is successfully created.", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();




                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                    }
                });
                dialog.show();
            }
        });

        return view;
    }

    public void loadRecyclerView() {
        ArrayList<Group> groupArrayList = new ArrayList<>();

        ArrayList<String> tempList = new ArrayList<>();
        for (int ctr = 0 ; ctr < 10; ctr++) {
            Group group = new Group("yeah",tempList);
            groupArrayList.add(group);
        }
        HomeAdapter homeAdapter = new HomeAdapter(getContext(),groupArrayList);
        recyclerView.setAdapter(homeAdapter);
    }

}
