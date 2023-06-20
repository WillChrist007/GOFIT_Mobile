package com.william.gofiit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class KelasActivity extends AppCompatActivity {

    private static final String TAG = KelasActivity.class.getSimpleName();

    private static final String BASE_URL = "https://200710569.gofit.backend.given.website/api/";
    private static final String API_BOOKING_KELAS = "bookingKelasTertentu/";
    private static final String API_KELAS = "kelas";

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelas);

        tableLayout = findViewById(R.id.table_layout);

        retrieveKelass();

        Button buttonTambahBooking = findViewById(R.id.buttonAddKelas);
        Button buttonDeleteBooking = findViewById(R.id.buttonDeleteKelas);

        // Menambahkan listener klik pada tombol Tambah Booking
        buttonTambahBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengarahkan pengguna ke halaman tambah booking kelas
                Intent intent = new Intent(KelasActivity.this, KelasAddActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonDeleteBooking.setOnClickListener(v -> deleteBookingKelas());

    }

    private void retrieveKelass() {
        String url = BASE_URL + API_KELAS;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray kelassArray = response.getJSONArray("data");

                            // Setelah mendapatkan data kelass, lanjutkan dengan memanggil retrieveBookingKelas
                            retrieveBookingKelas(kelassArray);
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

    private void retrieveBookingKelas(JSONArray kelassArray) {
        String url = BASE_URL + API_BOOKING_KELAS +getMemberId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray bookingKelasArray = response.getJSONArray("data");

                            for (int i = 0; i < bookingKelasArray.length(); i++) {
                                JSONObject bookingKelasObj = bookingKelasArray.getJSONObject(i);
                                int id_kelas = bookingKelasObj.getInt("id_kelas");

                                // Dapatkan objek kelas berdasarkan id_kelas dari data kelass
                                JSONObject kelasObj = findKelasById(kelassArray, id_kelas);

                                // Ambil nama_kelas dari objek kelas
                                String nama_kelas = kelasObj.getString("nama_kelas");

                                // Tampilkan data dalam tabel
                                displayData(bookingKelasObj, nama_kelas);
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

    private int getMemberId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("memberId", 0);
    }

    private void displayData(JSONObject bookingKelasObj, String nama_kelas) throws JSONException {
        String nomor_booking = bookingKelasObj.getString("nomor_booking");
        String waktu_kelas = bookingKelasObj.getString("waktu_kelas");
        String tanggal_jadwal = bookingKelasObj.getString("tanggal_jadwal");
        String waktu_presensi = bookingKelasObj.getString("waktu_presensi");

        TableRow row = new TableRow(this);

        TextView tvNomorBooking = createTextView(nomor_booking);
        TextView tvTanggalKelas = createTextView(tanggal_jadwal);
        TextView tvWaktuKelas = createTextView(waktu_kelas);
        TextView tvIdKelas = createTextView(nama_kelas);
        TextView tvWaktuPresensi = createTextView(waktu_presensi);

        row.addView(tvNomorBooking);
        row.addView(tvTanggalKelas);
        row.addView(tvWaktuKelas);
        row.addView(tvIdKelas);
        row.addView(tvWaktuPresensi);

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

    private void deleteBookingKelas() {
        String url = "https://200710569.gofit.backend.given.website/api/bookingKelas/" + getMemberId();

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