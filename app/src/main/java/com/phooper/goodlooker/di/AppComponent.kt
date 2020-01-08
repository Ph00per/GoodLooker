package com.phooper.goodlooker.di

import com.phooper.goodlooker.presentation.news.NewsPresenter
import com.phooper.goodlooker.presentation.news.feedlist.FeedlistPresenter
import com.phooper.goodlooker.presentation.picture.PicturePresenter
import com.phooper.goodlooker.presentation.post.PostPresenter
import com.phooper.goodlooker.presentation.search.SearchPresenter
import com.phooper.goodlooker.presentation.youtube.YoutubeVideoPresenter
import com.phooper.goodlooker.ui.AppActivity
import com.phooper.goodlooker.ui.widgets.recyclerview.adapter.ImageItemDelegateAdapter
import com.phooper.goodlooker.ui.widgets.recyclerview.adapter.PostItemDelegateAdapter
import com.phooper.goodlooker.ui.widgets.recyclerview.adapter.YoutubeItemDelegateAdapter
import com.phooper.goodlooker.util.ImageSaver
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, NavigationModule::class])
@Singleton
interface AppComponent {
    fun inject(activity: AppActivity)
    fun inject(presenter: NewsPresenter)
    fun inject(presenter: FeedlistPresenter)
    fun inject(presenter: PostPresenter)
    fun inject(presenter: PicturePresenter)
    fun inject(presenter: YoutubeVideoPresenter)
    fun inject(presenter: SearchPresenter)

    fun inject(imageSaver: ImageSaver)

    fun inject(adapter: PostItemDelegateAdapter)
    fun inject(adapter: ImageItemDelegateAdapter)
    fun inject(adapter: YoutubeItemDelegateAdapter)
}