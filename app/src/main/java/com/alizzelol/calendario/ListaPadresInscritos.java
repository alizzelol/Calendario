package com.alizzelol.calendario;

import android.os.Bundle;
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
    private ListaPadresAdapter listaPadresAdapter;
    private List<String> listaPadres = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_padres_inscritos);

        recyclerViewPadres = findViewById(R.id.recyclerViewPadres);
        recyclerViewPadres.setLayoutManager(new LinearLayoutManager(this));

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
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String padreId = document.getString("padreId");
                            db.collection("usuarios").document(padreId)
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            String nombrePadre = documentSnapshot.getString("nombre") + " " + documentSnapshot.getString("apellido");
                                            listaPadres.add(nombrePadre);
                                            listaPadresAdapter = new ListaPadresAdapter(listaPadres);
                                            recyclerViewPadres.setAdapter(listaPadresAdapter);
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(this, "Error al cargar padres inscritos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
