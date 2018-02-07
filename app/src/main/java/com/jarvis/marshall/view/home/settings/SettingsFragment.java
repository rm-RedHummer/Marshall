package com.jarvis.marshall.view.home.settings;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jarvis.marshall.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener{
    private View view;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        // 1. Wiring
        // nameBtn = view.findViewById(R.id.settings_nameBtn);
        // nameBtn.setOnClickListener(this);

        return view;
    }

    /*public void showDescriptionDialog(){
        LayoutInflater inflater = getLayoutInflater();
        final View view2 = inflater.inflate(R.layout.DIALOG_FULLNAME_SETTINGS,null); // MAY PAPALITAN DITO
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view2)
                .setTitle("Enter event description") //PALIT DITO -> Enter new full name
                .setPositiveButton("Confirm", null) //Set to null. We override the onclick
                .setNegativeButton("Cancel", null)
                .create();
        final EditText descriptionEditText = view2.findViewById(R.id.dgCreateEvent_description); // MAY PAPALITAN KA DITO
        //if(!descriptionBtn.getText().toString().equals("ENTER DESCRIPTION")){
        //    descriptionEditText.setText(description);
        //}

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    // COMMENT MO MUNA TO
                        description = descriptionEditText.getText().toString();
                        if(description.equals("")){
                            descriptionBtn.setText("ENTER DESCRIPTION");
                        } else {
                            String displayDescription = null;
                            if (description.length() > 25)
                                displayDescription = description.substring(0, 25);
                            else
                                displayDescription = description;
                            descriptionBtn.setText(displayDescription);
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }*/


    @Override
    public void onClick(View view) {


    }
    /*@Override SAMPLE ONCLICK
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.actCreateEvent_descriptionBtn:
                showDescriptionDialog();
                break;
            case R.id.actCreateEvent_dateBtn:
                showDialog(0);
                break;
            case R.id.actCreateEvent_startTimeBtn:
                showDialog(1);
                break;
            case R.id.actCreateEvent_endTimeBtn:
                showDialog(2);
                break;
            case R.id.actCreateEvent_venueBtn:
                showVenueDialog();
                break;
            case R.id.actCreateEvent_membersBtn:
                break;
            case R.id.actCreateEvent_cancelBtn:
                back();
                break;
            case R.id.actCreateEvent_saveBtn:
                createEvent();
                break;
        }
    }*/
}
