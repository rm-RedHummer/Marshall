package com.jarvis.marshall.view.home.task;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.MainActivity;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.TaskDA;
import com.jarvis.marshall.model.Task;
import com.jarvis.marshall.view.home.members.MembersFragment;

import java.util.ArrayList;

/**
 * Created by Jarvis on 13/02/2018.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ListHolder> {
    private Context context;
    private ArrayList<Task> taskArrayList;
    private LayoutInflater inflater;
    private TaskDA taskDA;
    private String userPosition;

    public TaskAdapter(Context context, ArrayList<Task> taskArrayList,String userPosition){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.taskArrayList = taskArrayList;
        taskDA = new TaskDA();
        this.userPosition = userPosition;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.viewholder_task, parent, false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, final int position) {
        final Task task = taskArrayList.get(position);




        holder.title.setText(task.getName());
        if(task.getStatus().equals("Task done")){
            holder.check.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
        }
        holder.status.setText(task.getStatus());

        holder.date.setText(task.getDeadlineDate());
        holder.time.setText(task.getDeadlineTime());

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.status.getText().toString().equals("Undone")){
                    holder.check.setColorFilter(context.getApplicationContext().getResources().getColor(R.color.colorPrimary));
                    holder.status.setText("Task done");
                    taskDA.setStatus(task.getKey(),"Task done");
                } else {
                    holder.check.setColorFilter(context.getApplicationContext().getResources().getColor(R.color.material_grey_500));
                    holder.status.setText("Undone");
                    taskDA.setStatus(task.getKey(),"Undone");
                }

            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(task,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        private TextView title, date,time, status;
        private ImageView check;
        private ConstraintLayout layout;
        public ListHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.vh_task_name);
            date = itemView.findViewById(R.id.vh_task_deadlineDate);
            time = itemView.findViewById(R.id.vh_task_deadlineTime);
            status = itemView.findViewById(R.id.vh_task_status);
            check = itemView.findViewById(R.id.vh_task_checkImage);
            layout = itemView.findViewById(R.id.vh_task_constraintlayout);
        }
    }

    public void showDialog(final Task task, final int position){
        final MainActivity mainActivity = (MainActivity) context;
        LayoutInflater inflater = mainActivity.getLayoutInflater();
        final View view2 = inflater.inflate(R.layout.dialog_task_detail, null);
        final AlertDialog optionsDialog = new AlertDialog.Builder(context)
                .setView(view2)
                .create();

        optionsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView title = view2.findViewById(R.id.dgTask_title);
        final TextView details = view2.findViewById(R.id.dgTask_details);
        final TextView deadline = view2.findViewById(R.id.dgTask_deadline);
        final TextView status = view2.findViewById(R.id.dgTask_status);
        FloatingActionButton fabEdit = view2.findViewById(R.id.dgTask_fabEdit);
        FloatingActionButton fabDelete = view2.findViewById(R.id.dgTask_fabDelete);
        FloatingActionButton fabDone = view2.findViewById(R.id.dgTask_fabDone);
        FloatingActionButton fabMembers = view2.findViewById(R.id.dgTask_fabViewMembers);
        CardView lowerCardView = view2.findViewById(R.id.cardView2);

        if(userPosition.equals("None")||userPosition.equals("Member"))
            lowerCardView.setVisibility(View.GONE);

        taskDA.getSpecificTask(task.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    String deadlineDate="";
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        if(ds.getKey().equals("deadlineDate"))
                            deadlineDate = ds.getValue().toString();
                        else if(ds.getKey().equals("deadlineTime"))
                            deadline.setText(deadlineDate+", "+ds.getValue().toString());
                        else if(ds.getKey().equals("details"))
                            details.setText(ds.getValue().toString());
                        else if(ds.getKey().equals("name"))
                            title.setText(ds.getValue().toString());
                        else if(ds.getKey().equals("status"))
                            status.setText(ds.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] dismiss  = new String[1];
                final AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Are you sure you want to delete this task?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                taskDA.deleteTask(task.getKey());
                                taskArrayList.remove(position);
                                notifyItemRemoved(position);
                                optionsDialog.dismiss();

                            }
                        }) //Set to null. We override the onclick
                        .setNegativeButton("No", null)
                        .create();

                dialog.show();
            }
        });

        fabMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) context;
                MembersFragment fragment = new MembersFragment();
                Bundle bundle = new Bundle();
                bundle.putString("taskKey",task.getKey());
                fragment.setArguments(bundle);

                FragmentTransaction ft = mainActivity.getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.enter_anim,R.anim.stay_anim,R.anim.stay_anim,R.anim.exit_anim);
                ft.replace(R.id.main_framelayout, fragment, "TaskMembers");
                ft.addToBackStack("TaskMembers");
                ft.commit();
                optionsDialog.dismiss();
            }
        });

        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionsDialog.dismiss();
            }
        });

        optionsDialog.show();
    }
}
