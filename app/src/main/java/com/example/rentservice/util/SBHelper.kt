package com.example.rentservice.util

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.rentservice.Server.POJO.User.User
import com.example.rentservice.Server.POJO.User.UserData

class SBHelper(context: Context) : SQLiteOpenHelper(context, "rent.db", null, 1) {
    val TAG = SBHelper::class.java.simpleName
    val USER_TABLE = "user"
    val ADDITIONAL_INFO_TABLE = "adinfo"

    companion object {
        val ID: String = "_id"
        val NICKNAME: String = "USERNAME"
        val EMAIL: String = "EMAIL"
        val FIRSTNAME: String = "FIRSTNAME"
        val LASTNAME: String = "LASTNAME"
        val ROLE: String = "ROLE"
        val PHONE: String = "PHONE"
        val DESC: String = "DESCRIPTION"
        val USERID: String = "USER_ID"
        val TOKEN: String = "TOKEN"
        val AVATAR: String = "AVATAR"
        val NAME: String = "TITLE"
    }

    val USER_CREATE =
        "CREATE TABLE if not exists $USER_TABLE ($ID integer PRIMARY KEY autoincrement,$NICKNAME text," +
                "$EMAIL text,$FIRSTNAME text,$LASTNAME text,$PHONE text,$ROLE text,$DESC text,$TOKEN text,$AVATAR text)"

    val TRAINING_CREATE =
        "CREATE TABLE if not exists $ADDITIONAL_INFO_TABLE ($ID integer PRIMARY KEY autoincrement,$NAME text,$USERID text)"



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
    fun dropSession(id: Int){
        writableDatabase.delete(USER_TABLE, "$ID == ?", arrayOf(id.toString()))
    }

    fun remeberUser(user: User, token: String){
        val vals = ContentValues()
        vals.put(ID, user.id)
        vals.put(NICKNAME, user.username)
        vals.put(EMAIL, user.email)
        vals.put(FIRSTNAME, user.first_name)
        vals.put(LASTNAME, user.last_name)
        vals.put(ROLE, user.role)
        vals.put(PHONE, user.phone)
        vals.put(DESC, user.desription)
        vals.put(AVATAR, user.avatar)
        vals.put(TOKEN, token)
        getWritableDatabase().insert(USER_TABLE, null, vals);
    }

    fun getUserData(): UserData {
        var cur = getReadableDatabase()
            .query(USER_TABLE, null, null, null, null, null, null);
        if (cur.moveToFirst()) {
            return UserData(cur.getString(8), cur.getInt(0), cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5), cur.getString(6), cur.getString(7), cur.getString(9))
        } else return UserData()
    }

    override fun onCreate(db: SQLiteDatabase) {
        //Log.d(TAG, "Creating: " + DATABASE_CREATE);
        db.execSQL(USER_CREATE)
        db.execSQL(TRAINING_CREATE)
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
    }

}