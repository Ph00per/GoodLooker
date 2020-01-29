package com.phooper.goodlooker.model.data.db.dao

import androidx.room.*
import com.phooper.goodlooker.entity.FavouritePost

@Dao
interface FavouritePostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNew(
        post: FavouritePost
    ): Long

    @Query("DELETE FROM favourite_posts WHERE postLink = :link")
    suspend fun deleteByLink(link: String)

    @Query("SELECT * FROM favourite_posts WHERE postLink = :link")
    suspend fun getByLink(link: String): FavouritePost?

    @Query("SELECT * FROM favourite_posts ORDER BY id DESC")
    suspend fun getAll(): List<FavouritePost>
}

