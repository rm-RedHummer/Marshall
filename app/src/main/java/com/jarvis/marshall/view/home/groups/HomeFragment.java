package com.jarvis.marshall.view.home.groups;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jarvis.marshall.LoginActivity;
import com.jarvis.marshall.MainActivity;
import com.jarvis.marshall.R;
import com.jarvis.marshall.Util.RecyclerItemClickListener;
import com.jarvis.marshall.dataAccess.DA;
import com.jarvis.marshall.dataAccess.GroupDA;
import com.jarvis.marshall.dataAccess.UserDA;
import com.jarvis.marshall.model.Group;
import com.jarvis.marshall.view.home.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

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
        /*ArrayList<Group> groupArrayList = new ArrayList<>();

        ArrayList<String> tempList = new ArrayList<>();
        for (int ctr = 0 ; ctr < 10; ctr++) {
            Group group = new Group("yeah",tempList);
            groupArrayList.add(group);
        }
        HomeAdapter homeAdapter = new HomeAdapter(getContext(),groupArrayList);
        recyclerView.setAdapter(homeAdapter);

        AlertDialog.Builder dg = new AlertDialog.Builder(getContext());
                dg.setMessage(group.getGroupName());
                dg.show();


        */
        //final ArrayList<String>[] groupList = new ArrayList[]{new ArrayList<String>()};
        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        final ArrayList<Group> groupArrayList = new ArrayList<>();
        final HomeAdapter adapter = new HomeAdapter(getContext(),groupArrayList,progressDialog);
        recyclerView.setAdapter(adapter);


        /*groupDA.getGroup("-L42zg_rezrn-RvkyREk").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    AlertDialog.Builder dg = new AlertDialog.Builder(getContext());
                    dg.setMessage("Wala daw laman");
                    dg.show();
                } else {
                    AlertDialog.Builder dg = new AlertDialog.Builder(getContext());
                    dg.setMessage(dataSnapshot.getValue().toString());
                    dg.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/



        userDA.getGroupList(mAuth.getCurrentUser().getUid().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> groupList = (ArrayList<String>) dataSnapshot.getValue();
                for (int ctr = 0 ; ctr < groupList.size() ; ctr++){

                    groupDA.getGroup(groupList.get(ctr)).addChildEventListener(new ChildEventListener() {
                        int num = 1;
                        String key,groupName = null,groupCode=null;
                        ArrayList<String> groupMembers = new ArrayList<>();
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (num == 1)
                                groupCode = dataSnapshot.getValue().toString();
                            else if (num == 2)
                                groupMembers = (ArrayList<String>) dataSnapshot.getValue();
                            else if (num == 3)
                                groupName = dataSnapshot.getValue().toString();
                            else if (num == 4) {
                                key = dataSnapshot.getValue().toString();
                                Group group = new Group(groupName, groupMembers);
                                group.setKey(key);
                                group.setGroupCode(groupCode);
                                groupArrayList.add(group);
                                adapter.notifyItemInserted(groupArrayList.size() - 1);
                            }
                            if (num < 5)
                                num++;
                            else
                                num = 1;
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
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*groupDA.getAllGroups().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int ctr = 1;
                String key,groupName = null,groupCode=null;
                ArrayList<String> groupMembers = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ctr == 1)
                        groupCode = ds.getValue().toString();
                    else if (ctr == 2)
                        groupMembers = (ArrayList<String>) ds.getValue();
                    else if (ctr == 3)
                        groupName = ds.getValue().toString();
                    else if (ctr == 4) {
                        key = ds.getValue().toString();
                        Group group = new Group(groupName, groupMembers);
                        group.setKey(key);
                        group.setGroupCode(groupCode);
                        groupArrayList.add(group);
                        adapter.notifyItemInserted(groupArrayList.size() - 1);
                    }
                    if (ctr < 5)
                        ctr++;
                    else
                        ctr = 1;
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
        });*/

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AlertDialog.Builder dg = new AlertDialog.Builder(getContext());
                dg.setMessage(groupArrayList.get(position).getGroupName());
                dg.show();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        /*groupDA.getAllGroups().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    AlertDialog.Builder dg = new AlertDialog.Builder(getContext());
                    dg.setMessage("Wala daw laman");
                    dg.show();
                } else {
                    Long val = dataSnapshot.getChildrenCount();
                    AlertDialog.Builder dg = new AlertDialog.Builder(getContext());
                    dg.setMessage(String.valueOf(val));
                    dg.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


        /*groupDA.getGroupList(mAuth.getCurrentUser().getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    AlertDialog.Builder dg = new AlertDialog.Builder(getContext());
                    dg.setMessage("May laman");
                    dg.show();
                } else {
                    AlertDialog.Builder dg = new AlertDialog.Builder(getContext());
                    dg.setMessage("Walang laman huhubels");
                    dg.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        /*groupDA.getGroup("-L3xathh_XdzXzO0ly6r").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    //Group group = dataSnapshot.getValue(Group.class);
                    Long val = dataSnapshot.getChildrenCount();
                    AlertDialog.Builder dg = new AlertDialog.Builder(getContext());
                    dg.setMessage(String.valueOf(val));
                    dg.show();
                } else {
                    AlertDialog.Builder dg = new AlertDialog.Builder(getContext());
                    dg.setMessage("Walang laman");
                    dg.show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); */

        /*userDA.getGroupList(mAuth.getCurrentUser().getUid().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    groupList[0] = (ArrayList<String>) dataSnapshot.getValue();
                    AlertDialog.Builder dg = new AlertDialog.Builder(getContext());
                    dg.setMessage("Yeah");
                    dg.show();
                    for(int ctr = 0 ; ctr < groupList[0].size(); ctr++) {
                        groupDA.getGroup(groupList[0].get(ctr)).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if(dataSnapshot.getValue()!=null){
                                    Group group = dataSnapshot.getValue(Group.class);
                                    groupArrayList.add(group);
                                    adapter.notifyItemInserted(groupArrayList.size()-1);

                                    AlertDialog.Builder dg2 = new AlertDialog.Builder(getContext());
                                    dg2.setMessage(group.getGroupName());
                                    dg2.show();

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
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
}
