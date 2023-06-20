package com.william.gofiit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class KelasAddActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://200710569.gofit.backend.given.website/api/";
    private static final String API_ENDPOINT = BASE_URL + "bookingKelas";

    private EditText editTextIdJadwalHarian, editTextMetodePembayaran;
    private Button buttonSubmit;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelas_add);
        
        editTextIdJadwalHarian = findViewById(R.id.editTextIdJadwalHarian);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addKelas();
                Intent intent = new Intent(KelasAddActivity.this, KelasActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addKelas() {
        int idMember = getMemberId(); // Dapatkan ID pengguna dari SharedPreferences atau sumber lainnya
        String token = getAccessToken(); // Dapatkan token login dari SharedPreferences atau sumber lainnya
        String idJadwalHarian = editTextIdJadwalHarian.getText().toString().trim();
        Spinner spinnerMetodePembayaran = findViewById(R.id.spinnerMetodePembayaran);
        String metodePembayaran = spinnerMetodePembayaran.getSelectedItem().toString();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("id_member", idMember);
            requestBody.put("id_jadwal_harian", idJadwalHarian);
            requestBody.put("metode_pembayaran", metodePembayaran);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, API_ENDPOINT, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            Toast.makeText(KelasAddActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(KelasAddActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private int getMemberId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("memberId", 0);
    }

    private String getAccessToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("accessToken", "");
    }
}
