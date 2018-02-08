package com.jarvis.marshall.view.home.createEvent;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
public class SelectMembersFragment extends Fragment implements View.OnClickListener {
    private View view;
    private String groupKey, eventLeaderKey;
    private GroupDA groupDA;
    private UserDA userDA;
    private RecyclerView recyclerView;
    private Button backBtn,confirmBtn;
    private SelectEventMemberAdapter adapter;
    private FragmentManager fm;

    public SelectMembersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_select_members, container, false);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            groupKey = bundle.getString("groupKey");
            eventLeaderKey = bundle.getString("eventLeaderPosition");
        }
        groupDA = new GroupDA();
        userDA = new UserDA();
        fm = getActivity().getSupportFragmentManager();

        recyclerView = view.findViewById(R.id.fragSelectMember_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        loadSelectEventMemberRecyclerView();

        confirmBtn = view.findViewById(R.id.fragSelectMember_confirmBtn);
        confirmBtn.setOnClickListener(this);
        backBtn = view.findViewById(R.id.fragSelectMember_backBtn);
        backBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case(R.id.fragSelectMember_confirmBtn):

                break;
            case(R.id.fragSelectMember_backBtn):
                fm.popBackStackImmediate();
                break;
        }
    }

    private void loadSelectEventMemberRecyclerView(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dialog);
        progressDialog.show();
        progressDialog.setMessage("Loading members..");

        final ArrayList<String> stringArrayList = new ArrayList<>();
        adapter = new SelectEventMemberAdapter(getContext(),stringArrayList,progressDialog);
        recyclerView.setAdapter(adapter);
        groupDA.getGroupMembers(groupKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if(!ds.getKey().toString().equals(eventLeaderKey)) {
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
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
