package com.phooper.goodlooker

import com.phooper.goodlooker.ui.news.NewsFragment
import com.phooper.goodlooker.ui.post.PostFragment
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
}