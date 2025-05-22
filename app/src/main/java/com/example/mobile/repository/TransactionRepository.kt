package com.example.mobile.repository

import com.example.mobile.data.database.dao.TransactionDao
import com.example.mobile.data.database.entities.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransactionRepository(private val dao: TransactionDao) {
    fun getAll(userId: Int) = dao.getAllForUser(userId)
    suspend fun insert(transaction: Transaction) {
        withContext(Dispatchers.IO) {
            dao.insert(transaction)
        }
    }
    fun update(tx: Transaction) = dao.update(tx)
    fun delete(tx: Transaction) = dao.delete(tx)
}
