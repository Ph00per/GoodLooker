package com.phooper.goodlooker.db.dao

import androidx.room.*
import com.phooper.goodlooker.db.entity.FavouritePosts

@Dao
interface FavouritePostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNew(
        post: FavouritePosts
    ): Long

    @Query("DELETE FROM favourite_posts WHERE linkPost = :link")
    suspend fun deleteByLink(link: String)

    @Query("SELECT * FROM favourite_posts WHERE linkPost = :link")
    suspend fun getByLink(link: String): FavouritePosts?

    @Query("SELECT * FROM favourite_posts ORDER BY id DESC")
    suspend fun getAll(): List<FavouritePosts>
}

