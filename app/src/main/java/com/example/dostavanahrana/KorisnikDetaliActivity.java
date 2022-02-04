    package com.example.dostavanahrana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

    public class KorisnikDetaliActivity extends AppCompatActivity {

    private TextView statusDesno, dostavuvacDesno, dostavuvacLevo, opisDostavuvac, rejting;
    private Button oceniDostavuvac;
    private EditText opisDostavuvacText;
    private RatingBar rejtingText;
    private String status, dostavuvac;
    private float sumaRejting,rejtingZaDostavuvac;
    private Integer brojRejting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korisnik_detali);
        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        String status = intent.getStringExtra("Status");
        String dostavuvac = intent.getStringExtra("Dostavuvac");
        String emailDostavuvac = intent.getStringExtra("EmailDostavuvac");

        statusDesno = findViewById(R.id.statusDetaliDesno);
        dostavuvacDesno = findViewById(R.id.dostavuvacDetaliDesno);
        dostavuvacLevo = findViewById(R.id.dostavuvacDetaliLevo);
        opisDostavuvac = findViewById(R.id.opisZaDostavuvac);
        opisDostavuvacText = findViewById(R.id.opisZaDostavuvacText);
        oceniDostavuvac = findViewById(R.id.ocenkaZaDostavuvac);
        rejting = findViewById(R.id.rejtingZaDostavuvac);
        rejtingText = findViewById(R.id.rejtingZaDostavuvacText);

        if(status.equals("Aktivna")){
            dostavuvacDesno.setVisibility(View.GONE);
            dostavuvacLevo.setVisibility(View.GONE);
            opisDostavuvac.setVisibility(View.GONE);
            rejting.setVisibility(View.GONE);
            oceniDostavuvac.setVisibility(View.GONE);
            opisDostavuvacText.setVisibility(View.GONE);
            rejtingText.setVisibility(View.GONE);
            statusDesno.setText("Во тек");
        }else if(status.equals("Kraj")){
            dostavuvacDesno.setText(dostavuvac);
            statusDesno.setText("Завршена");
        }else if(status.equals("Zavrsena")){
            oceniDostavuvac.setVisibility(View.GONE);
            dostavuvacDesno.setText(dostavuvac);
            statusDesno.setText("Завршена");
        }

        HashMap<String, Object> mapUser = new HashMap<>();
        HashMap<String, Object> mapNaracka = new HashMap<>();

        oceniDostavuvac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("KorisnickiNaracki").orderByChild("ID").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                            mapNaracka.put("Status","Zavrsena");
                            mapNaracka.put("OpisZaDostavuvac", opisDostavuvacText.getText().toString());
                            mapNaracka.put("RejtingZaDostavuvac", rejtingText.getRating());
                            FirebaseDatabase.getInstance().getReference("KorisnickiNaracki").child(snap.getKey()).updateChildren(mapNaracka);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                FirebaseDatabase.getInstance().getReference("Users").orderByChild("Email").equalTo(emailDostavuvac).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snap: snapshot.getChildren()){
                            sumaRejting = Integer.parseInt(snap.child("SumaRejting").getValue().toString());
                            brojRejting = Integer.parseInt(snap.child("BrojNaRejting").getValue().toString());
                        }
                        sumaRejting += rejtingText.getRating();
                        brojRejting += 1;
                        rejtingZaDostavuvac = sumaRejting/brojRejting;
                        mapUser.put("BrojNaRejting", brojRejting);
                        mapUser.put("SumaRejting", sumaRejting);
                        mapUser.put("Rejting", rejtingZaDostavuvac);
                        for(DataSnapshot snap: snapshot.getChildren()){
                            FirebaseDatabase.getInstance().getReference("Users").child(snap.getKey()).updateChildren(mapUser);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                startActivity(new Intent(KorisnikDetaliActivity.this, KorisnikMoiNarackiActivity.class));
            }
        });

    }
}