package com.example.roomapp.repository

import androidx.lifecycle.LiveData
import com.example.roomapp.data.UserDao
import com.example.roomapp.model.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    val readData: Flow<List<User>> = userDao.readData()
    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }
    fun readData(): Flow<List<User>> {
        return userDao.readData()
    }

    fun readAllData(): LiveData<List<User>> {
        return userDao.readAllData()
    }

    suspend fun updateUser (user: User){
        userDao.updateUser(user)
    }

    suspend fun deleteUser (user: User){
        userDao.deleteUser(user)
    }

    suspend fun deleteAllUsers (){
        userDao.deleteAllUsers()
    }

     fun searchDatabase(searchQuery: String): Flow<List<User>> {
        return userDao.searchDatabase(searchQuery)
    }



}