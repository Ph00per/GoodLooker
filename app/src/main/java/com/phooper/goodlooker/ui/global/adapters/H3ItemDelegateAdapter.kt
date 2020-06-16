package com.phooper.goodlooker.ui.global.adapters.adapter

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.entity.H3ItemViewModel
import kotlinx.android.synthetic.main.item_h1.*

class H3ItemDelegateAdapter : KDelegateAdapter<H3ItemViewModel>() {

    override fun getLayoutId() = R.layout.item_h3

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is H3ItemViewModel

    override fun onBind(item: H3ItemViewModel, viewHolder: KViewHolder) {
        with(viewHolder) {
            text.text = item.headerText
        }
    }
}