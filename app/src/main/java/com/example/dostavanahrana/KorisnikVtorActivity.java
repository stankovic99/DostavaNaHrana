package com.example.dostavanahrana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KorisnikVtorActivity extends AppCompatActivity {

    private ArrayList<String> lines = new ArrayList<>();
    private ListView listaProizvodi;
    String tipProizvod, imeProizvod, cenaProizvod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korisnik_vtor);

        Intent intent = getIntent();
        tipProizvod = intent.getStringExtra("TipProizvod");

        listaProizvodi = findViewById(R.id.listaProizvodi);


        FirebaseDatabase.getInstance().getReference("SiteProizvodi").child(tipProizvod).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    lines.add(postSnapshot.child("ImeProizvod").getValue().toString() + " - " + postSnapshot.child("CenaProizvod").getValue().toString() + " ден.");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        KorisnikVtorActivity.this, android.R.layout.simple_list_item_1, lines);
                listaProizvodi.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        listaProizvodi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] tipProizvodi = lines.get(i).split(" - ");
                imeProizvod = tipProizvodi[0];
                cenaProizvod = tipProizvodi[1];
                Intent intent = new Intent(KorisnikVtorActivity.this, KorisnikNarackaActivity.class);
                intent.putExtra("TipProizvod",tipProizvod);
                intent.putExtra("CenaProizvod",cenaProizvod);
                intent.putExtra("ImeProizvod",imeProizvod);
                startActivity(intent);
            }
        });
    }
}