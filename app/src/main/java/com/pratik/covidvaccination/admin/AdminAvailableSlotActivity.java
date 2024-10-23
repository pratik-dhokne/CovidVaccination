package com.pratik.covidvaccination.admin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pratik.covidvaccination.R;

import java.util.ArrayList;
import java.util.List;

public class AdminAvailableSlotActivity extends AppCompatActivity implements SlotAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private SlotAdapter slotAdapter;
    private List<AdminAddSlotData> slotList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_available_slot);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        slotList = new ArrayList<>();
        slotAdapter = new SlotAdapter(this, slotList, this);
        recyclerView.setAdapter(slotAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Slot Information");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                slotList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AdminAddSlotData slot = dataSnapshot.getValue(AdminAddSlotData.class);
                    slotList.add(slot);
                }
                slotAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminAvailableSlotActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                Log.e("AvailableSlotActivity", "onCancelled: ", error.toException());
            }
        });

        // Apply window insets to handle status bar and navigation bar padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    @Override
    public void onUpdateClick(int position) {
        AdminAddSlotData slot = slotList.get(position);
        showUpdateDialog(slot.getId(), slot.getName(), slot.getAddress(), slot.getDistrict(), slot.getTaluka(), slot.getTotalVaccines(), slot.getPinCode(), slot.getVaccineName(), slot.getState());
    }

    @Override
    public void onDeleteClick(int position) {
        AdminAddSlotData slot = slotList.get(position);
        deleteSlot(slot.getId());
    }

    private void showUpdateDialog(final String slotId, String slotName, String slotAddress, String slotDistrict, String slotTaluka, int totalVaccines, int pinCode, String vaccineName, String state) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText etUpdateName = dialogView.findViewById(R.id.etUpdateName);
        final EditText etUpdateAddress = dialogView.findViewById(R.id.etUpdateAddress);
        final EditText etUpdateDistrict = dialogView.findViewById(R.id.etUpdateDistrict);
        final EditText etUpdateTaluka = dialogView.findViewById(R.id.etUpdateTaluka);
        final EditText etUpdateTotalVaccines = dialogView.findViewById(R.id.etUpdateTotalVaccines);
        final EditText etUpdatePinCode = dialogView.findViewById(R.id.etUpdatePinCode);
        final EditText etUpdateVaccineName = dialogView.findViewById(R.id.etUpdateVaccineName);
        final EditText etStateUpdate = dialogView.findViewById(R.id.etStateUpdate);

        etUpdateName.setText(slotName);
        etUpdateAddress.setText(slotAddress);
        etUpdateDistrict.setText(slotDistrict);
        etUpdateTaluka.setText(slotTaluka);
        etUpdateTotalVaccines.setText(String.valueOf(totalVaccines));
        etUpdatePinCode.setText(String.valueOf(pinCode));
        etUpdateVaccineName.setText(vaccineName);
        etStateUpdate.setText(state);

        dialogBuilder.setTitle("Update Slot");
        dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etUpdateName.getText().toString().trim();
                String address = etUpdateAddress.getText().toString().trim();
                String district = etUpdateDistrict.getText().toString().trim();
                String taluka = etUpdateTaluka.getText().toString().trim();
                int totalVaccines = Integer.parseInt(etUpdateTotalVaccines.getText().toString().trim());
                int pinCode = Integer.parseInt(etUpdatePinCode.getText().toString().trim());
                String vaccineName = etUpdateVaccineName.getText().toString().trim();
                String state = etStateUpdate.getText().toString().trim();

                if (!name.isEmpty() && !address.isEmpty() && !district.isEmpty() && !taluka.isEmpty() && totalVaccines > 0 && pinCode > 0 && !vaccineName.isEmpty() && !state.isEmpty()) {
                    updateSlot(slotId, name, address, district, taluka, totalVaccines, pinCode, vaccineName,state);
                } else {
                    Toast.makeText(AdminAvailableSlotActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss dialog
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private boolean updateSlot(String id, String name, String address, String district, String taluka, int totalVaccines, int pinCode, String vaccineName,String state) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Slot Information").child(id);
        AdminAddSlotData slot = new AdminAddSlotData(id, name, address, district, taluka, totalVaccines, pinCode, vaccineName,state);
        databaseReference.setValue(slot);
        Toast.makeText(this, "Slot Updated Successfully", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void deleteSlot(String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Slot Information").child(id);
        databaseReference.removeValue();
        Toast.makeText(this, "Slot Deleted Successfully", Toast.LENGTH_SHORT).show();
    }
}
