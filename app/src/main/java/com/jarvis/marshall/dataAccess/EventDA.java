package com.jarvis.marshall.dataAccess;

import com.jarvis.marshall.model.Event;

/**
 * Created by Jarvis on 03/02/2018.
 */

public class EventDA extends  DA{
    private final String node = "event";
    public void createNewEvent(Event event){
        rootRef.child(node).child(event.getKey()).setValue(event);

    }
}
