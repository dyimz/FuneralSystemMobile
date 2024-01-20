package com.example.funeralsystemmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class PackageinfoActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private String packageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packageinfo);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_products);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_products) {
                    startActivity(new Intent(PackageinfoActivity.this, ProductsActivity.class));
                    return true;
                } else if (itemId == R.id.nav_messages) {
                    startActivity(new Intent(PackageinfoActivity.this, MessageActivity.class));
                    return true;
                } else if (itemId == R.id.nav_home) {
                    startActivity(new Intent(PackageinfoActivity.this, LandingActivity.class));
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    startActivity(new Intent(PackageinfoActivity.this, NotificationActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(PackageinfoActivity.this, ProfileActivity.class));
                    return true;
                }

                return false;
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            packageID = intent.getStringExtra("packageID");

            // Now you have the productId, you can use it as needed
            String token = getTokenFromSharedPreferences();
            String url = ApiConstants.packageInfoURL + packageID;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Handle the response from the profile endpoint
                            try {
                                // Parse and use profile information
                                String name = response.getString("name");
                                String description = response.getString("description");
                                String inclusions = response.getString("inclusions");
                                String img = response.getString("img");
                                String price = String.valueOf(response.getInt("price"));
                                String category = response.getString("category");

                                // Update your UI with profile information

                                String packageimageUrl = ApiConstants.BASE_URL + img;
                                ImageView packageImage = findViewById(R.id.packageImage);
                                Picasso.get().load(packageimageUrl).into(packageImage);

                                TextView nameLabel = findViewById(R.id.nameLabel);
                                nameLabel.setText(name);
                                TextView descriptionLabel = findViewById(R.id.descriptionLabel);
                                descriptionLabel.setText(description);
                                TextView priceLabel = findViewById(R.id.priceLabel);
                                priceLabel.setText("Php: " + price);
                                TextView inclusionsLabel = findViewById(R.id.inclusionsLabel);
                                inclusionsLabel.setText(inclusions);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("PackageinfoActivity", "Error parsing JSON response");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error response
                            Log.e("ProfileActivity", "Error fetching profile: " + error.getMessage());
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

//            TextView productIdTextView = findViewById(R.id.productIdTextView);
//            productIdTextView.setText("Product ID: " + productId);
        } else {
            Toast.makeText(getApplicationContext(), "Error Fetching Data", Toast.LENGTH_SHORT).show();
        }

    }

    private String getTokenFromSharedPreferences() {
        // Retrieve the token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }
}