package com.alizzelol.calendario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventoAdapterApuntarse extends RecyclerView.Adapter<EventoAdapterApuntarse.EventoViewHolder> {

    private List<Evento> eventos;
    private OnEventoClickListener listener;

    public EventoAdapterApuntarse(List<Evento> eventos, OnEventoClickListener listener) {
        this.eventos = eventos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento_lista, parent, false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        Evento evento = eventos.get(position);
        holder.textViewTitulo.setText(evento.getTitulo());
        holder.textViewDescripcion.setText(evento.getDescripcion());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.textViewFecha.setText(sdf.format(evento.getFecha()));
        holder.textViewTipo.setText(evento.getTipo());
        holder.textViewHora.setText(evento.getHora());
        holder.itemView.setOnClickListener(v -> listener.onEventoClick(evento.getId()));
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public static class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitulo, textViewDescripcion, textViewFecha, textViewHora, textViewTipo;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewDescripcion = itemView.findViewById(R.id.textViewDescripcion);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
            textViewHora = itemView.findViewById(R.id.textViewHora);
            textViewTipo = itemView.findViewById(R.id.textViewTipo);
        }
    }

    public interface OnEventoClickListener {
        void onEventoClick(String eventoId);
    }
}
