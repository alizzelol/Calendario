package com.alizzelol.calendario;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AnadirUsuarioActivity extends AppCompatActivity {
    private EditText editTextNombre, editTextApellido, editTextEmail, editTextPassword, editTextPassword2, editTextTelefono;
    private Spinner spinnerRol;
    private Button buttonAñadir;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_usuario);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassword2 = findViewById(R.id.editTextPassword2);
        editTextTelefono = findViewById(R.id.editTextTelefono);
        spinnerRol = findViewById(R.id.spinnerRol);
        buttonAñadir = findViewById(R.id.buttonAñadir);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRol.setAdapter(adapter);
        buttonAñadir.setOnClickListener(v -> añadirUsuario());
    }

    private void añadirUsuario() {
        String nombre = editTextNombre.getText().toString();
        String apellido = editTextApellido.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String password2 = editTextPassword2.getText().toString();
        String telefono = editTextTelefono.getText().toString();
        String rol = spinnerRol.getSelectedItem().toString();

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(password2)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        Map<String, Object> usuario = new HashMap<>();
                        usuario.put("nombre", nombre);
                        usuario.put("apellido", apellido);
                        usuario.put("email", email);
                        usuario.put("telefono", telefono);
                        usuario.put("rol", rol);

                        db.collection("usuarios").document(uid).set(usuario)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(AnadirUsuarioActivity.this, "Usuario añadido con éxito.", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(AnadirUsuarioActivity.this, "Error al añadir usuario.", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(AnadirUsuarioActivity.this, "Error al crear usuario.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}