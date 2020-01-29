package com.phooper.goodlooker.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.phooper.goodlooker.R
import com.phooper.goodlooker.model.data.db.AppDb
import com.phooper.goodlooker.model.data.db.dao.FavouritePostsDao
import com.phooper.goodlooker.model.data.db.dao.SearchHistoryDao
import com.phooper.goodlooker.model.data.site.Connector
import com.phooper.goodlooker.model.interactor.FavouriteListIneractor
import com.phooper.goodlooker.model.interactor.FeedInteractor
import com.phooper.goodlooker.model.interactor.PostInteractor
import com.phooper.goodlooker.model.interactor.SearchInteractor
import com.phooper.goodlooker.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext() = context

    @Provides
    @Singleton
    fun provideRequestOptions(): RequestOptions =
        RequestOptions
            .placeholderOf(R.drawable.default_image)
            .error(R.drawable.default_image)

    @Provides
    @Singleton
    fun provideGlideInstance(
        context: Context, requestOptions: RequestOptions
    ): RequestManager =
        Glide.with(context)
            .setDefaultRequestOptions(requestOptions)

    @Provides
    @Singleton
    fun provideRoomDataBase(context: Context): AppDb =
        Room.databaseBuilder(
            context,
            AppDb::class.java,
            "database"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideSearchHistoryDao(database: AppDb): SearchHistoryDao =
        database.getSearchHistoryDao()

    @Provides
    @Singleton
    fun provideFavouriteLinksDao(database: AppDb): FavouritePostsDao =
        database.getFavouriteLinksDao()

    @Provides
    @Singleton
    fun provideConnector() = Connector(BASE_URL)

    @Provides
    @Singleton
    fun provideFeedInteractor() = FeedInteractor()

    @Provides
    @Singleton
    fun provideSearchInteractor() = SearchInteractor()

    @Provides
    @Singleton
    fun providePostInteractor() = PostInteractor()

    @Provides
    @Singleton
    fun provideFavouriteListInteractor() = FavouriteListIneractor()
}
