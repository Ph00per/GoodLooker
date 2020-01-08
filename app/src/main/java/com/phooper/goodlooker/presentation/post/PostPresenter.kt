package com.phooper.goodlooker.presentation.post

import com.phooper.goodlooker.App
import com.phooper.goodlooker.R
import com.phooper.goodlooker.Screens
import com.phooper.goodlooker.db.AppDb
import com.phooper.goodlooker.db.dao.FavouritePostsDao
import com.phooper.goodlooker.db.entity.FavouritePosts
import com.phooper.goodlooker.parser.Parser
import com.phooper.goodlooker.ui.widgets.recyclerview.model.ConnectionRetryItemViewModel
import com.phooper.goodlooker.ui.widgets.recyclerview.model.PostItemViewModel
import com.phooper.goodlooker.util.Constants.Companion.BASE_URL
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject


@InjectViewState
class PostPresenter(private val postLink: String) : MvpPresenter<PostView>() {

    @Inject
    lateinit var router: Router

    init {
        App.daggerComponent.inject(this)
    }

    @Inject
    lateinit var database: AppDb

    @Inject
    lateinit var favouritePostsDao: FavouritePostsDao


    private val mapOfIcon =
        mapOf(false to R.drawable.ic_favour_unchecked, true to R.drawable.ic_favour_checked)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadPost()
        CoroutineScope(Dispatchers.Main).launch {
            viewState.changeFavouriteBtn(
                mapOfIcon.getValue(isPostFavourite())
            )
        }
    }

    private fun loadPost() {
        CoroutineScope(Dispatchers.IO).launch {
            repeat(7) {
                try {
                    withContext(Dispatchers.Main) {
                        viewState.apply {
                            fillList(withContext(Dispatchers.Default) {
                                Parser().parsePost(
                                   postLink
                                )
                            })
                            hideProgressBar()
                        }
                    }
                    return@repeat
                } catch (e: Exception) {
                    if (it != 6) delay(1000) else withContext(Dispatchers.Main) {
                        viewState.apply {
                            fillList(listOf(ConnectionRetryItemViewModel()))
                            hideProgressBar()
                        }
                    }
                }
            }
        }
    }

    fun onBackPressed() {
        router.exit()
    }

    fun showImage(imgLink: String) {
        router.navigateTo(Screens.Picture(imgLink))
    }

    fun showVideo(videoCode: String) {
        router.navigateTo(Screens.YoutubeVideo(videoCode))
    }

    fun linkClicked(link: String) {
        when {
            (link.contains("goodlooker.ru") && link.endsWith(".html")) -> {
                router.navigateTo(Screens.Post(link))
            }
            (link.startsWith("/") && link.endsWith(
                ".html"
            )) -> {
                router.navigateTo(Screens.Post(BASE_URL + link))
            }
            else -> {
                viewState.openBrowserLink(link)
            }
        }
    }

    fun favourBtnOnClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            if (isPostFavourite()) {
                favouritePostsDao.deleteByLink(postLink)
                withContext(Dispatchers.Main) {
                    viewState.apply {
                        showMessage("Публикация удалена из избранного")
                        changeFavouriteBtn(mapOfIcon.getValue(false))
                    }
                }
            } else {
                favouritePostsDao.addNew(
                    FavouritePosts(
                        0, postLink
                    )
                )
                withContext(Dispatchers.Main) {
                    viewState.apply {
                        showMessage("Публикация добавлена в избранное")
                        changeFavouriteBtn(mapOfIcon.getValue(true))

                    }
                }
            }
        }
    }

    private suspend fun isPostFavourite() =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            favouritePostsDao.getByLink(postLink)?.let {
                return@withContext true
            }
            false
        }

    fun retryConnection() {
        viewState.apply {
            fillList(emptyList())
            showProgressBar()
        }
        loadPost()
    }


}
