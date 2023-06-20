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

public class HistoryGymActivity extends AppCompatActivity {

    private static final String TAG = HistoryGymActivity.class.getSimpleName();

    private static final String BASE_URL = "https://200710569.gofit.backend.given.website/api/bookingGymTertentu/";

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_gym);

        tableLayout = findViewById(R.id.table_layout);

        retrieveHistoryGym();
    }

    private void retrieveHistoryGym() {
        String url = BASE_URL + getMemberId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray historyGymArray = response.getJSONArray("data");

                            for (int i = 0; i < historyGymArray.length(); i++) {
                                JSONObject historyGymObj = historyGymArray.getJSONObject(i);

                                // Tampilkan data dalam tabel
                                displayData(historyGymObj);
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

    private void displayData(JSONObject historyGymObj) throws JSONException {
        String tanggal = historyGymObj.getString("tanggal_gym");
        String waktu_gym = historyGymObj.getString("waktu_gym");
        String waktu_presensi = historyGymObj.getString("waktu_presensi");

        TableRow row = new TableRow(this);

        TextView tvTanggal = createTextView(tanggal);
        TextView tvJamKelas = createTextView(waktu_gym);
        TextView tvJamMulai = createTextView(waktu_presensi);

        row.addView(tvTanggal);
        row.addView(tvJamKelas);
        row.addView(tvJamMulai);

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

    private int getMemberId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("memberId", 0);
    }
}