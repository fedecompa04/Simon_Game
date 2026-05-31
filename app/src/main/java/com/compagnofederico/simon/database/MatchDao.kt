// Compagno Federico 2101752

package com.compagnofederico.simon.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * DAO that defines the database operations for the game matches.
 */
@Dao
interface MatchDao{
    // Saves a new match record into the database asynchronously
    @Insert
    suspend fun insertMatch(match: Match)
    // Retrieves all recorded matches from the table, sorted from newest to oldest
    @Query("SELECT * FROM ListOfGames ORDER BY id DESC")
    suspend fun getAllMatches(): List<Match>
}