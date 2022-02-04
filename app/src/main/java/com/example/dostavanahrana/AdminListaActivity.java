package com.example.dostavanahrana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdminListaActivity extends AppCompatActivity {

    private ListView lista;
    private ArrayList<String> lines = new ArrayList<>();
    private ArrayList<String> lines1= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lista);

        lista = findViewById(R.id.lista);

        FirebaseDatabase.getInstance().getReference("KorisnickiNaracki").orderByChild("Status").equalTo("Zavrsena").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    lines1.add(postSnapshot.child("ID").getValue().toString());
                    lines.add(postSnapshot.child("ImeProizvod").getValue().toString() + " - " + postSnapshot.child("VkupnaCena").getValue().toString() + " ден.");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        AdminListaActivity.this, android.R.layout.simple_list_item_1, lines);
                lista.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = lines1.get(i);
                Intent intent = new Intent(AdminListaActivity.this, AdminDetaliActivity.class);
                FirebaseDatabase.getInstance().getReference("KorisnickiNaracki").orderByChild("ID").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            intent.putExtra("ImeKorisnik", postSnapshot.child("ImeKorisnik").getValue().toString());
                            intent.putExtra("ImeDostavuvac", postSnapshot.child("ImeDostavuvac").getValue().toString());
                            intent.putExtra("RejtingZaKorisnik", postSnapshot.child("RejtingZaKorisnik").getValue().toString());
                            intent.putExtra("RejtingZaDostavuvac", postSnapshot.child("RejtingZaDostavuvac").getValue().toString());
                            intent.putExtra("RejtingNaKorisnik", postSnapshot.child("RejtingNaKorisnik").getValue().toString());
                            intent.putExtra("RejtingNaDostavuvac", postSnapshot.child("RejtingNaDostavuvac").getValue().toString());
                            intent.putExtra("OpisZaKorisnik", postSnapshot.child("OpisZaKorisnik").getValue().toString());
                            intent.putExtra("OpisZaDostavuvac", postSnapshot.child("OpisZaDostavuvac").getValue().toString());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}