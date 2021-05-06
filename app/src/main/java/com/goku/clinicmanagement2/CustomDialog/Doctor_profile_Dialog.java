package com.goku.clinicmanagement2.CustomDialog;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.goku.clinicmanagement2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Doctor_profile_Dialog extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {

    private EditText name, mobile, fees;
    private FloatingActionButton add;
    private CircleImageView circleImageView;
    String specialization="";

    private Doctor_profile_Listener listener;

    Spinner spin;

    String[] doctorOccupation = { "Cardiologist", "Dentist", "Surgeon", "ENT Specialist", "Child Specialist","Urologist", "Veterinarian", "Optometrist",
            "Chiropractor","Dermatologist","Gynaecologist","Herbalist","Paramedic","Psychiatrist"};

    Uri imageUri;

    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int READ_EXTERNAL_STORAGE_PERMISSION = 1;
    public static final int IMAGE_PICK_CODE = 2;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // createDialog();
        return createDialog();
    }

    private Dialog createDialog() {
        AlertDialog.Builder profileDialog= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.doctor_profile_inputfiled,null);

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
                        String Name= name.getText().toString().trim();
                        // String Specialization= specialization.getText().toString().trim();
                        String Mobile= mobile.getText().toString().trim();
                        String Fees= fees.getText().toString().trim();
                        if (Mobile.length() > 10) {
                            if (!Mobile.startsWith("0") || !Mobile.startsWith("+91")) {
                                mobile.setError("Please enter a valid mobile number");
                            }
                        }
                        else {
                            listener.applyFields(Name, specialization, Mobile, Fees, imageUri);
                        }
                    }
                });

        name= (EditText) view.findViewById(R.id.name_inputfield_id);
        spin= (Spinner) view.findViewById(R.id.specialization_inputfield_id);

        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, doctorOccupation);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        //  specialization= (EditText) view.findViewById(R.id.specialization_inputfield_id);
        mobile= (EditText) view.findViewById(R.id.mobile_inputfield_id);
        fees= (EditText) view.findViewById(R.id.fee_inputfield_id);

        circleImageView= (CircleImageView) view.findViewById(R.id.circularimage_id);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder photo= new AlertDialog.Builder(getActivity());
                photo.setTitle("Use appropriate actions");
                photo.setMessage("Upload or Click your profile photo!\n\n");
                photo.setPositiveButton("Click photo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
                            askCameraPermission();
                        }
                    }
                });
                photo.setNeutralButton("Upload photo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION);
                            }
                            else {
                                pickFromGallery();
                            }
                        }
                        else {
                            pickFromGallery();
                        }
                    }
                });
                AlertDialog a1= photo.create();
                a1.show();
            }
        });
        return profileDialog.create();
    }


    private void openDialog() {
        DateAndTime dp= new DateAndTime();
        dp.show(getActivity().getSupportFragmentManager(),"Date And Time");
    }

    private int getIntegerVersion(String s) {
        String Months[]= {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        HashMap<String,Integer> mapMonthToNum= new HashMap<>();
        int i=1;
        for (String s1: Months) {
            mapMonthToNum.put(s1,i);
            i++;
        }
        if (s.equals("Sept")) return 9;
        else {
            return mapMonthToNum.get(s);
        }
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
        else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
                else {
                    Toast.makeText(getActivity(), "Permission required to click photo!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0) {
                if (grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                }
                else {
                    Toast.makeText(getActivity(), "Permission required to upload photo!", Toast.LENGTH_SHORT).show();

                    //  pickFromGallery();
                }
            }
        }
    }

    private void pickFromGallery() {
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CAMERA_REQUEST_CODE) {
            Bitmap image= (Bitmap) data.getExtras().get("data");
            //  circleImageView.setImageBitmap(image);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), image, "Title", null);
            imageUri= Uri.parse(path);
            circleImageView.setImageURI(imageUri);
        }
        if (requestCode== IMAGE_PICK_CODE) {
            try {
                circleImageView.setImageURI(data.getData());
                imageUri = data.getData();
                circleImageView.setImageURI(imageUri);
            } catch (Exception e){}
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (Doctor_profile_Listener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " Must implement this Doctor_profile_Listener");
        };
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Toast.makeText(getActivity(),doctorOccupation[position] , Toast.LENGTH_LONG).show();
        specialization= doctorOccupation[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface Doctor_profile_Listener {
        public void applyFields(String name1, String specialization1, String mobile1, String fees1, Uri imageUri);
    }
}
