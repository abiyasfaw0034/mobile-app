package com.example.mobile.repository

import androidx.lifecycle.LiveData
import com.example.mobile.data.database.dao.CategoryDao
import com.example.mobile.data.database.entities.Category

class CategoryRepository(private val categoryDao: CategoryDao) {
    fun getAllCategories(): LiveData<List<Category>> = categoryDao.getAll()

    fun insertCategory(category: Category) = categoryDao.insert(category)
}
