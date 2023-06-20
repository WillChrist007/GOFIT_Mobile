package com.william.gofiit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.william.gofiit.R;
import com.william.gofiit.models.Ijin;

import java.util.List;

public class IjinAdapter extends RecyclerView.Adapter<IjinAdapter.ViewHolder> {
    private Context context;
    private List<Ijin> ijinList;

    public IjinAdapter(Context context, List<Ijin> ijinList) {
        this.context = context;
        this.ijinList = ijinList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ijin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ijin ijin = ijinList.get(position);

        holder.textViewIdInstruktur.setText(String.valueOf(ijin.getIdInstruktur()));
        holder.textViewIdPengganti.setText(String.valueOf(ijin.getIdPengganti()));
        holder.textViewTanggalPengajuan.setText(ijin.getTanggalPengajuan());
        holder.textViewTanggalIjin.setText(ijin.getTanggalIjin());
        holder.textViewTanggalKonfirmasi.setText(ijin.getTanggalKonfirmasi());
        holder.textViewStatus.setText(ijin.getStatus());
        holder.textViewKeterangan.setText(ijin.getKeterangan());
    }

    @Override
    public int getItemCount() {
        return ijinList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewIdInstruktur, textViewIdPengganti, textViewTanggalPengajuan, textViewTanggalIjin, textViewTanggalKonfirmasi, textViewStatus, textViewKeterangan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewIdInstruktur = itemView.findViewById(R.id.textViewIdInstruktur);
            textViewIdPengganti = itemView.findViewById(R.id.textViewIdPengganti);
            textViewTanggalPengajuan = itemView.findViewById(R.id.textViewTanggalPengajuan);
            textViewTanggalIjin = itemView.findViewById(R.id.textViewTanggalIjin);
            textViewTanggalKonfirmasi = itemView.findViewById(R.id.textViewTanggalKonfirmasi);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewKeterangan = itemView.findViewById(R.id.textViewKeterangan);
        }
    }


}

