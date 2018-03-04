package com.jarvis.marshall.dataAccess;

import com.google.firebase.database.Query;
import com.jarvis.marshall.model.Comment;

/**
 * Created by Jarvis on 04/03/2018.
 */

public class CommentDA extends DA{
    final String node = "comment";
    public void createComment(Comment comment){
        rootRef.child(node).child(comment.getKey()).setValue(comment);
    }
    public Query getAllComments(String eventKey){
        return rootRef.child(node).orderByChild("eventKey").equalTo(eventKey);
    }
}
