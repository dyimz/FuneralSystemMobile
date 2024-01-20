package com.example.funeralsystemmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CremationActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private List<Package> packageList = new ArrayList<>();
    private PackageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cremation);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_products);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_products) {
                    startActivity(new Intent(CremationActivity.this, ProductsActivity.class));
                    return true;
                } else if (itemId == R.id.nav_messages) {
                    startActivity(new Intent(CremationActivity.this, MessageActivity.class));
                    return true;
                } else if (itemId == R.id.nav_home) {
                    startActivity(new Intent(CremationActivity.this, LandingActivity.class));
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    startActivity(new Intent(CremationActivity.this, NotificationActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(CremationActivity.this, ProfileActivity.class));
                    return true;
                }

                return false;
            }
        });

        String token = getTokenFromSharedPreferences();

        GridView packageGridView = findViewById(R.id.packageGridView);
        adapter = new PackageAdapter(this, packageList);
        packageGridView.setAdapter(adapter);

        String url = ApiConstants.CremationpackagesListURL;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject productObject = response.getJSONObject(i);
                                Package packagee = new Package();

                                // Extract data from JSON and set it in the Deceased object
                                packagee.setId(productObject.getInt("id"));
                                packagee.setName(productObject.getString("name"));
                                packagee.setImg(productObject.getString("img"));
                                packagee.setPrice(productObject.getInt("price"));
                                // Set other properties similarly
                                packageList.add(packagee);
                            }

                            adapter.notifyDataSetChanged(); // Notify the adapter that data has changed
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Log.e("PackageActivity", "Error fetching profile: " + error.getMessage());
                    }
                }) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                // Add the token to the headers of the request
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Create a request queue using Volley.newRequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Add the request to the Volley request queue
        requestQueue.add(request);


    }

    private String getTokenFromSharedPreferences() {
        // Retrieve the token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }
}