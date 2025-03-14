package com.alizzelol.calendario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class EventoAdapterTipos extends RecyclerView.Adapter<EventoAdapterTipos.EventoViewHolder> {

    private ArrayList<Evento> eventos;

    public EventoAdapterTipos(ArrayList<Evento> eventos) {
        this.eventos = eventos;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento_tipos, parent, false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        Evento evento = eventos.get(position);
        holder.tituloTextView.setText(evento.getTitulo());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.fechaTextView.setText(sdf.format(evento.getFecha()));
        holder.descripcionTextView.setText(evento.getDescripcion());
        holder.horaTextView.setText(evento.getHora());
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public static class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTextView;
        TextView fechaTextView;
        TextView descripcionTextView;
        TextView horaTextView;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.tituloTextView);
            fechaTextView = itemView.findViewById(R.id.fechaTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionTextView);
            horaTextView = itemView.findViewById(R.id.horaTextView);
        }
    }
}
