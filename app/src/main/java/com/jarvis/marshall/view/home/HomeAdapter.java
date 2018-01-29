package com.jarvis.marshall.view.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.GroupDA;
import com.jarvis.marshall.model.Group;

import java.util.ArrayList;

/**
 * Created by Jarvis on 03/01/2018.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ListHolder>{
    private Context context;
    private ArrayList<Group> groupList;
    private LayoutInflater inflater;
    private GroupDA groupDA;
    private ProgressDialog progressDialog;


    public HomeAdapter(Context context, ArrayList<Group> groupList, ProgressDialog progressDialog){
        this.context = context;
        this.groupList = groupList;
        inflater = LayoutInflater.from(context);
        groupDA = new GroupDA();
        this.progressDialog = progressDialog;

    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.viewholder_groups, parent, false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, int position) {
        final Group group = groupList.get(position);
        //holder.groupName.setText(group.getGroupName());
        //holder.numOfMembers.setText(String.valueOf(group.getGroupMembers().size())+" Members");
        groupDA.getAllGroups().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int ctr = 1;
                ArrayList<String> groupMembers;
                String groupName;
                for(DataSnapshot ds:dataSnapshot.child(group.getKey()).getChildren()){
                    if (ctr == 2) {
                        groupMembers = (ArrayList<String>) ds.getValue();
                        holder.numOfMembers.setText(String.valueOf(groupMembers.size()) + " Joined");
                    }
                    else if (ctr == 3)
                        holder.groupName.setText(ds.getValue().toString());

                    if (ctr < 5)
                        ctr++;
                    else
                        ctr = 1;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        progressDialog.dismiss();
    }

    @Override
    public int getItemCount() { return groupList.size(); }

    public class ListHolder extends RecyclerView.ViewHolder {
        private TextView groupName, numOfMembers;
        public ListHolder(View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.vh_groups_title);
            numOfMembers = itemView.findViewById(R.id.vh_groups_members);
        }
    }
}
