package com.jarvis.marshall.view.home.members;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.EventDA;
import com.jarvis.marshall.dataAccess.GroupDA;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembersFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private GroupDA groupDA;
    private String groupKey="",eventKey;

    public MembersFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_members, container, false);
        Bundle bundle = getArguments();
        if(bundle!=null){
            if(bundle.containsKey("groupKey"))
                groupKey = bundle.getString("groupKey");
            else
                eventKey = bundle.getString("eventKey");
        }

        recyclerView = view.findViewById(R.id.fragMembers_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Members");
        if(!groupKey.equals(""))
            loadGroupRecyclerView();
        else
            loadEventRecyclerView();

        return view;
    }

    private void loadGroupRecyclerView(){
        final ArrayList<String> membersKeyList = new ArrayList<>();
        final ArrayList<String> membersPositionList = new ArrayList<>();
        final MembersAdapter adapter = new MembersAdapter(getContext(),membersKeyList,membersPositionList);
        recyclerView.setAdapter(adapter);

        groupDA = new GroupDA();
        groupDA.getGroupMembers(groupKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue()!=null){
                    membersKeyList.add(dataSnapshot.getKey());
                    membersPositionList.add(dataSnapshot.getValue().toString());
                    adapter.notifyItemInserted(membersPositionList.size()-1);
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

    private void loadEventRecyclerView(){
        final ArrayList<String> membersKeyList = new ArrayList<>();
        final ArrayList<String> membersPositionList = new ArrayList<>();
        final MembersAdapter adapter = new MembersAdapter(getContext(),membersKeyList,membersPositionList);
        recyclerView.setAdapter(adapter);

        EventDA eventDA = new EventDA();
        eventDA.getEventMembers(eventKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue()!=null){
                    membersKeyList.add(dataSnapshot.getKey());
                    membersPositionList.add(dataSnapshot.getValue().toString());
                    adapter.notifyItemInserted(membersPositionList.size()-1);
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
