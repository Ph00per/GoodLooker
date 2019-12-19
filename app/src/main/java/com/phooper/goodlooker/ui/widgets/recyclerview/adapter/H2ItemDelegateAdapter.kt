package com.phooper.goodlooker.ui.widgets.recyclerview.adapter

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.ui.widgets.recyclerview.model.H2ItemViewModel
import kotlinx.android.synthetic.main.item_h1.*

class H2ItemDelegateAdapter : KDelegateAdapter<H2ItemViewModel>() {

    override fun getLayoutId() = R.layout.item_h2

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is H2ItemViewModel

    override fun onBind(item: H2ItemViewModel, viewHolder: KViewHolder) {
        with(viewHolder) {
            text.text = item.headerText
        }
    }
}