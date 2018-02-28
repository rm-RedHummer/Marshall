package com.jarvis.marshall.dataAccess;

import com.google.firebase.database.Query;
import com.jarvis.marshall.model.Task;

/**
 * Created by Jarvis on 25/02/2018.
 */

public class TaskDA extends DA {
    private final String node = "task";
    public void createNewTask(Task task){
        rootRef.child(node).child(task.getKey()).setValue(task);
    }
    public void addTaskMember(String taskKey, String userKey, String name){
        rootRef.child(node).child(taskKey).child("taskMembers").child(userKey).setValue(name);
    }
    public Query getEventTasks(String eventKey){
        return rootRef.child(node).orderByChild("eventKey").equalTo(eventKey);
    }
}
