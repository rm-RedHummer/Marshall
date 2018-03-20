package com.jarvis.marshall.view.home.settings;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.jarvis.marshall.MainActivity;
import com.jarvis.marshall.R;
/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener
{
    private View view;
    private Button fullnameBtn, passwordBtn, emailBtn;
    private String fullname,password, newPass, email;
    private FirebaseAuth mAuth;
    ProgressDialog pd;
    Task task;

    public SettingsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mAuth = FirebaseAuth.getInstance();
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        String tag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");

        fullnameBtn = view.findViewById(R.id.settings_changeNameBtn);
        passwordBtn = view.findViewById(R.id.settings_changePasswordBtn);
        emailBtn = view.findViewById(R.id.settings_changeEmailBtn);
        fullnameBtn.setOnClickListener(this);
        passwordBtn.setOnClickListener(this);
        emailBtn.setOnClickListener(this);

        fullnameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View mainView) {
                fullnameDialog();
            }
        });
        passwordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View mainView) {
                passwordDialog();
            }
        });
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View mainView) {
                emailDialog();
            }
        });
        return view;
    }
    public void fullnameDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final View view2 = inflater.inflate(R.layout.dialog_fullname_settings, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view2)
                .setTitle("Enter New Full Name")
                .setPositiveButton("Confirm", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final EditText fullnameEditText = view2.findViewById(R.id.dgSettings_Fullname);
                        fullname = fullnameEditText.getText().toString();
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(fullname).build();
                        firebaseUser.updateProfile(profileUpdates);
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    public void passwordDialog()
    {
        LayoutInflater inflater = getLayoutInflater();
        final View view2 = inflater.inflate(R.layout.dialog_password_settings,null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view2)
                .setTitle("Enter New Password")
                .setPositiveButton("Confirm", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            public void onShow(DialogInterface dialogInterface)
            {
                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        EditText passwordEditText = view2.findViewById(R.id.dgPassword_settings);
                        password = passwordEditText.getText().toString();
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if(firebaseUser != null) {
                            pd.setMessage("Changing Password. Please wait");
                            pd.show();
                            firebaseUser.updatePassword(passwordEditText.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                pd.dismiss();
                                                Toast.makeText(getActivity(), "Your password has been changed", Toast.LENGTH_LONG);
                                                mAuth.signOut();
                                                getActivity().finish();
                                                Intent i = new Intent(getActivity(), MainActivity.class);
                                                startActivity(i);
                                                dialog.dismiss();
                                            } else {
                                                pd.dismiss();
                                                Toast.makeText(getActivity(), "Your password cannot be changed", Toast.LENGTH_LONG);
                                                dialog.dismiss();
                                            }
                                        }
                                    });

                        }
                        else{
                            pd.dismiss();
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_LONG);
                        }
                    }
                });
            }
        });
        dialog.show();
    }
    public void emailDialog()
    {
        LayoutInflater inflater = getLayoutInflater();
        final View view2 = inflater.inflate(R.layout.dialog_email_settings,null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view2)
                .setTitle("Enter New Email")
                .setPositiveButton("Confirm", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        final EditText emailEditText = view2.findViewById(R.id.dgSettings_email);
                        email = emailEditText.getText().toString();
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if(firebaseUser != null) {
                            firebaseUser.updateEmail(email);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.show();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dgSettings_Fullname:
                fullnameDialog();
                break;
            case R.id.dgPassword_settings:
                passwordDialog();
                break;
            case R.id.dgSettings_email:
                emailDialog();
                break;
        }
    }
}