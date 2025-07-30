package com.example.recyclerviewcardview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;
import java.util.List; // Import List jika Anda akan menggunakan List untuk data

public class SiswaAdapter extends RecyclerView.Adapter<SiswaAdapter.ViewHolder> {

    // Tambahkan variabel untuk menyimpan data siswa
    private Context context;
    private List<Siswa> siswaList; // Ganti Siswa dengan model data Anda jika berbeda

    // Konstruktor untuk menerima data dan activity (opsional)
    public SiswaAdapter(Context context, List<Siswa> siswaList) {
        this.context = context;
        this.siswaList = siswaList;
    }

    // ViewHolder untuk menampung tampilan item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txNama, txAlamat, txMenu; // Tambahkan txMenu di sini

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txNama = itemView.findViewById(R.id.txNama);
            txAlamat = itemView.findViewById(R.id.txAlamat);
            txMenu = itemView.findViewById(R.id.txMenu); // Inisialisasi txMenu
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_siswa, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) { // Perbaiki tipe parameter 'viewHolder' ke ViewHolder yang benar
        Siswa siswa = siswaList.get(position);
        viewHolder.txNama.setText(siswa.getNama());
        viewHolder.txAlamat.setText(siswa.getAlamat());

        viewHolder.txMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, viewHolder.txMenu);
                popupMenu.inflate(R.menu.menu_option);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Gunakan finalPosition agar aman di dalam inner class
                        final int currentPosition = viewHolder.getAdapterPosition();
                        if (currentPosition == RecyclerView.NO_POSITION) {
                            return false; // Item sudah dihapus atau tidak valid
                        }

                        // Perhatikan bahwa R.id.menu_simpan dan R.id.menu_hapus
                        // harus didefinisikan di file res/menu/menu_option.xml
                        int itemId = item.getItemId();
                        if (itemId == R.id.menu_simpan) {
                            Toast.makeText(context, "Simpan Data", Toast.LENGTH_SHORT).show();
                            return true; // Kembalikan true jika event ditangani
                        } else if (itemId == R.id.menu_hapus) {
                            siswaList.remove(currentPosition);
                            // Gunakan notifyItemRemoved untuk animasi yang lebih baik
                            notifyItemRemoved(currentPosition);
                            // Jika Anda perlu memperbarui posisi item lain setelah penghapusan
                            notifyItemRangeChanged(currentPosition, siswaList.size());
                            Toast.makeText(context, "Sudah di Hapus", Toast.LENGTH_SHORT).show();
                            return true; // Kembalikan true jika event ditangani
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return siswaList.size();
    }

    // HAPUS DEFINISI ViewHolder YANG INI KARENA SUDAH ADA DI ATAS
    /*
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txNama, txAlamat, txMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txNama = itemView.findViewById(R.id.txNama);
            txAlamat = itemView.findViewById(R.id.txAlamat);
            txMenu = itemView.findViewById(R.id.txMenu);
        }
    }
    */
}
