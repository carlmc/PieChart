package com.example.cmunayll.chartjson;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

/**
 * Created by cmunayll on 11/04/2018.
 */

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.ViewHolder> {

    List<Gasto> gastos;
    private int layout;

    public GastoAdapter(List<Gasto> gastos, int layout) {
        this.gastos = gastos;
        this.layout = layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(gastos.get(position));
    }

    @Override
    public int getItemCount() {
        return (gastos != null ? gastos.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTipo;
        public TextView tvMonto;
        public ImageView ivColor;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTipo = itemView.findViewById(R.id.tvType);
            tvMonto = itemView.findViewById(R.id.tvAmount);
            ivColor = itemView.findViewById(R.id.ivColor);
        }

        public void bind(final Gasto gasto) {
            this.tvTipo.setText(gasto.getType());
            this.tvMonto.setText("S/ "+gasto.getAmount());

            if (tvTipo.getText().equals("Pago de Servicios"))
                this.ivColor.setImageResource(R.drawable.c102255102);
            if (tvTipo.getText().equals("Comida"))
                this.ivColor.setImageResource(R.drawable.cyan);
            if (tvTipo.getText().equals("Supermercados"))
                this.ivColor.setImageResource(R.drawable.c255255102);
            if (tvTipo.getText().equals("Retiros"))
                this.ivColor.setImageResource(R.drawable.red);
            if (tvTipo.getText().equals("Otros"))
                this.ivColor.setImageResource(R.drawable.c102178255);
        }
    }
}
