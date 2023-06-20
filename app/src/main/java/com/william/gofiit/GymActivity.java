package com.william.gofiit;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.william.gofiit.adapters.GymAdapter;
import com.william.gofiit.models.Gym;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GymActivity extends AppCompatActivity {
    private RecyclerView recyclerViewGym;
    private Button buttonTambahBooking;
    private Button buttonDeleteBooking;

    private List<Gym> gymList;
    private GymAdapter gymAdapter;

    private String gymUrl = "https://200710569.gofit.backend.given.website/api/bookingGymTertentu/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);

        recyclerViewGym = findViewById(R.id.recyclerViewGym);
        recyclerViewGym.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGym.setHasFixedSize(true);

        buttonTambahBooking = findViewById(R.id.buttonAddGym);
        buttonDeleteBooking = findViewById(R.id.buttonDeleteGym);

        gymList = new ArrayList<>();
        gymAdapter = new GymAdapter(this, gymList);
        recyclerViewGym.setAdapter(gymAdapter);

        // Mendapatkan id_member yang login dari Shared Preferences
        int memberId = getMemberId();
        String token = getAccessToken();

        // Membuat URL API dengan menambahkan id_member yang login
        String apiUrl = gymUrl + memberId;

        // Memuat data gym dari API
        loadGym(apiUrl);

        // Menambahkan listener klik pada tombol Tambah Booking
        buttonTambahBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengarahkan pengguna ke halaman tambah booking gym
                Intent intent = new Intent(GymActivity.this, GymAddActivity.class);
                startActivity(intent);
            }
        });

        buttonDeleteBooking.setOnClickListener(v -> deleteBookingGym());
    }

    private void loadGym(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the response object and extract the gym data
                            JSONArray gymArray = response.getJSONArray("gym");

                            // Convert the gym data into a list of Gym objects
                            for (int i = 0; i < gymArray.length(); i++) {
                                JSONObject jsonObject = gymArray.getJSONObject(i);
                                Gym gym = new Gym(
                                        jsonObject.getString("nomor_booking"),
                                        jsonObject.getString("tanggal_booking"),
                                        jsonObject.getString("tanggal_jadwal"),
                                        jsonObject.getString("waktu_gym"),
                                        jsonObject.getString("waktu_presensi")
                                );
                                gymList.add(gym);
                            }

                            // Update the adapter after obtaining the gym data
                            gymAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GymActivity.this, "Failed to load gym", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(GymActivity.this, "Failed to load gym", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Set the header with the bearer token
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAccessToken());
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


    // Metode untuk mendapatkan ID member
    private int getMemberId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("memberId", 0);
    }

    // Metode untuk mendapatkan access token
    private String getAccessToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("accessToken", "");
    }

    private void deleteBookingGym() {
        String url = "https://200710569.gofit.backend.given.website/api/bookingGym/" + getMemberId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Tangani respons dari permintaan API
                        try {
                            // Ambil data yang diperlukan dari respons jika ada
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");

                            // Lakukan tindakan sesuai dengan respons API
                            if (success) {
                                // Proses berhasil
                                Log.d(TAG, "Kelas berhasil dipresensi");
                                // Lakukan sesuatu setelah kelas berhasil dipresensi
                            } else {
                                // Proses gagal
                                Log.d(TAG, "Gagal mepresensi kelas: " + message);
                                // Lakukan sesuatu jika gagal mepresensi kelas
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON exception: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tangani kesalahan pada permintaan API
                        Log.e(TAG, "Error: " + error.getMessage());
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Set header dengan bearer token
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAccessToken());
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}

