package com.pratik.covidvaccination;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {

    private TextView textviewwelcome, textViewfullname, textViewemail, textViewgender, textViewdob, textViewcontactno;

    private ProgressBar progressBar;
    private String fullname,email,gender,contactno,dob;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);

        textviewwelcome = findViewById(R.id.text_show_welcome);
        textViewfullname=findViewById(R.id.id_show_full_name);
        textViewemail=findViewById(R.id.id_email);
        textViewgender=findViewById(R.id.id_gender);
        textViewdob=findViewById(R.id.id_dob);
        textViewcontactno=findViewById(R.id.id_mobile);
        
        progressBar=findViewById(R.id.progressbar);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser==null){
            Toast.makeText(this, "The data is not available associated with this account", Toast.LENGTH_SHORT).show();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showUserProfile(FirebaseUser firebaseUser) {

        String userID=firebaseUser.getUid();

        DatabaseReference referenceprofile = FirebaseDatabase.getInstance().getReference("Registered User");
        referenceprofile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (firebaseUser!=null){
                    fullname=firebaseUser.getDisplayName();
                    email=firebaseUser.getEmail();
                    dob=readUserDetails.getDob();
                    gender=readUserDetails.getGender();
                    contactno=readUserDetails.getContact();

                    textviewwelcome.setText("Welcome, " + fullname + "!");
                    textViewfullname.setText(fullname);
                    textViewcontactno.setText(contactno);
                    textViewemail.setText(email);
                    textViewgender.setText(gender);
                    textViewdob.setText(dob);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccountActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}