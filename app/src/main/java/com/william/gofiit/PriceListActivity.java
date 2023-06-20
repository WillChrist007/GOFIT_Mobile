package com.william.gofiit;

import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PriceListActivity extends AppCompatActivity {

    private static final String API_URL = "https://200710569.gofit.backend.given.website/api/promo";

    private TableLayout tableLayout;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list);

        tableLayout = findViewById(R.id.tableLayout);
        requestQueue = Volley.newRequestQueue(this);

        fetchDataFromApi();
    }

    private void fetchDataFromApi() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject promo = dataArray.getJSONObject(i);

                                String namaPromo = promo.getString("nama_promo");
                                String keterangan = promo.getString("keterangan");
                                String jenis = promo.getString("jenis");

                                addDataToTable(namaPromo, keterangan, jenis);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PriceListActivity", "Error: " + error.getMessage());
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void addDataToTable(String namaPromo, String keterangan, String jenis) {
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        TextView tvNamaPromo = new TextView(this);
        tvNamaPromo.setText(namaPromo);
        tableRow.addView(tvNamaPromo);

        TextView tvKeterangan = new TextView(this);
        tvKeterangan.setText(keterangan);
        tableRow.addView(tvKeterangan);

        TextView tvJenis = new TextView(this);
        tvJenis.setText(jenis);
        tableRow.addView(tvJenis);

        tableLayout.addView(tableRow);
    }
}