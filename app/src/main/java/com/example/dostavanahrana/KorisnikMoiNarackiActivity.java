package com.example.dostavanahrana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KorisnikMoiNarackiActivity extends AppCompatActivity {

    private ListView moiNaracki;
    private ArrayList<String> lines = new ArrayList<>();
    private ArrayList<String> lines1 = new ArrayList<>();
    private ArrayList<String> lines2 = new ArrayList<>();
    private ArrayList<String> lines3 = new ArrayList<>();
    private ArrayList<String> lines4 = new ArrayList<>();
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korisnik_moi_naracki);

        auth = FirebaseAuth.getInstance();
        String emailKorsnik = auth.getCurrentUser().getEmail().toString();
        moiNaracki = findViewById(R.id.listaMoiNaracki);
        FirebaseDatabase.getInstance().getReference("KorisnickiNaracki").orderByChild("EmailKorisnik").equalTo(emailKorsnik).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    lines4.add(postSnapshot.child("EmailDostavuvac").getValue().toString());
                    lines3.add(postSnapshot.child("ImeDostavuvac").getValue().toString());
                    lines2.add(postSnapshot.child("Status").getValue().toString());
                    lines1.add(postSnapshot.child("ID").getValue().toString());
                    lines.add(postSnapshot.child("ImeProizvod").getValue().toString() + " - " + postSnapshot.child("VkupnaCena").getValue().toString() + " ден.");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        KorisnikMoiNarackiActivity.this, android.R.layout.simple_list_item_1, lines);
                moiNaracki.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        moiNaracki.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = lines1.get(i);
                String status = lines2.get(i);
                String dostavuvac = lines3.get(i);
                String emailDostavuvac = lines4.get(i);
                Intent intent = new Intent(KorisnikMoiNarackiActivity.this, KorisnikDetaliActivity.class);
                intent.putExtra("ID",id);
                intent.putExtra("Status",status);
                intent.putExtra("Dostavuvac",dostavuvac);
                intent.putExtra("EmailDostavuvac",emailDostavuvac);
                startActivity(intent);
            }
        });
    }
}