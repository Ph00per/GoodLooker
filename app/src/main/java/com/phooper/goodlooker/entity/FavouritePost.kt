package com.phooper.goodlooker.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_posts")
data class FavouritePost(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val postLink: String,
    val postTitle : String
)