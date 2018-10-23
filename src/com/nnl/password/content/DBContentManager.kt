package com.nnl.password.content

import android.content.ContentValues
import android.content.Context
import com.nnl.password.database.DBDefines
import com.nnl.password.database.DataBaseManager
import com.nnl.password.database.IDataBaseManager
import org.json.JSONObject
import java.io.FileWriter

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
    var databaseManager: DataBaseManager? = null

    init {
        databaseManager = DataBaseManager(context)
        databaseManager?.openDatabase()
        createTables()
        license = readLicense()
    }

    fun createTables() {
        var columes = ContentValues()
        columes.put(DBDefines.COLUME_ID, "integer primary key autoincrement")
        columes.put(DBDefines.COLUME_TITLE, "varchar(64)")
        columes.put(DBDefines.COLUME_USERNAME, "varchar(64)")
        columes.put(DBDefines.COLUME_PASSWORD, "varchar(64)")
        databaseManager?.createTable(DBDefines.TABLE_NAME, columes)

        var licenseColumes = ContentValues()
        licenseColumes.put(DBDefines.COLUME_LICENSE, "varchar(64)")
        databaseManager?.createTable(DBDefines.LICENSE_TABLE_NAME, licenseColumes)
    }

    fun readLicense() : String? {
        val sql = String.format("select %s from %s", DBDefines.COLUME_LICENSE, DBDefines.LICENSE_TABLE_NAME)
        var cursor = databaseManager?.rawQuery(sql)
        if (cursor != null && cursor!!.count > 0) {
            cursor.moveToNext()
            var license = cursor?.getString(0)
            cursor.close()
            return license
        }
        return null
    }

    fun writeLicense(license: String) {
        if (this.license == null) {
            var columes = ContentValues()
            columes.put(DBDefines.COLUME_LICENSE, license)
            databaseManager?.insert(DBDefines.LICENSE_TABLE_NAME, columes)
        } else {
            var values = ContentValues()
            values.put(DBDefines.COLUME_LICENSE, license)
            databaseManager?.update(DBDefines.LICENSE_TABLE_NAME, values, null, null)
        }
        this.license = license
    }

    override fun insert(title: String, username: String, password: String) {
        var values = ContentValues()
        values.put(DBDefines.COLUME_TITLE, title)
        values.put(DBDefines.COLUME_USERNAME, username)
        values.put(DBDefines.COLUME_PASSWORD, password)
        databaseManager?.insert(DBDefines.TABLE_NAME, values)
        updateData()
    }

    override fun delete(title: String) {
        var whereClause = String.format("%s = %s", DBDefines.COLUME_TITLE, title)
        databaseManager?.deleteByWhere(DBDefines.TABLE_NAME, whereClause, null)
        updateData()
    }

    override fun update(id: Int, title: String, username: String, password: String) {
        var values = ContentValues()
        values.put(DBDefines.COLUME_ID, id)
        values.put(DBDefines.COLUME_TITLE, title)
        values.put(DBDefines.COLUME_USERNAME, username)
        values.put(DBDefines.COLUME_PASSWORD, password)

        var whereClouse = String.format("%s = %d", DBDefines.COLUME_ID, id)
        databaseManager?.update(DBDefines.TABLE_NAME, values, whereClouse, null)
        updateData()
    }

    fun updateData(){
        var sql = String.format("select * from %s", DBDefines.TABLE_NAME)
        var cursor = databaseManager?.rawQuery(sql)

        var items = ArrayList<ContentItem>()
        while (cursor!!.moveToNext()) {
            var item = ContentItem()
            item.id = cursor.getInt(0)
            item.title = cursor.getString(1)
            item.userName = cursor.getString(2)
            item.password = cursor.getString(3)
            items.add(item)
        }
        cursor.close()
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
        databaseManager?.clear(DBDefines.TABLE_NAME)
    }

    fun uninit() {
        databaseManager?.closeDatabase()
    }

    fun itemToByteArray(item: ContentItem) : ByteArray? {
        var jsonObject = JSONObject()
        jsonObject.put("id", item.id)
        jsonObject.put("title", item.title)
        jsonObject.put("username", item.userName)
        jsonObject.put("password", item.userName)
        var jsonString = jsonObject.toString()
        return jsonString.toByteArray(Charsets.UTF_8)
    }

    fun makeFileHeaderBuffer() {
        var jsonObject = JSONObject()
    }
    fun backUp(path: String): Boolean {
        var fileWriter = FileWriter(path)
        contentItems?.forEach {

        }
        return false
    }

    fun recover() {

    }
}