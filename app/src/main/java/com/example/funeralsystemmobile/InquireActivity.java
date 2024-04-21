package com.example.funeralsystemmobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

public class InquireActivity extends AppCompatActivity {
    Calendar calendar;
    private EditText fname, mname, lname, relationship, causeofdeath, sex, religion,
            placeofdeath, citizenship, address, occupation,nameFather, nameMother, idType;
    private EditText standingflowers, lights, candleStand, flooringFlowers, cross, tarpaulin, curtains, candles, balloons, message,
            note, locationFrom, locationTo, nameCemetery, addressCemetery, obituaryDescription, valuableProperty;

    private RadioGroup radioGroupDeathLocation;
    private RadioGroup radioGroupCasketSize, radioGroupRemoveGlass, radioGroupAddLights,
            radioGroupClothesSize, radioGroupInjectFormalin, radioGroupAvailableHearses,
            radioGroupHaveMakeup, radioGroupCremated;
    private String optionDeath;
    private String casketSize, removeGlass, addLights, clothesSize, injectFormalin,
            availableHearses, haveMakeup, cremated;
    private String sfname,smname,slname,ssex,soccupation,sidType,srelationship,sreligion,saddress,scitizenship,
        snameMother,snameFather,scauseofdeath,splaceofdeath,sstandingflowers,slights,scandleStand,
        sflooringFlowers,scross,starpaulin,scurtains,scandles,sballoons,smessage,snote,
        slocationFrom,slocationTo,snameCemetery,saddressCemetery,sobituaryDescription,svaluableProperty;

    private String deceasedID, orderID;
    private EditText editTextDateOfBirth;
    private EditText editTextDateOfDeath;
    private EditText editTextWakeFrom;
    private EditText editTextWakeTo;
    private Spinner spinnerCivilStatus;
    private Spinner spinnerCategoryOfDeath;
    private Spinner spinnerModeOfPayment;
    private Spinner spinnerSex;
    private String selectedDateOfBirth = "";
    private String selectedDateOfDeath = "";
    private String selectedWakeFrom = "";
    private String selectedWakeTo = "";
    private String selectedCivilStatus;
    private String selectedCategoryOfDeath;
    private String selectedModeOfPayment;
    private Integer custid, packageID;
    private String token, name, price, inclusions;
    private static final int PICK_IMAGE_IMAGE = 100;
    private static final int PICK_IMAGE_VALID_ID = 101;
    private static final int PICK_IMAGE_PROOF_OF_DEATH = 102;
    private static final int PICK_IMAGE_TRANSFER_PERMIT = 103;
    private static final int PICK_IMAGE_SWAB_TEST = 104;
    private static final int PICK_IMAGE_OTHER_DOCUMENTS = 105;
    private static final int PICK_IMAGE_PROOF_OWNERSHIP = 106;
    private static final int PICK_IMAGE_SIGNATURE = 107;
    private TextView textViewImage, textViewValidId, textViewProofOfDeath, textViewTransferPermit,
            textViewSwabTest, textViewOtherDocuments, textViewProofOwnership, textViewUploadSignature;

    // Variables to hold the base64 encoded images
    private String encodedImagee = "", encodedValidId = "", encodedProofOfDeath = "", encodedTransferPermit = "",
            encodedSwabTest = "", encodedOtherDocuments = "", encodedProofOwnership = "", encodedSignature = "";


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
        packageName.setText("Package: " + name);
        packagePrice.setText("Price: " + price);
        packageInclusions.setText("Inclusions: " + inclusions);

        editTextDateOfBirth = findViewById(R.id.dateofbirth);
        editTextDateOfDeath = findViewById(R.id.dateofdeath);
        editTextWakeFrom = findViewById(R.id.wakeFrom);
        editTextWakeTo = findViewById(R.id.wakeTo);
        setupDatePicker(editTextDateOfBirth);
        setupDatePicker(editTextDateOfDeath);
        calendar = Calendar.getInstance();
        editTextWakeFrom.setOnClickListener(v -> showDateTimePicker(editTextWakeFrom));
        editTextWakeTo.setOnClickListener(v -> showDateTimePicker(editTextWakeTo));

        spinnerSex = findViewById(R.id.sexSpinner);
        setupSpinner(spinnerSex, R.array.sex_array);

        spinnerCivilStatus = findViewById(R.id.civilstatus);
        spinnerCategoryOfDeath = findViewById(R.id.categoryDeath);
        spinnerModeOfPayment = findViewById(R.id.modeOfPayment);
        setupSpinner(spinnerCivilStatus, R.array.civil_status_array);
        setupSpinner(spinnerCategoryOfDeath, R.array.category_of_death_array);
        setupSpinner(spinnerModeOfPayment, R.array.mode_of_payment_array);
        hideCardDetails();
        spinnerModeOfPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMode = parent.getItemAtPosition(position).toString();
                if (selectedMode.equals("CARD")) {
                    showCardDetails();
                } else {
                    hideCardDetails();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        radioGroupDeathLocation = findViewById(R.id.radioGroupoptionDeath);
        radioGroupCasketSize = findViewById(R.id.radioGroupCasketSize);
        radioGroupRemoveGlass = findViewById(R.id.radioGroupRemoveGlass);
        radioGroupAddLights = findViewById(R.id.radioGroupAddLights);
        radioGroupClothesSize = findViewById(R.id.radioGroupClothesSize);
        radioGroupInjectFormalin = findViewById(R.id.radioGroupInjectFormalin);
        radioGroupAvailableHearses = findViewById(R.id.radioGroupAvailableHearses);
        radioGroupHaveMakeup = findViewById(R.id.radioGroupHaveMakeup);
        radioGroupCremated = findViewById(R.id.radioGroupCremated);
        deathOptionDefault();
        radioGroupDeathLocation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonHospital) {
                    showHospitalViews();
                } else if (checkedId == R.id.radioButtonHouse) {
                    showHouseViews();
                } else if (checkedId == R.id.radioButtonPublic) {
                    showPublicViews();
                }
            }
        });

//        textViewImage = findViewById(R.id.textViewImage);
//        textViewValidId = findViewById(R.id.textViewValidId);
//        textViewProofOfDeath = findViewById(R.id.textViewProofOfDeath);
//        textViewTransferPermit = findViewById(R.id.textViewTransferPermit);
//        textViewSwabTest = findViewById(R.id.textViewSwabTest);
//        textViewOtherDocuments = findViewById(R.id.textViewOtherDocuments);
//        textViewProofOwnership = findViewById(R.id.textViewProofOwnership);
//        textViewUploadSignature = findViewById(R.id.textViewUploadSignature);

        setButtonClickListener(R.id.buttonChooseImage, PICK_IMAGE_IMAGE);
        setButtonClickListener(R.id.buttonChooseValidId, PICK_IMAGE_VALID_ID);
        setButtonClickListener(R.id.buttonChooseProofOfDeath, PICK_IMAGE_PROOF_OF_DEATH);
        setButtonClickListener(R.id.buttonChooseTransferPermit, PICK_IMAGE_TRANSFER_PERMIT);
        setButtonClickListener(R.id.buttonSwabTest, PICK_IMAGE_SWAB_TEST);
        setButtonClickListener(R.id.buttonOtherDocuments, PICK_IMAGE_OTHER_DOCUMENTS);
        setButtonClickListener(R.id.buttonProofOwnership, PICK_IMAGE_PROOF_OWNERSHIP);
        setButtonClickListener(R.id.buttonUploadSignature, PICK_IMAGE_SIGNATURE);

        fname = findViewById(R.id.fname);
        mname = findViewById(R.id.mname);
        lname = findViewById(R.id.lname);
//        sex = findViewById(R.id.sex);
        occupation = findViewById(R.id.occupation);
        idType = findViewById(R.id.idType);
        relationship = findViewById(R.id.relationship);
        religion = findViewById(R.id.religion);
        address = findViewById(R.id.address);
        citizenship = findViewById(R.id.citizenship);
        nameMother = findViewById(R.id.nameMother);
        nameFather = findViewById(R.id.nameFather);
        causeofdeath = findViewById(R.id.causeofdeath);
        placeofdeath = findViewById(R.id.placeofdeath);

        standingflowers = findViewById(R.id.standingFlowers);
        lights = findViewById(R.id.lights);
        candleStand = findViewById(R.id.candleStand);
        flooringFlowers = findViewById(R.id.flooringFlowers);
        cross = findViewById(R.id.cross);
        tarpaulin = findViewById(R.id.tarpaulin);
        curtains = findViewById(R.id.curtains);
        candles = findViewById(R.id.candles);
        balloons = findViewById(R.id.balloons);

        standingflowers.setText("1");
        lights.setText("1");
        candleStand.setText("1");
        flooringFlowers.setText("1");
        cross.setText("1");
        tarpaulin.setText("1");
        curtains.setText("1");
        candles.setText("1");
        balloons.setText("1");

        standingflowers.setFilters(new InputFilter[]{new QuantityInputFilter()});
        lights.setFilters(new InputFilter[]{new QuantityInputFilter()});
        candleStand.setFilters(new InputFilter[]{new QuantityInputFilter()});
        flooringFlowers.setFilters(new InputFilter[]{new QuantityInputFilter()});
        cross.setFilters(new InputFilter[]{new QuantityInputFilter()});
        tarpaulin.setFilters(new InputFilter[]{new QuantityInputFilter()});
        curtains.setFilters(new InputFilter[]{new QuantityInputFilter()});
        candles.setFilters(new InputFilter[]{new QuantityInputFilter()});
        balloons.setFilters(new InputFilter[]{new QuantityInputFilter()});

        message = findViewById(R.id.message);
        note = findViewById(R.id.note);
        locationFrom = findViewById(R.id.locationFrom);
        locationTo = findViewById(R.id.locationTo);
        nameCemetery = findViewById(R.id.nameCemetery);
        addressCemetery = findViewById(R.id.addressCemetery);
        obituaryDescription = findViewById(R.id.obituaryDescription);
        valuableProperty = findViewById(R.id.valuableProperty);

        EditText cardNumberEditText = findViewById(R.id.CardNumber);
        cardNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this implementation
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString().replaceAll("\\D", ""); // Remove non-digit characters

                // Limit input to 16 characters
                if (text.length() > 16) {
                    text = text.substring(0, 16);
                }

                StringBuilder formattedText = new StringBuilder();

                // Format the text as ****-****-****-****
                for (int i = 0; i < text.length(); i++) {
                    formattedText.append(text.charAt(i));
                    if ((i + 1) % 4 == 0 && (i + 1) < 16) {
                        formattedText.append("-");
                    }
                }

                cardNumberEditText.removeTextChangedListener(this);
                cardNumberEditText.setText(formattedText.toString());
                cardNumberEditText.setSelection(formattedText.length()); // Move cursor to the end
                cardNumberEditText.addTextChangedListener(this);
            }
        });

        EditText expMonthEditText = findViewById(R.id.exp_month);
        expMonthEditText.setFilters(new InputFilter[] {new InquireActivity.InputFilterMinMax("1", "12")});

        EditText editText = findViewById(R.id.CVV);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});

        Button orderPackage = findViewById(R.id.orderPackage);
        orderPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderInquiredPackage();
            }
        });
    }

    private static class InputFilterMinMax implements InputFilter {
        private int min, max;

        InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                String inputStr = dest.toString().substring(0, dstart) + source.toString() + dest.toString().substring(dend);
                int input = Integer.parseInt(inputStr);
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
                // Not a valid number, do nothing
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
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

            if (quantity < 0 || quantity > 1) {
                return ""; // Empty string indicates no change
            }

            return null; // Accept input
        }
    }

    private void showCardDetails() {
        TextView tvCVV = findViewById(R.id.tvCVV);
        TextView tvCardNumber = findViewById(R.id.tvCardNumber);
        TextView tvexp_month = findViewById(R.id.tvexp_month);
        TextView tvexp_year = findViewById(R.id.tvexp_year);
        EditText CVV = findViewById(R.id.CVV);
        EditText CardNumber = findViewById(R.id.CardNumber);
        EditText exp_month = findViewById(R.id.exp_month);
        EditText exp_year = findViewById(R.id.exp_year);
        tvCVV.setVisibility(View.VISIBLE);
        tvCardNumber.setVisibility(View.VISIBLE);
        tvexp_month.setVisibility(View.VISIBLE);
        tvexp_year.setVisibility(View.VISIBLE);
        CVV.setVisibility(View.VISIBLE);
        CardNumber.setVisibility(View.VISIBLE);
        exp_month.setVisibility(View.VISIBLE);
        exp_year.setVisibility(View.VISIBLE);
    }

    private void hideCardDetails() {
        TextView tvCVV = findViewById(R.id.tvCVV);
        TextView tvCardNumber = findViewById(R.id.tvCardNumber);
        TextView tvexp_month = findViewById(R.id.tvexp_month);
        TextView tvexp_year = findViewById(R.id.tvexp_year);
        EditText CVV = findViewById(R.id.CVV);
        EditText CardNumber = findViewById(R.id.CardNumber);
        EditText exp_month = findViewById(R.id.exp_month);
        EditText exp_year = findViewById(R.id.exp_year);
        tvCVV.setVisibility(View.GONE);
        tvCardNumber.setVisibility(View.GONE);
        tvexp_month.setVisibility(View.GONE);
        tvexp_year.setVisibility(View.GONE);
        CVV.setVisibility(View.GONE);
        CardNumber.setVisibility(View.GONE);
        exp_month.setVisibility(View.GONE);
        exp_year.setVisibility(View.GONE);
    }

    private void deathOptionDefault() {

        TextView placeofdeathLabel = findViewById(R.id.placeofdeathLabel);
        EditText placeofdeath = findViewById(R.id.placeofdeath);
        TextView transferPermitLabel = findViewById(R.id.transferPermitLabel);
        Button buttonChooseTransferPermit = findViewById(R.id.buttonChooseTransferPermit);
        TextView swabTestLabel = findViewById(R.id.swabTestLabel);
        Button buttonSwabTest = findViewById(R.id.buttonSwabTest);
        TextView otherDocumentsLabel = findViewById(R.id.otherDocumentsLabel);
        Button buttonOtherDocuments = findViewById(R.id.buttonOtherDocuments);
        // Show views related to hospital
        placeofdeathLabel.setVisibility(View.VISIBLE);
        placeofdeath.setVisibility(View.VISIBLE);
        transferPermitLabel.setVisibility(View.VISIBLE);
        buttonChooseTransferPermit.setVisibility(View.VISIBLE);
        swabTestLabel.setVisibility(View.VISIBLE);
        buttonSwabTest.setVisibility(View.VISIBLE);
        // Show other views related to hospital
        otherDocumentsLabel.setVisibility(View.GONE);
        buttonOtherDocuments.setVisibility(View.GONE);
        // Hide views not related to hospital
    }
    private void showHospitalViews() {

        TextView placeofdeathLabel = findViewById(R.id.placeofdeathLabel);
        EditText placeofdeath = findViewById(R.id.placeofdeath);
        TextView transferPermitLabel = findViewById(R.id.transferPermitLabel);
        Button buttonChooseTransferPermit = findViewById(R.id.buttonChooseTransferPermit);
        TextView swabTestLabel = findViewById(R.id.swabTestLabel);
        Button buttonSwabTest = findViewById(R.id.buttonSwabTest);
        TextView otherDocumentsLabel = findViewById(R.id.otherDocumentsLabel);
        Button buttonOtherDocuments = findViewById(R.id.buttonOtherDocuments);
        // Show views related to hospital
        placeofdeathLabel.setVisibility(View.VISIBLE);
        placeofdeath.setVisibility(View.VISIBLE);
        transferPermitLabel.setVisibility(View.VISIBLE);
        buttonChooseTransferPermit.setVisibility(View.VISIBLE);
        swabTestLabel.setVisibility(View.VISIBLE);
        buttonSwabTest.setVisibility(View.VISIBLE);
        // Show other views related to hospital
        otherDocumentsLabel.setVisibility(View.GONE);
        buttonOtherDocuments.setVisibility(View.GONE);
        // Hide views not related to hospital
    }

    private void showHouseViews() {

        TextView placeofdeathLabel = findViewById(R.id.placeofdeathLabel);
        EditText placeofdeath = findViewById(R.id.placeofdeath);
        TextView transferPermitLabel = findViewById(R.id.transferPermitLabel);
        Button buttonChooseTransferPermit = findViewById(R.id.buttonChooseTransferPermit);
        TextView swabTestLabel = findViewById(R.id.swabTestLabel);
        Button buttonSwabTest = findViewById(R.id.buttonSwabTest);
        TextView otherDocumentsLabel = findViewById(R.id.otherDocumentsLabel);
        Button buttonOtherDocuments = findViewById(R.id.buttonOtherDocuments);
        // Show views related to hospital
        placeofdeathLabel.setVisibility(View.GONE);
        placeofdeath.setVisibility(View.GONE);
        transferPermitLabel.setVisibility(View.VISIBLE);
        buttonChooseTransferPermit.setVisibility(View.VISIBLE);
        swabTestLabel.setVisibility(View.GONE);
        buttonSwabTest.setVisibility(View.GONE);
        // Show other views related to hospital
        otherDocumentsLabel.setVisibility(View.GONE);
        buttonOtherDocuments.setVisibility(View.GONE);
        // Hide views not related to hospital
    }

    private void showPublicViews() {

        TextView placeofdeathLabel = findViewById(R.id.placeofdeathLabel);
        EditText placeofdeath = findViewById(R.id.placeofdeath);
        TextView transferPermitLabel = findViewById(R.id.transferPermitLabel);
        Button buttonChooseTransferPermit = findViewById(R.id.buttonChooseTransferPermit);
        TextView swabTestLabel = findViewById(R.id.swabTestLabel);
        Button buttonSwabTest = findViewById(R.id.buttonSwabTest);
        TextView otherDocumentsLabel = findViewById(R.id.otherDocumentsLabel);
        Button buttonOtherDocuments = findViewById(R.id.buttonOtherDocuments);
        // Show views related to hospital
        placeofdeathLabel.setVisibility(View.VISIBLE);
        placeofdeath.setVisibility(View.VISIBLE);
        transferPermitLabel.setVisibility(View.GONE);
        buttonChooseTransferPermit.setVisibility(View.GONE);
        swabTestLabel.setVisibility(View.GONE);
        buttonSwabTest.setVisibility(View.GONE);
        // Show other views related to hospital
        otherDocumentsLabel.setVisibility(View.VISIBLE);
        buttonOtherDocuments.setVisibility(View.VISIBLE);
        // Hide views not related to hospital
    }


    private void orderInquiredPackage() {
        selectedDateOfBirth = editTextDateOfBirth.getText().toString();
        selectedDateOfDeath = editTextDateOfDeath.getText().toString();
        selectedWakeFrom = editTextWakeFrom.getText().toString();
        selectedWakeTo = editTextWakeTo.getText().toString();

        ssex = spinnerSex.getSelectedItem().toString();
        selectedCivilStatus = spinnerCivilStatus.getSelectedItem().toString();
        selectedCategoryOfDeath = spinnerCategoryOfDeath.getSelectedItem().toString();
        selectedModeOfPayment = spinnerModeOfPayment.getSelectedItem().toString();

        optionDeath = getSelectedOptionText(radioGroupDeathLocation);
        casketSize = getSelectedOptionText(radioGroupCasketSize);
        removeGlass = getSelectedOptionText(radioGroupRemoveGlass);
        addLights = getSelectedOptionText(radioGroupAddLights);
        clothesSize = getSelectedOptionText(radioGroupClothesSize);
        injectFormalin = getSelectedOptionText(radioGroupInjectFormalin);
        availableHearses = getSelectedOptionText(radioGroupAvailableHearses);
        haveMakeup = getSelectedOptionText(radioGroupHaveMakeup);
        cremated = getSelectedOptionText(radioGroupCremated);

         sfname = fname.getText().toString().trim();
         smname = mname.getText().toString().trim();
         slname = lname.getText().toString().trim();
//         ssex = sex.getText().toString().trim();
        //selectedDateOfBirth
         soccupation = occupation.getText().toString().trim();
        //encodedImagee
         sidType = idType.getText().toString().trim();
        //encodedValidId
         srelationship = relationship.getText().toString().trim();
         sreligion = religion.getText().toString().trim();
         saddress = address.getText().toString().trim();
         scitizenship = citizenship.getText().toString().trim();
        //selectedCivilStatus
         snameMother = nameMother.getText().toString().trim();
         snameFather = nameFather.getText().toString().trim();

        //optionDeath
        //selectedCategoryOfDeath
        //selectedDateOfDeath
         scauseofdeath = causeofdeath.getText().toString().trim();
        //encodedProofOfDeath
         splaceofdeath = placeofdeath.getText().toString().trim();
        //encodedTransferPermit
        //encodedSwabTest
        //encodedOtherDocuments

        //casketSize
        //removeGlass
        //addLights
        //clothesSize
        //injectFormalin
        //availableHearses
        //haveMakeup
        //cremated
         sstandingflowers = standingflowers.getText().toString().trim();
         slights = lights.getText().toString().trim();
         scandleStand = candleStand.getText().toString().trim();
         sflooringFlowers = flooringFlowers.getText().toString().trim();
         scross = cross.getText().toString().trim();
         starpaulin = tarpaulin.getText().toString().trim();
         scurtains = curtains.getText().toString().trim();
         scandles = candles.getText().toString().trim();
         sballoons = balloons.getText().toString().trim();
         smessage = message.getText().toString().trim();
         snote = note.getText().toString().trim();

         slocationFrom = locationFrom.getText().toString().trim();
         slocationTo = locationTo.getText().toString().trim();
        //selectedWakeFrom
        //selectedWakeTo
         snameCemetery = nameCemetery.getText().toString().trim();
         saddressCemetery = addressCemetery.getText().toString().trim();
         sobituaryDescription = obituaryDescription.getText().toString().trim();

         svaluableProperty = valuableProperty.getText().toString().trim();
        //encodedProofOwnership
        //encodedSignature
        //selectedModeOfPayment

        if (!sfname.isEmpty() && !smname.isEmpty() && !slname.isEmpty() && !ssex.isEmpty() && !selectedDateOfBirth.isEmpty()
                && !soccupation.isEmpty() && !encodedImagee.isEmpty() && !sidType.isEmpty()
                && !srelationship.isEmpty() && !sreligion.isEmpty() && !saddress.isEmpty() && !scitizenship.isEmpty()
                && !snameMother.isEmpty() && !snameFather.isEmpty()

                && !selectedCategoryOfDeath.isEmpty() && !selectedDateOfDeath.isEmpty()
                && !scauseofdeath.isEmpty() && !encodedProofOfDeath.isEmpty()

                && !sstandingflowers.isEmpty() && !slights.isEmpty() && !scandleStand.isEmpty()
                && !sflooringFlowers.isEmpty() && !scross.isEmpty() && !starpaulin.isEmpty()
                && !scurtains.isEmpty() && !scandles.isEmpty() && !sballoons.isEmpty()
                && !smessage.isEmpty() && !snote.isEmpty()

                && !slocationFrom.isEmpty() && !slocationTo.isEmpty() && !selectedWakeFrom.isEmpty()
                && !selectedWakeTo.isEmpty() && !snameCemetery.isEmpty() && !saddressCemetery.isEmpty()
                && !sobituaryDescription.isEmpty()

                && !svaluableProperty.isEmpty() && !encodedProofOwnership.isEmpty() && !encodedSignature.isEmpty()
                && !selectedModeOfPayment.isEmpty()
        ) {

            EditText CVV = findViewById(R.id.CVV);
            EditText CardNumber = findViewById(R.id.CardNumber);
            EditText exp_month = findViewById(R.id.exp_month);
            EditText exp_year = findViewById(R.id.exp_year);

            String sCVV = CVV.getText().toString().trim();
            String sCardNumber = CardNumber.getText().toString().replaceAll("-", "");
            String sexp_month = exp_month.getText().toString().trim();
            String sexp_year = exp_year.getText().toString().trim();

            if (selectedModeOfPayment.equals("CARD")) {
                if (sCardNumber.isEmpty() || sexp_month.isEmpty() || sexp_year.isEmpty() || sCVV.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Card Details Required ", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            createOrder();


        } else {
            Toast.makeText(getApplicationContext(), "Please Fill out all fields", Toast.LENGTH_SHORT).show();
        }

    }


    private void createOrder(){

        EditText CVV = findViewById(R.id.CVV);
        EditText CardNumber = findViewById(R.id.CardNumber);
        EditText exp_month = findViewById(R.id.exp_month);
        EditText exp_year = findViewById(R.id.exp_year);

        String sCVV = CVV.getText().toString().trim();
        String sCardNumber = CardNumber.getText().toString().replaceAll("-", "");
        String sexp_month = exp_month.getText().toString().trim();
        String sexp_year = exp_year.getText().toString().trim();

        JSONObject requestBody = new JSONObject();
        try {

            requestBody.put("customerID", custid);
            requestBody.put("fname", sfname);
            requestBody.put("mname", smname);
            requestBody.put("lname", slname);
            requestBody.put("sex", ssex);
            requestBody.put("dateOfBirth", selectedDateOfBirth);
            requestBody.put("occupation", soccupation);
//            requestBody.put("image", encodedImagee);
//                requestBody.put("image", "encodedImagee");
            requestBody.put("idType", sidType);
//            requestBody.put("validId", encodedValidId);
//                requestBody.put("validId", "encodedValidId");
            requestBody.put("relationship", srelationship);
            requestBody.put("religion", sreligion);
            requestBody.put("address", saddress);
            requestBody.put("citizenship", scitizenship);
            requestBody.put("civilStatus", selectedCivilStatus);
            requestBody.put("nameMother", snameMother);
            requestBody.put("nameFather", snameFather);

            requestBody.put("optionDeath", optionDeath);
            requestBody.put("categoryOfDeath", selectedCategoryOfDeath);
            requestBody.put("dateOfDeath", selectedDateOfDeath);
            requestBody.put("causeofdeath", scauseofdeath);
//            requestBody.put("proofOfDeath", encodedProofOfDeath);
//                requestBody.put("proofOfDeath", "encodedProofOfDeath");
            requestBody.put("placeofdeath", splaceofdeath);
//            requestBody.put("transferPermit", encodedTransferPermit);
//            requestBody.put("swabTest", encodedSwabTest);
//            requestBody.put("otherDocuments", encodedOtherDocuments);
//                requestBody.put("transferPermit", "encodedTransferPermit");
//                requestBody.put("swabTest", "encodedSwabTest");
//                requestBody.put("otherDocuments", "encodedOtherDocuments");

            requestBody.put("casketSize", casketSize);
            requestBody.put("removeGlass", removeGlass);
            requestBody.put("addLights", addLights);
            requestBody.put("clothesSize", clothesSize);
            requestBody.put("injectFormalin", injectFormalin);
            requestBody.put("availHearses", availableHearses);
            requestBody.put("haveMakeup", haveMakeup);
            requestBody.put("cremated", cremated);
            requestBody.put("standingflowers", sstandingflowers);
            requestBody.put("lights", slights);
            requestBody.put("scandleStand", scandleStand);
            requestBody.put("flooringFlowers", sflooringFlowers);
            requestBody.put("cross", scross);
            requestBody.put("tarpaulin", starpaulin);
            requestBody.put("curtains", scurtains);
            requestBody.put("candles", scandles);
            requestBody.put("balloons", sballoons);
            requestBody.put("message", smessage);
            requestBody.put("note", snote);

            requestBody.put("locationFrom", slocationFrom);
            requestBody.put("locationTo", slocationTo);
            requestBody.put("wakeFrom", selectedWakeFrom);
            requestBody.put("wakeTo", selectedWakeTo);
            requestBody.put("nameCemetery", snameCemetery);
            requestBody.put("addressCemetery", saddressCemetery);
            requestBody.put("obituaryDescription", sobituaryDescription);

            requestBody.put("valuableProperty", svaluableProperty);
//            requestBody.put("proofOwnership", encodedProofOwnership);
//            requestBody.put("signature", encodedSignature);
//                requestBody.put("proofOwnership", "encodedProofOwnership");
//                requestBody.put("signature", "encodedSignature");
            requestBody.put("MOP", selectedModeOfPayment);

            requestBody.put("price", price);
            requestBody.put("amount", price);
            requestBody.put("packageID", packageID);

            if (selectedModeOfPayment.equals("CARD")) {
                requestBody.put("card_number", sCardNumber);
                requestBody.put("exp_month", sexp_month);
                requestBody.put("exp_year", sexp_year);
                requestBody.put("cvc", sCVV);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("POTANGINA", String.valueOf(requestBody));
        // Make a POST request to the login API endpoint
        String url = ApiConstants.inquirePackageURL; // Replace with your actual login API endpoint
        Toast.makeText(getApplicationContext(), "Adding ... ", Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Assuming the API returns a message upon successful add
                            String message = response.getString("message");
                            deceasedID = response.getString("deceasedID");
                            orderID = response.getString("orderID");
                            if (deceasedID != null || orderID != null) {
//                                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                                Log.d("WHAAAAAAAAAA", "imagevalidid: ");
                                imagevalidid();
                                Log.d("WHAAAAAAAAAA", "imagevalidid: done ");
                                Log.d("WHAAAAAAAAAA", "proofofdeathtransferpermit:  ");
//                                Toast.makeText(getApplicationContext(), "proofofdeathtransferpermit", Toast.LENGTH_SHORT).show();
                                proofofdeathtransferpermit();
                                Log.d("WHAAAAAAAAAA", "proofofdeathtransferpermit: done ");
                                Log.d("WHAAAAAAAAAA", "swabtestotherdocuments:  ");
//                                Toast.makeText(getApplicationContext(), "swabtestotherdocuments", Toast.LENGTH_SHORT).show();
                                swabtestotherdocuments();
                                Log.d("WHAAAAAAAAAA", "swabtestotherdocuments: done ");
                                Log.d("WHAAAAAAAAAA", "proofofownershipsignature: ");
//                                Toast.makeText(getApplicationContext(), "proofofownershipsignature", Toast.LENGTH_SHORT).show();
                                proofofownershipsignature();
                                Log.d("WHAAAAAAAAAA", "proofofownershipsignature: done ");
                                Toast.makeText(getApplicationContext(), "Inquired Successfully", Toast.LENGTH_SHORT).show();
                            }
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
    }

    private void imagevalidid(){
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("deceasedID", deceasedID);
            if (encodedImagee != null && !encodedImagee.isEmpty()) {
                requestBody.put("image", encodedImagee);
            } else {
                requestBody.put("image", null); // or remove this line if you want to omit null values
            }

            if (encodedValidId != null && !encodedValidId.isEmpty()) {
                requestBody.put("validId", encodedValidId);
            } else {
                requestBody.put("validId", null); // or remove this line if you want to omit null values
            }
//            requestBody.put("proofOfDeath", encodedProofOfDeath);
//            requestBody.put("transferPermit", encodedTransferPermit);
//            requestBody.put("swabTest", encodedSwabTest);
//            requestBody.put("otherDocuments", encodedOtherDocuments);
//            requestBody.put("proofOwnership", encodedProofOwnership);
//            requestBody.put("signature", encodedSignature);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make a POST request to the login API endpoint
        String url = ApiConstants.imagevalidid; // Replace with your actual login API endpoint
        Toast.makeText(getApplicationContext(), "Adding ... ", Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Assuming the API returns a message upon successful add
                            String message = response.getString("message");
                            // For example, start a new activity after successful add
//                            startActivity(new Intent(InquireActivity.this, ProfileActivity.class));
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
    }

    private void proofofdeathtransferpermit(){
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("deceasedID", deceasedID);
//            requestBody.put("image", encodedImagee);
//            requestBody.put("validId", encodedValidId);
            if (encodedProofOfDeath != null && !encodedProofOfDeath.isEmpty()) {
                requestBody.put("proofOfDeath", encodedProofOfDeath);
            } else {
                requestBody.put("proofOfDeath", null); // or remove this line if you want to omit null values
            }

            if (encodedTransferPermit != null && !encodedTransferPermit.isEmpty()) {
                requestBody.put("transferPermit", encodedTransferPermit);
            } else {
                requestBody.put("transferPermit", null); // or remove this line if you want to omit null values
            }
//            requestBody.put("swabTest", encodedSwabTest);
//            requestBody.put("otherDocuments", encodedOtherDocuments);
//            requestBody.put("proofOwnership", encodedProofOwnership);
//            requestBody.put("signature", encodedSignature);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make a POST request to the login API endpoint
        String url = ApiConstants.proofofdeathtransferpermit; // Replace with your actual login API endpoint
        Toast.makeText(getApplicationContext(), "Adding ... ", Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Assuming the API returns a message upon successful add
                            String message = response.getString("message");
                            // For example, start a new activity after successful add
//                            startActivity(new Intent(InquireActivity.this, ProfileActivity.class));
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
    }

    private void swabtestotherdocuments(){
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("deceasedID", deceasedID);
//            requestBody.put("image", encodedImagee);
//            requestBody.put("validId", encodedValidId);
//            requestBody.put("proofOfDeath", encodedProofOfDeath);
//            requestBody.put("transferPermit", encodedTransferPermit);
            if (encodedSwabTest != null && !encodedSwabTest.isEmpty()) {
                requestBody.put("swabTest", encodedSwabTest);
            } else {
                requestBody.put("swabTest", null); // or remove this line if you want to omit null values
            }

            if (encodedOtherDocuments != null && !encodedOtherDocuments.isEmpty()) {
                requestBody.put("otherDocuments", encodedOtherDocuments);
            } else {
                requestBody.put("otherDocuments", null); // or remove this line if you want to omit null values
            }
//            requestBody.put("proofOwnership", encodedProofOwnership);
//            requestBody.put("signature", encodedSignature);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make a POST request to the login API endpoint
        String url = ApiConstants.swabtestotherdocuments; // Replace with your actual login API endpoint
        Toast.makeText(getApplicationContext(), "Adding ... ", Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Assuming the API returns a message upon successful add
                            String message = response.getString("message");
                            // For example, start a new activity after successful add
//                            startActivity(new Intent(InquireActivity.this, ProfileActivity.class));
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
    }

    private void proofofownershipsignature(){
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("orderID", orderID);
//            requestBody.put("image", encodedImagee);
//            requestBody.put("validId", encodedValidId);
//            requestBody.put("proofOfDeath", encodedProofOfDeath);
//            requestBody.put("transferPermit", encodedTransferPermit);
//            requestBody.put("swabTest", encodedSwabTest);
//            requestBody.put("otherDocuments", encodedOtherDocuments);
            if (encodedProofOwnership != null && !encodedProofOwnership.isEmpty()) {
                requestBody.put("proofOwnership", encodedProofOwnership);
            } else {
                requestBody.put("proofOwnership", null); // or remove this line if you want to omit null values
            }

            if (encodedSignature != null && !encodedSignature.isEmpty()) {
                requestBody.put("signature", encodedSignature);
            } else {
                requestBody.put("signature", null); // or remove this line if you want to omit null values
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make a POST request to the login API endpoint
        String url = ApiConstants.proofofownershipsignature; // Replace with your actual login API endpoint
        Toast.makeText(getApplicationContext(), "Adding ... ", Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Assuming the API returns a message upon successful add
                            String message = response.getString("message");
                            // For example, start a new activity after successful add
//                            startActivity(new Intent(InquireActivity.this, ProfileActivity.class));
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

    private void setupDatePicker(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Note: month is zero-based
                String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, month + 1, year);
                editText.setText(selectedDate);
            }
        };

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(InquireActivity.this, dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void showDateTimePicker(EditText editText) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(InquireActivity.this,
                            (view1, selectedHour, selectedMinute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                                calendar.set(Calendar.MINUTE, selectedMinute);
                                editText.setText(String.format("%02d-%02d-%d %02d:%02d", selectedDay, selectedMonth + 1, selectedYear, selectedHour, selectedMinute));
                            }, hour, minute, true);
                    timePickerDialog.show();
                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void setupSpinner(Spinner spinner, int arrayResourceId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                arrayResourceId,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private String getSelectedOptionText(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        return radioButton != null ? radioButton.getText().toString() : "Not selected";
    }

    private void setButtonClickListener(int buttonId, final int requestCode) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser(requestCode);
            }
        });
    }

    private void openImageChooser(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    String encodedImage = encodeImage(selectedImage);
                    switch (requestCode) {
                        case PICK_IMAGE_IMAGE:
                            ImageView imageViewPreview = findViewById(R.id.imageViewPreview);
                            imageViewPreview.setImageBitmap(selectedImage);
                            encodedImagee = encodedImage;
                            break;
                        case PICK_IMAGE_VALID_ID:
                            ImageView ValidIdViewPreview = findViewById(R.id.ValidIdViewPreview);
                            ValidIdViewPreview.setImageBitmap(selectedImage);
                            encodedValidId = encodedImage;
                            break;
                        case PICK_IMAGE_PROOF_OF_DEATH:
                            ImageView ProofOfDeathViewPreview = findViewById(R.id.ProofOfDeathViewPreview);
                            ProofOfDeathViewPreview.setImageBitmap(selectedImage);
                            encodedProofOfDeath = encodedImage;
                            break;
                        case PICK_IMAGE_TRANSFER_PERMIT:
                            ImageView TransferPermitViewPreview = findViewById(R.id.TransferPermitViewPreview);
                            TransferPermitViewPreview.setImageBitmap(selectedImage);
                            encodedTransferPermit = encodedImage;
                            break;
                        case PICK_IMAGE_SWAB_TEST:
                            ImageView SwabTestViewPreview = findViewById(R.id.SwabTestViewPreview);
                            SwabTestViewPreview.setImageBitmap(selectedImage);
                            encodedSwabTest = encodedImage;
                            break;
                        case PICK_IMAGE_OTHER_DOCUMENTS:
                            ImageView OtherDocumentsViewPreview = findViewById(R.id.OtherDocumentsViewPreview);
                            OtherDocumentsViewPreview.setImageBitmap(selectedImage);
                            encodedOtherDocuments = encodedImage;
                            break;
                        case PICK_IMAGE_PROOF_OWNERSHIP:
                            ImageView ProofOfOwnershipViewPreview = findViewById(R.id.ProofOfOwnershipViewPreview);
                            ProofOfOwnershipViewPreview.setImageBitmap(selectedImage);
                            encodedProofOwnership = encodedImage;
                            break;
                        case PICK_IMAGE_SIGNATURE:
                            ImageView SignatureViewPreview = findViewById(R.id.SignatureViewPreview);
                            SignatureViewPreview.setImageBitmap(selectedImage);
                            encodedSignature = encodedImage;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}