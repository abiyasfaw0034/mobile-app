package com.example.mobile.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val type: String, // "Income" or "Expense"
    val amount: Double,
    val category: String,
    val note: String?,
    val date: Long
)
