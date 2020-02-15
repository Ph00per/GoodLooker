package com.phooper.goodlooker.di

import com.phooper.goodlooker.presentation.drawer.DrawerPresenter
import com.phooper.goodlooker.presentation.feedlist.FeedlistPresenter
import com.phooper.goodlooker.presentation.picture.PicturePresenter
import com.phooper.goodlooker.presentation.post.PostPresenter
import com.phooper.goodlooker.presentation.search.SearchPresenter
import com.phooper.goodlooker.presentation.youtube.YoutubeVideoPresenter
import com.phooper.goodlooker.ui.AppActivity
import com.phooper.goodlooker.ui.global.adapters.ImageItemDelegateAdapter
import com.phooper.goodlooker.ui.global.adapters.PostItemDelegateAdapter
import com.phooper.goodlooker.ui.global.adapters.YoutubeItemDelegateAdapter
import com.phooper.goodlooker.model.interactor.FavouriteListInteractor
import com.phooper.goodlooker.model.interactor.FeedInteractor
import com.phooper.goodlooker.model.interactor.PostInteractor
import com.phooper.goodlooker.model.interactor.SearchInteractor
import com.phooper.goodlooker.presentation.favourite.FavouriteListPresenter
import com.phooper.goodlooker.util.ImageSaver
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, GlobalNavigationModule::class])
@Singleton
interface AppComponent {
    fun inject(activity: AppActivity)
    fun inject(presenter: DrawerPresenter)
    fun inject(presenter: FeedlistPresenter)
    fun inject(presenter: PostPresenter)
    fun inject(presenter: PicturePresenter)
    fun inject(presenter: YoutubeVideoPresenter)
    fun inject(presenter: SearchPresenter)
    fun inject(presenter: FavouriteListPresenter)

    fun inject(interactor: FeedInteractor)
    fun inject(interactor: SearchInteractor)
    fun inject(interactor: PostInteractor)
    fun inject(interactor: FavouriteListInteractor)

    fun inject(imageSaver: ImageSaver)

    fun inject(adapter: PostItemDelegateAdapter)
    fun inject(adapter: ImageItemDelegateAdapter)
    fun inject(adapter: YoutubeItemDelegateAdapter)
}