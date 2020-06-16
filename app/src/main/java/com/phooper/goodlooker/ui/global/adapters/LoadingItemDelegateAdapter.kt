package com.phooper.goodlooker.ui.global.adapters

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.entity.LoadingItemViewModel

class LoadingItemDelegateAdapter : KDelegateAdapter<LoadingItemViewModel>() {

    override fun getLayoutId() = R.layout.item_loading

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is LoadingItemViewModel

    override fun onBind(item: LoadingItemViewModel, viewHolder: KViewHolder) { }
}