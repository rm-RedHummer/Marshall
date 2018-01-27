package com.jarvis.marshall.view.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


    public HomeAdapter(Context context, ArrayList<Group> groupList){
        this.context = context;
        this.groupList = groupList;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.viewholder_groups, parent, false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        final Group group = groupList.get(position);
    }

    @Override
    public int getItemCount() {

        return groupList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        public ListHolder(View itemView) {
            super(itemView);
        }
    }
}
