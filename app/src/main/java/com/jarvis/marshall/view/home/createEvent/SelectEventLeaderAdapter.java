package com.jarvis.marshall.view.home.createEvent;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.GroupDA;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Jarvis on 08/02/2018.
 */

public class SelectEventLeaderAdapter extends RecyclerView.Adapter<SelectEventLeaderAdapter.ListHolder> {
    private Context context;
    private ArrayList<String> stringArrayList;
    private LayoutInflater inflater;
    private CompoundButton lastCheckedRB = null;

    public SelectEventLeaderAdapter(Context context, ArrayList<String> stringArrayList){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.stringArrayList =stringArrayList;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.viewholder_select_event_leader, parent, false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, int position) {
        final String name = stringArrayList.get(position);
        holder.name.setText(name);
        holder.radioButton.setOnCheckedChangeListener(ls);
        holder.radioButton.setTag(position);
        if(position==0){
            holder.radioButton.setChecked(true);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.radioButton.setChecked(true);
            }
        });
    }

    public int getCheckedButton(){
        return (int) lastCheckedRB.getTag();
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private CompoundButton radioButton;
        private ConstraintLayout layout;

        public ListHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.vhSelectEventLeader_userName);
            radioButton = itemView.findViewById(R.id.vhSelectEventLeader_RadioBtn);
            layout = itemView.findViewById(R.id.vhSelectEventLeader_constraintLayout);
        }
    }

    private CompoundButton.OnCheckedChangeListener ls = (new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int tag = (int) buttonView.getTag();
            if(lastCheckedRB == null)
                lastCheckedRB = buttonView;
            else if (tag!= (int) lastCheckedRB.getTag()){
                lastCheckedRB.setChecked(false);
                lastCheckedRB = buttonView;
            }
        }
    });
}
