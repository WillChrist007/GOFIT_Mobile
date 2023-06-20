package com.william.gofiit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileInstrukturActivity extends AppCompatActivity {
    private EditText emailEditText, nameEditText, birthDateEditText, phoneEditText, addressEditText, idEditText, jumlahTerlambatEditText;
    private int userId;
    private int instrukturId;
    private RequestQueue requestQueue;
    private static final String USER_URL = "https://200710569.gofit.backend.given.website/api/user/";
    private static final String MEMBER_URL = "https://200710569.gofit.backend.given.website/api/instruktur/";

    private Button buttonHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_instruktur);

        // Inisialisasi EditText
        emailEditText = findViewById(R.id.emailEditText);
        nameEditText = findViewById(R.id.nameEditText);
        birthDateEditText = findViewById(R.id.birthDateEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);

        idEditText = findViewById(R.id.idEditText);
        jumlahTerlambatEditText = findViewById(R.id.jumlahTerlambatEditText);

        // Mendapatkan ID pengguna dari SharedPreferences
        userId = getUserId();
        instrukturId = getInstrukturId();

        // Inisialisasi RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Memanggil method untuk mendapatkan data pengguna
        getUserData();
        getInstrukturData();

        //History
        buttonHistory = findViewById(R.id.buttonHistory);
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileInstrukturActivity.this, HistoryInstrukturActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getUserData() {
        String url = USER_URL + userId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSON Response", response.toString());
                        try {
                            JSONObject userData = response.getJSONObject("data");
                            String email = userData.optString("email", "");
                            String name = userData.optString("nama", "");
                            String birthDate = userData.optString("tanggal_lahir", "");
                            String phone = userData.optString("telepon", "");
                            String address = userData.optString("alamat", "");

                            emailEditText.setText(email);
                            nameEditText.setText(name);
                            birthDateEditText.setText(birthDate);
                            phoneEditText.setText(phone);
                            addressEditText.setText(address);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Menangani kesalahan ketika melakukan permintaan ke server
                        error.printStackTrace();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Set the header with the bearer token
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAccessToken());
                return headers;
            }
        };

        // Menambahkan permintaan ke RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private void getInstrukturData() {
        String url = MEMBER_URL + instrukturId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSON Response", response.toString());
                        try {
                            JSONObject instrukturData = response.getJSONObject("data");
                            String id = instrukturData.optString("id_instruktur", "");
                            String jumlahTerlambat = instrukturData.optString("jumlah_keterlambatan", "");

                            idEditText.setText(id);
                            jumlahTerlambatEditText.setText(jumlahTerlambat);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Menangani kesalahan ketika melakukan permintaan ke server
                        error.printStackTrace();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Set the header with the bearer token
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAccessToken());
                return headers;
            }
        };

        // Menambahkan permintaan ke RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private int getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", 0);
    }

    private int getInstrukturId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("instrukturId", 0);
    }

    // Metode untuk mendapatkan access token
    private String getAccessToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("accessToken", "");
    }

}