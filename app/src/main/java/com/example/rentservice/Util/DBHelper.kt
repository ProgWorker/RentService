package com.example.rentservice.Util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DbHelper(context: Context) : SQLiteOpenHelper(context, "train.db", null, 2) {
    val TAG = DbHelper.javaClass.simpleName
    val USER_TABLE = "user"
    val ADDITIONAL_INFO_TABLE = "adinfo"

    companion object {
        val ID: String = "_id"
        val NICKNAME: String = "NICKNAME"
        val USERID: String = "USER_ID"
        val NAME: String = "TITLE"
    }

    val USER_CREATE =
        "CREATE TABLE if not exists " + USER_TABLE + " (${ID} integer PRIMARY KEY autoincrement,${NICKNAME} text)"

    val TRAINING_CREATE =
        "CREATE TABLE if not exists " + ADDITIONAL_INFO_TABLE + " (${ID} integer PRIMARY KEY autoincrement,${NAME} text, ${USERID} text)"

    /*fun log(text: String) {
        val values = ContentValues()
        values.put(TEXT, text)
        values.put(TIMESTAMP, System.currentTimeMillis())
        getWritableDatabase().insert(TABLE, null, values);
    }*/

    /*fun getLogs() : Cursor {
        return getReadableDatabase()
            .query(USER_TABLE, null, null, null, null, null, null);
    }*/


    override fun onCreate(db: SQLiteDatabase) {
        //Log.d(TAG, "Creating: " + DATABASE_CREATE);
        db.execSQL(USER_CREATE)
        db.execSQL(TRAINING_CREATE)
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
    }

}