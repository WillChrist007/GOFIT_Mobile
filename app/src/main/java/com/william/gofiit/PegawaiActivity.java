package com.william.gofiit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PegawaiActivity extends AppCompatActivity {
    private Button buttonLogout;
    private Button buttonPassword;
    private Button buttonPresensi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pegawai);

        //Logout
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PegawaiActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Presensi
        buttonPresensi = findViewById(R.id.buttonPresensi);
        buttonPresensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PegawaiActivity.this, JadwalTodayActivity.class);
                startActivity(intent);
            }
        });

        //ChanngePassword
        buttonPassword = findViewById(R.id.buttonPassword);
        buttonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PegawaiActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}