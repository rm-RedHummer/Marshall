package com.jarvis.marshall.view.home.createEvent;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class SelectEventLeaderFragment extends Fragment {
    private View view;
    private String groupKey;
    private RecyclerView recyclerView;
    private GroupDA groupDA;
    private UserDA userDA;

    public SelectEventLeaderFragment() {
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

        recyclerView = view.findViewById(R.id.fragSelectEventLeader_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        loadSelectEventLeaderRecyclerView();

        return view;
    }

    public void loadSelectEventLeaderRecyclerView(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dialog);
        progressDialog.show();
        progressDialog.setMessage("Loading members..");

        final ArrayList<String> stringArrayList = new ArrayList<>();
        final SelectEventLeaderAdapter adapter = new SelectEventLeaderAdapter(getContext(),stringArrayList,progressDialog);
        recyclerView.setAdapter(adapter);
        groupDA.getGroupMembers(groupKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
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

}
