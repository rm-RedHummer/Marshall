package com.jarvis.marshall.view.home.members;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Jarvis on 12/03/2018.
 */

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ListHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> membersName, membersPosition;
    public MembersAdapter(Context context, ArrayList<String> membersName, ArrayList<String> membersPosition){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.membersName = membersName;
        this.membersPosition = membersPosition;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        public ListHolder(View itemView) {
            super(itemView);
        }
    }
}
