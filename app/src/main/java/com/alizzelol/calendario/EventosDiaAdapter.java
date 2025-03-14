package com.alizzelol.calendario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventosDiaAdapter extends RecyclerView.Adapter<EventosDiaAdapter.ViewHolder> {

    private List<Evento> eventos;

    public EventosDiaAdapter(List<Evento> eventos) {
        this.eventos = eventos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento_dia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Evento evento = eventos.get(position);
        holder.textViewTitulo.setText(evento.getTitulo());
        holder.textViewHora.setText(evento.getHora());
        holder.textViewTipo.setText(evento.getTipo());
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitulo, textViewHora, textViewTipo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewHora = itemView.findViewById(R.id.textViewHora);
            textViewTipo = itemView.findViewById(R.id.textViewTipo);
        }
    }
}
