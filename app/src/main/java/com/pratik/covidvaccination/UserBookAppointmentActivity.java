package com.pratik.covidvaccination;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pratik.covidvaccination.admin.AdminAddSlotData;
import com.pratik.covidvaccination.admin.AdminVacStatus;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserBookAppointmentActivity extends AppCompatActivity {

    private RecyclerView recyclerViewHospitals;
    private AppointmentBookingAdapter bookingAdapter;
    private List<AdminAddSlotData> slotList;
    private DatabaseReference databaseReference;
    private EditText etPincode;
    private Button btnSearchHospitals;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_appointment);

        recyclerViewHospitals = findViewById(R.id.recyclerViewHospitals);
        etPincode = findViewById(R.id.etPincode);
        btnSearchHospitals = findViewById(R.id.btnSearchHospitals);

        recyclerViewHospitals.setHasFixedSize(true);
        recyclerViewHospitals.setLayoutManager(new LinearLayoutManager(this));

        slotList = new ArrayList<>();
        bookingAdapter = new AppointmentBookingAdapter(this, slotList, this::showConfirmationDialog);
        recyclerViewHospitals.setAdapter(bookingAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Slot Information");

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        btnSearchHospitals.setOnClickListener(v -> {
            String pincode = etPincode.getText().toString().trim();
            if (!TextUtils.isEmpty(pincode)) {
                searchByPincode(pincode);
            } else {
                Toast.makeText(UserBookAppointmentActivity.this, "Please enter a pincode", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchByPincode(String pincode) {
        try {
            int pinCodeInt = Integer.parseInt(pincode);
            databaseReference.orderByChild("pinCode").equalTo(pinCodeInt)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            slotList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                AdminAddSlotData slot = dataSnapshot.getValue(AdminAddSlotData.class);
                                if (slot != null) {
                                    slotList.add(slot);
                                }
                            }
                            bookingAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(UserBookAppointmentActivity.this, "Failed to search", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (NumberFormatException e) {
            Toast.makeText(UserBookAppointmentActivity.this, "Invalid Pin code format", Toast.LENGTH_SHORT).show();
        }
    }

    private void showConfirmationDialog(int position) {
        AdminAddSlotData slot = slotList.get(position);
        if (slot == null) {
            Toast.makeText(UserBookAppointmentActivity.this, "Slot is not available", Toast.LENGTH_SHORT).show();
            return;
        }
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Appointment");
        builder.setMessage("Are you sure you want to book an appointment? \nTime : Up to 6PM\nDate :"+currentDate);
        builder.setPositiveButton("Yes", (dialog, which) -> bookAppointment(position));
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void bookAppointment(int position) {
        AdminAddSlotData slot = slotList.get(position);
        if (slot == null) {
            Toast.makeText(UserBookAppointmentActivity.this, "Slot is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reference to the slot in the database
        DatabaseReference slotRef = FirebaseDatabase.getInstance().getReference("Slot Information").child(slot.getId());

        // Check the current vaccine count
        slotRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    AdminAddSlotData currentSlot = snapshot.getValue(AdminAddSlotData.class);
                    if (currentSlot == null) {
                        Toast.makeText(UserBookAppointmentActivity.this, "Slot data is not available", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Check if vaccines are available
                    if (currentSlot.getTotalVaccines() <= 0) {
                        Toast.makeText(UserBookAppointmentActivity.this, "No vaccines available for this slot", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Check if user already has any appointment
                    DatabaseReference userBookingsRef = FirebaseDatabase.getInstance().getReference("UserBookings").child(userId);
                    userBookingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userBookingsSnapshot) {
                            boolean hasAppointment = false;
                            for (DataSnapshot bookingSnapshot : userBookingsSnapshot.getChildren()) {
                                AdminAddSlotData bookedSlot = bookingSnapshot.getValue(AdminAddSlotData.class);
                                if (bookedSlot != null) {
                                    hasAppointment = true;
                                    break;
                                }
                            }

                            if (hasAppointment) {
                                Toast.makeText(UserBookAppointmentActivity.this, "You already have a booked appointment", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Proceed with booking if vaccines are available and no existing appointment found
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Registered User").child(userId);

                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        Toast.makeText(UserBookAppointmentActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    ReadWriteUserDetails user = snapshot.getValue(ReadWriteUserDetails.class);
                                    if (user == null) {
                                        Toast.makeText(UserBookAppointmentActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    AdminVacStatus adminVacStatus = snapshot.getValue(AdminVacStatus.class);
                                    if (adminVacStatus==null){
                                        Toast.makeText(UserBookAppointmentActivity.this,"Failed", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    sample sample = snapshot.getValue(com.pratik.covidvaccination.sample.class);

                                    DatabaseReference userBookingRef = FirebaseDatabase.getInstance().getReference("UserBookings").child(userId).child(slot.getId());

                                    // Store the booking details
                                    userBookingRef.child("fullname").setValue(user.getFullname());
                                    userBookingRef.child("dob").setValue(user.getDob());
                                    userBookingRef.child("gender").setValue(user.getGender());
                                    userBookingRef.child("contact").setValue(user.getContact());
                                    userBookingRef.child("pinCode").setValue(slot.getPinCode());
                                    userBookingRef.child("district").setValue(slot.getDistrict());
                                    userBookingRef.child("taluka").setValue(slot.getTaluka());
                                    userBookingRef.child("id").setValue(slot.getId());
                                    userBookingRef.child("name").setValue(slot.getName());
                                    userBookingRef.child("address").setValue(slot.getAddress());
                                    userBookingRef.child("date").setValue(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
                                    userBookingRef.child("vaccineName").setValue(slot.getVaccineName());
                                    userBookingRef.child("uid").setValue(user.getUid());
                                    userBookingRef.child("status").setValue(adminVacStatus.getStatus());
                                    userBookingRef.child("aadhaarNumber").setValue(user.getAadhaarNumber());


                                    // Update the slot vaccines count
                                    slotRef.child("totalVaccines").setValue(currentSlot.getTotalVaccines() - 1).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(UserBookAppointmentActivity.this, "Appointment booked successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(UserBookAppointmentActivity.this, "Failed to book appointment", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(UserBookAppointmentActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(UserBookAppointmentActivity.this, "Failed to check existing appointments", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(UserBookAppointmentActivity.this, "Slot data is not available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserBookAppointmentActivity.this, "Failed to check slot availability", Toast.LENGTH_SHORT).show();
            }
        });
    }


}


