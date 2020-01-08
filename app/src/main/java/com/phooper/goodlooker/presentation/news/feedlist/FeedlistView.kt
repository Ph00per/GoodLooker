package com.phooper.goodlooker.presentation.news.feedlist

import com.example.delegateadapter.delegate.diff.IComparableItem
import moxy.MvpView
import moxy.viewstate.strategy.*

interface FeedlistView : MvpView {

    @StateStrategyType(value = SkipStrategy::class)
    fun startRefreshing()

    @StateStrategyType(value = SkipStrategy::class)
    fun stopRefreshing()

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun removeOnScrollListenerRV()

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun addOnScrollListenerRV()

    @StateStrategyType(value = SkipStrategy::class)
    fun scrollToBottom()

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun updateFeedList(listFeed: MutableList<IComparableItem>)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showMessage(msg: String)


}