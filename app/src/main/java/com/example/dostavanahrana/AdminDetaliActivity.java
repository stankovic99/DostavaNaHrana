package com.example.dostavanahrana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;

public class AdminDetaliActivity extends AppCompatActivity {

    private TextView imeDostavuvac, imeKorisnik, rejtingZaDostavuvac, rejtingZaKorisnik, rejtingNaDostavuvac, rejtingNaKorisnik, opisZaDostavuvac, opisZaKorisnik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detali);

        Intent intent = getIntent();
        String imeDostavuvacIntent = intent.getStringExtra("ImeDostavuvac");
        String imeKorisnikIntent = intent.getStringExtra("ImeKorisnik");
        String rejtingZaDostavuvacIntent = intent.getStringExtra("RejtingZaDostavuvac");
        String rejtingZaKorisnikIntent = intent.getStringExtra("RejtingZaKorisnik");
        String rejtingNaKorisnikIntent = intent.getStringExtra("RejtingNaKorisnik");
        String rejtingNaDostavuvacIntent = intent.getStringExtra("RejtingNaDostavuvac");
        String opisZaKorisnikIntent = intent.getStringExtra("OpisZaKorisnik");
        String opisZaDostavuvacIntent = intent.getStringExtra("OpisZaDostavuvac");

        imeDostavuvac = findViewById(R.id.imeDostavuvacDesno);
        imeKorisnik = findViewById(R.id.imeKorisnikDesno);
        rejtingZaDostavuvac = findViewById(R.id.rejtingZaDostavuvacDesno);
        rejtingZaKorisnik = findViewById(R.id.rejtingZaKorisnikDesno);
        rejtingNaDostavuvac = findViewById(R.id.rejtingNaDostavuvacDesno);
        rejtingNaKorisnik = findViewById(R.id.rejtingNaKorisnikDesno);
        opisZaDostavuvac = findViewById(R.id.opisZaDostavuvacDesno);
        opisZaKorisnik = findViewById(R.id.opisZaKorisnikDesno);

        imeDostavuvac.setText(imeDostavuvacIntent);
        imeKorisnik.setText(imeKorisnikIntent);
        rejtingZaDostavuvac.setText(rejtingZaDostavuvacIntent);
        rejtingZaKorisnik.setText(rejtingZaKorisnikIntent);
        rejtingNaDostavuvac.setText(rejtingNaDostavuvacIntent);
        rejtingNaKorisnik.setText(rejtingNaKorisnikIntent);
        opisZaKorisnik.setText(opisZaKorisnikIntent);
        opisZaDostavuvac.setText(opisZaDostavuvacIntent);
    }
}