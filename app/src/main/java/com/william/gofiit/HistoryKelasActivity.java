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

public class HistoryKelasActivity extends AppCompatActivity {

    private static final String TAG = HistoryKelasActivity.class.getSimpleName();

    private static final String BASE_URL = "https://200710569.gofit.backend.given.website/api/";
    private static final String API_BOOKING_KELAS = "bookingKelasTertentu/";
    private static final String API_KELAS = "kelas";

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_kelas);

        tableLayout = findViewById(R.id.table_layout);

        retrieveKelass();
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
        String url = BASE_URL + API_BOOKING_KELAS + getMemberId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray historyKelasArray = response.getJSONArray("data");

                            for (int i = 0; i < historyKelasArray.length(); i++) {
                                JSONObject historyKelasObj = historyKelasArray.getJSONObject(i);
                                int id_kelas = historyKelasObj.getInt("id_kelas");

                                // Dapatkan objek kelas berdasarkan id_kelas dari data kelass
                                JSONObject kelasObj = findKelasById(kelassArray, id_kelas);

                                // Ambil nama_kelas dari objek kelas
                                String nama_kelas = kelasObj.getString("nama_kelas");

                                // Tampilkan data dalam tabel
                                displayData(historyKelasObj, nama_kelas);
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

    private void displayData(JSONObject historyKelasObj, String nama_kelas) throws JSONException {
        String tanggal = historyKelasObj.getString("tanggal_jadwal");
        String waktu_kelas = historyKelasObj.getString("waktu_kelas");
        String waktu_presensi = historyKelasObj.getString("waktu_presensi");

        TableRow row = new TableRow(this);

        TextView tvTanggal = createTextView(tanggal);
        TextView tvIdKelas = createTextView(nama_kelas);
        TextView tvJamKelas = createTextView(waktu_kelas);
        TextView tvJamMulai = createTextView(waktu_presensi);

        row.addView(tvTanggal);
        row.addView(tvIdKelas);
        row.addView(tvJamKelas);
        row.addView(tvJamMulai);

        tableLayout.addView(row);
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        textView.setTextAppearance(this, android.R.style.TextAppearance_Small);
        textView.setGravity(android.view.Gravity.CENTER);
        return textView;
    }

    private int getMemberId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("memberId", 0);
    }
}