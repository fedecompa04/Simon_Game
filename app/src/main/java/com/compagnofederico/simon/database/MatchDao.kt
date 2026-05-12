package com.compagnofederico.simon.database

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query

@Dao
interface MatchDao{
    @Insert
    fun insertMatch(match: Match)

    @Delete
    fun deleteMatch(match: Match)

    @Query("SELECT * FROM sequence ORDER BY id DESC")
    fun getAllMatches(): List<Match>
}