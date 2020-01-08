package com.phooper.goodlooker.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "search_history", indices = [Index(name = "index_text", unique = true, value = ["text"])])
data class SearchHistory(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val text: String
)