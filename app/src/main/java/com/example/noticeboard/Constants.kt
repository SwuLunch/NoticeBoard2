package com.example.noticeboard

object Constants {
    //db name
    const val DB_NAME = "MY_RECORDS_DB"
    const val DB_VERSION = 1
    const val TABLE_NAME = "MY_RECORD_TABLE"
    //coulumns field of table
    const val C_ID = "ID"
    const val C_NAME = "NAME"
    const val C_IMAGE = "IMAGE"
    const val C_TIME = "TIME"
    const val C_ADDRESS = "ADDRESS"
    const val C_CONTENT = "CONTENT"
    const val C_ADDED_TIME_STAMP = "ADDED_TIME_STAMP"
    const val C_UPDATED_TIME_STAMP = "UPDATED_TIME_STAMP"

    //create table query
    const val CREATE_TABLE = (
            "CREATE TABLE " + TABLE_NAME + "("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_NAME + " TEXT, "
            + C_IMAGE + " TEXT, "
            + C_TIME + " TEXT, "
            + C_ADDRESS + " TEXT, "
            + C_CONTENT + " TEXT, "
            + C_ADDED_TIME_STAMP + " TEXT, "
            + C_UPDATED_TIME_STAMP + " TEXT"
                    + ")"
            )
}