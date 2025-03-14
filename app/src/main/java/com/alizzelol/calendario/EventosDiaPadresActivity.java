package com.alizzelol.calendario;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class EventosDiaPadresActivity extends AppCompatActivity {

    private RecyclerView eventosRecyclerView;
    private EventoAdapterPadres eventoAdapter;
    private List<Evento> eventosDelDia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_dia_padres);

        eventosRecyclerView = findViewById(R.id.eventosRecyclerView);
        eventosRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtener la lista de eventos del Intent
        eventosDelDia = (ArrayList<Evento>) getIntent().getSerializableExtra("eventos");

        if (eventosDelDia != null) {
            // Inicializar el adaptador y configurar el RecyclerView
            eventoAdapter = new EventoAdapterPadres(eventosDelDia);
            eventosRecyclerView.setAdapter(eventoAdapter);
        }
    }
}
