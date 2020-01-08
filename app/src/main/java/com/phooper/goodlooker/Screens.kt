package com.phooper.goodlooker

import com.phooper.goodlooker.ui.news.NewsFragment
import com.phooper.goodlooker.ui.picture.PictureFragment
import com.phooper.goodlooker.ui.post.PostFragment
import com.phooper.goodlooker.ui.search.SearchFragment
import com.phooper.goodlooker.ui.widgets.recyclerview.model.PostItemViewModel
import com.phooper.goodlooker.ui.youtube.YoutubeVideoFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object News : SupportAppScreen() {
        override fun getFragment() = NewsFragment()
    }

    object Search : SupportAppScreen() {
        override fun getFragment() = SearchFragment()
    }

    data class Post(
        val postLink: String
    ) : SupportAppScreen() {
        override fun getFragment() = PostFragment.create(postLink)
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