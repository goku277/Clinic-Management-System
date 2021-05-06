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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.goku.clinicmanagement2.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientProfileDialog extends AppCompatDialogFragment {

    CircleImageView cig;
    EditText name, age, mobile;
    RadioGroup rg;
    RadioButton male, female;

    String checkedItem="";

    Uri imageUri;

    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int READ_EXTERNAL_STORAGE_PERMISSION = 1;
    public static final int IMAGE_PICK_CODE = 2;


    private Patient_profile_Listener listener;

    FirebaseStorage fstorage;
    StorageReference ref;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return createDialog();
    }

    private Dialog createDialog() {
        AlertDialog.Builder profileDialog= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.patient_profile_inputfield,null);

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
                        String Age= age.getText().toString().trim();
                        if (male.isChecked()) {
                            checkedItem= "Male";
                        }
                        else if (female.isChecked()) {
                            checkedItem= "Female";
                        }
                        if (Mobile.length() > 10) {
                            if (!Mobile.startsWith("0") || !Mobile.startsWith("+91")) {
                                mobile.setError("Please enter a valid mobile number");
                            }
                        }
                        else {
                            listener.applyFields(Name, Mobile, Age, imageUri, checkedItem);
                        }
                    }
                });

        male= (RadioButton) view.findViewById(R.id.male_id);

        female= (RadioButton) view.findViewById(R.id.female_id);

        name= (EditText) view.findViewById(R.id.patient_profile_name_id);

        mobile= (EditText) view.findViewById(R.id.patient_profile_mobile_id);
        age= (EditText) view.findViewById(R.id.patient_profile_age_id);

        cig= (CircleImageView) view.findViewById(R.id.patient_profile_pic_id);

        cig.setOnClickListener(new View.OnClickListener() {
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
            if (data!=null) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                //  circleImageView.setImageBitmap(image);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), image, "Title", null);
                imageUri = Uri.parse(path);
                cig.setImageURI(imageUri);
            }
        }
        if (requestCode== IMAGE_PICK_CODE) {
            try {
                cig.setImageURI(data.getData());
                imageUri = data.getData();
                cig.setImageURI(imageUri);
            } catch (Exception e){}
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (Patient_profile_Listener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " Must implement this Doctor_profile_Listener");
        };
    }

    public interface Patient_profile_Listener {
        public void applyFields(String name1, String age, String mobile1, Uri imageUri, String checkedItem);
    }
}