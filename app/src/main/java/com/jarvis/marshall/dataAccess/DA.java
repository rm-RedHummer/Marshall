package com.jarvis.marshall.dataAccess;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Jarvis on 03/01/2018.
 */

public class DA {
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().getRoot();
}
