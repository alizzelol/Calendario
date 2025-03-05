package com.alizzelol.calendario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ListaUsuariosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsuarios;
    private UsuarioAdapter usuarioAdapter;
    private List<Usuario> listaUsuarios = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);

        recyclerViewUsuarios = findViewById(R.id.recyclerViewUsuarios);
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        db.collection("usuarios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaUsuarios.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Usuario usuario = new Usuario(
                                    document.getString("nombre"),
                                    document.getString("apellido"),
                                    document.getString("email"),
                                    document.getString("telefono"),
                                    document.getString("rol")
                            );
                            listaUsuarios.add(usuario);
                        }
                        usuarioAdapter = new UsuarioAdapter(listaUsuarios, usuarioId -> {
                            Intent intent = new Intent(ListaUsuariosActivity.this, DetallesUsuarioActivity.class);
                            intent.putExtra("usuarioId", usuarioId);
                            startActivity(intent);
                        });
                        recyclerViewUsuarios.setAdapter(usuarioAdapter);
                    } else {
                        Toast.makeText(this, "Error al cargar usuarios.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}