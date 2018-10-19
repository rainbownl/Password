package com.niulei.password.content

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.niulei.password.database.DBDefines
import com.niulei.password.database.DataBaseOpenHelper
import com.niulei.password.database.IDataBaseManager

class DBContentManager private constructor(context: Context) : IDataBaseManager{
    companion object {
        private var instance: DBContentManager? = null
        fun getInstance(context: Context) : DBContentManager {
            if (instance == null) {
                synchronized(DBContentManager::class) {
                    instance = DBContentManager(context)
                }
            }
            return instance!!
        }
    }
    var contentItems : List<ContentItem>? = null
    var license: String? = null
    var database: SQLiteDatabase? = null

    init {
        database = DataBaseOpenHelper(context).writableDatabase
        createTables()
        license = readLicense()
    }

    fun createTables() {
        val exec = String.format("create table if not exists %s (%s integer primary key autoincrement, %s varchar(64), %s varchar(64), %s varchar(64))",
                DBDefines.TABLE_NAME, DBDefines.COLUME_ID, DBDefines.COLUME_TITLE, DBDefines.COLUME_USERNAME, DBDefines.COLUME_PASSWORD)
        database?.execSQL(exec)

        var sql = String.format("create table if not exists %s (%s varchar(64))", DBDefines.LICENSE_TABLE_NAME, DBDefines.COLUME_LICENSE)
        database?.execSQL(sql)
    }

    fun readLicense() : String? {
        val sql = String.format("select %s from %s", DBDefines.COLUME_LICENSE, DBDefines.LICENSE_TABLE_NAME)
        var cursor = database?.rawQuery(sql, null)
        if (cursor != null && cursor!!.count > 0) {
            cursor.moveToNext()
            return cursor?.getString(0)
        }
        return null
    }

    fun writeLicense(license: String) {
        if (this.license == null) {
            val sql = String.format("insert into %s (%s) values ('%s')",
                    DBDefines.LICENSE_TABLE_NAME, DBDefines.COLUME_LICENSE, license)
            database?.execSQL(sql)
        } else {
            val sql = String.format("update %s set %s = '%s'",
                    DBDefines.LICENSE_TABLE_NAME, DBDefines.COLUME_LICENSE, license)
            database?.execSQL(sql)
        }
        this.license = license
    }

    override fun insert(title: String, username: String, password: String) {
        var sql = String.format("insert into %s (%s,%s,%s) values ('%s','%s','%s')",
                DBDefines.TABLE_NAME, DBDefines.COLUME_TITLE, DBDefines.COLUME_USERNAME, DBDefines.COLUME_PASSWORD,
                title, username, password)
        database?.execSQL(sql)
        updateData()
    }

    override fun delete(title: String) {
        var sql = String.format("delete from %s where %s = '%s'",
                DBDefines.TABLE_NAME, DBDefines.COLUME_TITLE, title)
        database?.execSQL(sql)
        updateData()
    }

    override fun update(id: Int, title: String, username: String, password: String) {
        var sql = String.format("update %s set %s='%s',%s='%s',%s='%s' where %s=%d",
                DBDefines.TABLE_NAME, DBDefines.COLUME_TITLE, title,
                DBDefines.COLUME_USERNAME, username,
                DBDefines.COLUME_PASSWORD, password,
                DBDefines.COLUME_ID, id)
        database?.execSQL(sql)
        updateData()
    }

    fun updateData(){
        var sql = String.format("select * from %s", DBDefines.TABLE_NAME)
        var cursor = database?.rawQuery(sql, null)

        var items = ArrayList<ContentItem>()
        while (cursor!!.moveToNext()) {
            var item = ContentItem()
            item.id = cursor.getInt(0)
            item.title = cursor.getString(1)
            item.userName = cursor.getString(2)
            item.password = cursor.getString(3)
            items.add(item)
        }
        contentItems = items
    }

    fun getItem(index: Int) : ContentItem? {
        if (index >= 0 && index < contentItems!!.size) {
            return contentItems!![index]
        }
        return null
    }

    fun getCount() : Int{
        if (contentItems == null) {
            return 0
        } else {
            return contentItems!!.size
        }
    }

    override fun clear() {
        var sql = String.format("delete * from %s", DBDefines.TABLE_NAME)
        database?.execSQL(sql)
    }

    fun uninit() {
        database?.close()
    }

}