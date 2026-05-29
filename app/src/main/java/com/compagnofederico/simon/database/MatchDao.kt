package com.compagnofederico.simon.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MatchDao{
    @Insert
    suspend fun insertMatch(match: Match)

    @Query("SELECT * FROM ListOfGames ORDER BY id DESC")
    suspend fun getAllMatches(): List<Match>
}