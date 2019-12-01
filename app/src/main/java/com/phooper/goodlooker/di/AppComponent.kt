package com.phooper.goodlooker.di

import com.phooper.goodlooker.presentation.news.NewsPresenter
import com.phooper.goodlooker.presentation.news.feedlist.FeedlistPresenter
import com.phooper.goodlooker.presentation.post.PostPresenter
import com.phooper.goodlooker.ui.AppActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, NavigationModule::class ])
@Singleton
interface AppComponent {
    fun inject(activity: AppActivity)
    fun inject(presenter: NewsPresenter)
    fun inject(presenter: FeedlistPresenter)
    fun inject(presenter: PostPresenter)
}