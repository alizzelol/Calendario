package com.alizzelol.calendario;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarioProfesor extends AppCompatActivity {

    private GridView calendarGrid;
    private Calendar calendar;
    private List<Date> days;
    private List<Evento> events;
    private CalendarAdapterPro calendarAdapter;
    private FirebaseFirestore db;
    private String filtro = "todos"; // Filtro inicial
    private TextView textMesAño; // Adicionado
    private static final int REQUEST_ADD_EVENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_profesor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        calendarGrid = findViewById(R.id.calendarGrid);
        calendar = Calendar.getInstance();
        db = FirebaseFirestore.getInstance();

        textMesAño = findViewById(R.id.textMesAno); // Inicializado

        ImageButton buttonPrevMonth = findViewById(R.id.buttonPrevMonth);
        ImageButton buttonNextMonth = findViewById(R.id.buttonNextMonth);

        buttonPrevMonth.setOnClickListener(v -> mostrarMesAnterior());
        buttonNextMonth.setOnClickListener(v -> mostrarMesSiguiente());

        generateCalendar();
        loadEvents();
        updateCalendar();
        actualizarTextoMesAño();

        calendarGrid.setOnItemClickListener((parent, view, position, id) -> {
            Date selectedDate = days.get(position);
            mostrarEventosDelDia(selectedDate);
        });
    }

    private void mostrarMesAnterior() {
        calendar.add(Calendar.MONTH, -1);
        generateCalendar();
        loadEvents();
        updateCalendar();
        actualizarTextoMesAño();
    }

    private void mostrarMesSiguiente() {
        calendar.add(Calendar.MONTH, 1);
        generateCalendar();
        loadEvents();
        updateCalendar();
        actualizarTextoMesAño();
    }

    private void actualizarTextoMesAño() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
        String mesAño = sdf.format(calendar.getTime());
        textMesAño.setText(mesAño.toUpperCase()); // Convertir a mayúsculas
    }

    private void generateCalendar() {
        days = new ArrayList<>();
        Calendar tempCalendar = (Calendar) calendar.clone();
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = tempCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        tempCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);
        for (int i = 0; i < 42; i++) {
            days.add(tempCalendar.getTime());
            tempCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void loadEvents() {
        events = new ArrayList<>();
        db.collection("eventos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        events.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Evento evento = new Evento(
                                    document.getId(),
                                    document.getString("título"),
                                    document.getString("descripción"),
                                    document.getDate("fecha"),
                                    document.getString("hora"),
                                    document.getString("tipo")
                            );
                            events.add(evento);
                        }
                        updateCalendar();
                    } else {
                        Toast.makeText(this, "Error al cargar eventos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateCalendar() {
        calendarAdapter = new CalendarAdapterPro(this, days, events, calendar);
        calendarGrid.setAdapter(calendarAdapter);
    }

    private void mostrarEventosDelDia(Date dataSelecionada) {
        List<Evento> eventosDoDia = new ArrayList<>();
        Calendar calDataSelecionada = Calendar.getInstance();
        calDataSelecionada.setTime(dataSelecionada);

        for (Evento evento : events) {
            Calendar calEvento = Calendar.getInstance();
            calEvento.setTime(evento.getFecha());

            if (calDataSelecionada.get(Calendar.YEAR) == calEvento.get(Calendar.YEAR) &&
                    calDataSelecionada.get(Calendar.MONTH) == calEvento.get(Calendar.MONTH) &&
                    calDataSelecionada.get(Calendar.DAY_OF_MONTH) == calEvento.get(Calendar.DAY_OF_MONTH)) {
                eventosDoDia.add(evento);
            }
        }

        if (!eventosDoDia.isEmpty()) {
            Intent intent = new Intent(this, EventosDiaActivity.class);
            intent.putExtra("eventos", (ArrayList<Evento>) eventosDoDia);
            startActivity(intent);
        } else {
            Toast.makeText(this, "No hay eventos este día.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_calendario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_anadir_usuario) {
            Intent intent = new Intent(this, AnadirUsuarioActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_lista_usuarios) {
            Intent intent = new Intent(this, ListaUsuariosActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_anadir_evento) {
            Intent intent = new Intent(this, AnadirEventoActivity.class);
            startActivityForResult(intent, REQUEST_ADD_EVENT);
            return true;
        } else if (id == R.id.action_lista_eventos) {
            Intent intent = new Intent(this, ListaEventosActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_EVENT) {
            if (resultCode == RESULT_OK) {
                loadEvents();
                updateCalendar();
            }
        }
    }
}