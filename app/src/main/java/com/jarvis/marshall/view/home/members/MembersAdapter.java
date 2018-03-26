package com.jarvis.marshall.view.home.members;

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
import com.jarvis.marshall.dataAccess.UserDA;

import java.util.ArrayList;

/**
 * Created by Jarvis on 12/03/2018.
 */

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ListHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> membersKey, membersPosition;
    private UserDA userDA;

    public MembersAdapter(Context context, ArrayList<String> membersKey, ArrayList<String> membersPosition){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.membersKey = membersKey;
        this.membersPosition = membersPosition;
        userDA = new UserDA();
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.viewholder_members,parent,false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, int position) {
        String pos = membersPosition.get(0);
        if(!pos.equals("Member")&&!pos.equals("Admin")&&!pos.equals("Manager")){
            holder.name.setText(membersPosition.get(position));
            holder.position.setText("");
        } else {
            userDA.getUserName(membersKey.get(position)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    holder.name.setText(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            holder.position.setText(membersPosition.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return membersPosition.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        private TextView name, position;
        public ListHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.vhMembers_name);
            position = itemView.findViewById(R.id.vhMembers_position);
        }
    }
}
