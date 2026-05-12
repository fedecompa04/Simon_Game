package com.compagnofederico.simon.database

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase

abstract class MatchDatabase : RoomDatabase(){
    abstract fun matchDao() : MatchDao

    companion object{
        private var db: MatchDatabase? = null

        fun getDatabase(context: Context) : MatchDatabase{
            if(db == null) {
                db = Room.databaseBuilder(context, MatchDatabase::class.java, "match_database")
                    .build()
            }
            return db!!
        }
    }
}