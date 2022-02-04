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

public class DostavuvacDetaliActivity extends AppCompatActivity {

    private TextView naracatel, telefon, tipProizvod, imeProizvod, cenaProizvod, tipPlakjanje, rastojanie, komentar, opisZaKorisnik, rejting;
    private Button prikaziRuta, oceniKorisnik;
    private RatingBar rejtingText;
    private EditText opisZaKorisnikText;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String imeDostavuvac, rejtingDostavuvac;
    private float sumaRejting,rejtingZaDostavuvac;
    private Integer brojRejting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dostavuvac_detali);

        naracatel = findViewById(R.id.naracatelDesno);
        telefon = findViewById(R.id.telefonDesno);
        tipProizvod = findViewById(R.id.tipProizvodDesnoDostavuvac);
        imeProizvod = findViewById(R.id.imeProizvodDesnoDostavuvac);
        cenaProizvod = findViewById(R.id.cenaProizvodDesnoDostavuvac);
        tipPlakjanje = findViewById(R.id.tipPlakjanjeDesno);
        rastojanie = findViewById(R.id.rastojanieDesno);
        komentar = findViewById(R.id.komentarDesno);
        opisZaKorisnik = findViewById(R.id.opisZaKorisnik);
        opisZaKorisnikText = findViewById(R.id.opisZaKorisnikText);
        prikaziRuta = findViewById(R.id.prikaziRuta);
        oceniKorisnik = findViewById(R.id.ocenkaZaKorisnik);
        rejting = findViewById(R.id.rejtingZaKorisnik);
        rejtingText = findViewById(R.id.rejtingZaKorisnikText);

        Intent intent = getIntent();
        String naracatelIntent = intent.getStringExtra("Naracatel");
        String telefonIntent = intent.getStringExtra("Telefon");
        String tipProizvodIntent = intent.getStringExtra("TipProizvod");
        String imeProizvodIntent = intent.getStringExtra("ImeProizvod");
        String cenaProizvodIntent = intent.getStringExtra("CenaProizvod");
        String tipPlakjanjeIntent = intent.getStringExtra("TipPlakjanje");
        String komentarIntent = intent.getStringExtra("Komentar");
        String rastojanieIntent = intent.getStringExtra("Rastojanie");
        String emailKorisnik = intent.getStringExtra("EmailKorisnik");
        String id = intent.getStringExtra("ID");

        Double korisnikLat = intent.getDoubleExtra("KorisnikLat",0);
        Double korisnikLon = intent.getDoubleExtra("KorisnikLon",0);
        Double dostavuvacLat = intent.getDoubleExtra("DostavuvacLat",0);
        Double dostavuvacLon = intent.getDoubleExtra("DostavuvacLon",0);

        naracatel.setText(naracatelIntent);
        telefon.setText(telefonIntent);
        tipProizvod.setText(tipProizvodIntent);
        imeProizvod.setText(imeProizvodIntent);
        cenaProizvod.setText(cenaProizvodIntent + " ден.");
        tipPlakjanje.setText(tipPlakjanjeIntent);
        komentar.setText(komentarIntent);
        rastojanie.setText(rastojanieIntent + " km одалеченост");

        HashMap<String, Object> mapUser = new HashMap<>();
        HashMap<String, Object> map = new HashMap<>();

        prikaziRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DostavuvacDetaliActivity.this, DostavuvacRutaActivity.class);
                intent.putExtra("KorisnikLat",korisnikLat);
                intent.putExtra("KorisnikLon",korisnikLon);
                intent.putExtra("DostavuvacLat",dostavuvacLat);
                intent.putExtra("DostavuvacLon",dostavuvacLon);
                startActivity(intent);
            }
        });

        oceniKorisnik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oceniKorisnik.setVisibility(View.GONE);
                String emailDostavuvac = auth.getCurrentUser().getEmail().toString();
                FirebaseDatabase.getInstance().getReference("Users").orderByChild("Email").equalTo(emailDostavuvac).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot datas: snapshot.getChildren()){
                            imeDostavuvac = datas.child("FullName").getValue().toString();
                            rejtingDostavuvac = datas.child("Rejting").getValue().toString();
                        }
                        map.put("EmailDostavuvac", emailDostavuvac);
                        map.put("ImeDostavuvac", imeDostavuvac);
                        map.put("RejtingNaDostavuvac", rejtingDostavuvac);
                        map.put("Status","Kraj");
                        map.put("OpisZaKorisnik", opisZaKorisnikText.getText().toString());
                        map.put("RejtingZaKorisnik", rejtingText.getRating());
                        FirebaseDatabase.getInstance().getReference("KorisnickiNaracki").orderByChild("ID").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                    FirebaseDatabase.getInstance().getReference().child("KorisnickiNaracki").child(postSnapshot.getKey()).updateChildren(map);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                FirebaseDatabase.getInstance().getReference("Users").orderByChild("Email").equalTo(emailKorisnik).addListenerForSingleValueEvent(new ValueEventListener() {
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
                startActivity(new Intent(DostavuvacDetaliActivity.this, DostavuvacListaActivity.class));
            }
        });
    }
}