// SiswaAdapter.java
package com.example.sqlitedatabase; // Sesuaikan dengan package Anda

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SiswaAdapter extends RecyclerView.Adapter<SiswaAdapter.SiswaViewHolder> {

    private Context context;
    private List<Siswa> dataSiswa;
    private OnItemClickListener listener;
    // Jika Anda membutuhkan interaksi klik, tambahkan listener di sini
    // private OnItemClickListener listener;

    public SiswaAdapter(Context context, List<Siswa> dataSiswa) {
        this.context = context;
        this.dataSiswa = dataSiswa;
    }

    @NonNull
    @Override
    public SiswaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_siswa, parent, false);
        return new SiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SiswaViewHolder holder, int position) {
        Siswa siswa = dataSiswa.get(position);
        holder.tvNamaSiswa.setText(siswa.getNamaSiswa());
        holder.tvAlamatSiswa.setText(siswa.getAlamatSiswa());

        // Jika ada tombol update/delete di item_siswa.xml, set listener di sini
        // holder.btnUpdateSiswa.setOnClickListener(v -> { ... });
        // holder.btnDeleteSiswa.setOnClickListener(v -> { ... });
    }

    @Override
    public int getItemCount() {
        return dataSiswa.size();
    }

    static class SiswaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaSiswa, tvAlamatSiswa;
        // Button btnUpdateSiswa, btnDeleteSiswa; // Jika ada

        public SiswaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaSiswa = itemView.findViewById(R.id.tvNamaSiswa);
            tvAlamatSiswa = itemView.findViewById(R.id.tvAlamatSiswa);
            // btnUpdateSiswa = itemView.findViewById(R.id.btnUpdateSiswa);
            // btnDeleteSiswa = itemView.findViewById(R.id.btnDeleteSiswa);

            // itemView.setOnClickListener(v -> { ... jika ingin klik pada seluruh item ... });
        }
    }


    public interface OnItemClickListener {
    void onItemClick(Siswa siswa);
     }

     public void setOnItemClickListener(OnItemClickListener listener) {
     this.listener = listener;
    }

    // Metode untuk memperbarui data di adapter
    public void updateData(List<Siswa> newDataSiswa) {
        this.dataSiswa.clear();
        this.dataSiswa.addAll(newDataSiswa);
        notifyDataSetChanged();
    }
}
    