package com.example.mobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mobile.data.database.AppDatabase
import com.example.mobile.data.database.entities.User
import com.example.mobile.repository.UserRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = UserRepository(AppDatabase.getDatabase(application).userDao())

    fun login(email: String, password: String): User? = repo.login(email, password)
    fun register(user: User) = repo.register(user)

    fun getUserById(id: Int): User? {
        return repo.getUserById(id)
    }

}
