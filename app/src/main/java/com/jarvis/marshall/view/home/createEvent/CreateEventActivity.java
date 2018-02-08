package com.jarvis.marshall.view.home.createEvent;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.EventDA;
import com.jarvis.marshall.model.Event;

import java.util.ArrayList;
import java.util.Calendar;


public class CreateEventActivity extends AppCompatActivity {
    private String groupKey;
    private FirebaseAuth mAuth;
    private FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        groupKey = getIntent().getExtras().getString("groupKey");
        mAuth = FirebaseAuth.getInstance();
        fm = getSupportFragmentManager();
        changeFragment(new CreateEventFragment(),"CreateEventFragment");
    }

    @Override
    public void onBackPressed(){
        back();
    }
    @SuppressLint("NewApi")
    public void back(){
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

    public void changeFragment(Fragment fragment, String tag){
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.actCreateEvent_frameLayout, fragment, tag);
        //ft.replace(R.id.main_framelayout,fragment,tag);
        ft.setCustomAnimations(R.anim.enter_anim,R.anim.stay_anim,R.anim.stay_anim,R.anim.exit_anim);
        ft.addToBackStack(tag);
        ft.commit();
    }
}
