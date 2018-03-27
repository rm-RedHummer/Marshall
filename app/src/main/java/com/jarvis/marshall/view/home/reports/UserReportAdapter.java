package com.jarvis.marshall.view.home.reports;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.EventDA;
import com.jarvis.marshall.dataAccess.TaskDA;
import com.jarvis.marshall.model.Task;

import java.util.ArrayList;

/**
 * Created by Jarvis on 27/03/2018.
 */

public class UserReportAdapter extends RecyclerView.Adapter<UserReportAdapter.ListHolder> {
    private Context context;
    private ArrayList<String> keyList;
    private LayoutInflater inflater;
    private TaskDA taskDA;

    public UserReportAdapter(Context context, ArrayList<String> keyList){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.keyList = keyList;
        taskDA = new TaskDA();
    }
    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.viewholder_user_report,parent,false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, int position) {
        taskDA.getSpecificTask(keyList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if(ds.getKey().equals("name"))
                        holder.taskName.setText(ds.getValue().toString());
                    else if(ds.getKey().equals("status")){
                        if(ds.getValue().toString().equals("Task done")){
                            holder.check.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
                        }
                        holder.status.setText(ds.getValue().toString());
                    }
                    else if(ds.getKey().equals("eventKey")){
                        EventDA eventDA = new EventDA();
                        eventDA.getSpecificEvent(ds.getValue().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot dsEvent: dataSnapshot.getChildren()){
                                    if(dsEvent.getKey().equals("name"))
                                        holder.eventName.setText(dsEvent.getValue().toString());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return keyList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        private TextView eventName, taskName, status;
        private ImageView check;
        public ListHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.vhUserReport_eventName);
            taskName = itemView.findViewById(R.id.vhUserReport_taskName);
            status = itemView.findViewById(R.id.vhUserReport_status);
            check = itemView.findViewById(R.id.vhUserReport_check);
        }
    }
}
