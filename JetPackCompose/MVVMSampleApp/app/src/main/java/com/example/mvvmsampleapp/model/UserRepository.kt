package com.example.mvvmsampleapp.model

class UserRepository {
    private val users = mutableListOf<UserData>()

    fun addUser(user: UserData) {
        users.add(user)
    }

    fun getUsers(): List<UserData> {
        return users.toList()
    }
}