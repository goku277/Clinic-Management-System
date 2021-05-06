package com.goku.clinicmanagement2.Credentials;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goku.clinicmanagement2.Admin.Admin;
import com.goku.clinicmanagement2.Doctor.Doctor_profile;
import com.goku.clinicmanagement2.Patient.Patient_Profile;
import com.goku.clinicmanagement2.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    EditText email, password;
    Button signIn;
    TextView goToSignUp;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth, mAuth;

    ImageView google, phone;

    GoogleSignInClient mGoogleSignInClient;

    UserStatus us;

    private final static int RC_SIGN_IN= 123;

    TextView forgetPassword;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user= firebaseAuth.getCurrentUser();

        if (user!=null) {
            SQLiteDatabase db = us.getReadableDatabase();
            String query = "select * from user";
            Cursor c1 = db.rawQuery(query, null);
            if (c1!=null && c1.getCount() > 0) {
                if (c1.moveToFirst()) {
                    String extractInfo= c1.getString(0);
                    if (extractInfo.contains("Doctor!")) {
                        startActivity(new Intent(getApplicationContext(), Doctor_profile.class));
                    }
                    else if (extractInfo.contains("Patient!")) {
                        startActivity(new Intent(getApplicationContext(), Patient_Profile.class));
                    }
                }
            }
            else {
                if (c1==null && c1.getCount() == 0) {
                    startActivity(new Intent(getApplicationContext(), SplashForAll.class));
                }
            }
            // startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        us= new UserStatus(SignIn.this);

        forgetPassword= (TextView) findViewById(R.id.forget_password_id);

        forgetPassword.setOnClickListener(this);

        createRequest();

        progressDialog= new ProgressDialog(this);

        firebaseAuth= FirebaseAuth.getInstance();

        mAuth= FirebaseAuth.getInstance();

        email= (EditText) findViewById(R.id.email_id);
        password= (EditText) findViewById(R.id.password_id);
        signIn= (Button) findViewById(R.id.signin_btn_id);
        goToSignUp= (TextView) findViewById(R.id.create_new_one_id);

        google= (ImageView) findViewById(R.id.google_image_icon_id);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        signIn.setOnClickListener(this);
        goToSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin_btn_id:
                Login();
                break;
            case R.id.create_new_one_id:
                startActivity(new Intent(this, SignUp.class));
                break;
            case R.id.forget_password_id:
                reset();
                break;
        }
    }

    private void reset() {
        AlertDialog.Builder a11= new AlertDialog.Builder(SignIn.this);
        a11.setTitle("Reset password");
        a11.setMessage("Are you sure to reset password");
        a11.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder a10= new AlertDialog.Builder(SignIn.this);
                a10.setTitle("Provide your email id");
                a10.setCancelable(false);
                a10.setMessage("Please provide your email id to receive password reset link");
                a10.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog a101= a10.create();
                a101.show();
            }
        });

        a11.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog a1= a11.create();
        a1.show();

        String Email= email.getText().toString().trim();
        if (TextUtils.isEmpty(Email) || !isValidEmail(Email)) {
            email.setError("Please enter emailid to receive reset link");
        }
        else {
            firebaseAuth.sendPasswordResetEmail(Email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(SignIn.this, "Password reset link is sent to your emailid", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignIn.this, "Error sending reset link due to: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    private void createRequest() {
        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("451638630559-h5mvtnthac05hkb0s08n73goo33gm6m1.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
    }

    private void signIn() {
        Intent signInIntent= mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account= task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {}
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                        //    FirebaseUser user= firebaseAuth.getCurrentUser();
                         //   startActivity(new Intent(getApplicationContext(), SplashForAll.class));

                            FirebaseUser user= firebaseAuth.getCurrentUser();

                            if (user!=null) {
                                SQLiteDatabase db = us.getReadableDatabase();
                                String query = "select * from user";
                                Cursor c1 = db.rawQuery(query, null);
                                if (c1 != null && c1.getCount() > 0) {
                                    if (c1.moveToFirst()) {
                                        String extractInfo = c1.getString(0);
                                        if (extractInfo.contains("Doctor!")) {
                                            startActivity(new Intent(getApplicationContext(), Doctor_profile.class));
                                        } else if (extractInfo.contains("Patient!")) {
                                            startActivity(new Intent(getApplicationContext(), Patient_Profile.class));
                                        }
                                    }
                                } else {
                                    if (c1 == null && c1.getCount() == 0) {
                                        startActivity(new Intent(getApplicationContext(), SplashForAll.class));
                                    }
                                }
                            }
                        }
                        else {
                            Toast.makeText(SignIn.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void Login() {
        String getEmail= email.getText().toString().trim();
        String getPassword= password.getText().toString().trim();

        if (TextUtils.isEmpty(getEmail)) {
            email.setError("Please input your Email id!");
            return;
        }
        if (TextUtils.isEmpty(getPassword)) {
            password.setError("Please input your Password!");
            return;
        }
        if (!isValidEmail(getEmail)) {
            email.setError("Please input valid email!");
            return;
        }
        if (getPassword.length()< 6) {
            password.setError("Password length must be > 5");
        }
        if (getEmail.equals("pogo.aman1234@gmail.com") && getPassword.equals("Im@12345678")) {
            startActivity(new Intent(SignIn.this, Admin.class));
            finish();
        }
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth.signInWithEmailAndPassword(getEmail,getPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignIn.this, "SignIn Successfull!", Toast.LENGTH_SHORT).show();
                    //    Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                    //    startActivity(intent);
                    //    finish();


                    SQLiteDatabase db = us.getWritableDatabase();
                    String query = "select * from user";
                    Cursor c1 = db.rawQuery(query, null);
                    if (c1!=null && c1.getCount() > 0) {
                        if (c1.moveToFirst()) {
                            String extractInfo= c1.getString(0);
                            System.out.println("From the SignIn class extractInfo: " + extractInfo);
                            if (extractInfo.contains("Doctor!")) {
                                startActivity(new Intent(getApplicationContext(), Doctor_profile.class));
                                finish();
                            }
                            else if (extractInfo.contains("Patient!")) {
                                startActivity(new Intent(getApplicationContext(), Patient_Profile.class));
                                finish();
                            }
                        }
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), SplashForAll.class));
                    }
                }
                else {
                    Toast.makeText(SignIn.this, "SignIn Failed!", Toast.LENGTH_SHORT).show();
                    System.out.println("Task failed due to: " + task.getException());
                }
                progressDialog.dismiss();
            }
        });
    }

    private boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

}