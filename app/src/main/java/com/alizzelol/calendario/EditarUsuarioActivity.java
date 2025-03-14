package com.alizzelol.calendario;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class EditarUsuarioActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextApellido, editTextEmail, editTextTelefono, editTextRol;
    private Button buttonGuardar;
    private FirebaseFirestore db;
    private String usuarioId;
    private DocumentSnapshot documentSnapshotUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextTelefono = findViewById(R.id.editTextTelefono);
        editTextRol = findViewById(R.id.editTextRol);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        db = FirebaseFirestore.getInstance();
        usuarioId = getIntent().getStringExtra("usuarioId");

        cargarDatosUsuario();

        buttonGuardar.setOnClickListener(v -> guardarCambios());
    }

    private void cargarDatosUsuario() {
        db.collection("usuarios").document(usuarioId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        documentSnapshotUsuario = documentSnapshot;
                        editTextNombre.setText(documentSnapshot.getString("nombre"));
                        editTextApellido.setText(documentSnapshot.getString("apellido"));
                        editTextEmail.setText(documentSnapshot.getString("email"));
                        editTextTelefono.setText(documentSnapshot.getString("telefono"));
                        editTextRol.setText(documentSnapshot.getString("rol"));
                    } else {
                        Toast.makeText(this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar datos del usuario.", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void guardarCambios() {
        if (documentSnapshotUsuario == null) {
            Toast.makeText(this, "Error: No se cargaron los datos del usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> usuarioActualizado = new HashMap<>();

        if (!editTextNombre.getText().toString().equals(documentSnapshotUsuario.getString("nombre"))) {
            usuarioActualizado.put("nombre", editTextNombre.getText().toString());
        }
        if (!editTextApellido.getText().toString().equals(documentSnapshotUsuario.getString("apellido"))) {
            usuarioActualizado.put("apellido", editTextApellido.getText().toString());
        }
        if (!editTextEmail.getText().toString().equals(documentSnapshotUsuario.getString("email"))) {
            usuarioActualizado.put("email", editTextEmail.getText().toString());
        }
        if (!editTextTelefono.getText().toString().equals(documentSnapshotUsuario.getString("telefono"))) {
            usuarioActualizado.put("telefono", editTextTelefono.getText().toString());
        }
        if (!editTextRol.getText().toString().equals(documentSnapshotUsuario.getString("rol"))) {
            usuarioActualizado.put("rol", editTextRol.getText().toString());
        }

        if (!usuarioActualizado.isEmpty()) {
            db.collection("usuarios").document(usuarioId)
                    .update(usuarioActualizado)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Usuario actualizado con éxito.", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al actualizar usuario.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No se realizaron cambios.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}