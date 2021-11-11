package com.example.dabebel.registro;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class Adaptador extends FirestoreRecyclerAdapter<POJO, com.example.dabebel.registro.Adaptador.ViewHolder> {

    private Context context;
    protected View.OnClickListener onClickListener;

    public Adaptador(Context context,
                     @NonNull FirestoreRecyclerOptions<POJO> options) {
        super(options);
        this.context = context.getApplicationContext();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView foto;
        public final TextView coste;
        public final TextView duracion;
        public final TextView fecha;
        public final TextView estacion;

        public ViewHolder(View itemView) {
            super(itemView);
            this.foto = (ImageView) itemView.findViewById(R.id.imgRecycler);
            this.coste = (TextView) itemView.findViewById(R.id.txtCoste);
            this.duracion = (TextView) itemView.findViewById(R.id.txtDuracion);
            this.fecha = (TextView) itemView.findViewById(R.id.txtFecha);
            this.estacion = (TextView) itemView.findViewById(R.id.txtEstacion);
        }
    }

    @Override public Adaptador.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.elemento_lista, parent, false);
        return new com.example.dabebel.registro.Adaptador.ViewHolder(view);
    }

    @Override protected void onBindViewHolder(@NonNull Adaptador
            .ViewHolder holder, int position, @NonNull POJO imagen) {
        holder.coste.setText(imagen.getCoste());
        holder.duracion.setText(imagen.getDuracion());
        holder.fecha.setText(imagen.getFecha());
        holder.estacion.setText(imagen.getEstacion());
        /*CharSequence prettyTime = DateUtils.getRelativeDateTimeString(
                context, imagen.gethInicio(), DateUtils.SECOND_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS, 0);
        holder.tiempo.setText(prettyTime);*/
        Glide.with(context)
                .load(imagen.getFoto())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.foto);
        holder.itemView.setOnClickListener(onClickListener);
    }

    public void setOnItemClickListener(View.OnClickListener onClick) {
        onClickListener = onClick;
    }

    public String getKey(int pos) {
        return super.getSnapshots().getSnapshot(pos).getId();
    }

}