package com.alizzelol.calendario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListaEventosPorTipos extends AppCompatActivity {

    private RecyclerView recyclerViewEventos;
    private EventoAdapterTipos eventoAdapter;
    private TextView tituloLista;
    private FirebaseFirestore db;
    private String tipoEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eventos_por_tipos);

        recyclerViewEventos = findViewById(R.id.recyclerViewEventos);
        tituloLista = findViewById(R.id.tituloLista);

        recyclerViewEventos.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        tipoEvento = intent.getStringExtra("tipoEvento");

        db = FirebaseFirestore.getInstance();

        if (tipoEvento != null) {
            tituloLista.setText("Lista de " + (tipoEvento.equalsIgnoreCase("curso") ? "Cursos" : "Talleres"));
            cargarEventos(tipoEvento);
        }
    }

    private void cargarEventos(String tipo) {
        db.collection("eventos")
                .whereEqualTo("tipo", tipo)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Evento> eventos = new ArrayList<>(); // Cambiar aquí
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
                            Log.d("ListaEventosPorTipos", "Evento cargado: " + evento.getTitulo() + ", Descripcion: " + evento.getDescripcion());
                        }

                        // Ordenar eventos por fecha
                        Collections.sort(eventos, new Comparator<Evento>() {
                            @Override
                            public int compare(Evento evento1, Evento evento2) {
                                return evento1.getFecha().compareTo(evento2.getFecha());
                            }
                        });
                        eventoAdapter = new EventoAdapterTipos(eventos);
                        recyclerViewEventos.setAdapter(eventoAdapter);
                    } else {
                        Toast.makeText(ListaEventosPorTipos.this, "Error al cargar eventos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}