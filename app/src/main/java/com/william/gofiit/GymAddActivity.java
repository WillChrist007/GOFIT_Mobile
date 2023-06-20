package com.william.gofiit;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GymAddActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://200710569.gofit.backend.given.website/api/";
    private static final String API_ENDPOINT = BASE_URL + "bookingGym";

    private EditText editTextTanggalGym;
    private Button buttonSubmit;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;

    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_add);

        editTextTanggalGym = findViewById(R.id.editTextTanggalGym);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Initialize selectedDate with the current date
        selectedDate = Calendar.getInstance();

        // Create a date formatter to format the selected date
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        editTextTanggalGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(dateFormatter);
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGym();
                Intent intent = new Intent(GymAddActivity.this, GymActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showDatePicker(final SimpleDateFormat dateFormatter) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, monthOfYear);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String selectedDateString = dateFormatter.format(selectedDate.getTime());
                editTextTanggalGym.setText(selectedDateString);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                GymAddActivity.this,
                dateSetListener,
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );

        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    private void addGym() {
        int idMember = getMemberId(); // Get the user ID from SharedPreferences or another source
        String token = getAccessToken(); // Get the login token from SharedPreferences or another source
        String tanggalGym = editTextTanggalGym.getText().toString().trim();
        Spinner spinnerWaktuGym = findViewById(R.id.spinnerWaktuGym);
        String waktuGym = spinnerWaktuGym.getSelectedItem().toString();

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("id_member", idMember);
            requestBody.put("tanggal_gym", tanggalGym);
            requestBody.put("waktu_gym", waktuGym);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, API_ENDPOINT, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            Toast.makeText(GymAddActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GymAddActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
