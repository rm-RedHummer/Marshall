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

import com.jarvis.marshall.MainActivity;
import com.jarvis.marshall.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Jarvis on 26/03/2018.
 */

public class GroupReportAdapter extends RecyclerView.Adapter<GroupReportAdapter.ListHolder> {
    private Context context;
    private ArrayList<String> keyList;
    private LayoutInflater inflater;
    private Integer ctr = 0;

    public GroupReportAdapter(Context context, ArrayList<String> keyList){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.keyList = keyList;
    }
    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.viewholder_group_report,parent, false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        final String[] split = keyList.get(position).split(":");
        holder.name.setText(split[1]);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new UserListFragment(),split[0]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return keyList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ConstraintLayout layout;
        public ListHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.vhGroupReport_name);
            layout = itemView.findViewById(R.id.vhGroupReport_layout);
        }
    }

    private void changeFragment(Fragment fragment, String groupKey){
        MainActivity mainActivity = (MainActivity) context;
        Bundle bundle = new Bundle();
        bundle.putString("groupKey",groupKey);
        fragment.setArguments(bundle);

        FragmentTransaction ft = mainActivity.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_anim,R.anim.stay_anim,R.anim.stay_anim,R.anim.exit_anim);
        ft.replace(R.id.main_framelayout, fragment, "UserList");
        ft.addToBackStack("UserList");
        ft.commit();
    }
}