package com.william.gofiit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.william.gofiit.R;
import com.william.gofiit.models.Kelas;

import java.util.List;

public class KelasAdapter extends RecyclerView.Adapter<KelasAdapter.KelasViewHolder> {
    private Context context;
    private List<Kelas> kelasList;

    public KelasAdapter(Context context, List<Kelas> kelasList) {
        this.context = context;
        this.kelasList = kelasList;
    }

    @NonNull
    @Override
    public KelasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_kelas, parent, false);
        return new KelasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KelasViewHolder holder, int position) {
        Kelas kelas = kelasList.get(position);

        holder.tvNomorBooking.setText(kelas.getNomorBooking());
        holder.tvTanggalBooking.setText(kelas.getTanggalBooking());
        holder.tvWaktuPresensi.setText(kelas.getWaktuPresensi());
        holder.tvTanggalJadwal.setText(kelas.getTanggalJadwal());
        holder.tvKelas.setText(kelas.getIdKelas());
        // Set other data as needed
    }

    @Override
    public int getItemCount() {
        return kelasList.size();
    }

    public class KelasViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomorBooking, tvTanggalBooking, tvWaktuPresensi, tvTanggalJadwal, tvKelas;
        // Declare other views as needed

        public KelasViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNomorBooking = itemView.findViewById(R.id.tvNomorBooking);
            tvTanggalBooking = itemView.findViewById(R.id.tvTanggalBooking);
            tvWaktuPresensi = itemView.findViewById(R.id.tvWaktuPresensi);
            tvTanggalJadwal = itemView.findViewById(R.id.tvTanggalJadwal);
            tvKelas = itemView.findViewById(R.id.tvKelas);
            // Initialize other views as needed
        }
    }
}

