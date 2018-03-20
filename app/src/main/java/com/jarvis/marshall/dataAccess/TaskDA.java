package com.jarvis.marshall.dataAccess;

import com.google.firebase.database.Query;
import com.jarvis.marshall.model.Task;

import java.util.HashMap;
import java.util.Map;

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
    public void setTaskMembers(String taskKey, Map map){
        rootRef.child(node).child(taskKey).child("taskMembers").setValue(map);
    }
    public Query getEventTasks(String eventKey){
        return rootRef.child(node).orderByChild("eventKey").equalTo(eventKey);
    }
    public void setStatus(String taskKey, String status){
        rootRef.child(node).child(taskKey).child("status").setValue(status);
    }

    public void deleteTask(String taskKey){
        rootRef.child(node).child(taskKey).removeValue();
    }

    public Query getSpecificTask(String taskKey){
        return rootRef.child(node).child(taskKey);
    }
}
