package com.jarvis.marshall.view.home.createTask;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jarvis.marshall.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateTaskFragment extends Fragment implements View.OnClickListener{
    private View view;
    private Button detailsBtn, dateBtn, TimeBtn, membersBtn, cancelBtn,saveBtn;

    public CreateTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_task, container, false);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case(R.id.actCreateTask_detailsBtn):

                break;
            case(R.id.actCreateTask_deadlineDateBtn):

                break;

            case(R.id.actCreateTask_deadlineTimeBtn):

                break;
            case(R.id.actCreateTask_members):

                break;
            case(R.id.actCreateTask_cancelBtn):

                break;
            case(R.id.actCreateTask_saveBtn):

                break;
        }
    }
}
