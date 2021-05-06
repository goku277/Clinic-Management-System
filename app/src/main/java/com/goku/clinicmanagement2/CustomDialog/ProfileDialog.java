package com.goku.clinicmanagement2.CustomDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatSpinner;

import com.goku.clinicmanagement2.R;

public class ProfileDialog extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {
    EditText profile_name, profile_specialization, profile_fees, profile_mobile;

    profile_Listener pl;

    Spinner spin;

    String[] doctorOccupation = { "Cardiologist", "Dentist", "Surgeon", "ENT Specialist", "Child Specialist","Urologist", "Veterinarian", "Optometrist",
            "Chiropractor","Dermatologist","Gynaecologist","Herbalist","Paramedic","Psychiatrist"};


    String selectedItemFromSpinner="";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder profileDialog= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.profiledialog,null);

        profileDialog.setView(view)
                .setTitle("Create profile")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String Name= profile_name.getText().toString().trim();
                        String Fees= profile_fees.getText().toString().trim();
                        String Mobile= profile_mobile.getText().toString().trim();
                        pl.applyProfileFields(Name,selectedItemFromSpinner,Mobile,Fees);
                    }
                });

        profile_name= (EditText) view.findViewById(R.id.profile_name_id);
        spin= (AppCompatSpinner) view.findViewById(R.id.profile_specialization_id);
        profile_mobile= (EditText) view.findViewById(R.id.profile_mobile_id);
        profile_fees= (EditText) view.findViewById(R.id.profile_fees_id);

        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, doctorOccupation);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        return profileDialog.create();

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            pl = (profile_Listener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " Must implement this Doctor_profile_Listener");
        };
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedItemFromSpinner= doctorOccupation[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface profile_Listener {
        public void applyProfileFields(String name1, String specialization1, String mobile1, String fees1);
    }
}
