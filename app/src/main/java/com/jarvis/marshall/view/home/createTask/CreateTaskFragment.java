package com.jarvis.marshall.view.home.createTask;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.TaskDA;
import com.jarvis.marshall.model.Task;
import com.jarvis.marshall.view.home.createEvent.CreateEventActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateTaskFragment extends Fragment implements View.OnClickListener{
    private View view;
    private Button detailsBtn, dateBtn, timeBtn, membersBtn, cancelBtn,saveBtn;
    private EditText taskTitleEditText;
    private String details="", date="", time="",taskTitle="", taskKey,status,eventKey,remarks;
    private Integer rawYear,rawMonth,rawDay,rawHour,rawMinute;
    private Intent dataBundle;
    private FragmentManager fm;
    private TaskDA taskDA;

    public CreateTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataBundle = getActivity().getIntent();
        eventKey = dataBundle.getExtras().getString("eventKey");
        fm = getActivity().getSupportFragmentManager();
        view = inflater.inflate(R.layout.fragment_create_task, container, false);
        taskDA = new TaskDA();
        wireViews();
        initializeDataToViews();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case(R.id.actCreateTask_detailsBtn):
                showDetailsDialog();
                break;
            case(R.id.actCreateTask_deadlineDateBtn):
                showDialog(0);
                break;

            case(R.id.actCreateTask_deadlineTimeBtn):
                showDialog(1);
                break;
            case(R.id.actCreateTask_members):
                if(taskTitle!=null)
                    dataBundle.putExtra("taskTitle",taskTitle);
                changeFragment(new TaskSelectMembersFragment(), "TaskSelectMembersFragment");
                break;
            case(R.id.actCreateTask_cancelBtn):
                back();

                break;
            case(R.id.actCreateTask_saveBtn):
                createTask();

                break;
        }
    }

    public void createTask(){
        Boolean isArrayListEmpty = true;
        ArrayList<String> taskMemberKeyList= new ArrayList<>(),taskMemberNameList= new ArrayList<>();
        if(dataBundle.getExtras().getStringArrayList("taskMembersArrayList")!=null){
            taskMemberKeyList = dataBundle.getExtras().getStringArrayList("taskMembersArrayList");
            taskMemberNameList = dataBundle.getExtras().getStringArrayList("taskMembersNamesArrayList");
            isArrayListEmpty = false;
        }
        if(!taskTitleEditText.getText().toString().isEmpty())
            taskTitle = taskTitleEditText.getText().toString();

        if(taskTitle.equals("")||details.equals("")||date.equals("")||time.equals("")||isArrayListEmpty==true){
            AlertDialog.Builder dg = new AlertDialog.Builder(getContext());
            dg.setMessage("Please enter all required information.");
            dg.setPositiveButton("OK",null);
            dg.show();
        } else {
            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().getRoot().child("task");
            taskKey = rootRef.push().getKey();
            status = "Undone";
            remarks = "none";
            Task task = new Task(taskKey,eventKey,taskTitle,details,date,status,time,remarks);
            Map map = new HashMap();
            for(int ctr = 0 ; ctr < taskMemberNameList.size(); ctr++){
                map.put(taskMemberKeyList.get(ctr),taskMemberNameList.get(ctr));
            }
            task.setTaskMembers(map);
            taskDA.createNewTask(task);
            back();
        }
    }

    public void showDetailsDialog(){
        LayoutInflater inflater = getLayoutInflater();
        final View view2 = inflater.inflate(R.layout.dialog_enter_description,null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view2)
                .setTitle("Enter task details")
                .setPositiveButton("Confirm", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", null)
                .create();
        final EditText descriptionEditText = view2.findViewById(R.id.dgCreateEvent_description);
        descriptionEditText.setHint("Enter details");
        if(!detailsBtn.getText().toString().toLowerCase().equals("enter details")){
            descriptionEditText.setText(details);
        }

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        details = descriptionEditText.getText().toString();
                        dataBundle.putExtra("details",details);
                        if(details.equals("")){
                            detailsBtn.setText("ENTER DETAILS");
                        } else {
                            String displayDescription = null;
                            if (details.length() > 20)
                                displayDescription = details.substring(0, 20);
                            else
                                displayDescription = details;
                            detailsBtn.setText(displayDescription);
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    private void showDialog(int id){
        final Calendar c = Calendar.getInstance();
        if(id==0) {
            int year = 0, month = 0, day = 0;
            if(dateBtn.getText().toString().toLowerCase().equals("select date")){
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            } else {
                year = rawYear;
                month = rawMonth;
                day = rawDay;
            }
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), datePickerListener, year, month, day);
            datePickerDialog.show();
        }
        else if(id==1) {
            int hour = 0, minute = 0;
            if(timeBtn.getText().toString().toLowerCase().equals("select time")){
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
            } else {
                hour = rawHour;
                minute = rawMinute;
            }

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), timePickerListener, hour,minute, DateFormat.is24HourFormat(getContext()));
            timePickerDialog.show();
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            rawYear = selectedYear;
            rawMonth = selectedMonth;
            rawDay = selectedDay;
            date = selectedMonth+"/"+selectedDay+"/"+selectedYear;
            dataBundle.putExtra("date",date);
            dateBtn.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            String timeChosen = String.valueOf(hour)+":"+String.valueOf(minute);
            rawHour = hour;
            rawMinute = minute;
            time = timeChosen;
            dataBundle.putExtra("time",time);
            timeBtn.setText(processTime(timeChosen));
        }
    };

    private String processTime(String time){
        String[] arrayTime;
        String ampm=" am";
        arrayTime = time.split(":");
        Integer hour = Integer.parseInt(arrayTime[0]);
        Integer min = Integer.parseInt(arrayTime[1]);

        if(hour>12){

            int num=0;
            for(int i = 12 ; i < hour ; i ++){
                num++;
            }
            time = num+":";
            ampm = " pm";
        }
        else {
            if(hour==12)
                ampm=" pm";
            time = hour + ":";
        }
        if(min<10){
            time = time + "0" + min + ampm;
        } else {
            time = time + min + ampm;
        }
        return time;
    }

    private void wireViews(){
        taskTitleEditText = view.findViewById(R.id.actCreateTask_taskName);
        detailsBtn = view.findViewById(R.id.actCreateTask_detailsBtn);
        timeBtn = view.findViewById(R.id.actCreateTask_deadlineTimeBtn);
        dateBtn = view.findViewById(R.id.actCreateTask_deadlineDateBtn);
        membersBtn = view.findViewById(R.id.actCreateTask_members);
        saveBtn = view.findViewById(R.id.actCreateTask_saveBtn);
        cancelBtn = view.findViewById(R.id.actCreateTask_cancelBtn);
        detailsBtn.setOnClickListener(this);
        timeBtn.setOnClickListener(this);
        dateBtn.setOnClickListener(this);
        membersBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    public void changeFragment(Fragment fragment, String tag){
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.actCreateTask_FrameLayout,fragment,tag);
        ft.setCustomAnimations(R.anim.enter_anim,R.anim.stay_anim,R.anim.stay_anim,R.anim.exit_anim);
        ft.addToBackStack(tag);
        ft.commit();
    }

    @SuppressLint("NewApi")
    public void back(){
        CreateTaskActivity createTaskActivity = (CreateTaskActivity) getContext();
        createTaskActivity.navigateUpTo(createTaskActivity.getIntent());
        createTaskActivity.finish();
        createTaskActivity.overridePendingTransition(R.anim.stay_anim,R.anim.exit_anim);
    }

    public void initializeDataToViews(){
        if(dataBundle.getExtras().containsKey("details")){
            details = dataBundle.getExtras().getString("details");
            if(details.equals("")){
                detailsBtn.setText("ENTER details");
            } else {
                String displayDescription = null;
                if (details.length() > 20)
                    displayDescription = details.substring(0, 20);
                else
                    displayDescription = details;
                detailsBtn.setText(displayDescription);
            }
        }
        if(dataBundle.getExtras().containsKey("date")){
            date = dataBundle.getExtras().getString("date");
            String[] dateSplit = date.split("/");
            rawDay = Integer.valueOf(dateSplit[1]);
            rawMonth = Integer.valueOf(dateSplit[0]);
            rawYear = Integer.valueOf(dateSplit[2]);
            dateBtn.setText(rawDay+"/"+(rawMonth+1)+"/"+rawYear);
        }
        if(dataBundle.getExtras().containsKey("time")){
            time = dataBundle.getExtras().getString("time");
            String[] timeSplit = time.split(":");
            rawHour = Integer.valueOf(timeSplit[0]);
            rawMinute = Integer.valueOf(timeSplit[1]);
            timeBtn.setText(processTime(time));
        }
    }
}
