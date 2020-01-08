package com.phooper.goodlooker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.phooper.goodlooker.db.dao.FavouritePostsDao
import com.phooper.goodlooker.db.dao.SearchHistoryDao
import com.phooper.goodlooker.db.entity.FavouritePosts
import com.phooper.goodlooker.db.entity.SearchHistory

@Database(entities = [SearchHistory::class, FavouritePosts::class], version = 1)
abstract class AppDb : RoomDatabase() {

    abstract fun getSearchHistoryDao(): SearchHistoryDao
    abstract fun getFavouriteLinksDao(): FavouritePostsDao
}