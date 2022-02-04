package com.example.dostavanahrana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminDodadiActivity extends AppCompatActivity {

    private Spinner spinner;
    private EditText imeProizvod, cenaProizvod;
    private Button dodadi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dodadi);

        spinner = findViewById(R.id.spinner);
        imeProizvod = findViewById(R.id.imeProizvodDesno);
        cenaProizvod = findViewById(R.id.cenaProizvodDesno);
        dodadi = findViewById(R.id.dodadiProizvod);

        HashMap<String, Object> map = new HashMap<>();

        dodadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.put("CenaProizvod",cenaProizvod.getText().toString());
                map.put("ImeProizvod",imeProizvod.getText().toString());
                FirebaseDatabase.getInstance().getReference("SiteProizvodi").child(spinner.getSelectedItem().toString()).push().updateChildren(map);
                startActivity(new Intent(AdminDodadiActivity.this, AdminPrvActivity.class));
            }
        });
    }
}