package com.phooper.goodlooker

import com.phooper.goodlooker.ui.drawer.DrawerFlowFragment
import com.phooper.goodlooker.ui.favourite.FavouriteListFragment
import com.phooper.goodlooker.ui.feedlist.*
import com.phooper.goodlooker.ui.picture.PictureFragment
import com.phooper.goodlooker.ui.post.PostFragment
import com.phooper.goodlooker.ui.search.SearchFragment
import com.phooper.goodlooker.ui.youtube.YoutubeVideoFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object Drawer : SupportAppScreen() {
        override fun getFragment() =
            DrawerFlowFragment()
    }

    //FeedList//

    object WholeSite : SupportAppScreen() {
        override fun getFragment() =
            WholeSiteFeed()
    }

    object Workout : SupportAppScreen() {
        override fun getFragment() =
            WorkoutFeed()
    }

    object FitnessEquip : SupportAppScreen() {
        override fun getFragment() =
            FitnessEquipFeed()
    }

    object FitnessProg : SupportAppScreen() {
        override fun getFragment() =
            FitnessProgFeed()
    }

    object FitnessAdvices : SupportAppScreen() {
        override fun getFragment() =
            FitnessAdvicesFeed()
    }

    object HealthyFood : SupportAppScreen() {
        override fun getFragment() =
            HealthyFoodFeed()
    }

    object YoutubeGuides : SupportAppScreen() {
        override fun getFragment() =
            YoutubeGuidesFeed()
    }

    object UsefullThings : SupportAppScreen() {
        override fun getFragment() =
            UsefullThingsFeed()
    }

    object FavouriteList : SupportAppScreen() {
        override fun getFragment() =
            FavouriteListFragment()
    }

    ////////////////////

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