package com.phooper.goodlooker.presentation.youtube

import com.phooper.goodlooker.App
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject


@InjectViewState
class YoutubeVideoPresenter(private val videoCode: String) :
    MvpPresenter<YoutubeVideoView>() {

    @Inject
    lateinit var router: Router

    init {
        App.daggerComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showVideo(videoCode)
    }

    fun onBackPressed(isInFullscreen: Boolean) {
        if (isInFullscreen) viewState.exitFullScreen() else router.exit()
    }
}


