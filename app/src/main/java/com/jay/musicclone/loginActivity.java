package com.jay.musicclone;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {
    EditText uname,pws;
    Button btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uname = findViewById(R.id.username);
        pws = findViewById(R.id.password);
        btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uname.getText().toString().equals("admin") && pws.getText().toString().equals("admin")) {
                    //Correct Password
                    Toast.makeText(loginActivity.this, "LOGIN SUCCESSFULLY", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(loginActivity.this,MainActivity.class));
                } else
                    //Incorrect
                    Toast.makeText(loginActivity.this, "LOGIN Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }
}