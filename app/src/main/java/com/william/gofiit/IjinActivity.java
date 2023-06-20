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

public class IjinActivity extends AppCompatActivity {

    private static final String TAG = IjinActivity.class.getSimpleName();

    private static final String BASE_URL = "https://200710569.gofit.backend.given.website/api/ijinInstrukturTertentu/";

    private TableLayout tableLayout;
    private Button buttonAddIjin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ijin);

        tableLayout = findViewById(R.id.table_layout);

        retrieveIjin();

        //Logout
        buttonAddIjin = findViewById(R.id.buttonAddIjin);
        buttonAddIjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IjinActivity.this, IjinAddActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void retrieveIjin() {
        String url = BASE_URL + getUserId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray ijinArray = response.getJSONArray("data");

                            for (int i = 0; i < ijinArray.length(); i++) {
                                JSONObject ijinObj = ijinArray.getJSONObject(i);

                                // Tampilkan data dalam tabel
                                displayData(ijinObj);
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

    // Metode untuk mendapatkan access token
    private String getAccessToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("accessToken", "");
    }

    private void displayData(JSONObject ijinObj) throws JSONException {
        String pengajuan = ijinObj.getString("tanggal_pengajuan");
        String ijin = ijinObj.getString("tanggal_ijin");
        String keterangan = ijinObj.getString("keterangan");
        String status = ijinObj.getString("status");

        TableRow row = new TableRow(this);

        TextView tvPengajuan = createTextView(pengajuan);
        TextView tvIjin = createTextView(ijin);
        TextView tvKeterangan = createTextView(keterangan);
        TextView tvStatus = createTextView(status);

        row.addView(tvPengajuan);
        row.addView(tvIjin);
        row.addView(tvKeterangan);
        row.addView(tvStatus);

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

    private int getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", 0);
    }
}