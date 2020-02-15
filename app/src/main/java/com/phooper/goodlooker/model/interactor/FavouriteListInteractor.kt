package com.phooper.goodlooker.model.interactor

import com.phooper.goodlooker.App
import com.phooper.goodlooker.model.data.db.dao.FavouritePostsDao
import com.phooper.goodlooker.model.data.site.Connector
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavouriteListInteractor {

    init {
        App.daggerComponent.inject(this)
    }

    @Inject
    lateinit var favouritePostsDao: FavouritePostsDao

    suspend fun getAllFavPosts() =
        favouritePostsDao.getAll()

}