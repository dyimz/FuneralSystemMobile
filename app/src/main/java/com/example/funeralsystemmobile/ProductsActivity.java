package com.example.funeralsystemmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

public class ProductsActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private List<Product> productList = new ArrayList<>();
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_products);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_products) {
                    startActivity(new Intent(ProductsActivity.this, ProductsActivity.class));
                    return true;
                } else if (itemId == R.id.nav_messages) {
                    startActivity(new Intent(ProductsActivity.this, MessageActivity.class));
                    return true;
                } else if (itemId == R.id.nav_home) {
                    startActivity(new Intent(ProductsActivity.this, LandingActivity.class));
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    startActivity(new Intent(ProductsActivity.this, NotificationActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(ProductsActivity.this, ProfileActivity.class));
                    return true;
                }

                return false;
            }
        });

        HorizontalScrollView horizontalScrollView = findViewById(R.id.horizontalscrollview);
        LinearLayout horizontalLinearLayout = horizontalScrollView.findViewById(R.id.horizontalLinearLayout);

        LinearLayout categoryCaskets = horizontalLinearLayout.findViewById(R.id.categoryCaskets);
        LinearLayout categoryDressings = horizontalLinearLayout.findViewById(R.id.categoryDressings);
        LinearLayout categoryFlowers = horizontalLinearLayout.findViewById(R.id.categoryFlowers);
        LinearLayout categoryUrn = horizontalLinearLayout.findViewById(R.id.categoryUrn);

        categoryCaskets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsActivity.this, CasketActivity.class));
            }
        });

        categoryDressings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsActivity.this, DressingActivity.class));
            }
        });

        categoryFlowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsActivity.this, FlowerActivity.class));
            }
        });

        categoryUrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsActivity.this, UrnActivity.class));
            }
        });

        HorizontalScrollView horizontalScrollViewp = findViewById(R.id.horizontalscrollviewp);
        LinearLayout horizontalLinearLayoutp = horizontalScrollViewp.findViewById(R.id.horizontalLinearLayoutp);

        LinearLayout allpackage = horizontalLinearLayoutp.findViewById(R.id.allpackage);
        LinearLayout embalmingpackage = horizontalLinearLayoutp.findViewById(R.id.embalmingpackage);
        LinearLayout cremationpackage = horizontalLinearLayoutp.findViewById(R.id.cremationpackage);
        LinearLayout allinpackage = horizontalLinearLayoutp.findViewById(R.id.allinpackage);

        allpackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsActivity.this, PackageActivity.class));
            }
        });

        embalmingpackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsActivity.this, EmbalmingActivity.class));
            }
        });

        cremationpackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsActivity.this, CremationActivity.class));
            }
        });

        allinpackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsActivity.this, AllinserviceActivity.class));
            }
        });

        String token = getTokenFromSharedPreferences();

        GridView productGridView = findViewById(R.id.productGridView);
        adapter = new ProductAdapter(this, productList);
        productGridView.setAdapter(adapter);

        String url = ApiConstants.productsListURL;
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

    private String getTokenFromSharedPreferences() {
        // Retrieve the token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }
}