package com.compagnofederico.simon.database

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import androidx.room3.ColumnInfo

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