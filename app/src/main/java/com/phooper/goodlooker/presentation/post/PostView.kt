package com.phooper.goodlooker.presentation.post

import com.example.delegateadapter.delegate.diff.IComparableItem
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

interface PostView : MvpView {

    @StateStrategyType(value = SkipStrategy::class)
    fun showMessage(msg: String)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun fillList(list: List<IComparableItem>)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun hideProgressBar()

    @StateStrategyType(value = SkipStrategy::class)
    fun showProgressBar()

    @StateStrategyType(value = SkipStrategy::class)
    fun openBrowserLink(link : String?)
}
