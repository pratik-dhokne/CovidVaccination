package com.pratik.covidvaccination;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    Button signupbtn;
    EditText editfullname, editemail, editcontactno, editdob, editpassword, editconfirmpassword,editaadharno;
    ProgressBar progressBar;
    RadioGroup radioGroupGender;
    RadioButton radioButtonGenderSelected;
    static final String TAG="SignupActivity";

    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        progressBar = findViewById(R.id.progressbar);
        signupbtn = findViewById(R.id.signup_button);
        editfullname = findViewById(R.id.fullname);
        editemail = findViewById(R.id.email);
        editdob = findViewById(R.id.dateofbirth);
        editcontactno = findViewById(R.id.contactno);
        editpassword = findViewById(R.id.password);
        editconfirmpassword = findViewById(R.id.confirm_password);

        editaadharno = findViewById(R.id.aadharno);



        // radiogroup
        radioGroupGender = findViewById(R.id.gender_group);
        radioGroupGender.clearCheck();

        editdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // Date picker dialog
                picker = new DatePickerDialog(SignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        editdob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
                radioButtonGenderSelected = findViewById(selectedGenderId);

                // obtain the entered data
                String textfullname = editfullname.getText().toString();
                String texteamil = editemail.getText().toString();
                String textdob = editdob.getText().toString();
                String textcontact = editcontactno.getText().toString();
                String textpassword = editpassword.getText().toString();
                String textconfirmpassword = editconfirmpassword.getText().toString();
                String textaadharno = editaadharno.getText().toString();
                String textGender;

                //validate mobile number
                String mobileregex = "[6-9][0-9]{9}";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileregex);
                mobileMatcher = mobilePattern.matcher(textcontact);

                if (TextUtils.isEmpty(textfullname)) {
                    Toast.makeText(SignupActivity.this, "Please Enter full name", Toast.LENGTH_SHORT).show();
                    editfullname.setError("Full name is required");
                    editfullname.requestFocus();
                } else if (TextUtils.isEmpty(texteamil)) {
                    Toast.makeText(SignupActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    editemail.setError("Email is required");
                    editemail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(texteamil).matches()) {
                    Toast.makeText(SignupActivity.this, "Please re-enter email", Toast.LENGTH_SHORT).show();
                    editemail.setError("Valid email is required");
                    editemail.requestFocus();
                } else if (radioGroupGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(SignupActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                    radioGroupGender.requestFocus();
                } else if (TextUtils.isEmpty(textdob)) {
                    Toast.makeText(SignupActivity.this, "Please Enter DOB", Toast.LENGTH_SHORT).show();
                    editdob.setError("DOB is required");
                    editdob.requestFocus();
                } else if (TextUtils.isEmpty(textcontact)) {
                    Toast.makeText(SignupActivity.this, "Please Enter contact no", Toast.LENGTH_SHORT).show();
                    editcontactno.setError("Contact no required");
                    editcontactno.requestFocus();
                } else if (textcontact.length() != 10) {
                    Toast.makeText(SignupActivity.this, "Please re-enter mobile no", Toast.LENGTH_SHORT).show();
                    editcontactno.setError("Mobile no should be 10 digits");
                    editcontactno.requestFocus();
                } else if (!mobileMatcher.find()) {
                    Toast.makeText(SignupActivity.this, "Enter valid contact no", Toast.LENGTH_SHORT).show();
                    editcontactno.setError("Please Enter valid Contact no");
                    editcontactno.requestFocus();
                } else if (TextUtils.isEmpty(textpassword)) {
                    Toast.makeText(SignupActivity.this, "Please Enter password", Toast.LENGTH_SHORT).show();
                    editpassword.setError("Password is required");
                    editpassword.requestFocus();
                } else if (textpassword.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                    editpassword.setError("Enter password properly");
                    editpassword.requestFocus();
                } else if (TextUtils.isEmpty(textconfirmpassword)) {
                    Toast.makeText(SignupActivity.this, "Please Enter confirm password", Toast.LENGTH_SHORT).show();
                    editconfirmpassword.setError("Confirm Password is Required");
                    editconfirmpassword.requestFocus();
                } else if (!textpassword.equals(textconfirmpassword)) {
                    Toast.makeText(SignupActivity.this, "Please enter the same password", Toast.LENGTH_SHORT).show();
                    editconfirmpassword.setError("Passwords do not match");
                    editconfirmpassword.requestFocus();
                    editpassword.clearComposingText();
                    editconfirmpassword.clearComposingText();
                } else {
                    textGender = radioButtonGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textfullname, texteamil, textdob, textcontact, textpassword, textGender, textconfirmpassword, textaadharno);
                }
            }

            private void registerUser(String textfullname, String texteamil, String textdob, String textcontact, String textpassword, String textGender, String textconfirmpassword, String textaadharno) {
                FirebaseAuth auth = FirebaseAuth.getInstance();

                // Create User Profile
                auth.createUserWithEmailAndPassword(texteamil, textpassword).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            // Update Display name of user
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textfullname).build();
                            firebaseUser.updateProfile(profileChangeRequest);

                            // Enter user data into the firebase realtime database;
                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(firebaseUser.getUid(), textfullname, textdob, textGender, textcontact, textaadharno);

                            // Database Reference
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered User");

                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (firebaseUser != null) {
                                            firebaseUser.sendEmailVerification();

                                            Toast.makeText(SignupActivity.this, "Registration Successful. Please Verify your email", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } else {
                                        Toast.makeText(SignupActivity.this, "User Registration Failed!!", Toast.LENGTH_SHORT).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                editpassword.setError("Weak Password! Kindly use a strong password that includes alphabets, numbers, special characters");
                                editpassword.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                editemail.setError("Your email is invalid or already in use");
                                editemail.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                editemail.setError("User is already registered with this email. Use another email");
                                editemail.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
