package com.phooper.goodlooker.ui.global.adapters

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.entity.SearchFailedItemViewModel

class SearchFailedItemDelegateAdapter : KDelegateAdapter<SearchFailedItemViewModel>() {

    override fun getLayoutId() = R.layout.item_search_failed

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is SearchFailedItemViewModel

    override fun onBind(item: SearchFailedItemViewModel, viewHolder: KViewHolder) { }
}