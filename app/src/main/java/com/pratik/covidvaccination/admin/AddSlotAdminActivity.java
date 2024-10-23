package com.pratik.covidvaccination.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pratik.covidvaccination.R;

public class AddSlotAdminActivity extends AppCompatActivity {

    private EditText etHospitalName, etHospitalAddress, etTaluka, etTotalVaccines, etPinCode, etVaccineName;
    private AutoCompleteTextView etState, etDistrict; // AutoCompleteTextView for state and district
    private Button btnSubmit;
    private DatabaseReference databaseReference;

    // List of Maharashtra districts
    private String[] districts = {
            "Ahmednagar", "Akola", "Amravati", "Aurangabad", "Beed", "Bhandara", "Buldhana", "Chandrapur", "Dhule",
            "Gadchiroli", "Gondia", "Hingoli", "Jalgaon", "Jalna", "Kolhapur", "Latur", "Mumbai City", "Mumbai Suburban",
            "Nagpur", "Nanded", "Nandurbar", "Nashik", "Osmanabad", "Palghar", "Parbhani", "Pune", "Raigad", "Ratnagiri",
            "Sangli", "Satara", "Sindhudurg", "Solapur", "Thane", "Wardha", "Washim", "Yavatmal"
    };

    // List of Indian states
    private String[] states = {
            "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana",
            "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur",
            "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
            "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_slot_admin);

        etHospitalName = findViewById(R.id.etHospitalName);
        etHospitalAddress = findViewById(R.id.etHospitalAddress);
        etTaluka = findViewById(R.id.etTaluka);
        etTotalVaccines = findViewById(R.id.etTotalVaccines);
        etPinCode = findViewById(R.id.etPinCode);
        etVaccineName = findViewById(R.id.etVaccineName);
        etState = findViewById(R.id.etState); // AutoCompleteTextView for State
        etDistrict = findViewById(R.id.etDistrict); // AutoCompleteTextView for District
        btnSubmit = findViewById(R.id.btnSubmit);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Slot Information");

        // Set up the ArrayAdapter for AutoCompleteTextView for states
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, states);
        etState.setAdapter(stateAdapter);
        etState.setThreshold(1); // Starts showing suggestions after 1 character is typed

        // Set up the ArrayAdapter for AutoCompleteTextView for Maharashtra districts
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, districts);
        etDistrict.setAdapter(districtAdapter);
        etDistrict.setThreshold(1); // Starts showing suggestions after 1 character is typed

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSlot();
            }
        });
    }

    private void addSlot() {
        String name = etHospitalName.getText().toString().trim();
        String address = etHospitalAddress.getText().toString().trim();
        String district = etDistrict.getText().toString().trim();
        String taluka = etTaluka.getText().toString().trim();
        String totalVaccines = etTotalVaccines.getText().toString().trim();
        String vaccineName = etVaccineName.getText().toString().trim();
        String pinCode = etPinCode.getText().toString().trim();
        String state = etState.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(district)
                && !TextUtils.isEmpty(taluka) && !TextUtils.isEmpty(totalVaccines) && !TextUtils.isEmpty(vaccineName) && !TextUtils.isEmpty(pinCode) && !TextUtils.isEmpty(state)) {

            String id = databaseReference.push().getKey();
            AdminAddSlotData addSlotData = new AdminAddSlotData(id, name, address, district, taluka, Integer.parseInt(totalVaccines), Integer.parseInt(pinCode), vaccineName, state);

            if (id != null) {
                databaseReference.child(id).setValue(addSlotData).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddSlotAdminActivity.this, "Slot added", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(AddSlotAdminActivity.this, "Failed to add", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        etHospitalName.setText("");
        etHospitalAddress.setText("");
        etDistrict.setText("");
        etTaluka.setText("");
        etTotalVaccines.setText("");
        etPinCode.setText("");
        etVaccineName.setText("");
        etState.setText("");
    }
}
