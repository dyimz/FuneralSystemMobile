package com.example.funeralsystemmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView txtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);

        String custId = getIdFromSharedPreferences();
        String token = getTokenFromSharedPreferences();

        if (custId != null && !custId.isEmpty() && token != null && !token.isEmpty()) {
//            Toast.makeText(getApplicationContext(), custId, Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
            startActivity(intent);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the logic to navigate to the registration screen or perform other actions
                // For example, you can start a new activity:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!username.isEmpty() && !password.isEmpty()) {
            // Create a JSON object for the request body
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("email", username);
                requestBody.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(), "Logging in ... ", Toast.LENGTH_SHORT).show();

            // Make a POST request to the login API endpoint
            String url = ApiConstants.loginURL; // Replace with your actual login API endpoint
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String message = response.getString("message");
                                if (message.equals("success")) {
                                    // Assuming the API returns a token upon successful login
                                    String userid = response.getString("userid");
                                    String custid = response.getString("custid");
                                    String token = response.getString("token");
                                    Toast.makeText(getApplicationContext(), "Login Successful ", Toast.LENGTH_SHORT).show();
                                    // You can save the token or navigate to another activity
                                    saveTokenToSharedPreferences(userid, custid, token);
                                    // For example, start a new activity after successful login
                                    startActivity(new Intent(LoginActivity.this, LandingActivity.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid login Credentials ", Toast.LENGTH_SHORT).show();
                                }

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
                    });

            // Create a request queue using Volley.newRequestQueue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            // Add the request to the Volley request queue
            requestQueue.add(request);
        } else {
            Toast.makeText(getApplicationContext(), "Email & Password Required", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTokenToSharedPreferences(String userid, String custid, String token) {
        // Save the token in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userid", userid);
        editor.putString("custid", custid);
        editor.putString("token", token);
        editor.apply();
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