package com.example.mobile.util

import android.content.Context
import com.example.mobile.data.database.AppDatabase
import com.example.mobile.data.database.entities.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CategorySeeder {
    fun reset(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.getDatabase(context).categoryDao()
            dao.deleteAll()

            val defaultCategories = listOf(
                Category(name = "Salary"),
                Category(name = "Bonus"),
                Category(name = "Investment"),
                Category(name = "Food"),
                Category(name = "Transport"),
                Category(name = "Shopping"),
                Category(name = "Bills"),
                Category(name = "Entertainment")
            )
            defaultCategories.forEach { dao.insert(it) }
        }
    }

    fun seed(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.getDatabase(context).categoryDao()
            val count = dao.getCategoryCount()
            if (count == 0) {
                val defaultCategories = listOf(
                    Category(name = "Salary"),
                    Category(name = "Bonus"),
                    Category(name = "Investment"),
                    Category(name = "Food"),
                    Category(name = "Transport"),
                    Category(name = "Shopping"),
                    Category(name = "Bills"),
                    Category(name = "Entertainment")
                )
                defaultCategories.forEach { dao.insert(it) }
            }
        }
    }
}

