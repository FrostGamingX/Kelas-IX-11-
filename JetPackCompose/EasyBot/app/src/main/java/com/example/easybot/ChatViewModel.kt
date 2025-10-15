package com.example.easybot

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val messageList = mutableStateListOf<MessageModel>()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash", // Atau model lain yang tersedia
        apiKey = "YOUR_GEMINI_API_KEY_HERE" // Ganti dengan API key Anda dari Google AI Studio
    )

    private val chat = generativeModel.startChat(
        history = listOf() // Mulai dengan history kosong
    )

    fun sendMessage(userMessage: String) {
        if (userMessage.isNotBlank()) {
            messageList.add(MessageModel(userMessage, "user"))

            viewModelScope.launch {
                try {
                    val response = chat.sendMessage(
                        content(role = "user") { text(userMessage) }
                    )
                    val botResponse = response.text ?: "Maaf, terjadi kesalahan."
                    messageList.add(MessageModel(botResponse, "model"))
                } catch (e: Exception) {
                    messageList.add(MessageModel("Error: ${e.message}", "model"))
                }
            }
        }
    }
}