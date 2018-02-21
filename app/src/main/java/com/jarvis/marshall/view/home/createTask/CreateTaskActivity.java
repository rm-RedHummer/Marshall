package com.jarvis.marshall.view.home.createTask;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jarvis.marshall.R;
import com.jarvis.marshall.view.home.createEvent.CreateEventFragment;

public class CreateTaskActivity extends AppCompatActivity {
    private String eventKey;
    private FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        eventKey = getIntent().getExtras().getString("eventKey");
        fm = getSupportFragmentManager();
        changeFragment(new CreateTaskFragment(),"CreateTaskFragment");
    }

    public void changeFragment(Fragment fragment, String tag){
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.actCreateTask_FrameLayout, fragment, tag);
        //ft.replace(R.id.main_framelayout,fragment,tag);
        ft.setCustomAnimations(R.anim.enter_anim,R.anim.stay_anim,R.anim.stay_anim,R.anim.exit_anim);
        ft.addToBackStack(tag);
        ft.commit();
    }
}
