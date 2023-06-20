package com.william.gofiit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JadwalTodayActivity extends AppCompatActivity {

    private static final String TAG = JadwalTodayActivity.class.getSimpleName();

    private static final String BASE_URL = "https://200710569.gofit.backend.given.website/api/";
    private static final String API_JADWAL_HARIAN = "jadwalHarianToday";
    private static final String API_KELAS = "kelas";

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_today);

        tableLayout = findViewById(R.id.table_layout);

        retrieveKelass();

        Button buttonMulai = findViewById(R.id.buttonMulai);
        buttonMulai.setOnClickListener(v -> mulaiKelas());

        Button buttonSelesai = findViewById(R.id.buttonSelesai);
        buttonSelesai.setOnClickListener(v -> selesaiKelas());

    }

    private void retrieveKelass() {
        String url = BASE_URL + API_KELAS;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray kelassArray = response.getJSONArray("data");

                            // Setelah mendapatkan data kelass, lanjutkan dengan memanggil retrieveJadwalInstruktur
                            retrieveJadwalInstruktur(kelassArray);
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON exception: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void retrieveJadwalInstruktur(JSONArray kelassArray) {
        String url = BASE_URL + API_JADWAL_HARIAN;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jadwalHarianArray = response.getJSONArray("data");

                            for (int i = 0; i < jadwalHarianArray.length(); i++) {
                                JSONObject jadwalHarianObj = jadwalHarianArray.getJSONObject(i);
                                int id_kelas = jadwalHarianObj.getInt("id_kelas");

                                // Dapatkan objek kelas berdasarkan id_kelas dari data kelass
                                JSONObject kelasObj = findKelasById(kelassArray, id_kelas);

                                // Ambil nama_kelas dari objek kelas
                                String nama_kelas = kelasObj.getString("nama_kelas");

                                // Tampilkan data dalam tabel
                                displayData(jadwalHarianObj, nama_kelas);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON exception: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    private JSONObject findKelasById(JSONArray kelassArray, int id_kelas) throws JSONException {
        for (int i = 0; i < kelassArray.length(); i++) {
            JSONObject kelasObj = kelassArray.getJSONObject(i);
            if (kelasObj.getInt("id") == id_kelas) {
                return kelasObj;
            }
        }
        return null;
    }

    // Metode untuk mendapatkan access token
    private String getAccessToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("accessToken", "");
    }

    private void displayData(JSONObject jadwalHarianObj, String nama_kelas) throws JSONException {
        String tanggal = jadwalHarianObj.getString("tanggal_jadwal");
        String jam_kelas = jadwalHarianObj.getString("jam_kelas");
        String jam_mulai = jadwalHarianObj.getString("jam_mulai");
        String jam_selesai = jadwalHarianObj.getString("jam_selesai");

        TableRow row = new TableRow(this);

        TextView tvTanggal = createTextView(tanggal);
        TextView tvIdKelas = createTextView(nama_kelas);
        TextView tvJamKelas = createTextView(jam_kelas);
        TextView tvJamMulai = createTextView(jam_mulai);
        TextView tvJamSelesai = createTextView(jam_selesai);

        row.addView(tvTanggal);
        row.addView(tvIdKelas);
        row.addView(tvJamKelas);
        row.addView(tvJamMulai);
        row.addView(tvJamSelesai);

        tableLayout.addView(row);
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        textView.setTextSize(12);
        textView.setGravity(android.view.Gravity.CENTER);
        return textView;
    }

    //fungsi untuk memulai kelas
    private void mulaiKelas() {
        String url = BASE_URL + "jadwalHarianToday/mulai-kelas";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null,
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
                                Log.d(TAG, "Kelas berhasil dimulai");
                                // Lakukan sesuatu setelah kelas berhasil dimulai
                            } else {
                                // Proses gagal
                                Log.d(TAG, "Gagal memulai kelas: " + message);
                                // Lakukan sesuatu jika gagal memulai kelas
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

    //fungsi untuk selesai kelas
    private void selesaiKelas() {
        String url = BASE_URL + "jadwalHarianToday/selesai-kelas";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null,
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
                                Log.d(TAG, "Kelas berhasil dimulai");
                                // Lakukan sesuatu setelah kelas berhasil dimulai
                            } else {
                                // Proses gagal
                                Log.d(TAG, "Gagal memulai kelas: " + message);
                                // Lakukan sesuatu jika gagal memulai kelas
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