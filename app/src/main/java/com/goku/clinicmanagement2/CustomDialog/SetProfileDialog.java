package com.goku.clinicmanagement2.CustomDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bumptech.glide.Glide;
import com.goku.clinicmanagement2.CPU.Time_1;
import com.goku.clinicmanagement2.Database.DoctorDb;
import com.goku.clinicmanagement2.Database.DoctorImagedb;
import com.goku.clinicmanagement2.DoctorModel.MemberDoctor;
import com.goku.clinicmanagement2.DoctorModel.SetProfileManager;
import com.goku.clinicmanagement2.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetProfileDialog extends AppCompatDialogFragment {

    TextView name, specialization, mobile, fees, schedule;
    CircleImageView cig;
    Uri imageUri;

    String fetchUri="";

    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseDatabase database, db2;
    DatabaseReference ref, childref;

    SetProfileManager pm;

    DoctorDb db;
    DoctorImagedb dimg;

    MemberDoctor md;
    Time_1 t1;

    String Specialization, Name, Mobile, Fees, Schedule, url;

    private SetProfileDialogListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        t1= new Time_1();
        db= new DoctorDb(getActivity());

        dimg= new DoctorImagedb(getActivity());

        pm= new SetProfileManager();

        try {

            SQLiteDatabase db1 = db.getReadableDatabase();
            String query = "select * from doctor";
            Cursor c1 = db1.rawQuery(query, null);

            if (c1 != null && c1.getCount() > 0) {
                if (c1.moveToFirst()) {
                    Specialization = c1.getString(1);
                    Name = c1.getString(0);
                    Mobile = c1.getString(2);
                    Fees = c1.getString(3);
                }
            }
        } catch (Exception e){}

        md= new MemberDoctor();

        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.set_profile_dialog, null);


        database= FirebaseDatabase.getInstance();

        ref= database.getInstance().getReference().child("Doctor"+"/"+ " " + Specialization + "/" + Name + " " + Mobile + " " + Fees);

        md = new MemberDoctor();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object obj = snapshot.getKey(), value = snapshot.getValue();
                String s1 = String.valueOf(value);
                System.out.println("obj is: " + obj);
                System.out.println("value is: " + value);

                int i = 0;

                String split[] = s1.split("accurateDetails=|[[{},]|\\ +]|null"), concat = "";

                for (String s : split) {
                    if (!s.isEmpty()) {
                        System.out.println("s: " + s + " i: " + i);
                        if (i > 0) {
                            concat += s + " ";
                        }
                        i++;
                    }
                }

                if (concat.contains("]")) {
                    concat = concat.replace("]", "");
                }

                concat = concat.trim();

                System.out.println("From setProfileDialog concat is: " + concat);

                String url1="";

                String str1[] = concat.split(" ");
                for (String s : str1) {
                    System.out.println("From setProfileDialog s: " + s);
                    if (s.contains("UrlImage=")) {
                        s= s.replace("UrlImage=", "");
                        url1= s;
                    }
                }

                System.out.println("From setProfileDialog url1: " + url1);

                SQLiteDatabase db1= db.getWritableDatabase();

                String query = "select * from doctor";
                Cursor c1 = db1.rawQuery(query, null);
                String spe="", nam="", URI1="";

                if (c1!=null && c1.getCount() > 0) {
                    if (c1.moveToFirst()) {
                        nam= c1.getString(0);
                        spe= c1.getString(1);
                    }
                }

                SQLiteDatabase db11= dimg.getWritableDatabase();

                String query1 = "select * from doctorimage";
                Cursor c11 = db11.rawQuery(query1, null);

                if (c11!=null && c11.getCount() > 0) {
                    if (c11.moveToFirst()) {
                        if (getActivity()!=null) {
                            Glide.with(getActivity()).load(String.valueOf(c11.getString(0))).centerCrop().into(cig);
                        }
                    }
                }

                else {


                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    StorageReference reference = storageReference.child("Doctor_Images" + "/" + " " + Specialization + "/" + Name);

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            System.out.println("DownloadUrl is: " + uri);
                            if (getActivity() != null) {
                                Glide.with(getActivity()).load(String.valueOf(uri)).centerCrop().into(cig);
                                dimg.insertData(String.valueOf(uri));
                                md.setUrlImage(String.valueOf(uri));
                            }
                        }
                    });
                }





                StringBuilder sb1 = new StringBuilder();

                System.out.println("str1: " + str1);


                Set<String> TimeSet = t1.init(str1);

                for (String s : TimeSet) {
                    if (!s.isEmpty()) {
                        sb1.append(s);
                        sb1.append("\n");
                    }
                }
                url= t1.getUrl();


                System.out.println("sb1.toString(): " + sb1.toString());

                schedule.setText(sb1.toString());
                name.setText(Name);
                mobile.setText(Mobile);
                fees.setText(Fees);
                specialization.setText(Specialization);

                System.out.println("From setProfileDialog t1.getUrl(): " + url);

                System.out.println();

                String sbToStr = sb1 + " ";

                System.out.println("From setProfileDialog: " + sbToStr);

                // System.out.println("From setProfileDialog url is: " + url);

                // if (url!=null) {

                //      Glide.with(getActivity()).load(url).centerCrop().into(cig);
                // }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        builder.setView(view)
                .setTitle("View Profile")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        name = (TextView) view.findViewById(R.id.name_placeholder_id);
        specialization = (TextView) view.findViewById(R.id.specialization_placeholder_id);
        mobile = (TextView) view.findViewById(R.id.mobile_placeholder_id);
        fees = (TextView) view.findViewById(R.id.fees_placeholder_id);
        schedule = (TextView) view.findViewById(R.id.schedule_placeholder_id);
        schedule.setMovementMethod(new ScrollingMovementMethod());
        cig = (CircleImageView) view.findViewById(R.id.profile_set_image_id);

        pm.setFees(fees.getText().toString());
        pm.setImageUrl(url);
        pm.setMobile(mobile.getText().toString());
        pm.setName(name.getText().toString());
        pm.setSchedule(schedule.getText().toString());
        pm.setSpecialization(specialization.getText().toString());



        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SetProfileDialogListener) context;
        } catch (Exception e){}
    }

    public interface SetProfileDialogListener {
        void applyTexts(String name, String specialization,  String mobile, String fees, String schedule, Uri imageUri);
    }
}
