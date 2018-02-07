package com.jarvis.marshall.view.home.createEvent;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.EventDA;
import com.jarvis.marshall.model.Event;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventFragment extends Fragment implements View.OnClickListener{
    private Button descriptionBtn,dateBtn,startTimeBtn,endTimeBtn,venueBtn,membersBtn,cancelBtn,saveBtn;
    private TextInputEditText eventNameEditText;
    private String groupKey,eventName,eventKey,description,date,startTime,endTime,venue,status,tag;
    private Integer rawStartHour, rawStartMinute,rawEndHour,rawEndMinute,rawYear,rawMonth,rawDay;
    private FirebaseAuth mAuth;
    private View view;

    public CreateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if(bundle!=null){
            groupKey = bundle.getString("groupKey");
        }
        view = inflater.inflate(R.layout.fragment_create_event, container, false);
        mAuth = FirebaseAuth.getInstance();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        tag = fm.getBackStackEntryAt(
                fm.getBackStackEntryCount() - 1).getName();
        wireViews();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.actCreateEvent_descriptionBtn:
                showDescriptionDialog();
                break;
            case R.id.actCreateEvent_dateBtn:
                showDialog(0);
                break;
            case R.id.actCreateEvent_startTimeBtn:
                showDialog(1);
                break;
            case R.id.actCreateEvent_endTimeBtn:
                showDialog(2);
                break;
            case R.id.actCreateEvent_venueBtn:
                showVenueDialog();
                break;
            case R.id.actCreateEvent_membersBtn:
                break;
            case R.id.actCreateEvent_cancelBtn:
                break;
            case R.id.actCreateEvent_saveBtn:
                createEvent();
                break;
        }
    }

    public void createEvent(){
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().getRoot().child("event");
        eventKey = rootRef.push().getKey();
        eventName = eventNameEditText.getText().toString();
        status = "On preparation";
        /*AlertDialog.Builder dg = new AlertDialog.Builder(getContext());
        dg.setMessage(eventName+"\n"+date+"\n"+startTime+"\n"+endTime+"\n"+venue+"\n"+description+"\n"+status+"\n"+eventKey);
        dg.show();*/
        Event event = new Event(eventName,date,startTime,endTime,venue,description,status,eventKey,groupKey);
        EventDA eventDA = new EventDA();
        eventDA.createNewEvent(event);
        eventDA.addEventMember(eventKey,mAuth.getCurrentUser().getUid(),"Admin");
    }

    public void showDescriptionDialog(){
        LayoutInflater inflater = getLayoutInflater();
        final View view2 = inflater.inflate(R.layout.dialog_enter_description,null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view2)
                .setTitle("Enter event description")
                .setPositiveButton("Confirm", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", null)
                .create();
        final EditText descriptionEditText = view2.findViewById(R.id.dgCreateEvent_description);
        if(!descriptionBtn.getText().toString().equals("ENTER DESCRIPTION")){
            descriptionEditText.setText(description);
        }

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        description = descriptionEditText.getText().toString();
                        if(description.equals("")){
                            descriptionBtn.setText("ENTER DESCRIPTION");
                        } else {
                            String displayDescription = null;
                            if (description.length() > 25)
                                displayDescription = description.substring(0, 25);
                            else
                                displayDescription = description;
                            descriptionBtn.setText(displayDescription);
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    public void showVenueDialog(){
        LayoutInflater inflater = getLayoutInflater();
        final View view2 = inflater.inflate(R.layout.dialog_enter_venue,null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view2)
                .setTitle("Enter event venue")
                .setPositiveButton("Confirm", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                final EditText venueEditText = view2.findViewById(R.id.dgCreateEvent_venue);
                if(!venueBtn.getText().toString().equals("ENTER VENUE")) {
                    venueEditText.setText(venue);
                }

                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        venue = venueEditText.getText().toString();
                        if(venue.equals("")){
                            venueBtn.setText("ENTER VENUE");
                        } else {
                            String displayVenue = null;
                            if(venue.length()>25)
                                displayVenue = venue.substring(0,25);
                            else
                                displayVenue = venue;
                            venueBtn.setText(displayVenue);
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
            if(startTimeBtn.getText().toString().toLowerCase().equals("select start time")){
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
            } else {
                hour = rawStartHour;
                minute = rawStartMinute;
            }

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), timePickerListener1, hour,minute, DateFormat.is24HourFormat(getContext()));
            timePickerDialog.show();
        }
        else {
            int hour = 0, minute = 0;
            if(endTimeBtn.getText().toString().toLowerCase().equals("select end time")){
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
            } else {
                hour = rawEndHour;
                minute = rawEndMinute;
            }
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), timePickerListener2, hour, minute, DateFormat.is24HourFormat(getContext()));
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
            dateBtn.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            String timeChosen = String.valueOf(hour)+":"+String.valueOf(minute);
            rawStartHour = hour;
            rawStartMinute = minute;
            startTime = timeChosen;
            startTimeBtn.setText(processTime(timeChosen));
        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            String timeChosen = String.valueOf(hour)+":"+String.valueOf(minute);
            rawEndHour = hour;
            rawEndMinute = minute;
            endTime = timeChosen;
            endTimeBtn.setText(processTime(timeChosen));
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
        eventNameEditText = view.findViewById(R.id.actCreateEvent_eventName);
        descriptionBtn = view.findViewById(R.id.actCreateEvent_descriptionBtn);
        dateBtn = view.findViewById(R.id.actCreateEvent_dateBtn);
        endTimeBtn = view.findViewById(R.id.actCreateEvent_endTimeBtn);
        startTimeBtn = view.findViewById(R.id.actCreateEvent_startTimeBtn);
        venueBtn = view.findViewById(R.id.actCreateEvent_venueBtn);
        membersBtn = view.findViewById(R.id.actCreateEvent_membersBtn);
        saveBtn = view.findViewById(R.id.actCreateEvent_saveBtn);
        cancelBtn = view.findViewById(R.id.actCreateEvent_cancelBtn);
        descriptionBtn.setOnClickListener(this);
        dateBtn.setOnClickListener(this);
        endTimeBtn.setOnClickListener(this);
        startTimeBtn.setOnClickListener(this);
        venueBtn.setOnClickListener(this);
        membersBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

}