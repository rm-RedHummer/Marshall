package com.jarvis.marshall.view.home.groups;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.EditText;

import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
import com.jarvis.marshall.view.home.eventsList.EventsListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
    private FirebaseAuth mAuth;
    private View view;
    private GroupDA groupDA;
    private UserDA userDA;
    private RecyclerView recyclerView;

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
        ButterKnife.bind(getActivity());

        FragmentManager fm = getActivity().getSupportFragmentManager();
        String tag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();



        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Groups");

        recyclerView = view.findViewById(R.id.recyclerview_home);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        loadRecyclerView();

        FloatingActionMenu floatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.home_fab_action_menu);
        FloatingActionButton fabCreateGroup = (FloatingActionButton) view.findViewById(R.id.home_fab_create_group);
        FloatingActionButton fabJoinGroup = (FloatingActionButton) view.findViewById(R.id.home_fab_join_group);

        fabJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View mainView) {
                joinGroup(mainView);
            }
        });

        fabCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View mainView) {
                createGroup(mainView);
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

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dialog);
        progressDialog.show();
        progressDialog.setMessage("Loading groups..");

        final ArrayList<Group> groupArrayList = new ArrayList<>();
        final HomeAdapter adapter = new HomeAdapter(getContext(),groupArrayList,progressDialog);
        recyclerView.setAdapter(adapter);

        userDA.getGroups(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    int numOfChildren = (int) dataSnapshot.getChildrenCount();
                    int num=1;
                    if(groupArrayList.size()==0){
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            loadToRecyclerView(ds,adapter,groupArrayList);
                        }
                    } else {
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            if(num==numOfChildren)
                                loadToRecyclerView(ds,adapter,groupArrayList);
                            num++;
                        }
                    }
                } else {
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadToRecyclerView(DataSnapshot ds, final HomeAdapter adapter, final ArrayList<Group>
            groupArrayList){
        String userPosition=null;
        groupDA.getGroup(ds.getKey().toString()).addChildEventListener(new ChildEventListener() {
            int num = 1;
            String key,groupName = null,groupCode=null;
            ArrayList<String> groupMembers;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (num == 1)
                    groupCode = dataSnapshot.getValue().toString();
                else if (num == 2) {
                    groupMembers = new ArrayList<String>();
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        groupMembers.add(ds.getKey().toString());
                    }
                }
                else if (num == 3)
                    groupName = dataSnapshot.getValue().toString();
                else if (num == 4) {
                    key = dataSnapshot.getValue().toString();
                    Group group = new Group(groupName, key, groupCode);
                    group.setGroupMembers(groupMembers);
                    groupArrayList.add(group);

                    if(adapter.getItemCount()>=groupArrayList.size())
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
        /*groupDA.getGroup(ds.getKey().toString()).addValueEventListener(new ValueEventListener() {
            int num = 1;
            String key,groupName = null,groupCode=null;
            ArrayList<String> groupMembers;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (num == 1)
                    groupCode = dataSnapshot.getValue().toString();
                else if (num == 2) {
                    groupMembers = new ArrayList<String>();
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        groupMembers.add(ds.getKey().toString());
                    }
                }
                else if (num == 3)
                    groupName = dataSnapshot.getValue().toString();
                else if (num == 4) {
                    key = dataSnapshot.getValue().toString();
                    Group group = new Group(groupName, key, groupCode);
                    group.setGroupMembers(groupMembers);
                    groupArrayList.add(group);
                    adapter.notifyItemInserted(groupArrayList.size() - 1);

                }
                if (num < 5)
                    num++;
                else
                    num = 1;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    public void joinGroup(final View mainView){
        LayoutInflater inflater = getLayoutInflater();
        final View view2 = inflater.inflate(R.layout.dialog_join_group, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view2)
                .setTitle("Enter the group code")
                .setPositiveButton("Confirm", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final EditText groupCodeEditText = view2.findViewById(R.id.dg_et_group_code);
                        final String groupCode = groupCodeEditText.getText().toString();
                        groupDA.checkGroupCode(groupCode).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue()!=null){
                                    String key = "";
                                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                                        key = ds.child("key").getValue().toString();
                                    }
                                    userDA.addGroup(mAuth.getCurrentUser().getUid(),key);
                                    groupDA.addUserToGroup(mAuth.getCurrentUser().getUid(),key,"Member");
                                    dialog.dismiss();
                                    Snackbar.make(mainView, "Group successfully joined", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                } else {
                                    Snackbar.make(view, "Group code does not exist.", Snackbar.LENGTH_LONG)
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

    public void createGroup(final View mainView){
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

                                    Snackbar.make(view, "Group name is already taken.", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                } else {
                                    dialog.dismiss();
                                    final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().getRoot().child("group");
                                    final String key = rootRef.push().getKey();
                                    final String groupCode = key.substring(key.length()-7,key.length()-1);
                                    final Group group = new Group(groupName.getText().toString(), key, groupCode);
                                    groupDA.createNewGroup(group);
                                    userDA.addGroup(mAuth.getCurrentUser().getUid(),key);
                                    groupDA.addUserToGroup(mAuth.getCurrentUser().getUid(),key,"Admin");
                                    Snackbar.make(mainView, "The group is successfully created.", Snackbar.LENGTH_LONG)
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
}
