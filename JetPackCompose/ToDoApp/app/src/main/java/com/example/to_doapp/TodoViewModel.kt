package com.example.to_doapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TodoViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<String>>(emptyList())
    val tasks: StateFlow<List<String>> = _tasks.asStateFlow()

    fun addTask(task: String) {
        _tasks.value = _tasks.value + task
    }

    fun removeTask(task: String) {
        _tasks.value = _tasks.value - task
    }
}