package com.goku.clinicmanagement2.CustomDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.goku.clinicmanagement2.R;

public class DateAndTime extends AppCompatDialogFragment {
    private EditText date, time;
    private Date_And_Time_Interface listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dateAndTimeDialog= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.date_and_time_layout,null);

        dateAndTimeDialog.setView(view)
                .setTitle("Set Date and Time")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String Date1= date.getText().toString().trim();
                        String Time1= time.getText().toString().trim();

                        listener.applyFields(Date1,Time1);

                    }
                });

        date= (EditText) view.findViewById(R.id.date_id);
        time= (EditText) view.findViewById(R.id.time_id);

        return dateAndTimeDialog.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (Date_And_Time_Interface) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " Must implement this Date_And_Time_Interface");
        };
    }

    public interface Date_And_Time_Interface {
        public void applyFields(String Date1, String Time1);
    }
}
