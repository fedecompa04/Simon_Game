// Compagno Federico 2101752

package com.compagnofederico.simon.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

/**
 * Database entity that defines the game data structure.
 */
@Entity(tableName = "ListOfGames")
data class Match(
    // Unique ID for each match, incremented automatically
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // Stores the final score reached by the player during the match
    @ColumnInfo(name = "score")
    val score: Int,
    // Stores the string representation of the color sequence
    @ColumnInfo(name = "sequence")
    val sequence: String,
    // Stores the list index pointing to the user's error
    @ColumnInfo(name = "errorPosition")
    val errorPosition: Int
)