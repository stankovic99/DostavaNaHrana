package com.example.dostavanahrana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

public class KorisnikNarackaActivity extends AppCompatActivity {

    private Button lokacija, naracaj;
    private FirebaseAuth auth;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private TextView tipNaProizvod, imeNaProizvod, cenaNaProizvod;
    private EditText kolicina, komentar;
    private String tipProizvod, imeProizvod, cenaProizvod, celoIme, rejting, telefon;
    private Switch plakjenjeSwitch;
    private Integer vkupnaCena;

    FusedLocationProviderClient fusedLocationProviderClient;
    private double lat, lon, newLat, newLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korisnik_naracka);

        auth = FirebaseAuth.getInstance();

        lokacija = findViewById(R.id.lokacija);
        naracaj = findViewById(R.id.naracaj);
        tipNaProizvod = findViewById(R.id.tipProizvodDesnoKorisnik);
        imeNaProizvod = findViewById(R.id.imeProizvodDesnoKorisnik);
        cenaNaProizvod = findViewById(R.id.cenaProizvodDesnoKorisnik);
        kolicina = findViewById(R.id.kolicinaDesno);
        komentar = findViewById(R.id.komentarDesno);
        plakjenjeSwitch = findViewById(R.id.plakjenjeSwitch);

        Intent intent = getIntent();
        tipProizvod = intent.getStringExtra("TipProizvod");
        imeProizvod = intent.getStringExtra("ImeProizvod");
        cenaProizvod = intent.getStringExtra("CenaProizvod");
        newLat = intent.getDoubleExtra("NewLat",0);
        newLon = intent.getDoubleExtra("NewLon",0);
        tipNaProizvod.setText(tipProizvod);
        imeNaProizvod.setText(imeProizvod);
        cenaNaProizvod.setText(cenaProizvod);

        HashMap<String, Object> map = new HashMap<>();
        String emailKorsnik = auth.getCurrentUser().getEmail().toString();
        map.put("EmailKorisnik", emailKorsnik);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(KorisnikNarackaActivity.this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull Location location) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                }
            });
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }

        naracaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kolicina.getText().toString().equals("")){
                    alert("Количина","Ве молиме внесете количина на продукти");
                }else{
                    FirebaseDatabase.getInstance().getReference("Users").orderByChild("Email").equalTo(emailKorsnik).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot datas: snapshot.getChildren()){
                                celoIme = datas.child("FullName").getValue().toString();
                                rejting = datas.child("Rejting").getValue().toString();
                                telefon = datas.child("Phone").getValue().toString();
                            }
                            if(newLat == 0.0 && newLon == 0.0){
                                map.put("KorisnikLat", lat);
                                map.put("KorisnikLon", lon);
                            }else {
                                map.put("KorisnikLat", newLat);
                                map.put("KorisnikLon", newLon);
                            }
                            if(plakjenjeSwitch.isChecked()){
                                map.put("TipPlakjanje","Картичка");
                            }else{
                                map.put("TipPlakjanje","Во готово");
                            }
                            String id = UUID.randomUUID().toString();
                            String[] novaCena = cenaProizvod.split(" ");
                            map.put("CenaProizvod",novaCena[0]);
                            map.put("ImeKorisnik",celoIme);
                            map.put("ID",id);
                            map.put("Komentar",komentar.getText().toString());
                            map.put("RejtingNaKorisnik",rejting);
                            map.put("EmailDostavuvac","");
                            map.put("ImeDostavuvac","");
                            map.put("Status","Aktivna");
                            map.put("TelefonKorisnik",telefon);
                            map.put("OpisZaKorisnik","");
                            map.put("OpisZaDostavuvac","");
                            map.put("RejtingZaKorisnik",0);
                            map.put("RejtingNaDostavuvac",0);
                            map.put("RejtingZaDostavuvac",0);
                            map.put("ImeProizvod",imeProizvod);
                            map.put("TipProizvod",tipProizvod);
                            vkupnaCena = Integer.parseInt(novaCena[0]) * Integer.parseInt(kolicina.getText().toString());
                            map.put("VkupnaCena",vkupnaCena);
                            FirebaseDatabase.getInstance().getReference().child("KorisnickiNaracki").push().updateChildren(map);
                            alert("Сметка","Вашата сметка изнесува " + vkupnaCena + " ден.");
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });

        lokacija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KorisnikNarackaActivity.this, MapsActivity.class);
                if(newLat == 0.0 && newLon == 0.0){
                    intent.putExtra("Lat", lat);
                    intent.putExtra("Lon", lon);
                    intent.putExtra("TipProizvod",tipProizvod);
                    intent.putExtra("CenaProizvod",cenaProizvod);
                    intent.putExtra("ImeProizvod",imeProizvod);
                }else{
                    intent.putExtra("Lat", newLat);
                    intent.putExtra("Lon", newLon);
                    intent.putExtra("TipProizvod",tipProizvod);
                    intent.putExtra("CenaProizvod",cenaProizvod);
                    intent.putExtra("ImeProizvod",imeProizvod);
                }
                startActivity(intent);
            }
        });
    }

    private void alert(String title, String message){
        AlertDialog dlg = new AlertDialog.Builder(KorisnikNarackaActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        dlg.show();
    }
}