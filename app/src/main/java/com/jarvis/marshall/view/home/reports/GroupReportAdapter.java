package com.jarvis.marshall.view.home.reports;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jarvis.marshall.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Jarvis on 26/03/2018.
 */

public class GroupReportAdapter extends RecyclerView.Adapter<GroupReportAdapter.ListHolder> {
    private Context context;
    private ArrayList<String> keyList;
    private LayoutInflater inflater;

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
        AlertDialog.Builder dg = new AlertDialog.Builder(context);
        dg.setMessage(String.valueOf(keyList.size()));
        dg.show();
        String[] split = keyList.get(position).split(":");
        holder.name.setText(split[1]);
    }

    @Override
    public int getItemCount() {
        return keyList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        private TextView name;
        public ListHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.vhGroupReport_name);
        }
    }
}
