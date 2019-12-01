package com.phooper.goodlooker.ui.widgets.recyclerview.adapter

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.ui.widgets.recyclerview.model.LoadingItemViewModel

class LoadingItemDelegateAdapter : KDelegateAdapter<LoadingItemViewModel>() {

    override fun getLayoutId() = R.layout.item_loading

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is LoadingItemViewModel

    override fun onBind(item: LoadingItemViewModel, viewHolder: KViewHolder) { }
}