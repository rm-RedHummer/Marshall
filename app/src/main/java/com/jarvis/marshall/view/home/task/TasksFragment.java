package com.jarvis.marshall.view.home.task;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jarvis.marshall.R;
import com.jarvis.marshall.view.home.createTask.CreateTaskActivity;

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

    }

    private void createTask(){
        CreateTaskActivity createTaskActivity = new CreateTaskActivity();
        Intent intent = new Intent(getContext(),createTaskActivity.getClass());
        intent.putExtra("eventKey",eventKey);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.enter_anim,R.anim.stay_anim);
    }

}
