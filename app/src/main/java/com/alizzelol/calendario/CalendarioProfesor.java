package com.alizzelol.calendario;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.appcompat.widget.Toolbar;

public class CalendarioProfesor extends AppCompatActivity {

    private GridView calendarGrid;
    private Calendar calendar;
    private List<String> days;
    private FirebaseFirestore db;
    private Map<Integer, String> events = new HashMap<>();
    private Map<Integer, String> eventTypes = new HashMap<>();
    private String currentFilter = "todos"; // Filtro inicial
    private CalendarAdapterPro adapter; // O private CalendarAdapterPro adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_profesor);

        calendarGrid = findViewById(R.id.calendarGrid);
        calendar = Calendar.getInstance();
        db = FirebaseFirestore.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        generateCalendar();
        loadEvents();
        updateCalendar();
    }

    private void generateCalendar() {
        days = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int month = cal.get(Calendar.MONTH);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < firstDayOfWeek - 1; i++) {
            days.add("");
        }

        for (int i = 1; i <= daysInMonth; i++) {
            days.add(String.valueOf(i));
        }
    }

    private void loadEvents() {
        db.collection("eventos").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                events.clear();
                eventTypes.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Date date = document.getDate("fecha");
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                    events.put(dayOfMonth, document.getString("título"));
                    eventTypes.put(dayOfMonth, document.getString("tipo"));
                }
                updateCalendar();
            }
        });
    }

    private void updateCalendar() {
        adapter = new CalendarAdapterPro(this, days, events, eventTypes);
        calendarGrid.setAdapter(adapter);
    }

    private void setupCalendar() {
        // Filtrar eventos según el filtro actual
        Map<Integer, String> filteredEvents = filterEvents(events, currentFilter);
        Map<Integer, String> filteredEventTypes = new HashMap<>();

        // Crear el mapa de filteredEventTypes
        for(Map.Entry<Integer, String> entry : filteredEvents.entrySet()){
            filteredEventTypes.put(entry.getKey(), eventTypes.get(entry.getKey()));
        }

        CalendarAdapterPro adapter = new CalendarAdapterPro(this, days, filteredEvents, filteredEventTypes);
        calendarGrid.setAdapter(adapter);

        calendarGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                int dayOfMonth = position + 2 - firstDayOfWeek;

                if (dayOfMonth > 0 && dayOfMonth <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    showEventDetails(dayOfMonth);
                }
            }
        });
    }

    private void showEventDetails(final int dayOfMonth) {
        if (events.containsKey(dayOfMonth)) {
            final String eventTitle = events.get(dayOfMonth);

            db.collection("eventos")
                    .whereEqualTo("título", eventTitle)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            String eventDescription = String.valueOf(document.get("descripción"));

                            AlertDialog.Builder builder = new AlertDialog.Builder(CalendarioProfesor.this);
                            builder.setTitle(eventTitle)
                                    .setMessage(eventDescription)
                                    .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss())
                                    .show();
                        } else {
                            Toast.makeText(CalendarioProfesor.this, "Error al obtener detalles del evento.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No hay eventos para este día.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profesor_filtro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_filter_todos) {
            currentFilter = "todos";
            setupCalendar(); // Recargar el calendario con todos los eventos
            return true;
        } else if (id == R.id.menu_filter_talleres) {
            currentFilter = "talleres";
            setupCalendar(); // Recargar el calendario con solo talleres
            return true;
        } else if (id == R.id.menu_filter_cursos) {
            currentFilter = "cursos";
            setupCalendar(); // Recargar el calendario con solo cursos
            return true;
        }

        if (id == R.id.menu_add_user) {
            Intent intent = new Intent(this, AnadirUsuarioActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_user_list) {
            Intent intent = new Intent(this, ListaUsuariosActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_add_event) {
            Intent intent = new Intent(this, AnadirEventoActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_event_list) {
            Intent intent = new Intent(this, ListaEventosActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Map<Integer, String> filterEvents(Map<Integer, String> events, String filter) {
        if (filter.equals("todos")) {
            return events; // Mostrar todos los eventos
        }

        Map<Integer, String> filteredEvents = new HashMap<>();
        for (Map.Entry<Integer, String> entry : events.entrySet()) {
            String eventTitle = entry.getValue();
            String eventType = getEventType(eventTitle);

            if (filter.equals("talleres") && eventType.equals("taller")) {
                filteredEvents.put(entry.getKey(), eventTitle);
            } else if (filter.equals("cursos") && eventType.equals("curso")) {
                filteredEvents.put(entry.getKey(), eventTitle);
            }
        }
        return filteredEvents;
    }

    private String getEventType(String eventTitle) {
        if (eventTitle.toLowerCase().contains("taller")) {
            return "taller";
        } else if (eventTitle.toLowerCase().contains("curso")) {
            return "curso";
        } else {
            return "otro";
        }
    }
}