package com.example.recyclerviewcardview;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SiswaAdapter adapter;
    List<Siswa> siswaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        load();
        isiData();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void load() {
        recyclerView = findViewById(R.id.rcvSiswa);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void isiData() {
        siswaList = new ArrayList<Siswa>();
        siswaList.add (new Siswa("Joni", "Surabaya" ));
        siswaList.add (new Siswa("Eko", "Sidoarjo" ));
        siswaList.add (new Siswa("Tejo", "Bogor" ));
        siswaList.add (new Siswa("Siti", "Jogja" ));
        siswaList.add (new Siswa("Roni", "Bali" ));
        siswaList.add (new Siswa("Even", "Palembang" ));
        siswaList.add (new Siswa("Joni", "Surabaya" ));


        adapter = new SiswaAdapter(this, siswaList);
        recyclerView.setAdapter(adapter);
    }

    public void btnTambah(View view) {
        siswaList.add(new Siswa("JONI RAMBO", "JAKARTA"));
        adapter.notifyDataSetChanged();
    }
}