package com.jarvis.marshall.dataAccess;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Jarvis on 03/01/2018.
 */

public class CurrentUserDA {
    private FirebaseAuth mAuth;
    public CurrentUserDA(){
        mAuth = FirebaseAuth.getInstance();
    }
    public String getUserId(){
        return mAuth.getCurrentUser().getUid();
    }
}
