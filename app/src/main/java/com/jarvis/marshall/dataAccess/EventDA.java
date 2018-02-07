package com.jarvis.marshall.dataAccess;

import com.google.firebase.database.Query;
import com.jarvis.marshall.model.Event;

/**
 * Created by Jarvis on 03/02/2018.
 */

public class EventDA extends  DA{
    private final String node = "event";
    public void createNewEvent(Event event){
        rootRef.child(node).child(event.getKey()).setValue(event);

    }

    public Query getAllEvents(String groupKey){
        return rootRef.child(node).orderByChild("groupKey").equalTo(groupKey);
    }

    public Query getSpecificEvent(String eventKey){
        return rootRef.child(node).child(eventKey);
    }

    public void addEventMember(String eventKey, String userKey, String position){
        rootRef.child(node).child(eventKey).child("eventMembers").child(userKey).setValue(position);
    }


}
