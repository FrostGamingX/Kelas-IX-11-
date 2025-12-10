package com.example.quotesapp

data class Quote(
    val text: String,
    val author: String
)

val quoteList = listOf(
    Quote("Hidup adalah perjalanan yang penuh liku. Jadikan setiap liku sebagai pelajaran.", "Anonim"),
    Quote("Jangan takut gagal, takutlah karena tidak mencoba.", "Raden Ajeng Kartini"),
    Quote("Kesuksesan adalah kemarin tidak menjamin kesuksesan esok hari.", "Soekarno"),
    Quote("Lebih baik mencoba dan gagal daripada tidak mencoba sama sekali.", "Thomas Alva Edison"),
    Quote("Mimpi yang paling indah adalah mimpi yang kamu wujudkan.", "Walt Disney"),
    Quote("Jangan menunggu waktu yang tepat. Ciptakan waktu yang tepat.", "Muhammad Ali"),
    Quote("Setiap hari adalah kesempatan kedua.", "Anonim"),
    Quote("Kegagalan adalah guru terbaik.", "Oprah Winfrey"),
    Quote("Keberanian adalah langkah pertama menuju impian.", "Nelson Mandela"),
    Quote("Jadilah dirimu sendiri, karena yang lain sudah ada yang memiliki.", "Oscar Wilde")
)