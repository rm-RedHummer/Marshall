package com.jarvis.marshall.view.home.createTask;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.EventDA;
import com.jarvis.marshall.dataAccess.UserDA;
import com.jarvis.marshall.view.home.createEvent.SelectEventMemberAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskSelectMembersFragment extends Fragment implements View.OnClickListener{
    private View view;
    private String eventKey;
    private EventDA eventDA;
    private UserDA userDA;
    private RecyclerView recyclerView;
    private Button confirmBtn, backBtn;
    private TaskMembersAdapter adapter;
    private ArrayList<String> userKeyArrayList,stringArrayList;
    private FragmentManager fm;

    public TaskSelectMembersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task_select_members, container, false);
        eventKey = getActivity().getIntent().getExtras().getString("eventKey");
        eventDA = new EventDA();
        userDA = new UserDA();
        userKeyArrayList = new ArrayList<>();
        fm = getActivity().getSupportFragmentManager();



        recyclerView = view.findViewById(R.id.fragTaskSelectMember_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        loadSelectTaskMemberRecyclerView();

        confirmBtn = view.findViewById(R.id.fragTaskSelectMember_confirmBtn);
        confirmBtn.setOnClickListener(this);
        backBtn = view.findViewById(R.id.fragTaskSelectMember_backBtn);
        backBtn.setOnClickListener(this);

        return view;
    }

    private void loadSelectTaskMemberRecyclerView(){
        stringArrayList = new ArrayList<>();
        adapter = new TaskMembersAdapter(getContext(),stringArrayList);
        recyclerView.setAdapter(adapter);
        eventDA.getEventMembers(eventKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                        userKeyArrayList.add(ds.getKey());
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
        switch(view.getId()){
            case(R.id.fragTaskSelectMember_confirmBtn):
                ArrayList<String> membersList = adapter.getChecked();
                ArrayList<String> memberKeyList = new ArrayList<>();
                ArrayList<String> memberNamesList = new ArrayList<>();

                for(int ctr = 0; ctr< membersList.size(); ctr++){
                    memberKeyList.add(userKeyArrayList.get(Integer.parseInt(membersList.get(ctr))));
                    memberNamesList.add(stringArrayList.get(Integer.parseInt(membersList.get(ctr))));
                }

                getActivity().getIntent().putExtra("taskMembersArrayList",memberKeyList);
                getActivity().getIntent().putExtra("taskMembersNamesArrayList",memberNamesList);
                fm.popBackStackImmediate();
                break;
            case(R.id.fragTaskSelectMember_backBtn):
                fm.popBackStackImmediate();
                break;
        }
    }
}
