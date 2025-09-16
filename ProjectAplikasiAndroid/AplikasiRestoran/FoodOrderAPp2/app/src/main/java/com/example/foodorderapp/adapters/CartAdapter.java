package com.example.foodorderapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodorderapp.R;
import com.example.foodorderapp.models.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

// Tambahkan Glide jika perlu untuk image

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private List<CartItem> items;

    public CartAdapter(Context context, List<CartItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.tvName.setText("Produk " + item.getProductId());  // Fetch name real dari DB jika perlu
        holder.tvPrice.setText("Rp " + item.getPrice());
        holder.tvQty.setText(String.valueOf(item.getQuantity()));

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        holder.btnPlus.setOnClickListener(v -> {
            int newQty = item.getQuantity() + 1;
            db.getReference("carts").child(uid).child(item.getProductId()).child("quantity").setValue(newQty);
        });

        holder.btnMinus.setOnClickListener(v -> {
            int newQty = item.getQuantity() - 1;
            if (newQty > 0) {
                db.getReference("carts").child(uid).child(item.getProductId()).child("quantity").setValue(newQty);
            } else {
                db.getReference("carts").child(uid).child(item.getProductId()).removeValue();
            }
        });

        holder.btnRemove.setOnClickListener(v -> db.getReference("carts").child(uid).child(item.getProductId()).removeValue());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvName, tvPrice, tvQty;
        Button btnPlus, btnMinus, btnRemove;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_cart_product);
            tvName = itemView.findViewById(R.id.tv_cart_name);
            tvPrice = itemView.findViewById(R.id.tv_cart_price);
            tvQty = itemView.findViewById(R.id.tv_qty);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            btnMinus = itemView.findViewById(R.id.btn_minus);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }
    }
}