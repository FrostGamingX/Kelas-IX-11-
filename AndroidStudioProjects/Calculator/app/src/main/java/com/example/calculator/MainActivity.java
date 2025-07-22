package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView txHasil;
    EditText Bil_1;
    EditText Bil_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        load();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void load() {
        txHasil = findViewById(R.id.txHasil);
        Bil_1 = findViewById(R.id.Bil_1);
        Bil_2 = findViewById(R.id.Bil_2);
    }

    public void btnJumlah(View view) {

        if (Bil_1.getText().toString().equals("") || Bil_2.getText().toString().equals("")) {
            Toast.makeText(this, "Bilangan Kosong", Toast.LENGTH_SHORT).show();
        } else {
            double Bil_1 = Double.parseDouble(this.Bil_1.getText().toString());
            double Bil_2 = Double.parseDouble(this.Bil_2.getText().toString());

            double hasil = Bil_1 + Bil_2;

            txHasil.setText(hasil + "");
        }
    }

    public void btnKurang(View view) {

        if (Bil_1.getText().toString().equals("") || Bil_2.getText().toString().equals("")) {
            Toast.makeText(this, "Bilangan Kosong", Toast.LENGTH_SHORT).show();
        } else {
            double Bil_1 = Double.parseDouble(this.Bil_1.getText().toString());
            double Bil_2 = Double.parseDouble(this.Bil_2.getText().toString());

            double hasil = Bil_1 - Bil_2;

            txHasil.setText(hasil + "");
        }
    }

    public void btnKali(View view) {

        if (Bil_1.getText().toString().equals("") || Bil_2.getText().toString().equals("")) {
            Toast.makeText(this, "Bilangan Kosong", Toast.LENGTH_SHORT).show();
        } else {
            double Bil_1 = Double.parseDouble(this.Bil_1.getText().toString());
            double Bil_2 = Double.parseDouble(this.Bil_2.getText().toString());

            double hasil = Bil_1 * Bil_2;

            txHasil.setText(hasil + "");
        }
    }

    public void btnBagi(View view) {

        if (Bil_1.getText().toString().equals("") || Bil_2.getText().toString().equals("")) {
            Toast.makeText(this, "Bilangan Kosong", Toast.LENGTH_SHORT).show();
        } else {
            double Bil_1 = Double.parseDouble(this.Bil_1.getText().toString());
            double Bil_2 = Double.parseDouble(this.Bil_2.getText().toString());

            double hasil = Bil_1 / Bil_2;

            txHasil.setText(hasil + "");
        }
    }

}