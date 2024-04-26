package com.example.funeralsystemmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExtendorderActivity extends AppCompatActivity {
    private String productID;
    private TextView formalinDaysLabel, standingflowersLabel, lightsLabel, candleStandLabel, flooringFlowersLabel, crossLabel, tarpaulinLabel, curtainsLabel, candlesLabel, balloonsLabel;
    private EditText formalinDay, standingflowers, lights, candleStand, flooringFlowers, cross, tarpaulin, curtains, candles, balloons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extendorder);
        Intent intent = getIntent();
        productID = intent.getStringExtra("productID");
//        Toast.makeText(this, "Product ID: " + productID, Toast.LENGTH_LONG).show();

        formalinDaysLabel = findViewById(R.id.formalinDaysLabel);
        standingflowersLabel = findViewById(R.id.standingFlowersLabel);
        lightsLabel = findViewById(R.id.lightsLabel);
        candleStandLabel = findViewById(R.id.candleStandLabel);
        flooringFlowersLabel = findViewById(R.id.flooringFlowersLabel);
        crossLabel = findViewById(R.id.crossLabel);
        tarpaulinLabel = findViewById(R.id.tarpaulinLabel);
        curtainsLabel = findViewById(R.id.curtainsLabel);
        candlesLabel = findViewById(R.id.candlesLabel);
        balloonsLabel = findViewById(R.id.balloonsLabel);

        formalinDay = findViewById(R.id.formalinDays);
        standingflowers = findViewById(R.id.standingFlowers);
        lights = findViewById(R.id.lights);
        candleStand = findViewById(R.id.candleStand);
        flooringFlowers = findViewById(R.id.flooringFlowers);
        cross = findViewById(R.id.cross);
        tarpaulin = findViewById(R.id.tarpaulin);
        curtains = findViewById(R.id.curtains);
        candles = findViewById(R.id.candles);
        balloons = findViewById(R.id.balloons);

        Integer custid =  Integer.parseInt(getIdFromSharedPreferences());
        String token = getTokenFromSharedPreferences();
        String url = ApiConstants.orderDetails + productID;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response from the profile endpoint
                        try {
                            JSONObject order = response.getJSONObject("order");
                            // Extract data from the order object
                            String extensiondays = order.getString("extensiondays");
                            String standingflower = order.getString("standingflower");
                            String light = order.getString("light");
                            String candlestand = order.getString("candlestand");
                            String flooringflower = order.getString("flooringflower");
                            String crosss = order.getString("crosss");
                            String tarpaulin = order.getString("tarpaulin");
                            String curtain = order.getString("curtain");
                            String candle = order.getString("candle");
                            String balloon = order.getString("balloon");

                            formalinDaysLabel.setText("Days of Formalin Extend (" + extensiondays + ")");
                            standingflowersLabel.setText("Standing Flowers (" + standingflower + ")");
                            lightsLabel.setText("Lights (" + light + ")");
                            candleStandLabel.setText("Candle Stand (" + candlestand + ")");
                            flooringFlowersLabel.setText("Flooring Flowers (" + flooringflower + ")");
                            crossLabel.setText("Cross (" + crosss + ")");
                            tarpaulinLabel.setText("Tarpaulin (" + tarpaulin + ")");
                            curtainsLabel.setText("Curtains (" + curtain + ")");
                            candlesLabel.setText("Candles (" + candle + ")");
                            balloonsLabel.setText("Balloons (" + balloon + ")");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("OrderinfoActivity", "Error parsing JSON response");
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

        formalinDay.setText("0");
        standingflowers.setText("0");
        lights.setText("0");
        candleStand.setText("0");
        flooringFlowers.setText("0");
        cross.setText("0");
        tarpaulin.setText("0");
        curtains.setText("0");
        candles.setText("0");
        balloons.setText("0");

        formalinDay.setFilters(new InputFilter[]{new ExtendorderActivity.QuantityInputFilter()});
        standingflowers.setFilters(new InputFilter[]{new ExtendorderActivity.QuantityInputFilter()});
        lights.setFilters(new InputFilter[]{new ExtendorderActivity.QuantityInputFilter()});
        candleStand.setFilters(new InputFilter[]{new ExtendorderActivity.QuantityInputFilter()});
        flooringFlowers.setFilters(new InputFilter[]{new ExtendorderActivity.QuantityInputFilter()});
        cross.setFilters(new InputFilter[]{new ExtendorderActivity.QuantityInputFilter()});
        tarpaulin.setFilters(new InputFilter[]{new ExtendorderActivity.QuantityInputFilter()});
        curtains.setFilters(new InputFilter[]{new ExtendorderActivity.QuantityInputFilter()});
        candles.setFilters(new InputFilter[]{new ExtendorderActivity.QuantityInputFilter()});
        balloons.setFilters(new InputFilter[]{new ExtendorderActivity.QuantityInputFilter()});

        Button extendOrder = findViewById(R.id.extendOrder);
        extendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extendOrder();
            }
        });
    }

    private class QuantityInputFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String input = dest.subSequence(0, dstart) + source.toString() + dest.subSequence(dend, dest.length());
            int quantity;
            try {
                quantity = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                return ""; // Empty string indicates no change
            }

            if (quantity < 0 || quantity > 9) {
                return ""; // Empty string indicates no change
            }

            return null; // Accept input
        }
    }

    private void extendOrder(){
        String formalinDays = formalinDay.getText().toString();
        String standingflowerss = standingflowers.getText().toString();
        String lightss = lights.getText().toString();
        String candleStands = candleStand.getText().toString();
        String flooringFlowerss = flooringFlowers.getText().toString();
        String crosss = cross.getText().toString();
        String tarpaulins = tarpaulin.getText().toString();
        String curtainss = curtains.getText().toString();
        String candless = candles.getText().toString();
        String balloonss = balloons.getText().toString();

        if (formalinDays.isEmpty() || standingflowerss.isEmpty() || lightss.isEmpty()
                || candleStands.isEmpty() || flooringFlowerss.isEmpty() || crosss.isEmpty()
                || tarpaulins.isEmpty() || curtainss.isEmpty()
                || candless.isEmpty() || balloonss.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Integer custid =  Integer.parseInt(getIdFromSharedPreferences());
            String token = getTokenFromSharedPreferences();

            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("extensiondays", formalinDays);
                requestBody.put("standingflower", standingflowerss);
                requestBody.put("light", lightss);
                requestBody.put("candlestand", candleStands);
                requestBody.put("flooringflower", flooringFlowerss);
                requestBody.put("crosss", crosss);
                requestBody.put("tarpaulin", tarpaulins);
                requestBody.put("curtain", curtainss);
                requestBody.put("candle", candless);
                requestBody.put("balloon", balloonss);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = ApiConstants.extendOrder + productID; // Replace with your actual login API endpoint
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
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
                                    Toast.makeText(getApplicationContext(), "Request Sent", Toast.LENGTH_SHORT).show();
                                    // For example, start a new activity after successful login
                                    startActivity(new Intent(ExtendorderActivity.this, ProfileActivity.class));
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
                            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
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

}