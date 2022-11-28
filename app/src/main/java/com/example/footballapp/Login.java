package com.example.footballapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    Button btnlogin,btnreg;
    ImageButton imggoogle,imgfb,imgig,imgwatsapp;
    GoogleSignInOptions signin;
    GoogleSignInClient gclient;

    EditText email,paswd;
    String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logina);

        btnlogin=findViewById(R.id.btnlogin);
        btnreg=findViewById(R.id.btnreg);

        email=findViewById(R.id.email);
        paswd=findViewById(R.id.paswd);
        imggoogle=findViewById(R.id.imggoogle);
        imgfb=findViewById(R.id.imgfb);
        imgig=findViewById(R.id.imgig);
        imgwatsapp=findViewById(R.id.imgwatsapp);


        signin=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gclient=GoogleSignIn.getClient(this,signin);

        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){

            navigateToSecondActivity();
        }

        progressDialog=new ProgressDialog(this);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();

            }
        });

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Login.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        imggoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                googleSignIn();

            }
        });
        imgfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://en-gb.facebook.com/logi")));


            }
        });
        imgig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/accounts/login/")));


            }
        });
        imgwatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.whatsapp.com/")));


            }
        });

    }

    private void googleSignIn() {

        Intent signIntent=gclient.getSignInIntent();
        startActivityForResult(signIntent,1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1000){

            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);


            try {
                task.getResult(ApiException.class);

                    navigateToSecondActivity();

            } catch (ApiException e) {
                Toast.makeText(this, "Something went wrong ", Toast.LENGTH_SHORT).show();
            }


        }

    }

     void navigateToSecondActivity() {

        finish();
        Intent intent=new Intent(Login.this,SplashScreen3.class);
        startActivity(intent);

    }

    private void login() {


        String mail=email.getText().toString();
        String pass=paswd.getText().toString();

        if(!mail.matches(emailPattern)){

            email.setError("Enter email");
        }else if(pass.isEmpty()||pass.length()<6){


            paswd.setError("Enter valid password");


        }else {

            progressDialog.setMessage("Please Wait");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            auth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Intent i=new Intent(Login.this,SplashScreen3.class);
                        startActivity(i);

                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    }



                }
            });
        }



    }


}