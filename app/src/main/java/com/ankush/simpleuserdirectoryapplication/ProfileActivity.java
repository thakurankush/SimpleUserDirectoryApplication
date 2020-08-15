package com.ankush.simpleuserdirectoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ProfileActivity extends AppCompatActivity {

    ImageView iv;
    TextView tvname,tvage,tvphone,tvmail,tvedu,tvadr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent i = getIntent();
        int index = Integer.parseInt(i.getStringExtra("Key1"));
        iv =(ImageView)findViewById(R.id.imageView);
        tvname = (TextView)findViewById(R.id.tvname);
        tvage = (TextView)findViewById(R.id.tvage);
        tvphone = (TextView)findViewById(R.id.tvphone);
        tvmail = (TextView)findViewById(R.id.tvmail);
        tvedu = (TextView)findViewById(R.id.tvedu);
        tvadr = (TextView)findViewById(R.id.tvadr);

        Glide.with(this).load(MainActivity.data.get(index).getImageurl()).into(iv);
        tvname.setText(MainActivity.data.get(index).getName());
        tvage.setText(MainActivity.data.get(index).getAge());
        tvphone.setText(MainActivity.data.get(index).getPhone());
        tvmail.setText(MainActivity.data.get(index).getMail());
        tvedu.setText(MainActivity.data.get(index).getEdu());
        tvadr.setText(MainActivity.data.get(index).getAddress());
    }
}
