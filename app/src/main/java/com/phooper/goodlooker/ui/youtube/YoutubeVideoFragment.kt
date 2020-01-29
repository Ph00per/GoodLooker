package com.phooper.goodlooker.ui.youtube

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import com.muddzdev.styleabletoast.StyleableToast
import com.phooper.goodlooker.R
import com.phooper.goodlooker.presentation.youtube.YoutubeVideoPresenter
import com.phooper.goodlooker.presentation.youtube.YoutubeVideoView
import com.phooper.goodlooker.ui.global.BaseFragment
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import kotlinx.android.synthetic.main.fragment_youtube_video.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class YoutubeVideoFragment : BaseFragment(), YoutubeVideoView {

    override val layoutRes = R.layout.fragment_youtube_video

    @InjectPresenter
    lateinit var presenter: YoutubeVideoPresenter

    @ProvidePresenter
    fun providePresenter(): YoutubeVideoPresenter =
        YoutubeVideoPresenter(arguments?.getString(VIDEO_CODE)!!)

    override fun showVideo(videoCode: String) {
        lifecycle.addObserver(youtube_player)
        youtube_player.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.apply {
                    loadOrCueVideo(
                        this@YoutubeVideoFragment.lifecycle,
                        videoCode, 0f
                    )
                }
            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        youtube_player.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                activity?.apply {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    window?.decorView?.systemUiVisibility =
                        (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                }
            }

            override fun onYouTubePlayerExitFullScreen() {
                activity?.apply {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    window?.decorView?.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                }
            }
        })

    }

    override fun exitFullScreen() {
        youtube_player.exitFullScreen()
    }

    override fun onBackPressed() {
        presenter.onBackPressed(youtube_player.isFullScreen())
    }

    override fun showMessage(msg: String) {
        StyleableToast.makeText(context!!, msg, R.style.toast).show()
    }

    companion object {
        private const val VIDEO_CODE = "VIDEO_LINK"
        fun create(videoCode: String) =
            YoutubeVideoFragment().apply {
                arguments = Bundle().apply {
                    putString(VIDEO_CODE, videoCode)
                }
            }
    }
}