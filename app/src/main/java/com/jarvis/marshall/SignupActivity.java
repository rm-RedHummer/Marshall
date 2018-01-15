package com.jarvis.marshall;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jarvis.marshall.dataAccess.UserDA;
import com.jarvis.marshall.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private FirebaseAuth mAuth;
    private UserDA userDA;

    @BindView(R.id.input_name) EditText nameText;
    @BindView(R.id.input_email) EditText emailText;
    @BindView(R.id.input_username) EditText usernameText;
    @BindView(R.id.input_password) EditText passwordText;
    @BindView(R.id.btn_signup) Button signupButton;
    @BindView(R.id.link_login) TextView loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        userDA = new UserDA();


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");
        boolean validation = validate();
        if (validation==false) {
            onSignupFailed();
            return;
        }
        Snackbar.make(findViewById(R.id.signup_scrollview), "Validation Successful", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            signupButton.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            final String name = nameText.getText().toString();
            final String username = usernameText.getText().toString();
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();



            // TODO: Implement your own signup logic here.
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setCancelable(true);


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                onSignupSuccess(name,username);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                builder1.setMessage("Failed ang paggawa ng account");
                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });


            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignupSuccess or onSignupFailed
                            // depending on success
                            // onSignupFailed();

                            progressDialog.dismiss();
                        }
                    }, 1000);

    }


    public void onSignupSuccess(String name,String username) {
        User user = new User(mAuth.getCurrentUser().getUid(),name,username);
        userDA.createNewUser(user);
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();

    }

    public void onSignupFailed() {
        //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
    }

    public boolean checkUsername(String username) {
        usernameText.setTag("false");
        userDA.checkUsername(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue() != null) {
                    usernameText.setError("Username is already taken");

                    /*User user = dataSnapshot.getValue(User.class);
                    if(usernameText.getText().toString().toLowerCase().equals(user.getUsername().toLowerCase())){
                        usernameText.setError("Username is already taken");
                        usernameText.setTag("false");
                    } else {
                        Toast.makeText(getBaseContext(), "pumasok sa else", Toast.LENGTH_LONG).show();
                        usernameText.setError(null);
                    }*/

                } else if(dataSnapshot.getValue() == null){
                    usernameText.setError(null);
                    usernameText.setTag("true");
                    Snackbar.make(findViewById(R.id.signup_scrollview), usernameText.getTag().toString(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Toast.makeText(getBaseContext(), usernameText.getTag().toString(), Toast.LENGTH_LONG).show();
        return false;
    }

    public boolean validate() {
        boolean valid = true;
        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String username = usernameText.getText().toString();

        //usernameText.setTag("true");

        if(username.isEmpty() || username.length() < 6 ) {
            usernameText.setError("at least 6 characters");
            valid = false;
        } else {
            //valid=checkUsername(username);
            usernameText.setError(null);
        }

        if (name.isEmpty() || name.length() < 6) {
            nameText.setError("at least 6 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            passwordText.setError("between 6 and 20 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}