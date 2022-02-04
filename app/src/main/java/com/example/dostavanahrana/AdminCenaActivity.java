package com.example.dostavanahrana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminCenaActivity extends AppCompatActivity {

    private TextView tipProizvodDesno, imeProizvodDesno, cenaProizvodDesno;
    private EditText novaCena;
    private Button potvrdi, izbrisiProdukt;
    private String imeNaProizvod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cena);

        Intent intent = getIntent();
        String tipProizvod = intent.getStringExtra("TipProizvod");
        String imeProizvod = intent.getStringExtra("ImeProizvod");
        String cenaProizvod = intent.getStringExtra("CenaProizvod");

        tipProizvodDesno = findViewById(R.id.tipProizvodDesnoAdmin);
        imeProizvodDesno = findViewById(R.id.imeProizvodDesnoAdmin);
        cenaProizvodDesno = findViewById(R.id.cenaProizvodDesnoAdmin);
        novaCena = findViewById(R.id.novaCenaDesno);
        potvrdi = findViewById(R.id.potvrdiCena);
        izbrisiProdukt = findViewById(R.id.izbrisiProdukt);

        tipProizvodDesno.setText(tipProizvod);
        imeProizvodDesno.setText(imeProizvod);
        cenaProizvodDesno.setText(cenaProizvod);

        HashMap<String, Object> map = new HashMap<>();

        potvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.put("CenaProizvod",novaCena.getText().toString());
                FirebaseDatabase.getInstance().getReference("SiteProizvodi").child(tipProizvod).orderByChild("ImeProizvod").equalTo(imeProizvod).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            FirebaseDatabase.getInstance().getReference("SiteProizvodi").child(tipProizvod).child(postSnapshot.getKey()).updateChildren(map);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                startActivity(new Intent(AdminCenaActivity.this, AdminPrvActivity.class));
            }
        });

        izbrisiProdukt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("SiteProizvodi").child(tipProizvod).orderByChild("ImeProizvod").equalTo(imeProizvod).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            FirebaseDatabase.getInstance().getReference("SiteProizvodi").child(tipProizvod).child(postSnapshot.getKey()).removeValue();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                startActivity(new Intent(AdminCenaActivity.this, AdminPrvActivity.class));
            }
        });
    }
}