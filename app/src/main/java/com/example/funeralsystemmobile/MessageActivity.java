package com.example.funeralsystemmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

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

public class MessageActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    private List<Message> messagesList = new ArrayList<>();
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_messages);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_products) {
                    startActivity(new Intent(MessageActivity.this, ProductsActivity.class));
                    return true;
                } else if (itemId == R.id.nav_messages) {
                    startActivity(new Intent(MessageActivity.this, MessageActivity.class));
                    return true;
                } else if (itemId == R.id.nav_home) {
                    startActivity(new Intent(MessageActivity.this, LandingActivity.class));
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    startActivity(new Intent(MessageActivity.this, NotificationActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(MessageActivity.this, ProfileActivity.class));
                    return true;
                }

                return false;
            }
        });

        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText messageEditText = findViewById(R.id.messageEditText);
                String messageText = messageEditText.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    sendMessage();
                }
            }
        });

        Integer custid = Integer.parseInt(getIdFromSharedPreferences());
        Integer userid = Integer.parseInt(getuserIdFromSharedPreferences());
        String token = getTokenFromSharedPreferences();

        ListView messagesListView = findViewById(R.id.messageListView);
        adapter = new MessageAdapter(this, messagesList,userid);
        messagesListView.setAdapter(adapter);
        String url = ApiConstants.showmessages + custid;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject messageObject = response.getJSONObject(i);
                                Message message = new Message();

                                // Extract data from JSON and set it in the Message object
                                message.setFromId(messageObject.getInt("from_id"));
                                message.setToId(messageObject.getInt("to_id"));
                                message.setBody(messageObject.getString("body"));

                                messagesList.add(message);
                            }

                            adapter.notifyDataSetChanged(); // Notify the adapter that data has changed
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MessageActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Log.e("MessageActivity", "Error fetching messages: " + error.getMessage());
                        Toast.makeText(MessageActivity.this, "Error fetching messages. Please try again later.", Toast.LENGTH_SHORT).show();
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

    private void sendMessage() {
        Integer custid = Integer.parseInt(getIdFromSharedPreferences());
        Integer userid = Integer.parseInt(getuserIdFromSharedPreferences());
        String token = getTokenFromSharedPreferences();
        EditText messageEditText = findViewById(R.id.messageEditText);
        String messageText = messageEditText.getText().toString().trim();
//        sendmessage

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("userid", userid);
            requestBody.put("messageText", messageText);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = ApiConstants.sendmessage;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Assuming the API returns a message upon successful add
                            String message = response.getString("message");
                            Toast.makeText(getApplicationContext(), "sent ", Toast.LENGTH_SHORT).show();
                            // For example, start a new activity after successful add
                            startActivity(new Intent(MessageActivity.this, MessageActivity.class));
                            finish();
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
                        Log.e("Activity", "Error fetching profile: " + error.getMessage());
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

    private String getuserIdFromSharedPreferences() {
        // Retrieve the id from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userid", "");
    }
    private String getTokenFromSharedPreferences() {
        // Retrieve the token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }
}