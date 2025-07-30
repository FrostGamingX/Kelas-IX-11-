package com.example.messagedialog;

import android.os.Bundle;
import android.view.View; // Import View
import android.widget.Button; // Import Button
import android.widget.Toast; // Import Toast

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog; // Import AlertDialog
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.DialogInterface; // Import DialogInterface

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- Tambahan kode untuk message dialog ---

        // Tombol untuk Toast
        // Pastikan Anda memiliki Button dengan ID button_toast di activity_main.xml
        Button buttonToast = findViewById(R.id.button_toast);
        buttonToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Selamat Belajar", Toast.LENGTH_SHORT).show();
            }
        });

        // Tombol untuk Alert Dialog sederhana
        // Pastikan Anda memiliki Button dengan ID button_alert_dialog di activity_main.xml
        Button buttonAlertDialog = findViewById(R.id.button_alert_dialog);
        buttonAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Info");
                builder.setMessage("Selamat Belajar");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Tombol untuk Alert Dialog dengan konfirmasi
        // Pastikan Anda memiliki Button dengan ID button_alert_dialog_button di activity_main.xml
        Button buttonAlertDialogButton = findViewById(R.id.button_alert_dialog_button);
        buttonAlertDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Konfirmasi");
                builder.setMessage("Yakin Akan Menghapus?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Aksi ketika tombol "Ya" ditekan
                        Toast.makeText(MainActivity.this, "Data dihapus!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Aksi ketika tombol "Tidak" ditekan
                        dialog.dismiss();
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // --- Akhir tambahan kode ---
    }
}
