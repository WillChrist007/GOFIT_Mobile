package com.william.gofiit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JadwalUmumActivity extends AppCompatActivity {

    private static final String TAG = JadwalUmumActivity.class.getSimpleName();

    private static final String BASE_URL = "https://200710569.gofit.backend.given.website/api/";
    private static final String API_JADWAL_UMUM = "jadwalUmum";
    private static final String API_KELAS = "kelas";

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_umum);

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

                            // Setelah mendapatkan data kelass, lanjutkan dengan memanggil retrieveJadwalUmum
                            retrieveJadwalUmum(kelassArray);
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

    private void retrieveJadwalUmum(JSONArray kelassArray) {
        String url = BASE_URL + API_JADWAL_UMUM;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jadwalUmumArray = response.getJSONArray("data");

                            for (int i = 0; i < jadwalUmumArray.length(); i++) {
                                JSONObject jadwalUmumObj = jadwalUmumArray.getJSONObject(i);
                                int id_kelas = jadwalUmumObj.getInt("id_kelas");

                                // Dapatkan objek kelas berdasarkan id_kelas dari data kelass
                                JSONObject kelasObj = findKelasById(kelassArray, id_kelas);

                                // Ambil nama_kelas dari objek kelas
                                String nama_kelas = kelasObj.getString("nama_kelas");

                                // Tampilkan data dalam tabel
                                displayData(jadwalUmumObj, nama_kelas);
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
                });

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

    private void displayData(JSONObject jadwalUmumObj, String nama_kelas) throws JSONException {
        String hari = jadwalUmumObj.getString("hari");
        String jam_kelas = jadwalUmumObj.getString("jam_kelas");

        TableRow row = new TableRow(this);

        TextView tvHari = createTextView(hari);
        TextView tvJamKelas = createTextView(jam_kelas);
        TextView tvIdKelas = createTextView(nama_kelas);
        TextView tvIdInstruktur = createTextView(jadwalUmumObj.getString("id_instruktur"));

        row.addView(tvHari);
        row.addView(tvJamKelas);
        row.addView(tvIdKelas);

        tableLayout.addView(row);
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        textView.setTextAppearance(this, android.R.style.TextAppearance_Medium);
        textView.setGravity(android.view.Gravity.CENTER);
        return textView;
    }
}