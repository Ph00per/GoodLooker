package com.phooper.goodlooker.presentation.post

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = OneExecutionStateStrategy::class)
interface PostView: MvpView {

fun showMessage(msg : String)
}