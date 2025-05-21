package com.example.mobile.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mobile.data.database.entities.User

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM User WHERE email = :email AND password = :password LIMIT 1")
    fun getUserByEmailAndPassword(email: String, password: String): User?

    @Query("SELECT * FROM User WHERE id = :id LIMIT 1")
    fun getUserById(id: Int): User?

}
