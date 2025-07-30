package com.example.sqlitedatabase;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
// import android.widget.Toast; // Tidak digunakan lagi secara langsung

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.ViewHolder> {

    private static final String TAG = "BarangAdapter"; // Untuk logging
    Context context; // Masih berguna untuk PopupMenu
    List<Barang> baranglist;
    private OnItemActionClickListener listener;

    public interface OnItemActionClickListener {
        void onUpdateClick(String idBarang);
        void onDeleteClick(String idBarang);
    }

    public BarangAdapter(Context context, List<Barang> baranglist, OnItemActionClickListener listener) {
        this.context = context;
        this.baranglist = baranglist;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_barang, viewGroup, false);
        // Pastikan R.layout.item_barang ada dan benar
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        if (baranglist == null || position < 0 || position >= baranglist.size()) {
            Log.e(TAG, "onBindViewHolder: Invalid position or baranglist is null. Position: " + position);
            return; // Hindari crash jika data tidak valid
        }

        Barang currentBarang = baranglist.get(position);
        if (currentBarang == null) {
            Log.e(TAG, "onBindViewHolder: currentBarang is null at position: " + position);
            // Set View ke default atau kosong untuk menghindari NullPointerException lebih lanjut
            viewHolder.txBarang.setText("");
            viewHolder.txStok.setText("");
            viewHolder.txHarga.setText("");
            return;
        }

        viewHolder.txBarang.setText(currentBarang.getBarang() != null ? currentBarang.getBarang() : "");

        // Asumsi Barang.java memiliki getStok() -> int dan getHarga() -> double
        viewHolder.txStok.setText(String.valueOf(currentBarang.getStok()));

        try {
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            viewHolder.txHarga.setText(formatRupiah.format(currentBarang.getHarga()));
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Error formatting harga: " + currentBarang.getHarga(), e);
            viewHolder.txHarga.setText(String.valueOf(currentBarang.getHarga())); // Fallback
        }


        viewHolder.txMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, viewHolder.txMenu);
            // Pastikan R.menu.menu_item ada di res/menu
            try {
                popupMenu.inflate(R.menu.menu_item);
            } catch (Exception e) {
                Log.e(TAG, "Error inflating popup menu. Pastikan R.menu.menu_item ada.", e);
                // Mungkin tampilkan Toast atau jangan tampilkan menu jika gagal inflate
                return;
            }


            popupMenu.setOnMenuItemClickListener(item -> {
                int currentBindingPosition = viewHolder.getBindingAdapterPosition();
                if (currentBindingPosition == RecyclerView.NO_POSITION) {
                    Log.w(TAG, "Menu item click: Invalid adapter position.");
                    return false;
                }
                // Pastikan currentBindingPosition masih valid dalam list
                if (currentBindingPosition >= baranglist.size()) {
                    Log.w(TAG, "Menu item click: Position out of bounds for baranglist.");
                    return false;
                }

                String idBarangToAct = baranglist.get(currentBindingPosition).getIdbarang();
                if (idBarangToAct == null) {
                    Log.e(TAG, "Menu item click: idBarangToAct is null at position " + currentBindingPosition);
                    return false;
                }

                int itemId = item.getItemId();
                if (itemId == R.id.ubah) { // Pastikan R.id.ubah ada di menu_item.xml
                    if (listener != null) {
                        listener.onUpdateClick(idBarangToAct);
                    } else {
                        Log.w(TAG, "Listener for onUpdateClick is null.");
                    }
                    return true;
                } else if (itemId == R.id.hapus) { // Pastikan R.id.hapus ada di menu_item.xml
                    if (listener != null) {
                        listener.onDeleteClick(idBarangToAct);
                    } else {
                        Log.w(TAG, "Listener for onDeleteClick is null.");
                    }
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return baranglist != null ? baranglist.size() : 0; // Hindari NullPointerException jika list null
    }

    // Buat static jika tidak mengakses field dari outer class (BarangAdapter)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txBarang, txStok, txHarga, txMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txBarang = itemView.findViewById(R.id.txBarang);
            txStok = itemView.findViewById(R.id.txStok);
            txHarga = itemView.findViewById(R.id.txHarga);
            txMenu = itemView.findViewById(R.id.txMenu);

            // Validasi apakah View ditemukan
            if (txBarang == null) Log.e(TAG, "ViewHolder: TextView txBarang tidak ditemukan!");
            if (txStok == null) Log.e(TAG, "ViewHolder: TextView txStok tidak ditemukan!");
            if (txHarga == null) Log.e(TAG, "ViewHolder: TextView txHarga tidak ditemukan!");
            if (txMenu == null) Log.e(TAG, "ViewHolder: TextView txMenu (menu anchor) tidak ditemukan!");
        }
    }

    // Metode untuk mengupdate data di adapter dengan aman
    public void updateData(List<Barang> newBarangList) {
        if (newBarangList != null) {
            this.baranglist.clear();
            this.baranglist.addAll(newBarangList);
        } else {
            this.baranglist.clear(); // Jika list baru null, bersihkan list yang ada
            Log.w(TAG, "updateData: newBarangList is null. Clearing current data.");
        }
        notifyDataSetChanged();
    }
}
