package com.phooper.goodlooker.presentation.favourite

import com.phooper.goodlooker.App
import com.phooper.goodlooker.Screens
import com.phooper.goodlooker.model.interactor.FavouriteListInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class FavouriteListPresenter : MvpPresenter<FavouriteListView>() {


    @Inject
    lateinit var router: Router

    init {
        App.daggerComponent.inject(this)
    }

    @Inject
    lateinit var favouriteListInteractor: FavouriteListInteractor

    override fun attachView(view: FavouriteListView?) {
        super.attachView(view)
        CoroutineScope(Main).launch {
            viewState.apply {
                setToolbarTitle("Избранное")
                withContext(IO) {
                    favouriteListInteractor.getAllFavPosts()
                }.let {
                    if (it.isEmpty()) {
                        showNothingFound()
                    } else {
                        hideNothingFound()
                        updateAndShowRecyclerList(it)
                    }
                }

            }
        }
    }

    fun postClicked(link: String) {
        router.navigateTo(Screens.Post(link))
        onDestroy()
    }

    fun searchOnClicked() {
        router.navigateTo(Screens.Search)
    }

}