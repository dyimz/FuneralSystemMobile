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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DeceasedaddActivity extends AppCompatActivity {

    private EditText fname, mname, lname, relationship, causeofdeath, sex, religion, age, dateofbirth, dateofdeath, placeofdeath, citizenship, address, civilstatus, occupation, namecemetery, addresscemetery, nameFather, nameMother;
    private Button addDeceased;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deceasedadd);

        addDeceased = findViewById(R.id.addDeceased);
        addDeceased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDeceasedPerson();
            }
        });

        fname = findViewById(R.id.fname);
        mname = findViewById(R.id.mname);
        lname = findViewById(R.id.lname);
        relationship = findViewById(R.id.relationship);
        causeofdeath = findViewById(R.id.causeofdeath);
        sex = findViewById(R.id.sex);
        religion = findViewById(R.id.religion);
        age = findViewById(R.id.age);
        dateofbirth = findViewById(R.id.dateofbirth);
        dateofdeath = findViewById(R.id.dateofdeath);
        placeofdeath = findViewById(R.id.placeofdeath);
        citizenship = findViewById(R.id.citizenship);
        address = findViewById(R.id.address);
        civilstatus = findViewById(R.id.civilstatus);
        occupation = findViewById(R.id.occupation);
        namecemetery = findViewById(R.id.namecemetery);
        addresscemetery = findViewById(R.id.addresscemetery);
        nameFather = findViewById(R.id.nameFather);
        nameMother = findViewById(R.id.nameMother);

    }

    private void addDeceasedPerson() {
        String sfname = fname.getText().toString().trim();
        String smname = mname.getText().toString().trim();
        String slname = lname.getText().toString().trim();
        String srelationship = relationship.getText().toString().trim();
        String scauseofdeath = causeofdeath.getText().toString().trim();
        String ssex = sex.getText().toString().trim();
        String sreligion = religion.getText().toString().trim();
        String sage = age.getText().toString().trim();
        String sdateofbirth = dateofbirth.getText().toString().trim();
        String sdateofdeath = dateofdeath.getText().toString().trim();
        String splaceofdeath = placeofdeath.getText().toString().trim();
        String scitizenship = citizenship.getText().toString().trim();
        String saddress = address.getText().toString().trim();
        String scivilstatus = civilstatus.getText().toString().trim();
        String soccupation = occupation.getText().toString().trim();
        String snamecemetery = namecemetery.getText().toString().trim();
        String saddresscemetery = addresscemetery.getText().toString().trim();
        String snameFather = nameFather.getText().toString().trim();
        String snameMother = nameMother.getText().toString().trim();


        // Assume you have the token and profileId stored
        Integer custid =  Integer.parseInt(getIdFromSharedPreferences());
        String token = getTokenFromSharedPreferences();

        if (!sfname.isEmpty() && !smname.isEmpty() && !slname.isEmpty() && !srelationship.isEmpty() && !ssex.isEmpty() && !scauseofdeath.isEmpty()
                && !sreligion.isEmpty() && !sage.isEmpty() && !sdateofbirth.isEmpty() && !sdateofdeath.isEmpty() && !splaceofdeath.isEmpty()
                && !scitizenship.isEmpty() && !saddress.isEmpty() && !scivilstatus.isEmpty() && !soccupation.isEmpty() && !snamecemetery.isEmpty()
                && !saddresscemetery.isEmpty() && !snameFather.isEmpty() && !snameMother.isEmpty() ) {

            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("customerID", custid);
                requestBody.put("fname", sfname);
                requestBody.put("mname", smname);
                requestBody.put("lname", slname);
                requestBody.put("relationship", srelationship);
                requestBody.put("causeofdeath", scauseofdeath);
                requestBody.put("sex", ssex);
                requestBody.put("religion", sreligion);
                requestBody.put("age", sage);
                requestBody.put("dateofbirth", sdateofbirth);
                requestBody.put("dateofdeath", sdateofdeath);
                requestBody.put("placeofdeath", splaceofdeath);
                requestBody.put("citizenship", scitizenship);
                requestBody.put("address", saddress);
                requestBody.put("civilstatus", scivilstatus);
                requestBody.put("occupation", soccupation);
                requestBody.put("namecemetery", snamecemetery);
                requestBody.put("addresscemetery", saddresscemetery);
                requestBody.put("nameFather", snameFather);
                requestBody.put("nameMother", snameMother);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(), "Adding ... ", Toast.LENGTH_SHORT).show();

            // Make a POST request to the login API endpoint
            String url = ApiConstants.addDeceasedURL; // Replace with your actual login API endpoint
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Assuming the API returns a message upon successful add
                                String message = response.getString("message");
                                Toast.makeText(getApplicationContext(), "Added Successfully ", Toast.LENGTH_SHORT).show();
                                // For example, start a new activity after successful add
                                 startActivity(new Intent(DeceasedaddActivity.this, DeceasedActivity.class));
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
                            Log.e("DeceasedaddActivity", "Error fetching profile: " + error.getMessage());
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

        } else {
            Toast.makeText(getApplicationContext(), "Please Fill out all fields", Toast.LENGTH_SHORT).show();
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