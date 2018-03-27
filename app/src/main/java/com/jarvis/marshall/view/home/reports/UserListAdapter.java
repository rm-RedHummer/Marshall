package com.jarvis.marshall.view.home.reports;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.MainActivity;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.UserDA;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Jarvis on 27/03/2018.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ListHolder> {
    private Context context;
    private ArrayList<String> userKeyList;
    private LayoutInflater inflater;
    private UserDA userDA;
    private String groupKey;

    public UserListAdapter(Context context, ArrayList<String> userKeyList, String groupKey){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.userKeyList = userKeyList;
        userDA = new UserDA();
        this.groupKey= groupKey;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.viewholder_members, parent, false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, int position) {
        final String[] split = userKeyList.get(position).split(":");
        userDA.getUserName(split[0]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.name.setText(dataSnapshot.getValue().toString());
                holder.position.setText(split[1]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new UserReportFragment(),split[0],holder.name.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userKeyList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        private TextView name, position;
        private ConstraintLayout layout;
        public ListHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.vhMembers_name);
            position = itemView.findViewById(R.id.vhMembers_position);
            layout = itemView.findViewById(R.id.vhMembers);
        }
    }

    private void changeFragment(Fragment fragment, String userKey, String userName){
        MainActivity mainActivity = (MainActivity) context;
        Bundle bundle = new Bundle();
        bundle.putString("userKey",userKey);
        bundle.putString("groupKey",groupKey);
        fragment.setArguments(bundle);

        FragmentTransaction ft = mainActivity.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_anim,R.anim.stay_anim,R.anim.stay_anim,R.anim.exit_anim);
        ft.replace(R.id.main_framelayout, fragment, "UserReport");
        ft.addToBackStack("UserReport");
        ft.commit();
    }
}
