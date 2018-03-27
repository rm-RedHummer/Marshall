package com.jarvis.marshall.view.home.reports;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.EventDA;
import com.jarvis.marshall.dataAccess.GroupDA;
import com.jarvis.marshall.dataAccess.TaskDA;
import com.jarvis.marshall.dataAccess.UserDA;
import com.jarvis.marshall.model.Task;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserReportFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private String userKey, groupKey="";
    private TextView name, finished, total, percent;


    public UserReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_report, container, false);

        Bundle bundle = getArguments();
        if(bundle!=null) {
            if (bundle.containsKey("userKey"))
                userKey = bundle.getString("userKey");
            if(bundle.containsKey("groupKey"))
                groupKey= bundle.getString("groupKey");
        }



        UserDA userDA = new UserDA();

        wireViews();

        userDA.getUserName(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        recyclerView = view.findViewById(R.id.vhUserReport_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        loadRecyclerView();

        return view;
    }

    public void loadRecyclerView(){
        final ArrayList<Task> taskArrayList = new ArrayList<>();
        final ArrayList<String> keyList = new ArrayList<>();
        final UserReportAdapter adapter = new UserReportAdapter(getContext(),keyList);
        recyclerView.setAdapter(adapter);


        final TaskDA taskDA = new TaskDA();
        final EventDA eventDA = new EventDA();
        UserDA userDA = new UserDA();
        final GroupDA groupDA = new GroupDA();

        finished.setText(String.valueOf(0));
        total.setText(String.valueOf(0));
        percent.setText(String.valueOf(0)+"%");

        eventDA.getAllEvents(groupKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dsEvent) {
                for(DataSnapshot dsEventKey: dsEvent.getChildren()){
                    taskDA.getEventTasks(dsEventKey.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dsTask) {
                            for(DataSnapshot dsTaskKey: dsTask.getChildren()){ //Tasks
                                boolean isUsersTask=false;
                                for(DataSnapshot dsTaskChildren: dsTaskKey.getChildren()){ // Attributes
                                    if(dsTaskChildren.getKey().equals("taskMembers")){
                                        for(DataSnapshot dstm: dsTaskChildren.getChildren()){

                                            if(dstm.getKey().equals(userKey)) {
                                                isUsersTask = true;

                                            }
                                        }
                                    }
                                }

                                if(isUsersTask==true) {
                                    for(DataSnapshot dsTaskChildren: dsTaskKey.getChildren()){
                                        if(dsTaskChildren.getKey().equals("key")){
                                            keyList.add(dsTaskChildren.getValue().toString());
                                            total.setText(String.valueOf(keyList.size()));
                                            adapter.notifyItemInserted(keyList.size()-1);
                                        }
                                        if(dsTaskChildren.getKey().equals("status")){
                                            if(dsTaskChildren.getValue().toString().equals("Task done")){
                                                Integer num = Integer.parseInt(finished.getText().toString());
                                                num++;
                                                finished.setText(String.valueOf(num));
                                            }
                                        }
                                    }
                                    Integer fin = Integer.parseInt(finished.getText().toString()), tot = Integer.parseInt(total.getText().toString());
                                    Double quo = (fin.doubleValue() / tot.doubleValue()) * 100;
                                    Integer intPercent = quo.intValue();
                                    percent.setText(String.valueOf(intPercent)+"%");
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

    public void wireViews(){
        name = view.findViewById(R.id.vhUserReport_name);
        finished = view.findViewById(R.id.vhUserReport_finished);
        total = view.findViewById(R.id.vhUserReport_total);
        percent  = view.findViewById(R.id.vhUserReport_percent);
    }

}
