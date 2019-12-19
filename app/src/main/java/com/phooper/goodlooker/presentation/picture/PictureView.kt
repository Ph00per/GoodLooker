package com.phooper.goodlooker.presentation.picture

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.gif.GifDrawable
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

interface PictureView : MvpView {

    @StateStrategyType(value = SkipStrategy::class)
    fun showMessage(msg: String)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showImage(requestBuilder: RequestBuilder<Drawable>)

    @StateStrategyType(value = SkipStrategy::class)
    fun askForWritePermission()

    @StateStrategyType(value = SkipStrategy::class)
    fun checkWritePermission()

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun lockOrientation()

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun unlockOrientation()
}
