package com.nnl.password.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.lang.reflect.Type

class DataBaseManager(var context: Context){

    private var helper = DataBaseOpenHelper(context)

    private var database: SQLiteDatabase? = null

    fun openDatabase(){
        database = helper.writableDatabase
    }

    fun closeDatabase() {
        if (database != null) {
            database?.close()
            database = null
        }
    }

    fun insert(table: String, values: ContentValues) {
        database?.insert(table, null, values)
    }

    fun deleteById(id: Int) {
    }

    fun deleteByWhere(table: String, whereClause: String?, whereArgs: Array<String>?) {
        database?.delete(table, whereClause, whereArgs)
    }

    fun update(table: String, values: ContentValues, whereClause: String?, whereArgs: Array<String>?) {
        database?.update(table, values, whereClause, whereArgs)
    }

    fun clear(table: String) {
        database?.delete(table, null, null)
    }

    fun createTable(name: String, values: ContentValues){
        var columes = ""
        values.keySet().forEach{
            columes += (it + " " + values[it] + ",")
        }
        columes = columes.dropLast(1)
        var sql = String.format("create table if not exists %s(%s)", name, columes)
        database?.execSQL(sql)
    }

    fun rawQuery(sql: String) : Cursor?{
        return database?.rawQuery(sql, null)
    }
}