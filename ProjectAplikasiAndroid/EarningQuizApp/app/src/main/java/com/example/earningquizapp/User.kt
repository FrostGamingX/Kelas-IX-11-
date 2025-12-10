package com.example.earningquizapp

import androidx.room.Entity
import androidx.room.PrimaryKey

// User.kt
@Entity(tableName = "users")
data class User(
    @PrimaryKey val uid: String = "user_1",
    val name: String = "",
    val email: String = "",
    val balance: Long = 0,
    val totalPoints: Long = 0,
    val referralCode: String = "",
    val referredBy: String = ""
)

// Question.kt
@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val correctOption: Int, // 1-4
    val points: Int = 10
)