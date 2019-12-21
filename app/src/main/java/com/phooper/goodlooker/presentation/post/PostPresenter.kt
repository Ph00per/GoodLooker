package com.phooper.goodlooker.presentation.post

import com.phooper.goodlooker.App
import com.phooper.goodlooker.Screens
import com.phooper.goodlooker.parser.Parser
import com.phooper.goodlooker.util.Constants.Companion.BASE_URL
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject


@InjectViewState
class PostPresenter(private val postLink: String?) : MvpPresenter<PostView>() {

    @Inject
    lateinit var router: Router

    init {
        App.daggerComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        postLink?.let {
            loadPost(it)
        }
    }

    private fun loadPost(link: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = async { Parser().parsePost(link) }
            withContext(Dispatchers.Main) {
                viewState.apply {
                    fillList(result.await())
                    hideProgressBar()
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
        //TODO: Apply this with RegEx way
        when {
            (link.startsWith("/") && link.endsWith(".html")) -> {
                router.navigateTo(Screens.Post(BASE_URL + link))
            }
            (link.contains("goodlooker.ru") && link.endsWith(".html")) -> {
                router.navigateTo(Screens.Post(link))
            }
            else -> {
                viewState.openBrowserLink(link)
            }
        }
    }

}
