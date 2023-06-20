package com.william.gofiit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.william.gofiit.R;
import com.william.gofiit.models.Gym;

import java.util.List;

public class GymAdapter extends RecyclerView.Adapter<GymAdapter.GymViewHolder> {
    private Context context;
    private List<Gym> gymList;

    public GymAdapter(Context context, List<Gym> gymList) {
        this.context = context;
        this.gymList = gymList;
    }

    @NonNull
    @Override
    public GymViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gym, parent, false);
        return new GymViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GymViewHolder holder, int position) {
        Gym gym = gymList.get(position);

        holder.tvNomorBooking.setText(gym.getNomorBooking());
        holder.tvTanggalBooking.setText(gym.getTanggalBooking());
        holder.tvTanggalGym.setText(gym.getTanggalGym());
        holder.TvWaktuGym.setText(gym.getWaktuGym());
        holder.tvWaktuPresensi.setText(gym.getWaktuPresensi());
        // Set other data as needed
    }

    @Override
    public int getItemCount() {
        return gymList.size();
    }

    public class GymViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomorBooking, tvTanggalBooking, tvTanggalGym, TvWaktuGym,tvWaktuPresensi;
        // Declare other views as needed

        public GymViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNomorBooking = itemView.findViewById(R.id.tvNomorBooking);
            tvTanggalBooking = itemView.findViewById(R.id.tvTanggalBooking);
            tvTanggalGym = itemView.findViewById(R.id.tvTangalGym);
            TvWaktuGym = itemView.findViewById(R.id.tvWaktuGym);
            tvWaktuPresensi = itemView.findViewById(R.id.tvWaktuPresensi);
            // Initialize other views as needed
        }
    }
}

