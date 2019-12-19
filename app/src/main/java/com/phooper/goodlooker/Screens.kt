package com.phooper.goodlooker

import com.phooper.goodlooker.ui.news.NewsFragment
import com.phooper.goodlooker.ui.picture.PictureFragment
import com.phooper.goodlooker.ui.post.PostFragment
import com.phooper.goodlooker.ui.youtube.YoutubeVideoFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object News : SupportAppScreen() {
        override fun getFragment() = NewsFragment()
    }

    data class Post(
        val linkPost: String
    ) : SupportAppScreen() {
        override fun getFragment() = PostFragment.create(linkPost)
    }

    data class Picture(
        val imageLink: String
    ) : SupportAppScreen() {
        override fun getFragment() = PictureFragment.create(imageLink)
    }

    data class YoutubeVideo(
        val videoLink: String
    ) : SupportAppScreen() {
        override fun getFragment() = YoutubeVideoFragment.create(videoLink)
    }
}