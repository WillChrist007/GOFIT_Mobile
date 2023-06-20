package com.william.gofiit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MemberActivity extends AppCompatActivity {
    private Button buttonLogout;
    private Button buttonPassword;
    private Button buttonGym;
    private Button buttonKelas;
    private Button buttonProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        //Logout
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Profile
        buttonProfile = findViewById(R.id.buttonProfile);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberActivity.this, ProfileMemberActivity.class);
                startActivity(intent);
            }
        });

        //ChanngePassword
        buttonPassword = findViewById(R.id.buttonPassword);
        buttonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        //Gym
        buttonGym = findViewById(R.id.buttonGym);
        buttonGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberActivity.this, GymActivity.class);
                startActivity(intent);
            }
        });

        //Kelas
        buttonKelas = findViewById(R.id.buttonKelas);
        buttonKelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberActivity.this, KelasActivity.class);
                startActivity(intent);
            }
        });
    }
}