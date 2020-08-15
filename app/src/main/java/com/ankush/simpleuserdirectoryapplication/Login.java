package com.ankush.simpleuserdirectoryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button bt;
    EditText etuser,etpass;

    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bt = (Button)findViewById(R.id.btlogin);
        etpass = (EditText)findViewById(R.id.etlogpass);
        etuser = (EditText)findViewById(R.id.etlguser);


        mauth = FirebaseAuth.getInstance();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user = etuser.getText().toString();
                String pass = etpass.getText().toString();
                final ProgressDialog progressDialog = new ProgressDialog(Login.this);
                progressDialog.setCancelable(false);
                progressDialog.setTitle("Loading . . .");
                progressDialog.show();
                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(Login.this, "Please enter username and password", Toast.LENGTH_SHORT).show();

                } else {
                    mauth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                SignUpActivity.ed.putString("UserName",user);
                                SignUpActivity.ed.commit();
                                Intent i = new Intent(Login.this, MainActivity.class);
                                startActivity(i);
                                finish();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }
}
