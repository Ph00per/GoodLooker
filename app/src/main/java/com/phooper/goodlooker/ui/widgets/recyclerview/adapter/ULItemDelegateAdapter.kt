package com.phooper.goodlooker.ui.widgets.recyclerview.adapter

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.ui.widgets.recyclerview.model.H3ItemViewModel
import com.phooper.goodlooker.ui.widgets.recyclerview.model.ULItemViewModel
import kotlinx.android.synthetic.main.item_h1.*

class ULItemDelegateAdapter : KDelegateAdapter<ULItemViewModel>() {

    override fun getLayoutId() = R.layout.item_ul_li

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is ULItemViewModel

    override fun onBind(item: ULItemViewModel, viewHolder: KViewHolder) {
        with(viewHolder) {
            text.text = item.text
        }
    }
}