package com.jarvis.marshall.view.home.reports;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
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
public class GroupReportFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;

    public GroupReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_group_report, container, false);
        recyclerView = view.findViewById(R.id.fragGroupReport_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        loadRecyclerView();

        return view;
    }

    public void loadRecyclerView(){
        final ArrayList<String> keyList = new ArrayList<>();
        final GroupReportAdapter adapter = new GroupReportAdapter(getContext(), keyList);
        recyclerView.setAdapter(adapter);

        UserDA userDA = new UserDA();
        final GroupDA groupDA = new GroupDA();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userDA.getGroups(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    groupDA.getGroup(ds.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot ds1) {
                            for(DataSnapshot ds2: ds1.getChildren()){
                                if(ds2.getKey().equals("groupName")){
                                    keyList.add(ds1.getKey()+":"+ds2.getValue());
                                    adapter.notifyItemInserted(keyList.size()-1);

                                }
                            }
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
