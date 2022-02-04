package com.example.dostavanahrana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdminPrvActivity extends AppCompatActivity {

    private ArrayList<String> lines = new ArrayList<>();
    private Button logout;
    private Button dodadi;
    private Button listaNaracki;
    private ListView listaTipProizvod;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_prv);

        logout = findViewById(R.id.logout);
        dodadi = findViewById(R.id.dodadiProdukt);
        listaNaracki = findViewById(R.id.listaNaracki);
        listaTipProizvod = findViewById(R.id.listaTipProizvod);

        FirebaseDatabase.getInstance().getReference("SiteProizvodi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    lines.add(postSnapshot.getKey().toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        AdminPrvActivity.this, android.R.layout.simple_list_item_1, lines);
                listaTipProizvod.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        listaTipProizvod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tipProizvod = lines.get(i);
                Intent intent = new Intent(AdminPrvActivity.this, AdminVtorActivity.class);
                intent.putExtra("TipProizvod",tipProizvod);
                startActivity(intent);
            }
        });

        dodadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPrvActivity.this, AdminDodadiActivity.class));
            }
        });

        listaNaracki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPrvActivity.this,AdminListaActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(AdminPrvActivity.this,MainActivity.class));
            }
        });
    }
}