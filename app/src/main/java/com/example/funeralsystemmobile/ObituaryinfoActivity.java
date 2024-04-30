package com.example.funeralsystemmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ObituaryinfoActivity extends AppCompatActivity {
    private String DECEASED_ID, name, token;
    private Integer custid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obituaryinfo);
        Intent intent = getIntent();
        DECEASED_ID = intent.getStringExtra("DECEASED_ID");
//        Toast.makeText(this, DECEASED_ID, Toast.LENGTH_LONG).show();

        custid =  Integer.parseInt(getIdFromSharedPreferences());
        token = getTokenFromSharedPreferences();
        name = getNameFromSharedPreferences();

        String url = ApiConstants.showObituary + DECEASED_ID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response from the profile endpoint
                        try {
                            // Parse and use profile information
                            String fname = response.getString("fname");
                            String mname = response.getString("mname");
                            String lname = response.getString("lname");
                            String description = response.getString("description");
                            String dateofbirth = response.getString("dateofbirth");
                            String dateofdeath = response.getString("dateofdeath");
                            String durationFrom = response.getString("durationFrom");
                            String durationTo = response.getString("durationTo");
                            String wakeLocation = response.getString("wakeLocation");
                            String namecemetery = response.getString("namecemetery");
                            String addresscemetery = response.getString("addresscemetery");
                            String image = response.getString("image");

                            // Update your UI with profile information

                            String imageUrl = ApiConstants.BASE_URL + image;
                            ImageView ivImage = findViewById(R.id.deceasedImage);
                            Picasso.get().load(imageUrl).into(ivImage);

                            TextView Fullname = findViewById(R.id.fullname);
                            Fullname.setText(fname +" "+ mname +" "+ lname);

                            TextView descriptionn = findViewById(R.id.description);
                            descriptionn.setText(description);

                            TextView dateofbirthh = findViewById(R.id.dateofbirth);
                            try {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());

                                String formattedDateOfBirth = outputFormat.format(inputFormat.parse(dateofbirth));
                                String formattedDateOfDeath = outputFormat.format(inputFormat.parse(dateofdeath));

                                dateofbirthh.setText(formattedDateOfBirth + " - " + formattedDateOfDeath);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                // Handle error if parsing fails
                                dateofbirthh.setText(dateofbirth + " - " + dateofdeath);
                            }

                            TextView durationFromm = findViewById(R.id.durationFrom);
                            durationFromm.setText("From: " + durationFrom);

                            TextView durationToo = findViewById(R.id.durationTo);
                            durationToo.setText("To: " + durationTo);

                            TextView wakeLocationn = findViewById(R.id.wakeLocation);
                            wakeLocationn.setText("Location: " + wakeLocation);

                            TextView namecemeteryy = findViewById(R.id.namecemetery);
                            namecemeteryy.setText("Name: " +namecemetery);

                            TextView addresscemeteryy = findViewById(R.id.addresscemetery);
                            addresscemeteryy.setText("Address: " +addresscemetery);

                            JSONArray commentsArray = response.getJSONArray("comments");
                            List<Comment> commentsList = new ArrayList<>();
                            for (int i = 0; i < commentsArray.length(); i++) {
                                JSONObject commentObj = commentsArray.getJSONObject(i);
                                String name = commentObj.getString("name");
                                String content = commentObj.getString("content");
                                Comment comment = new Comment(name, content);
                                commentsList.add(comment);
                            }
                            ListView commentsListView = findViewById(R.id.commentsListView);
                            CommentAdapter adapter = new CommentAdapter(ObituaryinfoActivity.this, commentsList);
                            commentsListView.setAdapter(adapter);

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

        Button postComment = findViewById(R.id.postComment);
        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();  // Show date picker when user clicks the EditText
            }
        });
    }

    private void postComment(){
        EditText addComment = findViewById(R.id.addComment);
        String message = addComment.getText().toString();

        if (message.isEmpty()) {
            Toast.makeText(this, "Please input comment", Toast.LENGTH_SHORT).show();
            return;
        } else {

            JSONObject profileData = new JSONObject();
            try {
                profileData.put("name", name);
                profileData.put("message", message);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = ApiConstants.commentobituaries + DECEASED_ID; // Replace with your actual login API endpoint
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, profileData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Assuming the API returns a message upon successful login
                                String message = response.getString("message");
                                if (message.equals("failed")) {
                                    Toast.makeText(getApplicationContext(), "Check Internet Connection ", Toast.LENGTH_SHORT).show();
                                }
                                else if (message.equals("success")) {
                                    Toast.makeText(getApplicationContext(), "Comment Requested ", Toast.LENGTH_SHORT).show();
                                    // For example, start a new activity after successful login
                                    startActivity(new Intent(ObituaryinfoActivity.this, ProfileActivity.class));
                                }
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

    private String getNameFromSharedPreferences() {
        // Retrieve the token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("name", "");
    }
}