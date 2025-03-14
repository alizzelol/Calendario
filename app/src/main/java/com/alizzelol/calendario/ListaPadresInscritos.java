package com.alizzelol.calendario;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ListaPadresInscritos extends AppCompatActivity {

    private RecyclerView recyclerViewPadres;
    private PadresInscritosAdapter padresInscritosAdapter;
    private List<String> listaPadres = new ArrayList<>();
    private FirebaseFirestore db;
    private TextView mensajeNoPadres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_padres_inscritos);

        recyclerViewPadres = findViewById(R.id.recyclerViewPadres);
        recyclerViewPadres.setLayoutManager(new LinearLayoutManager(this));

        mensajeNoPadres = findViewById(R.id.mensajeNoPadres);

        db = FirebaseFirestore.getInstance();
        cargarPadresInscritos();
    }

    private void cargarPadresInscritos() {
        String eventoId = getIntent().getStringExtra("eventoId");
        db.collection("inscripciones")
                .whereEqualTo("eventoId", eventoId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaPadres.clear();
                        if (task.getResult().isEmpty()) {
                            // No hay inscripciones, mostrar mensaje
                            mensajeNoPadres.setVisibility(View.VISIBLE);
                            recyclerViewPadres.setVisibility(View.GONE);
                        } else {
                            // Hay inscripciones, cargar datos
                            mensajeNoPadres.setVisibility(View.GONE);
                            recyclerViewPadres.setVisibility(View.VISIBLE);

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String padreId = document.getString("padreId");
                                db.collection("usuarios").document(padreId)
                                        .get()
                                        .addOnSuccessListener(documentSnapshot -> {
                                            if (documentSnapshot.exists()) {
                                                String nombrePadre = documentSnapshot.getString("nombre") + " " + documentSnapshot.getString("apellido");
                                                listaPadres.add(nombrePadre);
                                                padresInscritosAdapter = new PadresInscritosAdapter(listaPadres);
                                                recyclerViewPadres.setAdapter(padresInscritosAdapter);
                                            }
                                        });
                            }
                        }
                    } else {
                        Toast.makeText(this, "Error al cargar padres inscritos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}