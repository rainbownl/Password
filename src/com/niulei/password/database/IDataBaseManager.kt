package com.niulei.password.database

interface IDataBaseManager {
    fun insert(title: String, username: String, password: String)
    fun delete(title: String)
    fun update(id: Int, title: String, username: String, password: String)
    fun clear()
}