package com.example.adminapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.adminapp.AddProductActivity;
import com.example.adminapp.R;
import com.example.adminapp.models.Product;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> products;

    public AdminProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText("Rp " + product.getPrice());
        Glide.with(context).load(product.getImageUrl()).into(holder.ivProduct);

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddProductActivity.class);
            intent.putExtra("product_id", product.getId());
            intent.putExtra("name", product.getName());
            intent.putExtra("desc", product.getDescription());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("image_url", product.getImageUrl());
            // Tambah extra lain jika perlu
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            FirebaseDatabase.getInstance().getReference("products").child(product.getId()).removeValue();
            Toast.makeText(context, "Produk dihapus", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvName, tvPrice;
        Button btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_admin_product);
            tvName = itemView.findViewById(R.id.tv_admin_name);
            tvPrice = itemView.findViewById(R.id.tv_admin_price);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}