package com.jarvis.marshall.view.home.reports;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.EventDA;
import com.jarvis.marshall.dataAccess.TaskDA;
import com.jarvis.marshall.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jarvis on 23/03/2018.
 */

public class MonthlyReportAdapter extends RecyclerView.Adapter<MonthlyReportAdapter.ListHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Task> taskList;
    private ArrayList<String> groupKeyList;

    public MonthlyReportAdapter(Context context, ArrayList<String> groupKeyList){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.groupKeyList = groupKeyList;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.viewholder_monthly_reports, parent, false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, int position) {
        String groupKey="";
        ArrayList<String> eventKeyList = new ArrayList<>();

        final ArrayList<Task> taskArrayList = new ArrayList<>();
        holder.total.setText(String.valueOf(0));
        holder.finished.setText(String.valueOf(0));
        holder.percent.setText(String.valueOf(0)+"%");
        EventDA eventDA = new EventDA();
        final TaskDA taskDA = new TaskDA();
        for(int ctr = 0; ctr < groupKeyList.size(); ctr++){
            if(ctr == position) {
                String[] split = groupKeyList.get(ctr).split(":");
                holder.groupName.setText(split[1]);
                groupKey = split[0];

                final Integer totalTask = 0, finishedTask = 0;

                eventDA.getAllEvents(groupKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dsEvent) {
                        for(DataSnapshot dsEventKey: dsEvent.getChildren()){
                            taskDA.getEventTasks(dsEventKey.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dsTask) {
                                    if(dsTask==null)
                                        holder.total.setText(String.valueOf(0));
                                    else {
                                        String date = null, details = null, time = null, eventKey = null, key = null, name = null, remarks = null, status = null;
                                        for (DataSnapshot dsTaskParent : dsTask.getChildren()) {
                                            int num = 1;
                                            for (DataSnapshot dsTaskKey : dsTaskParent.getChildren()) {
                                                switch (num) {
                                                    case (1):
                                                        date = dsTaskKey.getValue().toString();
                                                        break;
                                                    case (2):
                                                        time = dsTaskKey.getValue().toString();
                                                        break;
                                                    case (3):
                                                        details = dsTaskKey.getValue().toString();
                                                        break;
                                                    case (4):
                                                        eventKey = dsTaskKey.getValue().toString();
                                                        break;
                                                    case (5):
                                                        key = dsTaskKey.getValue().toString();
                                                        break;
                                                    case (6):
                                                        name = dsTaskKey.getValue().toString();
                                                        break;
                                                    case (7):
                                                        remarks = dsTaskKey.getValue().toString();
                                                        break;
                                                    case (8):
                                                        status = dsTaskKey.getValue().toString();
                                                        if(status.equals("Task done")){
                                                            Integer finished = Integer.parseInt(holder.finished.getText().toString());
                                                            finished++;
                                                            holder.finished.setText(String.valueOf(finished));
                                                        }

                                                        break;
                                                    case (9):
                                                        Map newMap = new HashMap();
                                                        for (DataSnapshot dataSnapshot1 : dsTaskKey.getChildren()) {
                                                            newMap.put(dataSnapshot1.getKey().toString(), dataSnapshot1.getValue().toString());
                                                        }
                                                        Task task = new Task(key, eventKey, name, details, date, status, time, remarks);
                                                        task.setTaskMembers(newMap);
                                                        taskArrayList.add(task);
                                                        holder.total.setText(String.valueOf(taskArrayList.size()));
                                                        Integer total = taskArrayList.size(), finished = Integer.parseInt(holder.finished.getText().toString());
                                                        Double percent = (finished.doubleValue()/total.doubleValue())*100;
                                                        holder.percent.setText(String.valueOf(percent)+"%");
                                                        break;
                                                }
                                                if (num <= 9)
                                                    num++;
                                                else
                                                    num = 1;
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }








        /*
        for(String key: groupMap.keySet()){
            if(groupMap.get(key).equals(groupKey)){
                eventKeyList.add(key);
            }
        }*/


        /*for(String key: groupMap.keySet()){

            if(groupMap.get(key).equals(groupKey)){

                eventKeyList.add(key);
            }
        }

        for(int ctrEvent = 0; ctrEvent < eventKeyList.size(); ctrEvent++){
            for(int ctrTask = 0; ctrTask < taskArrayList.size(); ctrTask++){
                if(taskArrayList.get(ctrTask).getEventKey().equals(eventKeyList.get(ctrEvent))){
                    if(taskArrayList.get(ctrTask).getStatus().equals("Task done"))
                        finishedTask++;
                    totalTask++;

                }
            }
        }
        holder.finished.setText(String.valueOf(finishedTask));
        holder.total.setText(String.valueOf(totalTask));*/



    }

    @Override
    public int getItemCount() {
        return groupKeyList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        private TextView groupName, finished, total, percent;
        public ListHolder(View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.vhmonthly_groupName);
            finished = itemView.findViewById(R.id.vhmonthly_finishedTasks);
            total = itemView.findViewById(R.id.vhmonthly_totalTasks2);
            percent = itemView.findViewById(R.id.vhmonthly_percent);
        }
    }
}
