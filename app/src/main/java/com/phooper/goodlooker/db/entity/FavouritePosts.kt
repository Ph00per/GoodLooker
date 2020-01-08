package com.phooper.goodlooker.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_posts")
data class FavouritePosts(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val linkPost: String
)