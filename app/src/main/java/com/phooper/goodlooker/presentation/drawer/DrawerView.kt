package com.phooper.goodlooker.presentation.drawer

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType


interface DrawerView : MvpView {
    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun onScreenChanged(index: Int)

    @StateStrategyType(value = SkipStrategy::class)
    fun showMessage(msg: String)

}
