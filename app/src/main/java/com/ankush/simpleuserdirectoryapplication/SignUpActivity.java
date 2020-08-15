package com.ankush.simpleuserdirectoryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    Button sbmt,lgin;
    EditText etname,etage,etpass,etcnpass,etadrs,etphone,etmail;
    Spinner education;
    ImageButton image;
    String name,pass,age,phone,mail,address,cnpass,edu;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    StorageReference storageReference;
    DatabaseReference mDatabase;
    FirebaseAuth auth;
    static SharedPreferences sp;
    static SharedPreferences.Editor ed;

    // paths for storage and databse on firebase
    public static final String St_path = "uploads/";
    public static final String Db_path = "uploads";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sbmt = (Button)findViewById(R.id.btnsbt);
        lgin = (Button)findViewById(R.id.btnlogin);
        image = (ImageButton)findViewById(R.id.imageButton);
        etname = (EditText)findViewById(R.id.etname);
        etage = (EditText)findViewById(R.id.etage);
        etadrs = (EditText)findViewById(R.id.etaddr);
        etphone = (EditText)findViewById(R.id.etnum);
        etpass = (EditText)findViewById(R.id.etpass);
        etcnpass = (EditText)findViewById(R.id.etcnpass);
        etmail = (EditText)findViewById(R.id.etmail);
        education = (Spinner)findViewById(R.id.edu);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Db_path);
        auth = FirebaseAuth.getInstance();

        sp = getPreferences(MODE_PRIVATE);      //single preference file initialized
        ed = sp.edit();

        if(SignUpActivity.sp.contains("UserName"))
        {
            //if registered bypassing the activity
            Intent i = new Intent(SignUpActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }

        lgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to go to the login activity
                Intent i = new Intent(SignUpActivity.this,Login.class);
                startActivity(i);
                finish();

            }
        });



        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //to open a chooser for selecting image
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

            }
        });

        sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etname.getText().toString();
                pass = etpass.getText().toString();
                age = etage.getText().toString();
                phone = etphone.getText().toString();
                mail = etmail.getText().toString();
                address = etadrs.getText().toString();
                cnpass = etcnpass.getText().toString();
                edu = education.getSelectedItem().toString();
                // checking if the data entered
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(age) ||TextUtils.isEmpty(phone) || TextUtils.isEmpty(mail) || TextUtils.isEmpty(address) || TextUtils.isEmpty(cnpass))
                {
                    Toast.makeText(SignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    // validating data
                    if(mail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
                    {
                        if(phone.length()<10)
                        {
                            etphone.setError("Invalid mobile number");
                        }
                        else {
                            Pattern pattern;
                            Matcher matcher;
                            final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
                            pattern = Pattern.compile(PASSWORD_PATTERN);
                            matcher = pattern.matcher(pass);
                            if (matcher.matches() &&pass.length()>=8) {
                                if (pass.equals(cnpass)) {
                                    if (imageUri == null) {
                                        Toast.makeText(SignUpActivity.this, "Please choose an image file by clicking on Image button", Toast.LENGTH_LONG).show();
                                    } else {
                                        uploadData();
                                    }

                                } else {
                                    etcnpass.setError("Password didn't match");
                                }

                            }
                            else
                            {
                                etpass.setError("Please choose a strong password");
                            }
                        }
                    }
                    else
                    {
                        etmail.setError("Invalid email");
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    //function to deal with the data upload to the firebase
    private void uploadData() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Uploading the data . . .");
        progressDialog.show();


        auth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    final StorageReference sRef = storageReference.child(St_path + System.currentTimeMillis() + "." + getFileExtension(imageUri));

                    sRef.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();


                                    //sending image to the storage or data to firbase database
                                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            uploadData upload = new uploadData(name,String.valueOf(uri),pass,age,phone,mail,address,edu);
                                            String uploadId = auth.getCurrentUser().getUid();
                                            mDatabase.child(uploadId).setValue(upload);
                                            ed.putString("UserName",mail);
                                            ed.commit();
                                            Toast.makeText(getApplicationContext(), "Registered Successfully ", Toast.LENGTH_LONG).show();
                                            Intent i = new Intent(SignUpActivity.this,MainActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                }
                            });
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        }


        //to get the extention of the file
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


}
