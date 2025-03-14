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
import java.util.List;

public class ApuntarseEventoActivity extends AppCompatActivity implements EventoAdapterApuntarse.OnEventoClickListener {

    private RecyclerView eventosRecyclerView;
    private EventoAdapterApuntarse eventoAdapter;
    private List<Evento> eventos;
    private FirebaseFirestore db;
    private String padreId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apuntarse_evento);

        eventosRecyclerView = findViewById(R.id.eventosRecyclerView);
        eventosRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventos = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        padreId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadEvents();
    }

    private void loadEvents() {
        db.collection("eventos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        eventos.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Evento evento = new Evento(
                                    document.getId(),
                                    document.getString("título"),
                                    document.getString("descripción"),
                                    document.getDate("fecha"),
                                    document.getString("hora"),
                                    document.getString("tipo")
                            );
                            eventos.add(evento);
                        }
                        eventoAdapter = new EventoAdapterApuntarse(eventos, this);
                        eventosRecyclerView.setAdapter(eventoAdapter);
                    } else {
                        Toast.makeText(this, "Error al cargar eventos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onEventoClick(String eventoId) {
        mostrarDialogoApuntarse(eventoId);
    }

    private void mostrarDialogoApuntarse(String eventoId) {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Seguro que quieres apuntarte a " + evento.getTitulo() + "?")
                        .setPositiveButton("Apuntarse", (dialog, which) -> apuntarUsuarioEvento(eventoId))
                        .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
    }

    private void apuntarUsuarioEvento(String eventoId) {
        Inscripcion inscripcion = new Inscripcion(padreId, eventoId);
        db.collection("inscripciones")
                .add(inscripcion)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Apuntado al evento.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("ApuntarseEventoActivity", "Error al apuntarse: " + e.getMessage());
                    Toast.makeText(this, "Error al apuntarse.", Toast.LENGTH_SHORT).show();
                });
    }
}