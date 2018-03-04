package com.jarvis.marshall.view.home.comment;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.CommentDA;
import com.jarvis.marshall.model.Comment;

import java.util.ArrayList;

/**
 * Created by Jarvis on 04/03/2018.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ListHolder> {
    private Context context;
    private ArrayList<Comment> commentArrayList;
    private LayoutInflater inflater;
    private CommentDA commentDA;

    public CommentsAdapter(Context context, ArrayList<Comment> commentArrayList){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.commentArrayList = commentArrayList;
        commentDA = new CommentDA();

    }


    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.viewholder_comments,parent,false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        Comment comment = commentArrayList.get(position);
        holder.comment.setText(comment.getComment());
        holder.name.setText(comment.getName());
        holder.date.setText(processDate(comment.getDate()));
        holder.time.setText(" "+comment.getTime());
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        private TextView name, date, time, comment;
        public ListHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.vh_comments_userName);
            date = itemView.findViewById(R.id.vh_comments_date);
            time = itemView.findViewById(R.id.vh_comments_time);
            comment = itemView.findViewById(R.id.vh_comments_comment);
        }
    }
    private String processTime(String time){
        String newTime="";
        String[] timeArray = time.split(":");

        return newTime;
    }

    private String processDate(String date){
        String newDate="";
        String[] splitDate = date.split("/");
        String[] month = {"Jan","Feb","March","April","May","June","July","Aug","Sept","Oct","Nov","Dec"};
        for(int i = 0; i <12; i++){
            if(splitDate[0].equals(String.valueOf(i)))
                newDate = month[i]+" "+splitDate[1]+", "+"20"+splitDate[2].substring(splitDate[2].length()-2);
        }
        return newDate;
    }
}
