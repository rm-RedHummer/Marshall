package com.jarvis.marshall.view.home.reports;


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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private String groupKey;

    public UserListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_user_list, container, false);

        Bundle bundle = getArguments();
        if(bundle!=null) {
            if (bundle.containsKey("groupKey"))
                groupKey = bundle.getString("groupKey");
        }

        recyclerView = view.findViewById(R.id.fragUserList_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        loadRecyclerView();

        return view;
    }

    public void loadRecyclerView(){
        final ArrayList<String> keyList = new ArrayList<>();
        final UserListAdapter adapter = new UserListAdapter(getContext(), keyList, groupKey);
        recyclerView.setAdapter(adapter);

        GroupDA groupDA = new GroupDA();
        groupDA.getGroupMembers(groupKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    keyList.add(ds.getKey()+":"+ds.getValue().toString());
                    adapter.notifyItemInserted(keyList.size());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
