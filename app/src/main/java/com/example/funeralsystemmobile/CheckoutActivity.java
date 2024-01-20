package com.example.funeralsystemmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private String COName, COAddress, COPhone, COMOP, COPOP;
    private BottomNavigationView bottomNavigationView;
    private List<Product> productList = new ArrayList<>();
    private CheckoutAdapter adapter;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_products) {
                    startActivity(new Intent(CheckoutActivity.this, ProductsActivity.class));
                    return true;
                } else if (itemId == R.id.nav_messages) {
                    startActivity(new Intent(CheckoutActivity.this, MessageActivity.class));
                    return true;
                } else if (itemId == R.id.nav_home) {
                    startActivity(new Intent(CheckoutActivity.this, LandingActivity.class));
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    startActivity(new Intent(CheckoutActivity.this, NotificationActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(CheckoutActivity.this, ProfileActivity.class));
                    return true;
                }
                return false;
            }
        });


        Integer custid =  Integer.parseInt(getIdFromSharedPreferences());
        String token = getTokenFromSharedPreferences();

        ListView cartListView = findViewById(R.id.cartListView);
        adapter = new CheckoutAdapter(this, productList);
        cartListView.setAdapter(adapter);

        String url = ApiConstants.cartListURL + custid;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject productObject = response.getJSONObject(i);
                                Product product = new Product();

                                // Extract data from JSON and set it in the Deceased object
                                product.setId(productObject.getInt("id"));
                                product.setName(productObject.getString("name"));
                                product.setDescription(productObject.getString("description"));
                                product.setPrice(productObject.getInt("price"));
                                product.setImg(productObject.getString("img"));
                                product.setCategory(productObject.getString("category"));
                                product.setStock(productObject.getInt("stock"));
                                // Set other properties similarly
                                productList.add(product);
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
                        Log.e("CheckoutActivity", "Error fetching profile: " + error.getMessage());
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

        String urll = ApiConstants.cartTotalURL + custid;
        JsonObjectRequest requestt = new JsonObjectRequest(Request.Method.GET, urll, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response from the profile endpoint
                        try {
                            // Parse and use profile information
                            String total = response.getString("message");

                            // Update your UI with profile information
                            TextView totalLabel = findViewById(R.id.totalLabel);
                            totalLabel.setText("Total: " + total);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("CheckoutActivity", "Error parsing JSON response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(getApplicationContext(), "Check Internet Connection ", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueuee = Volley.newRequestQueue(this);
        // Add the request to the Volley request queue
        requestQueuee.add(requestt);


        String urlll = ApiConstants.profileURL + custid;
        JsonObjectRequest requesttt = new JsonObjectRequest(Request.Method.GET, urlll, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response from the profile endpoint
                        try {
                            // Parse and use profile information
                            String fname = response.getString("fname");
                            String lname = response.getString("lname");
                            String address = response.getString("address");
                            String contact = response.getString("contact");

                            COName = fname + " " + lname;
                            COAddress = address;
                            COPhone = contact;

                            // Update your UI with profile information
                            TextView FullName = findViewById(R.id.FullName);
                            FullName.setText(fname + " " + lname);
                            TextView Address = findViewById(R.id.Address);
                            Address.setText(address);
                            TextView Phone = findViewById(R.id.Phone);
                            Phone.setText(contact);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ProfileActivity", "Error parsing JSON response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(getApplicationContext(), "Check Internet Connection ", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueueee = Volley.newRequestQueue(this);
        // Add the request to the Volley request queue
        requestQueueee.add(requesttt);


        RequestQueue requestQueueeee = Volley.newRequestQueue(this);
        checkoutButton = findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText MOP = findViewById(R.id.MOP);
                EditText POP = findViewById(R.id.POP);
                COMOP = MOP.getText().toString().trim();
                COPOP = POP.getText().toString().trim();

                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("customerID", custid);
                    requestBody.put("name", COName);
                    requestBody.put("address", COAddress);
                    requestBody.put("contact", COPhone);
                    requestBody.put("modeofpayment", COMOP);
                    requestBody.put("proofofpayment", COPOP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String urllll = ApiConstants.checkoutURL;
                JsonObjectRequest requestttt = new JsonObjectRequest(Request.Method.POST, urllll, requestBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    // Assuming the API returns a message upon successful add
                                    String message = response.getString("message");
                                    Toast.makeText(getApplicationContext(), "Order Received ", Toast.LENGTH_SHORT).show();
                                    // For example, start a new activity after successful add
                                    startActivity(new Intent(CheckoutActivity.this, ProfileActivity.class));
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
                                Log.e("CheckoutActivity", "Error fetching profile: " + error.getMessage());
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
                requestQueueeee.add(requestttt);

            }
        });
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