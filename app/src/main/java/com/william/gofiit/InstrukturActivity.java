package com.william.gofiit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InstrukturActivity extends AppCompatActivity {
    private Button buttonLogout;
    private Button buttonPassword;
    private Button buttonProfile;
    private Button buttonIjin;
    private Button buttonPresensi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruktur);

        //Logout
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstrukturActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //ChanngePassword
        buttonPassword = findViewById(R.id.buttonPassword);
        buttonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstrukturActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        //Profile
        buttonProfile = findViewById(R.id.buttonProfile);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstrukturActivity.this, ProfileInstrukturActivity.class);
                startActivity(intent);
            }
        });

        //Ijin
        buttonIjin = findViewById(R.id.buttonIjin);
        buttonIjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstrukturActivity.this, IjinActivity.class);
                startActivity(intent);
            }
        });

        //Presensi
        buttonPresensi = findViewById(R.id.buttonPresensi);
        buttonPresensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstrukturActivity.this, PresensiKelasActivity.class);
                startActivity(intent);
            }
        });
    }
}