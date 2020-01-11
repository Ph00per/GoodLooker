package com.phooper.goodlooker.presentation.search

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.db.entity.SearchHistory
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

interface SearchView : MvpView {
    @StateStrategyType(value = SkipStrategy::class)
    fun showMessage(msg: String)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun enterInputMode()

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun exitInputMode()

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showClearBtn()

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun hideClearBtn()

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setInputText(text : String)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun fillHistoryList(historyList : List<SearchHistory>)

    @StateStrategyType(value = SkipStrategy::class)
    fun deleteHistoryListItem(position : Int)

    @StateStrategyType(value = SkipStrategy::class)
    fun startLoading()

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun stopLoading()

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun removeOnScrollListenerRV()

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun addOnScrollListenerRV()

    @StateStrategyType(value = SkipStrategy::class)
    fun scrollToBottom()

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun updateFeedList(listFeed: MutableList<IComparableItem>)

}