package com.phooper.goodlooker.presentation.news.feedlist

import com.example.delegateadapter.delegate.diff.IComparableItem
import moxy.MvpView
import moxy.viewstate.strategy.*

interface FeedlistView : MvpView {

    @StateStrategyType(value = SingleStateStrategy::class)
    fun startRefreshing()

    @StateStrategyType(value = SingleStateStrategy::class)
    fun stopRefreshing()

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun removeOnScrollListenerRV()

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun addOnScrollListenerRV()

    @StateStrategyType(value = SkipStrategy::class)
    fun scrollToPos(pos : Int)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showMessage(msg: String)

//    @StateStrategyType(value = OneExecutionStateStrategy::class)
//    fun showConnectionProblems()

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun updateFeedList(listNews: MutableList<IComparableItem>)

}