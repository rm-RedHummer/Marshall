package com.jarvis.marshall.view.home.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.jarvis.marshall.R;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener
{
    private View view;
    private Button fullnameBtn, passwordBtn, emailBtn;
    private String fullname,password,email;
    private FirebaseAuth mAuth;

    public SettingsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {// Inflate the layout for this fragment

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
                    // if (!fullnameBtn.getText().toString().equals("CHANGE FULLNAME")) {
                    //fullnameEditText.setText(fullname);
                    @Override
                    public void onClick(View view) {
                        final EditText fullnameEditText = view2.findViewById(R.id.dgSettings_Fullname);
                        fullname = fullnameEditText.getText().toString();
                    }
                       /* if (fullname.equals("")) {
                            fullnameBtn.setText("ENTER DESCRIPTION");
                        } else {
                            String displayDescription = null;
                            if (fullname.length() > 25)
                                displayDescription = fullname.substring(0, 25);
                            else
                                displayDescription = fullname;
                            fullnameBtn.setText(displayDescription);
                        }
                        dialog.dismiss();*/
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
        /*if(!passwordBtn.getText().toString().equals("ENTER PASSWORD"))
        {
            //    passwordEditText.setText(description);
        }*/
        dialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        final EditText passwordEditText = view2.findViewById(R.id.dgPassword_settings);
                        password = passwordEditText.getText().toString();
                    }
                        /*if(description.equals(""))
                        {
                            descriptionBtn.setText("ENTER DESCRIPTION");
                        } else
                        {
                            String displayDescription = null;
                            if (description.length() > 25)
                                displayDescription = description.substring(0, 25);
                            else
                                displayDescription = description;
                            descriptionBtn.setText(displayDescription);
                        }
                        dialog.dismiss();*/
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
       /* if(!emailBtn.getText().toString().equals("ENTER EMAIL"))
        {
            //    emailEditText.setText(description);
        }*/
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
                        /*if(description.equals(""))
                        {
                            descriptionBtn.setText("ENTER DESCRIPTION");
                        } else
                        {
                            String displayDescription = null;
                            if (description.length() > 25)
                                displayDescription = description.substring(0, 25);
                            else
                                displayDescription = description;
                            descriptionBtn.setText(displayDescription);
                        }
                        dialog.dismiss();*/
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