package com.goku.clinicmanagement2.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.goku.clinicmanagement2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Postpone extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spin;

    ArrayList<String> spinnerAdapter= new ArrayList<>();

    ArrayList<String> timings= new ArrayList<>();

    String timings1="", day_of_week="", initialTiming="", concat="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postpone);

        spin= (Spinner) findViewById(R.id.spinner_postpone_id);

        Intent receiveSchedule= getIntent();
        String getTimings= receiveSchedule.getStringExtra("schedule");

        getTimings= getTimings.replace("[","");

        getTimings= getTimings.replace("]","");


        String split[]= getTimings.split(",");

        spinnerAdapter.add(0,"Select your schedule");

        for (String s: split) {
            s= s.replace("\\s+","");
            System.out.println("From Postpone s: " + s);
            spinnerAdapter.add(s);
        }

        System.out.println("split[0]: " + split[0]);

        System.out.println("split[1]: " + split[1]);

        System.out.println("From Postpone getTimings: " + getTimings);

        System.out.println("spinnerAdapter: " + spinnerAdapter);


        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item, spinnerAdapter);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(), ""+ spinnerAdapter.get(i), Toast.LENGTH_LONG).show();
        timings1= spinnerAdapter.get(i);

        AlertDialog.Builder showDateTime= new AlertDialog.Builder(Postpone.this);

        showDateTime.setTitle("Pick a date and a time");

        showDateTime.setMessage("Choose a re-schedule date and a time");

        showDateTime.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                show_Date_Time_Dialog();
            }
        });

        showDateTime.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog a1= showDateTime.create();
        a1.show();
    }

    private boolean show_Date_Time_Dialog() {
        final Calendar calendar= Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DATE,date);

                TimePickerDialog.OnTimeSetListener timeSetListener= new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hour);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat sd= new SimpleDateFormat("dd-mm-yyyy HH:mm");

                        String formattedDate= sd.format(calendar.getTime());

                        String splitter[]= formattedDate.split(" ");

                        String day= getEquivalentDay(calendar.get(Calendar.DAY_OF_WEEK));

                        day_of_week= day;

                        String accurateDate= String.valueOf(calendar.get(Calendar.DATE)) + "/" + String.valueOf((calendar.get(Calendar.MONTH)+1) + "/" + String.valueOf(calendar.get(Calendar.YEAR))) + " " +  splitter[1] + "  " + day;

                        initialTiming= formattedDate;

                        System.out.println("From Postpone: Selected Date and Time: " + accurateDate);

                        timings.add(accurateDate);

                        System.out.println("From Postpone: From 1st timepicker timings: " + timings);

                        String accDate[]= accurateDate.split("\\ +");

                        accurateDate="";
                        accurateDate+= accDate[2] + " " + accDate[1];

                        concat+= accurateDate;

                        System.out.println("From Postpone: concat: " + concat);
                    }
                };
                new TimePickerDialog(Postpone.this,timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(Postpone.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();

        return true;
    }

    private String getEquivalentDay(int i1) {
        String weekDays[]= {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        HashMap<Integer,String> mapEqivalentDay= new HashMap<>();
        int i=1;
        for (String s1: weekDays) {
            mapEqivalentDay.put(i,s1);
            i++;
        }
        return mapEqivalentDay.get(i1);
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}