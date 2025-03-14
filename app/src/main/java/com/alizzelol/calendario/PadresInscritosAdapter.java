package com.alizzelol.calendario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PadresInscritosAdapter extends RecyclerView.Adapter<PadresInscritosAdapter.PadreViewHolder> {

    private List<String> listaPadres;

    public PadresInscritosAdapter(List<String> listaPadres) {
        this.listaPadres = listaPadres;
    }

    @NonNull
    @Override
    public PadreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new PadreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PadreViewHolder holder, int position) {
        String padre = listaPadres.get(position);
        holder.textViewNombrePadre.setText(padre);
    }

    @Override
    public int getItemCount() {
        return listaPadres.size();
    }

    public static class PadreViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombrePadre;

        public PadreViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombrePadre = itemView.findViewById(android.R.id.text1);
        }
    }
}
