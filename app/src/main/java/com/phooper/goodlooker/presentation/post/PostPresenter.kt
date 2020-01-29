package com.phooper.goodlooker.presentation.post

import android.util.Log
import com.phooper.goodlooker.App
import com.phooper.goodlooker.R
import com.phooper.goodlooker.Screens
import com.phooper.goodlooker.entity.ConnectionRetryItemViewModel
import com.phooper.goodlooker.entity.H1ItemViewModel
import com.phooper.goodlooker.model.interactor.PostInteractor
import com.phooper.goodlooker.util.Constants.Companion.BASE_URL
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
class PostPresenter(private val postLink: String) : MvpPresenter<PostView>() {

    @Inject
    lateinit var router: Router

    init {
        App.daggerComponent.inject(this)
    }

    private var postHeader = ""

    @Inject
    lateinit var postInteractor: PostInteractor

    private val mapOfIcon =
        mapOf(false to R.drawable.ic_favour_unchecked, true to R.drawable.ic_favour_checked)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        setPost()
        CoroutineScope(Main).launch {
            viewState.changeFavouriteBtn(
                mapOfIcon.getValue(withContext(IO) { postInteractor.isPostFavourite(postLink) })
            )
        }
    }

    private fun setPost() {
        CoroutineScope(IO).launch {
            postInteractor.getPostByLink(postLink).let {
                if (it.isSuccessful) {
                    postHeader = (it.listContent!![0] as H1ItemViewModel).headerText
                    withContext(Main) {
                        viewState.apply {
                            fillList(it.listContent)
                            hideProgressBar()
                        }
                    }
                } else {
                    withContext(Main) {
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
        if (postHeader.isNotEmpty()) {
            CoroutineScope(IO).launch {
                if (postInteractor.isPostFavourite(postLink)) {
                    postInteractor.deletePostFromFav(postLink)
                    withContext(Main) {
                        viewState.apply {
                            showMessage("Публикация удалена из избранного")
                            changeFavouriteBtn(mapOfIcon.getValue(false))
                        }
                    }
                } else {
                    postInteractor.addNewFavPost(postLink, postHeader)
                    withContext(Main) {
                        viewState.apply {
                            showMessage("Публикация добавлена в избранное")
                            changeFavouriteBtn(mapOfIcon.getValue(true))

                        }
                    }
                }
            }
        }
    }

    fun retryConnection() {
        viewState.apply {
            fillList(emptyList())
            showProgressBar()
        }
        setPost()
    }


}
