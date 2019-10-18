package com.bridgelabz.myfundooapp.user_module

interface IUserDataManager {

    fun insertUserData(name: String, email: String, password: String)
    fun isUserPresent(email: String, password: String): Boolean
}