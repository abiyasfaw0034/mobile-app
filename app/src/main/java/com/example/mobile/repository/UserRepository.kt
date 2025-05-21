package com.example.mobile.repository

import com.example.mobile.data.database.dao.UserDao
import com.example.mobile.data.database.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

class UserRepository(private val userDao: UserDao) {

    fun register(user: User) {
        Executors.newSingleThreadExecutor().execute {
            userDao.insert(user)
        }
    }

    fun login(email: String, password: String): User? {
        return runBlocking {
            withContext(Dispatchers.IO) {
                userDao.getUserByEmailAndPassword(email, password)
            }
        }
    }

    fun getUserById(id: Int): User? {
        return runBlocking {
            withContext(Dispatchers.IO) {
                userDao.getUserById(id)
            }
        }
    }

}
