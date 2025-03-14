package com.alizzelol.calendario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListaEventosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEventos;
    private EventoAdapter eventoAdapter;
    private List<Evento> listaEventos = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eventos);

        recyclerViewEventos = findViewById(R.id.recyclerViewEventos);
        recyclerViewEventos.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        cargarEventos();
    }

    private void cargarEventos() {
        db.collection("eventos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaEventos.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Evento evento = new Evento(
                                    document.getId(),
                                    document.getString("título"),
                                    document.getString("descripción"),
                                    document.getDate("fecha"),
                                    document.getString("hora"),
                                    document.getString("tipo")
                            );
                            listaEventos.add(evento);
                        }
                        eventoAdapter = new EventoAdapter(listaEventos, eventoId -> mostrarDetallesEvento(eventoId));
                        recyclerViewEventos.setAdapter(eventoAdapter);
                    } else {
                        Toast.makeText(this, "Error al cargar eventos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mostrarDetallesEvento(String eventoId) {
        db.collection("eventos").document(eventoId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String titulo = documentSnapshot.getString("título");
                        String descripcion = documentSnapshot.getString("descripción");
                        java.util.Date fechaDate = documentSnapshot.getDate("fecha");
                        String hora = documentSnapshot.getString("hora");
                        String tipo = documentSnapshot.getString("tipo");

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                        String fecha = sdf.format(fechaDate);

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle(titulo)
                                .setMessage("Título" + titulo + "Descripción: " + descripcion + "\nFecha: " + fecha + "\nHora:" + hora + "\nTipo: " + tipo)
                                .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss())
                                .setNegativeButton("Eliminar", (dialog, which) -> eliminarEvento(eventoId))
                                .setNeutralButton("Padres", (dialog, which) -> mostrarPadresInscritos(eventoId))
                                .show();
                    } else {
                        Toast.makeText(this, "Evento no encontrado.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar detalles del evento.", Toast.LENGTH_SHORT).show());
    }

    private void mostrarPadresInscritos(String eventoId) {
        Intent intent = new Intent(this, ListaPadresInscritos.class);
        intent.putExtra("eventoId", eventoId);
        startActivity(intent);
    }

    private void eliminarEvento(String eventoId) {
        db.collection("eventos").document(eventoId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Evento eliminado con éxito.", Toast.LENGTH_SHORT).show();
                    cargarEventos(); // Recargar la lista de eventos
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar evento.", Toast.LENGTH_SHORT).show());
    }
}