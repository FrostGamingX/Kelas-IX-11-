package com.example.mvvmsampleapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mvvmsampleapp.model.UserData
import com.example.mvvmsampleapp.model.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val repository = UserRepository()
    private val _users = MutableStateFlow(repository.getUsers())
    val users: StateFlow<List<UserData>> = _users.asStateFlow()

    fun addUser(name: String) {
        repository.addUser(UserData(name = name))
        _users.value = repository.getUsers()
    }
}