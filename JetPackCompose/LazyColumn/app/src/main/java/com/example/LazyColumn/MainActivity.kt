package com.example.LazyColumn

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.LazyColumn.R  // Asumsi resource gambar ada di R.drawable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScrollableContent()
                }
            }
        }
    }
}

data class ItemData(
    val name: String,
    val description: String,
    val imageRes: Int  // Resource ID untuk gambar
)

@Composable
fun ScrollableContent() {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // Data sample untuk LazyColumn (daftar vertikal dengan data kompleks: gambar + teks)
    val itemList = listOf(
        ItemData("Item 1", "Description for item 1", R.drawable.ic_launcher_foreground),
        ItemData("Item 2", "Description for item 2", R.drawable.ic_launcher_foreground),
        ItemData("Item 3", "Description for item 3", R.drawable.ic_launcher_foreground),
        ItemData("Item 4", "Description for item 4", R.drawable.ic_launcher_foreground),
        ItemData("Item 5", "Description for item 5", R.drawable.ic_launcher_foreground)
    )

    // Data sample untuk LazyRow (daftar horizontal sederhana)
    val horizontalItems = listOf("Horizontal 1", "Horizontal 2", "Horizontal 3", "Horizontal 4", "Horizontal 5")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)  // Tambahkan vertical scroll dengan rememberScrollState
            .padding(16.dp)
    ) {
        Text(
            text = "LazyColumn Example (Vertical List with Complex Data)",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .height(300.dp)  // Batasi tinggi agar bisa scroll
                .fillMaxWidth()
        ) {
            itemsIndexed(itemList) { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            // Fungsi klik: Tampilkan Toast dengan indeks dan nama item
                            Toast.makeText(context, "Clicked item at index $index: ${item.name}", Toast.LENGTH_SHORT).show()
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Gambar
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = item.name,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Teks kompleks
                    Column {
                        Text(text = "Index: $index", fontSize = 16.sp)
                        Text(text = item.name, fontSize = 18.sp)
                        Text(text = item.description, fontSize = 14.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "LazyRow Example (Horizontal List)",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)  // Batasi tinggi
        ) {
            itemsIndexed(horizontalItems) { index, item ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            // Fungsi klik: Tampilkan Toast dengan indeks dan item
                            Toast.makeText(context, "Clicked horizontal item at index $index: $item", Toast.LENGTH_SHORT).show()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "$item (Index: $index)", fontSize = 16.sp)
                }
            }
        }
    }
}