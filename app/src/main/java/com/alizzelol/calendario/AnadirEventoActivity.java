package com.alizzelol.calendario;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AnadirEventoActivity extends AppCompatActivity {

    private EditText editTextTitulo, editTextDescripcion, editTextHora;
    private Button buttonFecha, buttonGuardar;
    private Calendar calendar = Calendar.getInstance();
    private FirebaseFirestore db;
    private Spinner spinnerTipoEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_evento);

        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        editTextHora = findViewById(R.id.editTextHora);
        buttonFecha = findViewById(R.id.buttonFecha);
        buttonGuardar = findViewById(R.id.buttonGuardar);
        spinnerTipoEvento = findViewById(R.id.spinnerTipoEvento);

        db = FirebaseFirestore.getInstance();

        // Inicializar el Spinner con las opciones "taller" y "curso"
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.tipos_evento, // Crear un array en strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoEvento.setAdapter(adapter);

        buttonFecha.setOnClickListener(v -> mostrarDatePicker());
        buttonGuardar.setOnClickListener(v -> guardarEvento());
    }

    private void mostrarDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    buttonFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void guardarEvento() {
        String titulo = editTextTitulo.getText().toString();
        String descripcion = editTextDescripcion.getText().toString();
        String hora = editTextHora.getText().toString();
        String tipoEvento = spinnerTipoEvento.getSelectedItem().toString(); // Obtener el tipo de evento

        if (titulo.isEmpty() || descripcion.isEmpty() || hora.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> evento = new HashMap<>();
        evento.put("título", titulo);
        evento.put("descripción", descripcion);
        evento.put("fecha", calendar.getTime());
        evento.put("hora", hora);
        evento.put("tipo", tipoEvento); // Guardar el tipo de evento

        db.collection("eventos")
                .add(evento)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Evento guardado con éxito.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Indicar que la operación fue exitosa
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al guardar el evento.", Toast.LENGTH_SHORT).show();
                });
    }
}