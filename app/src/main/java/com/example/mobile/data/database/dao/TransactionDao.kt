package com.example.mobile.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mobile.data.database.entities.Transaction

@Dao
interface TransactionDao {
    @Insert
    fun insert(transaction: Transaction)

    @Update
    fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("SELECT * FROM `Transaction` WHERE userId = :userId ORDER BY date DESC")
    fun getAllForUser(userId: Int): LiveData<List<Transaction>>
}
