package com.jarvis.marshall.view.home.eventsList;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.jarvis.marshall.R;
import com.jarvis.marshall.model.Event;

import java.util.Calendar;


public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {
    private Button descriptionBtn,dateBtn,startTimeBtn,endTimeBtn,venueBtn,membersBtn,cancelBtn,saveBtn;
    private EditText eventNameEditText;
    private String groupKey,description,date,startTime,endTime,venue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        groupKey = getIntent().getExtras().getString("groupKey");
        wireViews();

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
                break;
        }
    }

    public void showDescriptionDialog(){
        LayoutInflater inflater = getLayoutInflater();
        final View view2 = inflater.inflate(R.layout.dialog_enter_description,null);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view2)
                .setTitle("Enter event description")
                .setPositiveButton("Confirm", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final EditText descriptionEditText = view2.findViewById(R.id.dgCreateEvent_description);
                        description = descriptionEditText.getText().toString();
                        String displayDescription = descriptionEditText.getText().toString().substring(0,25);
                        descriptionBtn.setText(displayDescription);
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
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view2)
                .setTitle("Enter event venue")
                .setPositiveButton("Confirm", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final EditText venueEditText = view2.findViewById(R.id.dgCreateEvent_venue);
                        venue = venueEditText.getText().toString();
                        String displayVenue = venueEditText.getText().toString().substring(0,25);
                        descriptionBtn.setText(displayVenue);
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        if(id==0) {
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        else if(id==1) {
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(this, timePickerListener1, hour,minute,DateFormat.is24HourFormat(this));
        }
        else {
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(this, timePickerListener2, hour, minute, DateFormat.is24HourFormat(this));
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            dateBtn.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            startTimeBtn.setText(hour+": "+minute);
        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            endTimeBtn.setText(hour+": "+minute);
        }
    };


    private void wireViews(){
        descriptionBtn = findViewById(R.id.actCreateEvent_descriptionBtn);
        dateBtn = findViewById(R.id.actCreateEvent_dateBtn);
        endTimeBtn = findViewById(R.id.actCreateEvent_endTimeBtn);
        startTimeBtn = findViewById(R.id.actCreateEvent_startTimeBtn);
        venueBtn = findViewById(R.id.actCreateEvent_venueBtn);
        membersBtn = findViewById(R.id.actCreateEvent_membersBtn);
        saveBtn = findViewById(R.id.actCreateEvent_saveBtn);
        cancelBtn = findViewById(R.id.actCreateEvent_cancelBtn);
        descriptionBtn.setOnClickListener(this);
        dateBtn.setOnClickListener(this);
        endTimeBtn.setOnClickListener(this);
        startTimeBtn.setOnClickListener(this);
        venueBtn.setOnClickListener(this);
        membersBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed(){
        navigateUpTo(getIntent());
        finish();
        overridePendingTransition(R.anim.stay_anim,R.anim.exit_anim);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.stay_anim,R.anim.exit_anim);
                break;
        }
        return true;
    }
}
