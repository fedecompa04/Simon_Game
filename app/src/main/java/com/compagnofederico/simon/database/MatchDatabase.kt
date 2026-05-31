// Compagno Federico 2101752

package com.compagnofederico.simon.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database configuration that defines the data entities.
 */
@Database(entities = [Match::class], version = 1, exportSchema = false)
abstract class MatchDatabase : RoomDatabase(){
    // Provides the data access to the DAO
    abstract fun matchDao() : MatchDao
    companion object {
        // Volatile instance guarantees that updates to the database reference are immediately visible to all threads
        @Volatile
        private var INSTANCE: MatchDatabase? = null
        fun getDatabase(context: Context): MatchDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MatchDatabase::class.java,
                    "match_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}