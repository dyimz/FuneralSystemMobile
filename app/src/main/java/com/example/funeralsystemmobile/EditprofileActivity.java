package com.example.funeralsystemmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditprofileActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private String encodedcustimage = null , encodedidimage = null  ;
    private static final int PICK_CUSTOMER_IMAGE_REQUEST = 1;  // Request code for picking customer image
    private static final int PICK_VALID_ID_IMAGE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_products) {
                    startActivity(new Intent(EditprofileActivity.this, ProductsActivity.class));
                    return true;
                } else if (itemId == R.id.nav_messages) {
                    startActivity(new Intent(EditprofileActivity.this, MessageActivity.class));
                    return true;
                } else if (itemId == R.id.nav_home) {
                    startActivity(new Intent(EditprofileActivity.this, LandingActivity.class));
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    startActivity(new Intent(EditprofileActivity.this, NotificationActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(EditprofileActivity.this, ProfileActivity.class));
                    return true;
                }
                return false;
            }
        });

        ImageView ivCustImage = findViewById(R.id.imgCustomer);
        ImageView ivCustValidId = findViewById(R.id.imgValidID);
        ivCustImage.setOnClickListener(v -> openImageSelector(PICK_CUSTOMER_IMAGE_REQUEST));
        ivCustValidId.setOnClickListener(v -> openImageSelector(PICK_VALID_ID_IMAGE_REQUEST));

        Spinner spinnerSex = findViewById(R.id.spinnerSex);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSex.setAdapter(adapter);

        Spinner spinnerIdType = findViewById(R.id.spinnerIdType);  // Ensure you have a Spinner with this ID in your XML
        ArrayAdapter<CharSequence> adapterr = ArrayAdapter.createFromResource(this,
                R.array.valid_id_array, android.R.layout.simple_spinner_item);
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdType.setAdapter(adapterr);

        EditText editBirthDate = findViewById(R.id.editBirthDate);
        editBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();  // Show date picker when user clicks the EditText
            }
        });

        showprofile();

        Button submiteditprofile = findViewById(R.id.btnSubmit);
        submiteditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editprofile();  // Show date picker when user clicks the EditText
            }
        });

        
    }

    private void showprofile(){
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
                            String bday = response.getString("birthdate");
                            String sex = response.getString("sex");
                            String address = response.getString("address");
                            String contact = response.getString("contact");
                            String idtype = response.getString("idtype");
                            String custimage = response.getString("custimage");
                            String custvalidid = response.getString("custvalidid");
                            String custgcashqr = response.getString("custgcashqr");

                            Spinner spinnerSex = findViewById(R.id.spinnerSex);
                            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerSex.getAdapter();
                            int position = adapter.getPosition(sex); // Find position of 'sex' in the adapter
                            spinnerSex.setSelection(position);

                            Spinner spinnerIdType = findViewById(R.id.spinnerIdType);
                            ArrayAdapter<CharSequence> adapterr = (ArrayAdapter<CharSequence>) spinnerIdType.getAdapter();
                            int positionn = adapterr.getPosition(idtype);  // Get the position of the ID type in the adapter
                            spinnerIdType.setSelection(positionn);

                            // Converting date from yyyy-mm-dd to MM-dd-yyyy
                            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            SimpleDateFormat targetFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
                            String formattedDate = "";
                            try {
                                formattedDate = targetFormat.format(originalFormat.parse(bday));
                            } catch (Exception e) {
                                Log.e("DateFormatError", "Error in parsing date", e);
                            }

                            // Update your UI with profile information
                            String custimageUrl = ApiConstants.BASE_URL + custimage;
                            ImageView ivCustImage = findViewById(R.id.imgCustomer);
                            Picasso.get().load(custimageUrl).into(ivCustImage);

                            String custvalididUrl = ApiConstants.BASE_URL + custvalidid;
                            ImageView ivCustValidId = findViewById(R.id.imgValidID);
                            Picasso.get().load(custvalididUrl).into(ivCustValidId);

                            TextView FirstName = findViewById(R.id.editFirstName);
                            FirstName.setText(fname);
                            TextView LastName = findViewById(R.id.editLastName);
                            LastName.setText(lname);
                            TextView Birthdate = findViewById(R.id.editBirthDate);
                            Birthdate.setText(formattedDate);
                            TextView Address = findViewById(R.id.editAddress);
                            Address.setText(address);
                            TextView Contact = findViewById(R.id.editPhoneNumber);
                            Contact.setText(contact);

//                            Toast.makeText(getApplicationContext(), spinnerSex.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

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

    private void editprofile(){
        EditText firstNameEditText = findViewById(R.id.editFirstName);
        EditText lastNameEditText = findViewById(R.id.editLastName);
        EditText birthDateEditText = findViewById(R.id.editBirthDate);
        Spinner sexSpinner = findViewById(R.id.spinnerSex);
        EditText phoneNumberEditText = findViewById(R.id.editPhoneNumber);
        EditText addressEditText = findViewById(R.id.editAddress);
        Spinner idTypeSpinner = findViewById(R.id.spinnerIdType);

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String birthDate = birthDateEditText.getText().toString();
        String sex = sexSpinner.getSelectedItem().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String idType = idTypeSpinner.getSelectedItem().toString();

        if (firstName.isEmpty() || lastName.isEmpty() || birthDate.isEmpty() ||
                phoneNumber.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Integer custid =  Integer.parseInt(getIdFromSharedPreferences());
            String token = getTokenFromSharedPreferences();

            JSONObject profileData = new JSONObject();
            try {
                profileData.put("fname", firstName);
                profileData.put("lname", lastName);
                profileData.put("birthdate", birthDate);
                profileData.put("sex", sex);
                profileData.put("contact", phoneNumber);
                profileData.put("address", address);
                profileData.put("idType", idType);
                profileData.put("custimage", encodedcustimage);
                profileData.put("custvalidid", encodedidimage);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = ApiConstants.editprofileURL + custid; // Replace with your actual login API endpoint
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
                                    Toast.makeText(getApplicationContext(), "Edited Successfully ", Toast.LENGTH_SHORT).show();
                                    // For example, start a new activity after successful login
                                    startActivity(new Intent(EditprofileActivity.this, ProfileActivity.class));
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
                            Toast.makeText(getApplicationContext(), "Registration Failed ", Toast.LENGTH_SHORT).show();
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

    private void showDatePickerDialog() {
        Calendar today = Calendar.getInstance();  // Get today's date
        Calendar minAdultAge = Calendar.getInstance();  // Get a calendar instance for comparison
        minAdultAge.add(Calendar.YEAR, -18);  // Set calendar to 18 years ago

        int year = minAdultAge.get(Calendar.YEAR);  // Use year from 18 years ago
        int month = minAdultAge.get(Calendar.MONTH);  // Use month from today (or from 18 years ago)
        int day = minAdultAge.get(Calendar.DAY_OF_MONTH);  // Use day from today (or from 18 years ago)

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);

                        // Format the date chosen by the user
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
                        String formattedDate = dateFormat.format(selectedDate.getTime());

                        EditText birthDate = findViewById(R.id.editBirthDate);
                        birthDate.setText(formattedDate);
                    }
                }, year, month, day);

        // Set the maximum date to today minus 18 years to ensure no one under 18 can choose their birthdate
        datePickerDialog.getDatePicker().setMaxDate(minAdultAge.getTimeInMillis());

        datePickerDialog.show();
    }

    private void openImageSelector(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            try {
                // Convert URI to Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                // Encode Bitmap to Base64
                String imageBase64 = convertBitmapToBase64(bitmap);

                // Check which imageView should receive the image
                if (requestCode == PICK_CUSTOMER_IMAGE_REQUEST) {
                    ImageView ivCustImage = findViewById(R.id.imgCustomer);
                    Picasso.get().load(selectedImageUri).into(ivCustImage);
                    encodedcustimage = imageBase64;  // Store base64 string
                } else if (requestCode == PICK_VALID_ID_IMAGE_REQUEST) {
                    ImageView ivCustValidId = findViewById(R.id.imgValidID);
                    Picasso.get().load(selectedImageUri).into(ivCustValidId);
                    encodedidimage = imageBase64;  // Store base64 string
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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