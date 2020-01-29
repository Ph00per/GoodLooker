package com.phooper.goodlooker.presentation.favourite

import com.example.delegateadapter.delegate.diff.IComparableItem
import com.phooper.goodlooker.entity.FavouritePost
import moxy.MvpView
import moxy.viewstate.strategy.*

interface FavouriteListView : MvpView {

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun updateRecyclerList(listPosts: List<FavouritePost>)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun setToolbarTitle(title: String)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showNothingFound()

}