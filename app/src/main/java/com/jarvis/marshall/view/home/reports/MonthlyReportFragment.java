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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.EventDA;
import com.jarvis.marshall.dataAccess.GroupDA;
import com.jarvis.marshall.dataAccess.TaskDA;
import com.jarvis.marshall.dataAccess.UserDA;
import com.jarvis.marshall.model.Group;
import com.jarvis.marshall.model.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyReportFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;

    public MonthlyReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_monthly_report, container, false);
        recyclerView = view.findViewById(R.id.fragMonthly_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        loadMonthlyReportRecyclerView();
        return view;
    }

    private void loadMonthlyReportRecyclerView(){
        final ArrayList<String> groupKeyList = new ArrayList<>();
        final MonthlyReportAdapter adapter = new MonthlyReportAdapter(getContext(), groupKeyList);
        recyclerView.setAdapter(adapter);

        UserDA userDA = new UserDA();
        final GroupDA groupDA = new GroupDA();
        final EventDA eventDA = new EventDA();
        final TaskDA taskDA = new TaskDA();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userDA.getGroups(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dsUserGroup) {
                if (dsUserGroup.getValue() != null) {
                    for (DataSnapshot dsUserGroupKey : dsUserGroup.getChildren()) {
                        groupDA.getGroup(dsUserGroupKey.getKey().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dsGroupKey) {
                                for(DataSnapshot ds: dsGroupKey.getChildren()){
                                    if(ds.getKey().equals("groupName")) {
                                        groupKeyList.add(dsGroupKey.getKey()+":"+ds.getValue().toString());
                                        adapter.notifyItemInserted(groupKeyList.size()-1);
                                    }
                                }
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
        });

    }
}
