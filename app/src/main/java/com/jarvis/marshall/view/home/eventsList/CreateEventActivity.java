package com.jarvis.marshall.view.home.eventsList;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.jarvis.marshall.R;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
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
