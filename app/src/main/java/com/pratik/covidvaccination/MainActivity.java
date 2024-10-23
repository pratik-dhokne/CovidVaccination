package com.pratik.covidvaccination;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends AppCompatActivity {

    LinearLayout precautions;
    LinearLayout aboutus, account, aarogyasetu, nearvaccinationhos, bookapp, logoutuserr,his;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        precautions = findViewById(R.id.precautions);
        aboutus = findViewById(R.id.aboutus);
        account = findViewById(R.id.accoutsection);
        aarogyasetu = findViewById(R.id.aarogyasetu1);
        nearvaccinationhos = findViewById(R.id.nearVaccinationCenter);
        bookapp = findViewById(R.id.bookAppointment);
        logoutuserr = findViewById(R.id.logoutuser);
        his=findViewById(R.id.history);

        precautions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PrecautionsActivity.class);
                startActivity(intent);
            }
        });

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AboutusActivity.class);
                startActivity(intent);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
            }
        });

        aarogyasetu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AarogyaSetuActivity.class);
                startActivity(intent);
            }
        });

        nearvaccinationhos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UserAvailableSlotActivity.class);
                startActivity(i);
            }
        });

        bookapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UserBookAppointmentActivity.class);
                startActivity(i);
            }
        });



        logoutuserr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutConfirmationDialog();
            }
        });



        his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),UserViewAppointmentActivity.class);
                startActivity(i);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showLogoutConfirmationDialog() {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        // Set the message and title
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        // Set positive button (Yes) and its click listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Log out the user
                logOut();
            }
        });

        // Set negative button (No) and its click listener
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut(); // Sign out the user
        Intent intent = new Intent(MainActivity.this, LoginActivity.class); // Redirect to login activity
        startActivity(intent);
        finish(); // Close the current activity
    }
}
