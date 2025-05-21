package com.example.mobile.repository

import com.example.mobile.data.database.dao.TransactionDao
import com.example.mobile.data.database.entities.Transaction

class TransactionRepository(private val dao: TransactionDao) {
    fun getAll(userId: Int) = dao.getAllForUser(userId)
    fun insert(tx: Transaction) = dao.insert(tx)
    fun update(tx: Transaction) = dao.update(tx)
    fun delete(tx: Transaction) = dao.delete(tx)
}
