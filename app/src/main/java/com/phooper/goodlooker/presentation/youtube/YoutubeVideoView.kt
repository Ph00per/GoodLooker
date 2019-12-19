package com.phooper.goodlooker.presentation.youtube

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

interface YoutubeVideoView : MvpView {

    @StateStrategyType(value = SkipStrategy::class)
    fun showMessage(msg: String)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showVideo(videoCode: String)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun exitFullScreen()
}
