package com.jarvis.marshall.view.home.task;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.jarvis.marshall.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;

    public TasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tasks, container, false);

        recyclerView = view.findViewById(R.id.fragment_tasks_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        loadTasksRecyclerView();

        FloatingActionButton fab = view.findViewById(R.id.fragment_tasks_fab);

        return view;
    }

    private void loadTasksRecyclerView(){

    }

}
