package com.jarvis.marshall.view.home.comment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jarvis.marshall.R;
import com.jarvis.marshall.dataAccess.CommentDA;
import com.jarvis.marshall.dataAccess.TaskDA;
import com.jarvis.marshall.model.Comment;
import com.jarvis.marshall.model.Task;
import com.jarvis.marshall.view.home.task.TaskAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsFragment extends Fragment {
    private View view;
    private String eventKey;
    private EditText commentEditText;
    private ImageButton send;
    private RecyclerView recyclerView;

    public CommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comments, container, false);
        Bundle bundle = getArguments();
        if(bundle!=null){
            eventKey = bundle.getString("eventKey");
        }

        recyclerView = view.findViewById(R.id.fragComment_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        loadComments();


        commentEditText = view.findViewById(R.id.fragComment_comment);
        send = view.findViewById(R.id.fragComment_sendButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!commentEditText.getText().toString().equals("")){ // if it is not empty
                    final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().getRoot().child("comment");
                    final String key = rootRef.push().getKey();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    Date calendar = Calendar.getInstance().getTime();
                    String date = String.valueOf(calendar.getMonth())+"/"+String.valueOf(calendar.getDate())+"/"+String.valueOf(calendar.getYear());
                    String time = String.valueOf(calendar.getHours())+":"+String.valueOf(calendar.getMinutes());
                    Comment comment = new Comment(key,eventKey,mAuth.getCurrentUser().getDisplayName(),commentEditText.getText().toString(),date,time);
                    CommentDA commentDA = new CommentDA();
                    commentDA.createComment(comment);
                    commentEditText.setText("");
                }
            }
        });

        return view;
    }

    private void loadComments(){
        final ArrayList<Comment> commentArrayList = new ArrayList<>();
        final CommentsAdapter adapter = new CommentsAdapter(getContext(),commentArrayList);
        recyclerView.setAdapter(adapter);
        CommentDA commentDA = new CommentDA();
        commentDA.getAllComments(eventKey).addChildEventListener(new ChildEventListener() {
            int ctr = 0;
            String comment, date, eventKey, key, name, time;
            @Override
            public void onChildAdded(DataSnapshot ds, String s) {
                for(DataSnapshot dataSnapshot: ds.getChildren()){
                    switch (ctr){
                        case(0):
                            comment = dataSnapshot.getValue().toString();
                            break;
                        case(1):
                            date = dataSnapshot.getValue().toString();
                            break;
                        case(2):
                            eventKey = dataSnapshot.getValue().toString();
                            break;
                        case(3):
                            key = dataSnapshot.getValue().toString();
                            break;
                        case(4):
                            name = dataSnapshot.getValue().toString();
                            break;
                        case(5):
                            time = dataSnapshot.getValue().toString();
                            Comment commentObj = new Comment(key,eventKey,name,comment,date,time);
                            commentArrayList.add(commentObj);
                            adapter.notifyItemInserted(commentArrayList.size()-1);
                            break;
                    }
                    if(ctr ==5)
                        ctr = 0;
                    else
                        ctr++;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
