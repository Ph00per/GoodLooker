package com.phooper.goodlooker.model.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.phooper.goodlooker.model.data.db.dao.FavouritePostsDao
import com.phooper.goodlooker.model.data.db.dao.SearchHistoryDao
import com.phooper.goodlooker.entity.FavouritePost
import com.phooper.goodlooker.entity.SearchHistory

@Database(entities = [SearchHistory::class, FavouritePost::class], version = 1)
abstract class AppDb : RoomDatabase() {

    abstract fun getSearchHistoryDao(): SearchHistoryDao
    abstract fun getFavouriteLinksDao(): FavouritePostsDao
}