package com.alizzelol.calendario;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetallesUsuarioActivity extends AppCompatActivity {

    private TextView textViewNombre, textViewApellido, textViewEmail, textViewTelefono, textViewRol;
    private Button buttonEliminar;
    private FirebaseFirestore db;
    private String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_usuario);

        textViewNombre = findViewById(R.id.textViewNombre);
        textViewApellido = findViewById(R.id.textViewApellido);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewTelefono = findViewById(R.id.textViewTelefono);
        textViewRol = findViewById(R.id.textViewRol);
        buttonEliminar = findViewById(R.id.buttonEliminar);

        db = FirebaseFirestore.getInstance();
        usuarioId = getIntent().getStringExtra("usuarioId");

        cargarDetallesUsuario();

        buttonEliminar.setOnClickListener(v -> eliminarUsuario());
    }

    private void cargarDetallesUsuario() {
        db.collection("usuarios").document(usuarioId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        textViewNombre.setText("Nombre: " + documentSnapshot.getString("nombre"));
                        textViewApellido.setText("Apellido: " + documentSnapshot.getString("apellido"));
                        textViewEmail.setText("Email: " + documentSnapshot.getString("email"));
                        textViewTelefono.setText("Teléfono: " + documentSnapshot.getString("telefono"));
                        textViewRol.setText("Rol: " + documentSnapshot.getString("rol"));
                    } else {
                        Toast.makeText(this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar detalles del usuario.", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void eliminarUsuario() {
        db.collection("usuarios").document(usuarioId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Usuario eliminado con éxito.", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al eliminar usuario.", Toast.LENGTH_SHORT).show();
                });
    }
}