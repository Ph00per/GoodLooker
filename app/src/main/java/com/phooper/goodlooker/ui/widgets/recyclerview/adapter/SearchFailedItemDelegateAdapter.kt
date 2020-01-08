package com.phooper.goodlooker.ui.widgets.recyclerview.adapter

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.ui.widgets.recyclerview.model.LoadingItemViewModel
import com.phooper.goodlooker.ui.widgets.recyclerview.model.SearchFailedItemViewModel

class SearchFailedItemDelegateAdapter : KDelegateAdapter<SearchFailedItemViewModel>() {

    override fun getLayoutId() = R.layout.item_search_failed

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is SearchFailedItemViewModel

    override fun onBind(item: SearchFailedItemViewModel, viewHolder: KViewHolder) { }
}