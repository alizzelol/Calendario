package com.alizzelol.calendario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {
    private List<Usuario> listaUsuarios;
    private OnUsuarioClickListener listener;

    public interface OnUsuarioClickListener {
        void onUsuarioClick(String usuarioId);
    }

    public UsuarioAdapter(List<Usuario> listaUsuarios, OnUsuarioClickListener listener) {
        this.listaUsuarios = listaUsuarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);
        holder.textViewNombre.setText(usuario.getNombre());
        holder.textViewApellido.setText(usuario.getApellido());
        holder.textViewEmail.setText(usuario.getEmail());
        holder.textViewTelefono.setText(usuario.getTelefono());
        holder.textViewRol.setText(usuario.getRol());
        holder.itemView.setOnClickListener(v -> listener.onUsuarioClick(usuario.getDocumentId()));
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        TextView textViewApellido;
        TextView textViewEmail;
        TextView textViewTelefono;
        TextView textViewRol;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewApellido = itemView.findViewById(R.id.textViewApellido);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewTelefono = itemView.findViewById(R.id.textViewTelefono);
            textViewRol = itemView.findViewById(R.id.textViewRol);
        }
    }
}
