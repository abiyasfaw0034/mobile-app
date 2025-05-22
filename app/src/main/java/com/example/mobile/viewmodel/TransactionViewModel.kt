package com.example.mobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mobile.data.database.AppDatabase
import com.example.mobile.data.database.entities.Transaction
import com.example.mobile.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = TransactionRepository(AppDatabase.getDatabase(application).transactionDao())

    fun getAll(userId: Int): LiveData<List<Transaction>> = repo.getAll(userId)
    fun insert(tx: Transaction) {
        viewModelScope.launch {
            repo.insert(tx)
        }
    }

    fun update(tx: Transaction) = repo.update(tx)
    fun delete(tx: Transaction) = repo.delete(tx)
}
