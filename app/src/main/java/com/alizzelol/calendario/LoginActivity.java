package com.alizzelol.calendario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etContraseña;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etEmail);
        etContraseña = findViewById(R.id.etContraseña);
        btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String contraseña = etContraseña.getText().toString();
                mAuth.signInWithEmailAndPassword(email, contraseña)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String uid = mAuth.getCurrentUser().getUid();

                                    db.collection("usuarios").document(uid).get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful() && task.getResult().exists()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        Map<String, Object> userData = document.getData();
                                                        String rol = (String) userData.get("rol");

                                                        if (rol != null) {
                                                            redirigirUsuario(rol);
                                                        } else {
                                                            Toast.makeText(LoginActivity.this, "Rol de usuario no encontrado.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(LoginActivity.this, "Error al obtener los datos del usuario.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(LoginActivity.this, "Error en el inicio de sesión: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void redirigirUsuario(String rol) {
        Intent intent;
        switch (rol) {
            case "admin":
                intent = new Intent(LoginActivity.this, CalendarioProfesor.class);
                startActivity(intent);
                finish();
                break;
            case "padre":
                intent = new Intent(LoginActivity.this, CalendarioPadres.class);
                startActivity(intent);
                finish();
                break;
            default:
                Toast.makeText(LoginActivity.this, "Rol no reconocido", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}