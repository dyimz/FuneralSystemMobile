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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class InquireActivity extends AppCompatActivity {
    private EditText fname, mname, lname, relationship, causeofdeath, sex, religion, age, dateofbirth,
            dateofdeath, placeofdeath, citizenship, address, civilstatus, occupation, namecemetery, addresscemetery, nameFather, nameMother,
            idType, validId, image, transferPermit, swabTest, proofOfDeath, description,
            MOP;

    private Integer custid, packageID;
    private String token, name, price, inclusions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire);

        custid =  Integer.parseInt(getIdFromSharedPreferences());
        token = getTokenFromSharedPreferences();
        packageID = Integer.parseInt(getIntent().getStringExtra("packageID"));
        name = getIntent().getStringExtra("name");
        price = getIntent().getStringExtra("price");
        inclusions = getIntent().getStringExtra("inclusions");

        TextView packageName = findViewById(R.id.packageName);
        TextView packagePrice = findViewById(R.id.packagePrice);
        TextView packageInclusions = findViewById(R.id.packageInclusions);

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

        idType = findViewById(R.id.idType);
        validId = findViewById(R.id.validId);
        image = findViewById(R.id.image);
        transferPermit = findViewById(R.id.transferPermit);
        swabTest = findViewById(R.id.swabTest);
        proofOfDeath = findViewById(R.id.proofOfDeath);
        description = findViewById(R.id.description);

        packageName.setText("Package: " + name);
        packagePrice.setText("Price: " + price);
        packageInclusions.setText("Inclusions: " + inclusions);

        Button orderPackage = findViewById(R.id.orderPackage);
        orderPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderInquiredPackage();
            }
        });
    }

    private void orderInquiredPackage() {
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
        String sidType = "PWD Card";
        String svalidId = "storage/images/old1.jpg";
        String simage = "storage/images/old1.jpg";
        String stransferPermit = "storage/images/old1.jpg";
        String sswabTest = "storage/images/old1.jpg";
        String sproofOfDeath = "storage/images/old1.jpg";
        String sdescription = "Passed away peacefully in his sleep on blablabla, concluding a brave battle against cancer and various health challenges. His enduring positive attitude and resilience serve as a lasting tribute to his spirit. Born in St. Louis, MO, on May 1, 1944, he graduated from Mercy High School in 1962.  Survived by a loving spouse of over 56 years, he leaves behind cherished children and grandchildren. He was the son of late parents, a sibling, and a beloved figure to many in various roles.  Known for his distinctive humor, he found value in every day. A past member of organizations and clubs, he pursued interests in fishing, woodworking, and other hobbies. An avid follower of history and politics, he never missed voting or watching debates. His insightful commentaries on life will be missed. Above all, he held deep affection for family and friends. He embraced each day fully, leaving a positive impact on the world.  Gratitude is extended to the compassionate healthcare staff for their kindness over the years.  A private burial is planned, with a Memorial Mass scheduled at a local church, followed by an open house luncheon to celebrate his life. In lieu of flowers, donations can be made to a local parish with a designated memo line.";

        String sdiscounted = "NO";
        String sMOP = "GCASH";
        String sPOP = "storage/images/old1.jpg";
        String smessage = "This is my message";
        String scascketsize = "STANDARD";
        String scremate = "YES";
        String sformalin = "YES";
        String smemorialproducts = "YES";
        String smakeup = "YES";
        String snote = "This is my note";
        String slocationfrom = "Taguig";
        String slocationto = "Manila Memorial";
        String sdurationfrom = "01/23/2024";
        String sdurationto = "01/31/2024";
        String spaymentstatus = "NOT PAID";

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

                requestBody.put("idType", sidType);
                requestBody.put("validId", svalidId);
                requestBody.put("image", simage);
                requestBody.put("transferPermit", stransferPermit);
                requestBody.put("swabTest", sswabTest);
                requestBody.put("proofOfDeath", sproofOfDeath);
                requestBody.put("description", sdescription);

                requestBody.put("discounted", sdiscounted);
                requestBody.put("subtotal", price);
                requestBody.put("total_price", price);
                requestBody.put("MOP", sMOP);
                requestBody.put("POP", sPOP);
                requestBody.put("packageID", packageID);
                requestBody.put("message", smessage);
                requestBody.put("cascketsize", scascketsize);
                requestBody.put("cremate", scremate);
                requestBody.put("formalin", sformalin);
                requestBody.put("memorialproducts", smemorialproducts);
                requestBody.put("makeup", smakeup);
                requestBody.put("note", snote);
                requestBody.put("locationfrom", slocationfrom);
                requestBody.put("locationto", slocationto);
                requestBody.put("durationfrom", sdurationfrom);
                requestBody.put("durationto", sdurationto);
                requestBody.put("paymentstatus", spaymentstatus);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(), "Adding ... ", Toast.LENGTH_SHORT).show();

            // Make a POST request to the login API endpoint
            String url = ApiConstants.inquirePackageURL; // Replace with your actual login API endpoint
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Assuming the API returns a message upon successful add
                                String message = response.getString("message");
                                Toast.makeText(getApplicationContext(), "Added Successfully ", Toast.LENGTH_SHORT).show();
                                // For example, start a new activity after successful add
                                startActivity(new Intent(InquireActivity.this, ProfileActivity.class));
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