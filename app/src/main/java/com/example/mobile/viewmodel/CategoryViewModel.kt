package com.example.mobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mobile.data.database.AppDatabase
import com.example.mobile.data.database.entities.Category
import com.example.mobile.repository.CategoryRepository

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: CategoryRepository =
        CategoryRepository(AppDatabase.getDatabase(application).categoryDao())

    fun getAllCategories(): LiveData<List<Category>> = repo.getAllCategories()

    fun insertCategory(category: Category) = repo.insertCategory(category)
}
