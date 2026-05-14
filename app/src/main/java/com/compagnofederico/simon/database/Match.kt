package com.compagnofederico.simon.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "ListOfGames")
data class Match(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "score")
    val score: Int,
    @ColumnInfo(name = "sequence")
    val sequence: String,
    @ColumnInfo(name = "errorPosition")
    val errorPosition: Int
)