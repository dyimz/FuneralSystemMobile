package com.example.funeralsystemmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ObituariesActivity extends AppCompatActivity {
    private List<Deceased> deceasedList = new ArrayList<>();
    private DeceasedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obituaries);

        Integer custid =  Integer.parseInt(getIdFromSharedPreferences());
        String token = getTokenFromSharedPreferences();

        ListView deceasedListView = findViewById(R.id.deceasedListView);
        adapter = new DeceasedAdapter(this, deceasedList);
        deceasedListView.setAdapter(adapter);

        String url = ApiConstants.obituaries;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject deceasedObject = response.getJSONObject(i);
                                Deceased deceased = new Deceased();

                                // Extract data from JSON and set it in the Deceased object
                                deceased.setId(deceasedObject.getInt("id"));
                                deceased.setFname(deceasedObject.getString("fname"));
                                deceased.setMname(deceasedObject.getString("mname"));
                                deceased.setLname(deceasedObject.getString("lname"));
                                deceased.setImage(deceasedObject.getString("image"));
                                deceased.setDateofbirth(deceasedObject.getString("dateofbirth"));
                                deceased.setDateofdeath(deceasedObject.getString("dateofdeath"));
                                deceased.setCauseofdeath(deceasedObject.getString("causeofdeath"));
                                // Set other properties similarly

                                deceasedList.add(deceased);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setDeceasedList(deceasedList);
                                }
                            });
                            adapter.notifyDataSetChanged(); // Notify the adapter that data has changed
//                            Toast.makeText(getApplicationContext(), "Deceased list loaded with size: " + deceasedList.size(), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Log.e("DeceasedActivity", "Error fetching profile: " + error.getMessage());
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

        EditText searchInput = findViewById(R.id.searching);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
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