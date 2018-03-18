package com.jarvis.marshall.view.home.members;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.GroupDA;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembersFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private GroupDA groupDA;
    private String groupKey;

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
            groupKey = bundle.getString("groupKey");
        }
        groupDA = new GroupDA();

        /*recyclerView = view.findViewById(R.id.fragMembers_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        loadRecyclerView();*/

        return view;
    }

    private void loadRecyclerView(){
        final ArrayList<String> membersNameList = new ArrayList<>();
        final ArrayList<String> membersPositionList = new ArrayList<>();
        final MembersAdapter adapter = new MembersAdapter(getContext(),membersNameList,membersPositionList);
        recyclerView.setAdapter(adapter);


    }

}
