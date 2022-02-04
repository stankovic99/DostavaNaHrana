package com.example.dostavanahrana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button login;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if(TextUtils.isEmpty(txt_email)||TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this, "Пополнете ги сите полиња", Toast.LENGTH_SHORT).show();
                }else{
                    auth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user.getDisplayName().equals("Korisnik")) {
                                    Toast.makeText(LoginActivity.this, "Корисник се најави", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, KorisnikPrvActivity.class));
                                } else if (user.getDisplayName().equals("Dostavuvac")) {
                                    Toast.makeText(LoginActivity.this, "Доставувач се најави", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, DostavuvacListaActivity.class));
                                } else if (user.getDisplayName().equals("Administrator")){
                                    Toast.makeText(LoginActivity.this, "Администратор се најави", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, AdminPrvActivity.class));
                                }
                            }else {
                                Toast.makeText(LoginActivity.this, "Неуспешна најава", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}