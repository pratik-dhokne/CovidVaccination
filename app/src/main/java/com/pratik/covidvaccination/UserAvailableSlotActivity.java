package com.pratik.covidvaccination;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.pratik.covidvaccination.admin.AdminAddSlotData;

import java.util.ArrayList;
import java.util.List;

public class UserAvailableSlotActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserSlotAdapter slotAdapter;
    private List<AdminAddSlotData> slotList;
    private DatabaseReference databaseReference;
    private EditText etSearchPincode;
    private ImageButton btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_available_slot);

        recyclerView = findViewById(R.id.recyclerView);
        etSearchPincode = findViewById(R.id.etSearchPincode);
        btnSearch = findViewById(R.id.btnSearch);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        slotList = new ArrayList<>();
        slotAdapter = new UserSlotAdapter(this, slotList);
        recyclerView.setAdapter(slotAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Slot Information");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pincode = etSearchPincode.getText().toString().trim();
                if (!TextUtils.isEmpty(pincode)) {
                    searchByPincode(pincode);
                } else {
                    Toast.makeText(UserAvailableSlotActivity.this, "Please enter a pincode", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Load all slots initially
        loadAllSlots();
    }

    private void loadAllSlots() {
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
                Toast.makeText(UserAvailableSlotActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchByPincode(String pincode) {
        databaseReference.orderByChild("pinCode").equalTo(Integer.parseInt(pincode)).addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(UserAvailableSlotActivity.this, "Failed to search", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
