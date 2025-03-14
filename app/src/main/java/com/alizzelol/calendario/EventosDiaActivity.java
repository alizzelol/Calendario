package com.alizzelol.calendario;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class EventosDiaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_dia);

        RecyclerView recyclerViewEventosDia = findViewById(R.id.recyclerViewEventosDia);
        recyclerViewEventosDia.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Evento> eventos = (ArrayList<Evento>) getIntent().getSerializableExtra("eventos");
        EventosDiaAdapter adapter = new EventosDiaAdapter(eventos);
        recyclerViewEventosDia.setAdapter(adapter);
    }
}