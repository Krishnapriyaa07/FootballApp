package com.example.footballapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    Button btnreg;
     EditText email,paswd,name,mobile;
     String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
     ProgressDialog progressDialog;
     FirebaseAuth auth;
     FirebaseUser user;
     FirebaseFirestore firestore;
     String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnreg=findViewById(R.id.btnreg);

        email=findViewById(R.id.email);
        paswd=findViewById(R.id.pswd);
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.mobile);
        progressDialog=new ProgressDialog(this);
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        user=auth.getCurrentUser();
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                performAuthentication();

            }
        });


    }

    private void performAuthentication() {


        String mail=email.getText().toString();
        String pass=paswd.getText().toString();
        String username=name.getText().toString();
        String usermob=mobile.getText().toString();

        if(!mail.matches(emailPattern)){

            email.setError("Enter email");
        }else if(pass.isEmpty()||pass.length()<6){


            paswd.setError("Enter valid password");


        }else{

            progressDialog.setMessage("Please Wait");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            auth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        progressDialog.dismiss();


                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                        userID=auth.getCurrentUser().getUid();

                        DocumentReference documentReference=firestore.collection("users").document(userID);
                        Map<String,Object> user=new HashMap<>();
                        user.put("Name",username);
                        user.put("Mobile",usermob);
                        user.put("Email",mail);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Log.d(TAG,"On Success: user profile created"+userID);


                            }
                        });


                        Intent i=new Intent(RegisterActivity.this,SplashScreen3.class);
                        startActivity(i);


                    }else{

                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();



                    }



                }
            });



        }
    }
}