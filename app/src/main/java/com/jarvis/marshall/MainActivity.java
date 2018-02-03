package com.jarvis.marshall;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.dataAccess.GroupDA;
import com.jarvis.marshall.dataAccess.UserDA;
import com.jarvis.marshall.model.Group;
import com.jarvis.marshall.view.home.HomeAdapter;
import com.jarvis.marshall.view.home.groups.HomeFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        fm = getSupportFragmentManager();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView name = header.findViewById(R.id.nav_header_name);
        name.setText(mAuth.getCurrentUser().getDisplayName());
        TextView email = header.findViewById(R.id.nav_header_email);
        email.setText(mAuth.getCurrentUser().getEmail());

        changeFragment(new HomeFragment(), "GroupListFragment");
        /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_framelayout, new HomeFragment());
        ft.commit();*/
    }

    public Context getAppContext() {
        return getBaseContext();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        FrameLayout frameLayout = findViewById(R.id.main_framelayout);
        String tag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(fm.getBackStackEntryCount()==1){
            //super.onBackPressed();

            AlertDialog.Builder dg = new AlertDialog.Builder(this);
            dg.setMessage("Do you want to exit?");
            dg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    moveTaskToBack(true);
                }
            });
            dg.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dg.show();
        } else if(fm.getBackStackEntryCount()==2 && !tag.equals("Edit_Settings")){
            fm.popBackStack();
        } else if(fm.getBackStackEntryCount()==3){
            fm.popBackStack();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_group) {
            while (fm.getBackStackEntryCount() != 1) {
                fm.popBackStackImmediate();
            }
        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeFragment(Fragment fragment, String tag){
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.main_framelayout, fragment, tag);
        //ft.replace(R.id.main_framelayout,fragment,tag);
        ft.setCustomAnimations(R.anim.enter_anim,R.anim.stay_anim,R.anim.stay_anim,R.anim.exit_anim);
        ft.addToBackStack(tag);
        ft.commit();
    }
}
