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

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {

    private List<Evento> listaEventos;
    private OnEventoClickListener listener;

    public interface OnEventoClickListener {
        void onEventoClick(String eventoId);
    }

    public EventoAdapter(List<Evento> listaEventos, OnEventoClickListener listener) {
        this.listaEventos = listaEventos;
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
        Evento evento = listaEventos.get(position);
        holder.textViewTitulo.setText(evento.getTitulo());
        holder.textViewDescripcion.setText(evento.getDescripcion());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.textViewFecha.setText(sdf.format(evento.getFecha()));
        holder.textViewHora.setText(evento.getHora());
        holder.textViewTipo.setText(evento.getTipo());
        holder.itemView.setOnClickListener(v -> listener.onEventoClick(evento.getId()));
    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public static class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitulo, textViewDescripcion, textViewFecha, textViewHora, textViewTipo;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewDescripcion = itemView.findViewById(R.id.textViewDescripcion);
            textViewHora = itemView.findViewById(R.id.textViewHora);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
            textViewTipo = itemView.findViewById(R.id.textViewTipo);
        }
    }
}
