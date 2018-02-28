package com.jarvis.marshall.view.home.task;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.TaskDA;
import com.jarvis.marshall.model.Task;
import com.jarvis.marshall.view.home.createTask.CreateTaskActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private String eventKey;
    public TasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tasks, container, false);

        Bundle bundle = getArguments();
        if(bundle!=null){
            eventKey = bundle.getString("eventKey");
        }



        recyclerView = view.findViewById(R.id.fragment_tasks_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        loadTasksRecyclerView();

        FloatingActionButton fab = view.findViewById(R.id.fragment_tasks_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTask();
            }
        });

        return view;
    }

    private void loadTasksRecyclerView(){
        final ArrayList<Task> taskArrayList = new ArrayList<>();
        final TaskAdapter adapter = new TaskAdapter(getContext(),taskArrayList);
        recyclerView.setAdapter(adapter);
        TaskDA taskDA = new TaskDA();

        /*taskDA.getEventTasks(eventKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int ctr = 0;
                String date=null, details=null, time=null, eventKey=null, key=null, name=null, remarks=null, status=null;
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    switch (ctr){
                        case(0):
                            date = ds.getValue().toString();
                            break;
                        case(1):
                            time = ds.getValue().toString();
                            break;
                        case(2):
                            details = ds.getValue().toString();
                            break;
                        case(3):
                            eventKey = ds.getValue().toString();
                            break;
                        case(4):
                            key = ds.getValue().toString();
                            break;
                        case(5):
                            name = ds.getValue().toString();
                            break;
                        case(6):
                            remarks = ds.getValue().toString();
                            break;
                        case(7):
                            status = ds.getValue().toString();
                            break;
                        case(8):
                            ArrayList<String> memberList = new ArrayList<>();
                            for(DataSnapshot dataSnapshot1: ds.getChildren()){
                                memberList.add(dataSnapshot1.getKey()+":"+dataSnapshot1.getValue());
                            }
                            Task task = new Task(key, eventKey,name,details,date,status,time,remarks);
                            task.setMembers(memberList);
                            taskArrayList.add(task);
                            adapter.notifyItemInserted(taskArrayList.size() - 1);
                            break;
                    }
                    if(ctr<9)
                        ctr++;
                    else
                        ctr = 0;
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
        });*/
        taskDA.getEventTasks(eventKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapShot) {
                for(DataSnapshot dataSnapshot: dataSnapShot.getChildren()){
                    int ctr = 0;
                    String date=null, details=null, time=null, eventKey=null, key=null, name=null, remarks=null, status=null;
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        switch (ctr){
                            case(0):
                                date = ds.getValue().toString();
                                break;
                            case(1):
                                time = ds.getValue().toString();
                                break;
                            case(2):
                                details = ds.getValue().toString();
                                break;
                            case(3):
                                eventKey = ds.getValue().toString();
                                break;
                            case(4):
                                key = ds.getValue().toString();
                                break;
                            case(5):
                                name = ds.getValue().toString();
                                break;
                            case(6):
                                remarks = ds.getValue().toString();
                                break;
                            case(7):
                                status = ds.getValue().toString();
                                break;
                            case(8):
                                ArrayList<String> memberList = new ArrayList<>();
                                for(DataSnapshot dataSnapshot1: ds.getChildren()){
                                    memberList.add(dataSnapshot1.getKey()+":"+dataSnapshot1.getValue());
                                }
                                Task task = new Task(key, eventKey,name,details,date,status,time,remarks);
                                task.setMembers(memberList);
                                taskArrayList.add(task);
                                adapter.notifyItemInserted(taskArrayList.size() - 1);
                                break;
                        }
                        if(ctr<9)
                            ctr++;
                        else
                            ctr = 0;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void createTask(){
        CreateTaskActivity createTaskActivity = new CreateTaskActivity();
        Intent intent = new Intent(getContext(),createTaskActivity.getClass());
        intent.putExtra("eventKey",eventKey);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.enter_anim,R.anim.stay_anim);
    }

}
