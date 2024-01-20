package com.example.funeralsystemmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_products) {
                    startActivity(new Intent(ProfileActivity.this, ProductsActivity.class));
                    return true;
                } else if (itemId == R.id.nav_messages) {
                    startActivity(new Intent(ProfileActivity.this, MessageActivity.class));
                    return true;
                } else if (itemId == R.id.nav_home) {
                    startActivity(new Intent(ProfileActivity.this, LandingActivity.class));
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    startActivity(new Intent(ProfileActivity.this, NotificationActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                    return true;
                }
                return false;
            }
        });

        TextView Orders = findViewById(R.id.Orders);
        Orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, OrderActivity.class));
            }
        });

        TextView Cart = findViewById(R.id.Cart);
        Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, CartActivity.class));
            }
        });

        TextView Deceased = findViewById(R.id.Deceased);
        Deceased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, DeceasedActivity.class));
            }
        });

        TextView Requestz = findViewById(R.id.Requestz);
        Requestz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, RequestzActivity.class));
            }
        });

        TextView Logout = findViewById(R.id.Logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSharedPreferences();
                Toast.makeText(getApplicationContext(), "Logged Out ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        // Assume you have the token and profileId stored
        Integer custid =  Integer.parseInt(getIdFromSharedPreferences());
        String token = getTokenFromSharedPreferences();

        String url = ApiConstants.profileURL + custid;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response from the profile endpoint
                        try {
                            // Parse and use profile information
                            String fname = response.getString("fname");
                            String lname = response.getString("lname");
                            String age = response.getString("age");
                            String sex = response.getString("sex");
                            String address = response.getString("address");
                            String contact = response.getString("contact");
                            String idtype = response.getString("idtype");
                            String custimage = response.getString("custimage");
                            String custvalidid = response.getString("custvalidid");
                            String custgcashqr = response.getString("custgcashqr");

                            // Update your UI with profile information

                            String custimageUrl = ApiConstants.BASE_URL + custimage;
                            ImageView ivCustImage = findViewById(R.id.ivCustImage);
                            Picasso.get().load(custimageUrl).into(ivCustImage);

                            String custvalididUrl = ApiConstants.BASE_URL + custvalidid;
                            ImageView ivCustValidId = findViewById(R.id.ivCustValidId);
                            Picasso.get().load(custvalididUrl).into(ivCustValidId);

                            String custgcashqrUrl = ApiConstants.BASE_URL + custgcashqr;
                            ImageView ivCustGcashQr = findViewById(R.id.ivCustGcashQr);
                            Picasso.get().load(custgcashqrUrl).into(ivCustGcashQr);

                            TextView FirstName = findViewById(R.id.FirstName);
                            FirstName.setText(fname);
                            TextView LastName = findViewById(R.id.LastName);
                            LastName.setText(lname);
                            TextView Age = findViewById(R.id.Age);
                            Age.setText(age);
                            TextView Sex = findViewById(R.id.Sex);
                            Sex.setText(sex);
                            TextView Address = findViewById(R.id.Address);
                            Address.setText(address);
                            TextView Contact = findViewById(R.id.Contact);
                            Contact.setText(contact);
                            TextView IdType = findViewById(R.id.IdType);
                            IdType.setText(idtype);

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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Add the request to the Volley request queue
        requestQueue.add(request);


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

    private void clearSharedPreferences() {
        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Get the SharedPreferences.Editor instance
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear all the data
        editor.clear();

        // Apply the changes
        editor.apply();
    }

}