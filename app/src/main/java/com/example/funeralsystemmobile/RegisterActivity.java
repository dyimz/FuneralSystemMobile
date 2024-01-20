package com.example.funeralsystemmobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity {

    private ImageView customerImage, ValidId, GcashQr;
    private EditText etFirstName, etLastName, etSex, etAge, etAddress, etContact, etIdType, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private static final int PICK_IMAGE_REQUEST_CUSTOMER = 1;
    private static final int PICK_IMAGE_REQUEST_VALID_ID = 2;
    private static final int PICK_IMAGE_REQUEST_GCASH_QR = 3;
    private Bitmap selectedCustomerImage, selectedValidId, selectedGcashQr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        customerImage = findViewById(R.id.customerImage);
        ValidId = findViewById(R.id.ValidId);
        GcashQr = findViewById(R.id.GcashQr);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etSex = findViewById(R.id.etSex);
        etAge = findViewById(R.id.etAge);
        etAddress = findViewById(R.id.etAddress);
        etContact = findViewById(R.id.etContact);
        etIdType = findViewById(R.id.etIdType);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        customerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker(PICK_IMAGE_REQUEST_CUSTOMER);
            }
        });

        ValidId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker(PICK_IMAGE_REQUEST_VALID_ID);
            }
        });

        GcashQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker(PICK_IMAGE_REQUEST_GCASH_QR);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void openImagePicker(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            Bitmap selectedBitmap = decodeSelectedImage(selectedImageUri);

            switch (requestCode) {
                case PICK_IMAGE_REQUEST_CUSTOMER:
                        customerImage.setImageBitmap(selectedBitmap);
                        this.selectedCustomerImage = selectedBitmap;
                    break;
                case PICK_IMAGE_REQUEST_VALID_ID:
                    ValidId.setImageBitmap(selectedBitmap);
                    this.selectedValidId = selectedBitmap;
                    break;
                case PICK_IMAGE_REQUEST_GCASH_QR:
                    GcashQr.setImageBitmap(selectedBitmap);
                    this.selectedGcashQr = selectedBitmap;
                    break;
            }
        }
    }

    private Bitmap decodeSelectedImage(Uri selectedImageUri) {
        try {
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void registerUser() {
        String FirstName = etFirstName.getText().toString().trim();
        String LastName = etLastName.getText().toString().trim();
        String Sex = etSex.getText().toString().trim();
        String Age = etAge.getText().toString().trim();
        String Address = etAddress.getText().toString().trim();
        String Contact = etContact.getText().toString().trim();
        String IdType = etIdType.getText().toString().trim();
        String Email = etEmail.getText().toString().trim();
        String Password = etPassword.getText().toString().trim();
        String ConfirmPassword = etConfirmPassword.getText().toString().trim();

        if (!FirstName.isEmpty() && !LastName.isEmpty() && !Sex.isEmpty() && !Age.isEmpty() && !Address.isEmpty() && !Contact.isEmpty() && !IdType.isEmpty() && !Email.isEmpty() && !Password.isEmpty() && !ConfirmPassword.isEmpty()) {
            if (Password.equals(ConfirmPassword)) {
                if (selectedCustomerImage != null && selectedValidId != null && selectedGcashQr != null) {
                    // Convert the Bitmap to a Base64 encoded string
                    String encodedCustomerImage = encodeImage(selectedCustomerImage);
                    String encodedValidId = encodeImage(selectedValidId);
                    String encodedGcashQr = encodeImage(selectedGcashQr);

                // Passwords match
                // Your logic here, for example, proceed with registration or other actions

                // Create a JSON object for the request body
                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("fname", FirstName);
                    requestBody.put("lname", LastName);
                    requestBody.put("sex", Sex);
                    requestBody.put("age", Age);
                    requestBody.put("address", Address);
                    requestBody.put("contact", Contact);
                    requestBody.put("idtype", IdType);
                    requestBody.put("custimage", "avatar.png");
                    requestBody.put("custvalidid", "avatar.png");
                    requestBody.put("custgcashqr", "avatar.png");
                    requestBody.put("email", Email);
                    requestBody.put("password", Password);
                    requestBody.put("customer_image", encodedCustomerImage);
                    requestBody.put("valid_id", encodedValidId);
                    requestBody.put("gcash_qr", encodedGcashQr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Registering ... ", Toast.LENGTH_SHORT).show();

                // Make a POST request to the login API endpoint
                String url = ApiConstants.registerURL; // Replace with your actual login API endpoint
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    // Assuming the API returns a message upon successful login
                                    String message = response.getString("message");
                                    if (message.equals("failed")) {
                                        Toast.makeText(getApplicationContext(), "Email Already Used ", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (message.equals("success")) {
                                        Toast.makeText(getApplicationContext(), "Registered Successfully ", Toast.LENGTH_SHORT).show();
                                        // For example, start a new activity after successful login
                                         startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
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
                        });

                // Create a request queue using Volley.newRequestQueue
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                // Add the request to the Volley request queue
                requestQueue.add(request);


            } else {
                // No image selected
                Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                }

            } else {
                // Passwords do not match
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Please Fill out all fields", Toast.LENGTH_SHORT).show();
        }
    }
    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


}