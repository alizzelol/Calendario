package com.alizzelol.calendario;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MisEventosActivity extends AppCompatActivity implements EventoAdapterMisEventos.OnEventoClickListener {

    private RecyclerView misEventosRecyclerView;
    private EventoAdapterMisEventos eventoAdapter;
    private List<Evento> misEventos;
    private FirebaseFirestore db;
    private String padreId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_eventos);

        misEventosRecyclerView = findViewById(R.id.misEventosRecyclerView);
        misEventosRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        misEventos = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        padreId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadMisEventos();
    }

    private void loadMisEventos() {
        misEventos.clear();

        db.collection("inscripciones")
                .whereEqualTo("padreId", padreId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> eventoIds = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Inscripcion inscripcion = document.toObject(Inscripcion.class);
                            eventoIds.add(inscripcion.getEventoId());
                        }

                        loadEventosDetails(eventoIds);
                    } else {
                        Toast.makeText(this, "Error al cargar mis eventos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadEventosDetails(List<String> eventoIds) {
        if (eventoIds.isEmpty()) {
            eventoAdapter = new EventoAdapterMisEventos(misEventos, MisEventosActivity.this);
            misEventosRecyclerView.setAdapter(eventoAdapter);
            return;
        }

        AtomicInteger loadedCount = new AtomicInteger(0);

        for (String eventoId : eventoIds) {
            db.collection("eventos").document(eventoId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Evento evento = new Evento(
                            documentSnapshot.getId(),
                            documentSnapshot.getString("título"),
                            documentSnapshot.getString("descripción"),
                            documentSnapshot.getDate("fecha"),
                            documentSnapshot.getString("hora"),
                            documentSnapshot.getString("tipo")
                    );
                    misEventos.add(evento);
                }

                int count = loadedCount.incrementAndGet();

                if (count == eventoIds.size()) {
                    Collections.sort(misEventos, (evento1, evento2) -> evento1.getFecha().compareTo(evento2.getFecha()));
                    eventoAdapter = new EventoAdapterMisEventos(misEventos, MisEventosActivity.this);
                    misEventosRecyclerView.setAdapter(eventoAdapter);
                }
            });
        }
    }

    @Override
    public void onEventoClick(Evento evento) {
        mostrarDialogoBorrar(evento);
    }

    private void mostrarDialogoBorrar(Evento evento) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Seguro que quieres borrar " + evento.getTitulo() + " de tus eventos?")
                .setPositiveButton("Borrar", (dialog, which) -> borrarEvento(evento))
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void borrarEvento(Evento evento) {
        db.collection("inscripciones")
                .whereEqualTo("padreId", padreId)
                .whereEqualTo("eventoId", evento.getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().delete().addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Evento borrado.", Toast.LENGTH_SHORT).show();
                                actualizarListaEventos(evento);
                            }).addOnFailureListener(e -> {
                                Log.e("MisEventosActivity", "Error al borrar evento: " + e.getMessage());
                                Toast.makeText(this, "Error al borrar el evento.", Toast.LENGTH_SHORT).show();
                            });
                            return;
                        }
                        Toast.makeText(this, "No se encontró el evento.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("MisEventosActivity", "Error al buscar inscripciones: " + task.getException().getMessage());
                        Toast.makeText(this, "Error al borrar el evento.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void actualizarListaEventos(Evento eventoBorrado) {
        misEventos.removeIf(evento -> evento.getId().equals(eventoBorrado.getId()));
        eventoAdapter.notifyDataSetChanged();
    }
}