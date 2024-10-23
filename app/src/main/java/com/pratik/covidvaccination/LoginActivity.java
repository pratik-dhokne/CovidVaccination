package com.pratik.covidvaccination;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.pratik.covidvaccination.admin.AdminActivity;


public class LoginActivity extends AppCompatActivity {

    TextView txtsignup;
    EditText editusername, editpassword;

    Button btnlogin;

    TextView tempdl;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);


        editusername=findViewById(R.id.auto_email);
        editpassword=findViewById(R.id.password);
        txtsignup=findViewById(R.id.signup_link);

        progressBar=findViewById(R.id.progresslogin);
        btnlogin=findViewById(R.id.login_button);

        mAuth = FirebaseAuth.getInstance();




        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textusername = editusername.getText().toString();
                String textpassword = editpassword.getText().toString();
                if (TextUtils.isEmpty(textusername)){
                    Toast.makeText(LoginActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    editusername.setError("Email required");
                    editusername.requestFocus();

                }
                else if(TextUtils.isEmpty(textpassword)){
                    Toast.makeText(LoginActivity.this, "Please Enter password", Toast.LENGTH_SHORT).show();
                    editpassword.setError("Password required");
                    editpassword.requestFocus();
                }
                else {

                    String username = editusername.getText().toString();
                    String password = editpassword.getText().toString();

                    if (username.equals("admin") && password.equals("admin")) {
                        startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        loginUser(textusername,textpassword);
                    }
                }
            }
        });

        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;


        });
    }

    private void loginUser(String email, String pwd) {
        mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @OptIn(markerClass = UnstableApi.class)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    //Get instance of the current user
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                    if (firebaseUser.isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "Login in Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                    else {
                        firebaseUser.sendEmailVerification();
                        mAuth.signOut();
                        showAlertDialog();
                    }
                }

                else{

                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        editusername.setError("User does not exists or no longer valid! Register your account");
                        editusername.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        editusername.setError("Invalid credentials");
                        editusername.requestFocus();
                    } catch(Exception e){
                        Log.e(TAG,e.getMessage());

                    }
                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {

        //Set up alert
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email is not verified");
        builder.setMessage("Please verify your email now. You cannot login without email verification");

        //Open email apps
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // open app in new windows not within application
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser()!=null){
            Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "You can login now", Toast.LENGTH_SHORT).show();
        }
    }
}