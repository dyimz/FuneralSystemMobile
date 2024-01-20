package com.example.funeralsystemmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class ProductinfoActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productinfo);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_products);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_products) {
                    startActivity(new Intent(ProductinfoActivity.this, ProductsActivity.class));
                    return true;
                } else if (itemId == R.id.nav_messages) {
                    startActivity(new Intent(ProductinfoActivity.this, MessageActivity.class));
                    return true;
                } else if (itemId == R.id.nav_home) {
                    startActivity(new Intent(ProductinfoActivity.this, LandingActivity.class));
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    startActivity(new Intent(ProductinfoActivity.this, NotificationActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(ProductinfoActivity.this, ProfileActivity.class));
                    return true;
                }

                return false;
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            productId = intent.getStringExtra("productID");


            // Now you have the productId, you can use it as needed
            String token = getTokenFromSharedPreferences();
            String url = ApiConstants.productInfoURL + productId;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Handle the response from the profile endpoint
                            try {
                                // Parse and use profile information
                                String name = response.getString("name");
                                String description = response.getString("description");
                                String details = response.getString("details");
                                String img = response.getString("img");
                                String price = String.valueOf(response.getInt("price"));
                                String category = response.getString("category");
                                String stock = String.valueOf(response.getInt("stock"));

                                // Update your UI with profile information

                                String productimageUrl = ApiConstants.BASE_URL + img;
                                ImageView productImage = findViewById(R.id.productImage);
                                Picasso.get().load(productimageUrl).into(productImage);

                                TextView nameLabel = findViewById(R.id.nameLabel);
                                nameLabel.setText(name);
                                TextView descriptionLabel = findViewById(R.id.descriptionLabel);
                                descriptionLabel.setText(description);
                                TextView stocksLabel = findViewById(R.id.stocksLabel);
                                stocksLabel.setText("Stocks: " + stock);
                                TextView priceLabel = findViewById(R.id.priceLabel);
                                priceLabel.setText("Php: " + price);
                                TextView detailsLabel = findViewById(R.id.detailsLabel);
                                detailsLabel.setText(details);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("ProductinfoActivity", "Error parsing JSON response");
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

            Integer custid =  Integer.parseInt(getIdFromSharedPreferences());
            String scustid =  custid.toString();
            Button AddToCart = findViewById(R.id.AddToCart);
            RequestQueue atcrequestQueue = Volley.newRequestQueue(this);
            AddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getApplicationContext(), productId, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(), scustid, Toast.LENGTH_SHORT).show();
                    JSONObject atcrequestBody = new JSONObject();
                    try {
                        atcrequestBody.put("customerID", scustid);
                        atcrequestBody.put("productID", productId);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String addToCartURL = ApiConstants.addToCartURL;
                    JsonObjectRequest atcrequest = new JsonObjectRequest(Request.Method.POST, addToCartURL, atcrequestBody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        // Assuming the API returns a message upon successful add
                                        String message = response.getString("message");
                                        Toast.makeText(getApplicationContext(), "Added to Cart ", Toast.LENGTH_SHORT).show();
                                        // For example, start a new activity after successful add
                                        startActivity(new Intent(ProductinfoActivity.this, ProductsActivity.class));
                                    } catch (JSONException e) {
                                        Toast.makeText(getApplicationContext(), "Check Internet Connection ", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Handle error response
                                    Log.e("ProductinfoActivity", "Error fetching profile: " + error.getMessage());
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

                    // Add the request to the Volley request queue
                    atcrequestQueue.add(atcrequest);

                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "Error Fetching Data", Toast.LENGTH_SHORT).show();
        }

    }

    private String getIdFromSharedPreferences() {
        // Retrieve the id from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("custid", "");
    }

    private String getTokenFromSharedPreferences() {
        // Retrieve the token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }
}