package com.william.gofiit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private String loginUrl = "https://200710569.gofit.backend.given.website/api/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, loginUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    String message = jsonObject.getString("message");

                                    if (success) {
                                        // Menyimpan bearer token ke SharedPreferences
                                        String accessToken = jsonObject.getString("access_token");
                                        saveAccessToken(accessToken);

                                        // Mendapatkan data user
                                        JSONObject user = jsonObject.getJSONObject("user");
                                        int idPegawai = user.isNull("id_pegawai") ? 0 : user.getInt("id_pegawai");
                                        int idInstruktur = user.isNull("id_instruktur") ? 0 : user.getInt("id_instruktur");
                                        int idMember = user.isNull("id_member") ? 0 : user.getInt("id_member");

                                        // Menyimpan id pengguna ke SharedPreferences
                                        int userId = user.getInt("id");
                                        saveUserId(userId);

                                        // Memilih aktivitas berdasarkan peran user
                                        if (idPegawai != 0) {
                                            // Jika id_pegawai tidak 0, arahkan ke PegawaiActivity
                                            Intent intent = new Intent(LoginActivity.this, PegawaiActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else if (idInstruktur != 0) {
                                            // Menyimpan id instruktur ke SharedPreferences
                                            int instrukturId = user.getInt("id_instruktur");
                                            saveInstrukturId(instrukturId);

                                            // Jika id_instruktur tidak 0, arahkan ke InstrukturActivity
                                            Intent intent = new Intent(LoginActivity.this, InstrukturActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else if (idMember != 0) {
                                            // Menyimpan id member ke SharedPreferences
                                            int memberId = user.getInt("id_member");
                                            saveMemberId(memberId);

                                            // Jika id_member tidak 0, arahkan ke MemberActivity
                                            Intent intent = new Intent(LoginActivity.this, MemberActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // Tampilkan pesan login gagal
                                            Toast.makeText(LoginActivity.this, "Tidak ada role yang sesuai", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        // Tampilkan pesan login gagal
                                        Toast.makeText(LoginActivity.this, "Email atau Password Salah", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                // Tangani kesalahan saat permintaan login
                                // Misalnya, tampilkan pesan kesalahan yang sesuai
                                Toast.makeText(LoginActivity.this, "Terjadi kesalahan pada server", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("password", password);
                        return params;
                    }
                };

                Volley.newRequestQueue(LoginActivity.this).add(stringRequest);
            }
        });
    }

    private void saveAccessToken(String accessToken) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accessToken", accessToken);
        editor.apply();
    }

    private String getAccessToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("accessToken", "");
    }

    private void saveUserId(int userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userId);
        editor.apply();
    }

    private int getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", 0);
    }

    private void saveMemberId(int memberId) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("memberId", memberId);
        editor.apply();
    }

    private int getMemberId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("memberId", 0);
    }

    private void saveInstrukturId(int instrukturId) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("instrukturId", instrukturId);
        editor.apply();
    }

    private int getInstrukturId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("instrukturId", 0);
    }
}