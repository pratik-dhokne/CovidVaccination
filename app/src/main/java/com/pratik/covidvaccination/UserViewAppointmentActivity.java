package com.pratik.covidvaccination;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pratik.covidvaccination.admin.AdminAddSlotData;
import com.pratik.covidvaccination.admin.AdminVacStatus;

import java.util.ArrayList;
import java.util.List;

public class UserViewAppointmentActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAppointments;
    private UserAppointmentAdapter userAppointmentAdapter;

    private ShimmerFrameLayout shimmerLayout;
    private List<ReadWriteUserDetails> appointmentList;
    private List<AdminAddSlotData> appointmentList1;

    private List<AdminVacStatus> adminVacStatusList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_appointment);

        shimmerLayout = findViewById(R.id.shimmerLayout);

        recyclerViewAppointments = findViewById(R.id.recyclerViewAppointments1);
        recyclerViewAppointments.setHasFixedSize(true);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(this));

        appointmentList = new ArrayList<>();
        appointmentList1 = new ArrayList<>();
        adminVacStatusList = new ArrayList<>();
        userAppointmentAdapter = new UserAppointmentAdapter(this, appointmentList, appointmentList1, adminVacStatusList);
        recyclerViewAppointments.setAdapter(userAppointmentAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("UserBookings");

        shimmerLayout.startShimmer();
        fetchAppointments();
    }

    private void fetchAppointments() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the logged-in user's ID
        DatabaseReference userBookingRef = databaseReference.child(userId);

        userBookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentList.clear();
                appointmentList1.clear();
                adminVacStatusList.clear();
                for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                    ReadWriteUserDetails appointment = appointmentSnapshot.getValue(ReadWriteUserDetails.class);
                    AdminAddSlotData slotData = appointmentSnapshot.getValue(AdminAddSlotData.class);
                    AdminVacStatus adminVacStatus = appointmentSnapshot.getValue(AdminVacStatus.class);
                    if (appointment != null && slotData != null) {
                        appointmentList.add(appointment);
                        appointmentList1.add(slotData);
                        adminVacStatusList.add(adminVacStatus);
                    }
                }
                userAppointmentAdapter.notifyDataSetChanged();


                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                recyclerViewAppointments.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserViewAppointmentActivity.this, "Failed to fetch appointments", Toast.LENGTH_SHORT).show();

                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                recyclerViewAppointments.setVisibility(View.VISIBLE);
            }
        });
    }
}
