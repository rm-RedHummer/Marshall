package com.jarvis.marshall.view.home.createEvent;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.GroupDA;
import com.jarvis.marshall.dataAccess.UserDA;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectEventLeaderFragment extends Fragment implements View.OnClickListener{
    private View view;
    private String groupKey;
    private RecyclerView recyclerView;
    private GroupDA groupDA;
    private UserDA userDA;
    private Button backBtn,saveBtn;
    private FragmentManager fm;
    private SelectEventLeaderAdapter adapter;
    private ArrayList<String> userKeyList;

    public SelectEventLeaderFragment(){
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_event_leader, container, false);
        Bundle bundle = getArguments();
        if(bundle!=null)
            groupKey = bundle.getString("groupKey");
        groupDA = new GroupDA();
        userDA = new UserDA();
        fm = getActivity().getSupportFragmentManager();

        recyclerView = view.findViewById(R.id.fragSelectEventLeader_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        loadSelectEventLeaderRecyclerView();

        saveBtn = view.findViewById(R.id.fragSelectEventLeader_confirmBtn);
        saveBtn.setOnClickListener(this);
        backBtn = view.findViewById(R.id.fragSelectEventLeader_backBtn);
        backBtn.setOnClickListener(this);


        return view;
    }

    public void loadSelectEventLeaderRecyclerView(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dialog);
        progressDialog.show();
        progressDialog.setMessage("Loading members..");

        userKeyList = new ArrayList<>();
        final ArrayList<String> stringArrayList = new ArrayList<>();
        adapter = new SelectEventLeaderAdapter(getContext(),stringArrayList,progressDialog);
        recyclerView.setAdapter(adapter);
        groupDA.getGroupMembers(groupKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        userKeyList.add(ds.getKey().toString());
                        userDA.getUserName(ds.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot1) {
                                stringArrayList.add(dataSnapshot1.getValue().toString());
                                adapter.notifyItemInserted(stringArrayList.size() - 1);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case(R.id.fragSelectEventLeader_confirmBtn):
                SelectMembersFragment fragment = new SelectMembersFragment();
                FragmentTransaction ft = fm.beginTransaction();
                Bundle bundle = new Bundle();
                String tag = "SelectEventMembers";
                bundle.putString("groupKey",groupKey);
                bundle.putString("eventLeaderPosition",userKeyList.get(adapter.getCheckedButton()));

                fragment.setArguments(bundle);
                //ft.add(R.id.actCreateEvent_frameLayout, fragment, tag);
                ft.replace(R.id.actCreateEvent_frameLayout,fragment,tag);
                ft.setCustomAnimations(R.anim.enter_anim,R.anim.stay_anim,R.anim.stay_anim,R.anim.exit_anim);
                ft.addToBackStack(tag);
                ft.commit();
                break;
            case(R.id.fragSelectEventLeader_backBtn):

                break;
        }
    }
}
