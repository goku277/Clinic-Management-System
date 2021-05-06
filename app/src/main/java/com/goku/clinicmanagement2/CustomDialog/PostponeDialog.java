package com.goku.clinicmanagement2.CustomDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.goku.clinicmanagement2.Database.Rescheduledb;
import com.goku.clinicmanagement2.R;

import java.util.ArrayList;

public class PostponeDialog extends DialogFragment implements AdapterView.OnItemSelectedListener{

    Spinner spin;

    ArrayList<String> doctor_reschedule;

    PostponeListener listener;

    String specialization="";

    Rescheduledb rdb;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder profileDialog= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.reschedule,null);

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
                        Toast.makeText(getActivity(), "Specialization is: " + specialization, Toast.LENGTH_SHORT).show();
                        listener.applyScheduleFields(specialization);
                    }
                });

        spin= (Spinner) view.findViewById(R.id.reschedule_spinner_id);

        spin.setOnItemSelectedListener(this);

        rdb= new Rescheduledb(getActivity());

        SQLiteDatabase db= rdb.getWritableDatabase();

        String query = "select * from reschedule";
        Cursor c1 = db.rawQuery(query, null);

        int i=0;

        doctor_reschedule= new ArrayList<>();

        if (c1!=null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                do {
                    if (!c1.getString(0).trim().isEmpty()) {
                        if (!doctor_reschedule.contains(c1.getString(0))) {
                            doctor_reschedule.add(c1.getString(0));
                        }
                    }
                } while (c1.moveToNext());
            }
        }

        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, doctor_reschedule);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);



        return profileDialog.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (PostponeListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " Must implement this Doctor_profile_Listener");
        };
    }

    public interface PostponeListener {
        public void applyScheduleFields(String schedule);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(getActivity(), doctor_reschedule.get(position) , Toast.LENGTH_LONG).show();
        specialization= doctor_reschedule.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}