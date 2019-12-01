package com.phooper.goodlooker.presentation.news

import com.phooper.goodlooker.ui.news.feedlist.FeedListFragment
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType


interface NewsView : MvpView {

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun changeToolBarIcon(resId: Int)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun changeFeedList(targetFragment: FeedListFragment)

    @StateStrategyType(value = SkipStrategy::class)
    fun showMessage(msg: String)

}