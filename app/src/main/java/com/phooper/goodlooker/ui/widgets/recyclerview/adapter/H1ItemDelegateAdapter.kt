package com.phooper.goodlooker.ui.widgets.recyclerview.adapter

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.ui.widgets.recyclerview.model.H1ItemViewModel
import kotlinx.android.synthetic.main.item_h1.*

class H1ItemDelegateAdapter : KDelegateAdapter<H1ItemViewModel>() {

    override fun getLayoutId() = R.layout.item_h1

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is H1ItemViewModel

    override fun onBind(item: H1ItemViewModel, viewHolder: KViewHolder) {
        with(viewHolder) {
            text.text = item.headerText
        }
    }
}