package com.example.noticeboard

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper(context: Context?):SQLiteOpenHelper(
    context,Constants.DB_NAME, null, Constants.DB_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Constants.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME)
        onCreate(db)
    }

    fun insertRecord(
        name:String?,
        image:String?,
        time:String?,
        address:String?,
        contentt:String?,
        addedTime:String?,
        updatedTime:String?
    ):Long{
        //get writeable database because we want write data
        val db = this.writableDatabase
        val values = ContentValues()
        //insert data
        values.put(Constants.C_NAME, name)
        values.put(Constants.C_IMAGE, image)
        values.put(Constants.C_ADDRESS, address)
        values.put(Constants.C_TIME, time)
        values.put(Constants.C_CONTENT, contentt)
        values.put(Constants.C_ADDED_TIME_STAMP, addedTime)
        values.put(Constants.C_UPDATED_TIME_STAMP, updatedTime)

        //insert row, it will return record id of saved record
        val id = db.insert(Constants.TABLE_NAME, null, values)
        db.close()
        return id

    }

    fun getAllRecords(orderBy:String):ArrayList<ModelRecord>{

        val recordList = ArrayList<ModelRecord>()
        val selectQuery = "SELECT * FROM ${Constants.TABLE_NAME} ORDER BY $orderBy"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if(cursor.moveToFirst()){
            do{
                val modelRecord = ModelRecord(
                    ""+cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_NAME)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDRESS)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_IMAGE)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_TIME)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_CONTENT)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIME_STAMP)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_UPDATED_TIME_STAMP))
                )
                recordList.add(modelRecord)
            }while (cursor.moveToNext())
        }
        db.close()
        return recordList
    }

    fun searchRecords(query:String):ArrayList<ModelRecord>{
        val recordList = ArrayList<ModelRecord>()
        val selectQuery = "SELECT * FROM ${Constants.TABLE_NAME} WHERE ${Constants.C_NAME} LIKE '%$query%'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if(cursor.moveToFirst()){
            do{
                val modelRecord = ModelRecord(
                    ""+cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_NAME)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDRESS)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_IMAGE)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_TIME)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_CONTENT)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIME_STAMP)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_UPDATED_TIME_STAMP))
                )
                recordList.add(modelRecord)
            }while (cursor.moveToNext())
        }
        db.close()
        return recordList
    }
}