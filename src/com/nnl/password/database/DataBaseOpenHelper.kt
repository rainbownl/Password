package com.nnl.password.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseOpenHelper(context: Context, name: String, cursorFactory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, cursorFactory, version) {
    constructor(context: Context) : this(context, DBDefines.DB_NAME, null, DBDefines.DB_VERSION) {

    }
    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}