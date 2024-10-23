package com.pratik.covidvaccination.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pratik.covidvaccination.R;
import com.pratik.covidvaccination.ReadWriteUserDetails;
import com.pratik.covidvaccination.admin.AdminAddSlotData;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class AdminViewAppointmentsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAppointments;
    private ShimmerFrameLayout shimmerLayout;
    private AppointmentAdapter appointmentAdapter;

    private List<ReadWriteUserDetails> appointmentList;

    private List<AdminAddSlotData> adminAddSlotDataList;

    List<AdminVacStatus> adminVacStatusList;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_appointments);

        recyclerViewAppointments = findViewById(R.id.recyclerViewAppointments1);
        shimmerLayout = findViewById(R.id.shimmerLayout);

        recyclerViewAppointments.setHasFixedSize(true);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(this));

        appointmentList = new ArrayList<>();
        adminAddSlotDataList= new ArrayList<>();

        adminVacStatusList = new ArrayList<>();

        appointmentAdapter = new AppointmentAdapter(this, appointmentList,adminAddSlotDataList,adminVacStatusList);

        recyclerViewAppointments.setAdapter(appointmentAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("UserBookings");

        // Start the shimmer effect
        shimmerLayout.startShimmer();

        // Fetch the appointments
        fetchAppointments();
    }

    private void fetchAppointments() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentList.clear();
                adminAddSlotDataList.clear();
                adminVacStatusList.clear();
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot appointmentSnapshot : userSnapshot.getChildren()) {
                            ReadWriteUserDetails appointment = appointmentSnapshot.getValue(ReadWriteUserDetails.class);
                            AdminAddSlotData slotData = appointmentSnapshot.getValue(AdminAddSlotData.class);
                            AdminVacStatus adminVacStatus = appointmentSnapshot.getValue(AdminVacStatus.class);
                            if (appointment != null && slotData!=null) {
                                appointmentList.add(appointment);
                                adminAddSlotDataList.add(slotData);
                                adminVacStatusList.add(adminVacStatus);
                            }
                        }
                    }

                appointmentAdapter.notifyDataSetChanged();

                // Stop shimmer and show RecyclerView once data is loaded
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                recyclerViewAppointments.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminViewAppointmentsActivity.this, "Failed to fetch appointments", Toast.LENGTH_SHORT).show();

                // Stop shimmer and handle error
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                recyclerViewAppointments.setVisibility(View.VISIBLE);
            }
        });
    }

}
