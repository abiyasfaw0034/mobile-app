package com.example.mobile.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mobile.data.database.entities.Category

@Dao
interface CategoryDao {
    @Insert
    fun insert(category: Category)

    @Query("SELECT * FROM Category")
    fun getAll(): LiveData<List<Category>>

    @Query("SELECT COUNT(*) FROM Category")
    suspend fun getCategoryCount(): Int

    @Query("DELETE FROM Category")
    suspend fun deleteAll()
}
