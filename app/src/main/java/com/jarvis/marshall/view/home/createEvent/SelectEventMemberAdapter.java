package com.jarvis.marshall.view.home.createEvent;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.jarvis.marshall.R;

import java.util.ArrayList;

/**
 * Created by Jarvis on 08/02/2018.
 */

public class SelectEventMemberAdapter extends RecyclerView.Adapter<SelectEventMemberAdapter.ListHolder> {
    private Context context;
    private ArrayList<String> stringArrayList,positionArrayList;
    private LayoutInflater inflater;
    public SelectEventMemberAdapter(Context context, ArrayList<String> stringArrayList){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.stringArrayList =stringArrayList;
        positionArrayList = new ArrayList<>();
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.viewholder_select_event_members, parent, false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, final int position) {
        holder.checkBox.setText(stringArrayList.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkBox.isChecked()==false)
                    holder.checkBox.setChecked(true);
                 else
                    holder.checkBox.setChecked(false);
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked==true)
                    positionArrayList.add(String.valueOf(position));
                else
                    positionArrayList.remove(String.valueOf(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public ArrayList<String> getChecked(){
        return positionArrayList;
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout layout;
        private CheckBox checkBox;
        public ListHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.vhSelectEventMembers_constraintLayout);
            checkBox = itemView.findViewById(R.id.vhSelectEventMembers_checkBox);
        }
    }
}
