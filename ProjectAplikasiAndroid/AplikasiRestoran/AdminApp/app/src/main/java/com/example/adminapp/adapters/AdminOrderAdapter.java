package com.example.adminapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.adminapp.R;
import com.example.adminapp.models.Order;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.ViewHolder> {
    private Context context;
    private List<Order> orders;

    public AdminOrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvOrderId.setText("Order ID: " + order.getId());
        holder.tvUserId.setText("User: " + order.getUserId());
        holder.tvTotal.setText("Total: Rp " + order.getTotalPrice());
        holder.tvStatus.setText("Status: " + order.getStatus());

        holder.btnUpdateStatus.setOnClickListener(v -> {
            // Cycle status: Pending -> Diproses -> Selesai
            String newStatus = "Diproses";
            if ("Diproses".equals(order.getStatus())) newStatus = "Selesai";
            else if ("Selesai".equals(order.getStatus())) newStatus = "Pending";
            FirebaseDatabase.getInstance().getReference("orders").child(order.getId()).child("status").setValue(newStatus);
            Toast.makeText(context, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
        });

        holder.btnSendNotif.setOnClickListener(v -> {
            // Kirim FCM ke user
            // Untuk implementasi full, gunakan HTTP request ke FCM server dengan token user (simpan token di users/token)
            // Contoh sederhana: Push ke notifications/userId
            FirebaseDatabase.getInstance().getReference("notifications").child(order.getUserId()).push()
                    .child("message").setValue("Pesanan Anda (" + order.getId() + ") telah diupdate: " + order.getStatus());
            Toast.makeText(context, "Notif dikirim", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvUserId, tvTotal, tvStatus;
        Button btnUpdateStatus, btnSendNotif;

        public ViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvUserId = itemView.findViewById(R.id.tv_user_id);
            tvTotal = itemView.findViewById(R.id.tv_total);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnUpdateStatus = itemView.findViewById(R.id.btn_update_status);
            btnSendNotif = itemView.findViewById(R.id.btn_send_notif);
        }
    }
}