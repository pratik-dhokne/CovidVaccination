package com.pratik.covidvaccination;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class AboutusActivity extends AppCompatActivity {

    TextView precautions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aboutus);

        // Link an Particular Websites
        TextView t = findViewById(R.id.aboutpublic);
        t.setMovementMethod(LinkMovementMethod.getInstance());

        TextView a = findViewById(R.id.myth);
        a.setMovementMethod(LinkMovementMethod.getInstance());

        TextView b = findViewById(R.id.questionsans);
        b.setMovementMethod(LinkMovementMethod.getInstance());

        TextView c = findViewById(R.id.situations);
        c.setMovementMethod(LinkMovementMethod.getInstance());



        precautions=findViewById(R.id.precau);

        precautions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),PrecautionsActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}