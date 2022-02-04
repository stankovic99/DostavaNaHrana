package com.example.dostavanahrana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.ArrayList;

public class KorisnikPrvActivity extends AppCompatActivity {

    private ArrayList<String> lines = new ArrayList<>();
    private Button logout;
    private Button listaNaNaracki;
    private ListView listaTipProizvod;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korisnik_prv);

        logout = findViewById(R.id.logout);
        listaNaNaracki = findViewById(R.id.listaProdukti);
        listaTipProizvod = findViewById(R.id.listaNaProizvodi);

        FirebaseDatabase.getInstance().getReference("SiteProizvodi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    lines.add(postSnapshot.getKey().toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        KorisnikPrvActivity.this, android.R.layout.simple_list_item_1, lines);
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
                Intent intent = new Intent(KorisnikPrvActivity.this, KorisnikVtorActivity.class);
                intent.putExtra("TipProizvod",tipProizvod);
                startActivity(intent);
            }
        });

        listaNaNaracki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KorisnikPrvActivity.this, KorisnikMoiNarackiActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(KorisnikPrvActivity.this, MainActivity.class));
            }
        });
    }
}