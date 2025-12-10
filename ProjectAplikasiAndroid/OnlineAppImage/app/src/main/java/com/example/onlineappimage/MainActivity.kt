package com.example.onlineappimage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.onlineappimage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUrl = "https://images.unsplash.com/photo-1606787366850-de6330128bfc?q=80&w=1000&auto=format&fit=crop"

        binding.btnLoad.setOnClickListener {
            binding.imageView.load(imageUrl) {
                crossfade(true)
                placeholder(R.drawable.loading) // buat drawable loading.xml atau pakai ic_launcher
                error(R.drawable.error)         // buat drawable error
            }
        }

        // Opsional: langsung load saat buka aplikasi
        // binding.imageView.load(imageUrl) { ... }
    }
}