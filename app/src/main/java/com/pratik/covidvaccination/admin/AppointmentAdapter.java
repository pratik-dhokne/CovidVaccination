//package com.pratik.covidvaccination.admin;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.pratik.covidvaccination.R;
//import com.pratik.covidvaccination.ReadWriteUserDetails;
//
//import java.util.List;
//
//public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
//
//    private Context context;
//    private List<ReadWriteUserDetails> appointmentList;
//    private List<AdminAddSlotData> adminAddSlotDataList;
//    private List<AdminVacStatus> adminVacStatusList;
//
//    private DatabaseReference userBookingsRef;
//
//    public AppointmentAdapter(Context context, List<ReadWriteUserDetails> appointmentList, List<AdminAddSlotData> adminAddSlotDataList, List<AdminVacStatus> adminVacStatusList) {
//        this.context = context;
//        this.appointmentList = appointmentList;
//        this.adminAddSlotDataList = adminAddSlotDataList;
//        this.adminVacStatusList = adminVacStatusList;
//        this.userBookingsRef = FirebaseDatabase.getInstance().getReference("UserBookings");
//    }
//
//    @NonNull
//    @Override
//    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
//        return new AppointmentViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
//        ReadWriteUserDetails appointment = appointmentList.get(position);
//        AdminAddSlotData slotData = adminAddSlotDataList.get(position);
//        AdminVacStatus adminVacStatus = adminVacStatusList.get(position);
//
//        holder.tvSlotId.setText("Slot ID: " + slotData.getId());
//        holder.tvPatientName.setText("Name: " + appointment.getFullname());
//        holder.tvHospitalName.setText("Hospital Name: " + slotData.getName());
//        holder.tvVaccineName.setText("Vaccine Name: " + slotData.getVaccineName());
//        holder.tvDistrict.setText("District: " + slotData.getDistrict());
//        holder.tvpincode.setText("Pin Code: " + slotData.getPinCode());
//        holder.tvContact.setText("Mobile No: " + appointment.getContact());
//        holder.tvUid.setText("User ID: " + appointment.getUid());
//
//        holder.tvstatus.setText("Status: " + adminVacStatus.getStatus());
//
//
//        // Set OnClickListener for the "Mark as Vaccinated" button
//        holder.btnMarkVaccinated.setOnClickListener(v -> {
//            // Show confirmation dialog
//            new AlertDialog.Builder(context)
//                    .setTitle("Confirm Vaccination")
//                    .setMessage("Are you sure you want to mark this as vaccinated?")
//                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                        // Get the user ID and slot ID
//                        String userId = appointment.getUid();
//                        String slotId = slotData.getId();
//
//                        // Update the status in the Firebase database
//                        userBookingsRef.child(userId).child(slotId).child("status").setValue("vaccinated")
//                                .addOnSuccessListener(aVoid -> {
//                                    Toast.makeText(context, "Status updated to Vaccinated", Toast.LENGTH_SHORT).show();
//                                    holder.tvstatus.setText("Status: vaccinated"); // Update the status text in the UI
//                                })
//                                .addOnFailureListener(e -> {
//                                    Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
//                                });
//                    })
//                    .setNegativeButton(android.R.string.no, null)
//                    .show();
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return Math.min(appointmentList.size(), adminAddSlotDataList.size());
//    }
//
//    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
//
//        TextView tvSlotId, tvPatientName, tvHospitalName, tvVaccineName, tvContact, tvDistrict, tvpincode, tvUid, tvstatus;
//        Button btnMarkVaccinated;
//
//        public AppointmentViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvSlotId = itemView.findViewById(R.id.tvSlotIdAdmin);
//            tvHospitalName = itemView.findViewById(R.id.tvHospitalNameAdmin);
//            tvPatientName = itemView.findViewById(R.id.tvPatientNameAdmin);
//            tvDistrict = itemView.findViewById(R.id.tvDistrictAdmin);
//            tvVaccineName = itemView.findViewById(R.id.tvvaccineNameAdmin);
//            tvpincode = itemView.findViewById(R.id.tvPinCodeAdmin);
//            tvContact = itemView.findViewById(R.id.tvContactNoAdmin);
//            tvUid = itemView.findViewById(R.id.tvUserIdAdmin);
//            tvstatus = itemView.findViewById(R.id.tvStatusAdmin);
//            btnMarkVaccinated = itemView.findViewById(R.id.btnMarkVaccinated);
//        }
//    }
//}
package com.pratik.covidvaccination.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pratik.covidvaccination.R;
import com.pratik.covidvaccination.ReadWriteUserDetails;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private Context context;
    private List<ReadWriteUserDetails> appointmentList;
    private List<AdminAddSlotData> adminAddSlotDataList;
    private List<AdminVacStatus> adminVacStatusList;

    private DatabaseReference userBookingsRef;

    public AppointmentAdapter(Context context, List<ReadWriteUserDetails> appointmentList, List<AdminAddSlotData> adminAddSlotDataList, List<AdminVacStatus> adminVacStatusList) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.adminAddSlotDataList = adminAddSlotDataList;
        this.adminVacStatusList = adminVacStatusList;
        this.userBookingsRef = FirebaseDatabase.getInstance().getReference("UserBookings");
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        ReadWriteUserDetails appointment = appointmentList.get(position);
        AdminAddSlotData slotData = adminAddSlotDataList.get(position);
        AdminVacStatus adminVacStatus = adminVacStatusList.get(position);

        holder.tvSlotId.setText("Slot ID : " + slotData.getId());
        holder.tvPatientName.setText("" + appointment.getFullname());
        holder.tvHospitalName.setText("Hospital Name: " + slotData.getName());
        holder.tvVaccineName.setText("Vaccine Name: " + slotData.getVaccineName());
        holder.tvDistrict.setText("District: " + slotData.getDistrict());
        holder.tvpincode.setText("Pin Code: " + slotData.getPinCode());
        holder.tvContact.setText("Mobile No: " + appointment.getContact());
        holder.tvUid.setText("User ID: " + appointment.getUid());

        String status = adminVacStatus.getStatus();
        holder.tvstatus.setText("Status : " + status);

        // Hide the "Mark as Vaccinated" button if the user is already vaccinated
        if ("vaccinated".equalsIgnoreCase(status)) {
            holder.btnMarkVaccinated.setVisibility(View.GONE);
        } else {
            holder.btnMarkVaccinated.setVisibility(View.VISIBLE);

            // Set OnClickListener for the "Mark as Vaccinated" button
            holder.btnMarkVaccinated.setOnClickListener(v -> {
                // Show dialog to enter last 4 digits of Aadhaar
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Verification");

                // Set up the input
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setHint("Enter last 4 digits of Aadhaar");
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Verify", (dialog, which) -> {
                    String enteredDigits = input.getText().toString();

                    // Check if the input is empty
                    if (enteredDigits.isEmpty()) {
                        Toast.makeText(context, "Please enter the last 4 digits of Aadhaar", Toast.LENGTH_SHORT).show();
                        return; // Do nothing further if the input is empty
                    }

                    // Verify if the entered digits match the last 4 digits of the user's Aadhaar
                    if (enteredDigits.length() == 4 && enteredDigits.equals(appointment.getAadhaarNumber().substring(8))) {
                        String userId = appointment.getUid();
                        String slotId = slotData.getId();

                        // Check if the user is already vaccinated
                        userBookingsRef.child(userId).child(slotId).child("status").get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String currentStatus = task.getResult().getValue(String.class);

                                if ("vaccinated".equalsIgnoreCase(currentStatus)) {
                                    Toast.makeText(context, "This user is already vaccinated.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Update the status in the Firebase database
                                    userBookingsRef.child(userId).child(slotId).child("status").setValue("vaccinated")
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(context, "Status updated to Vaccinated", Toast.LENGTH_SHORT).show();
                                                holder.tvstatus.setText("Status : vaccinated"); // Update the status text in the UI
                                                holder.btnMarkVaccinated.setVisibility(View.GONE); // Hide the button after vaccination
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
                                            });
                                }
                            } else {
                                Toast.makeText(context, "Failed to check current status", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(context, "Verification failed. Please check the Aadhaar number.", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                builder.show();
            });
        }
    }


    @Override
    public int getItemCount() {
        return Math.min(appointmentList.size(), adminAddSlotDataList.size());
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {

        TextView tvSlotId, tvPatientName, tvHospitalName, tvVaccineName, tvContact, tvDistrict, tvpincode, tvUid, tvstatus;
        Button btnMarkVaccinated;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSlotId = itemView.findViewById(R.id.tvSlotIdAdmin);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalNameAdmin);
            tvPatientName = itemView.findViewById(R.id.tvPatientNameAdmin);
            tvDistrict = itemView.findViewById(R.id.tvDistrictAdmin);
            tvVaccineName = itemView.findViewById(R.id.tvvaccineNameAdmin);
            tvpincode = itemView.findViewById(R.id.tvPinCodeAdmin);
            tvContact = itemView.findViewById(R.id.tvContactNoAdmin);
            tvUid = itemView.findViewById(R.id.tvUserIdAdmin);
            tvstatus = itemView.findViewById(R.id.tvStatusAdmin);
            btnMarkVaccinated = itemView.findViewById(R.id.btnMarkVaccinated);
        }
    }
}
