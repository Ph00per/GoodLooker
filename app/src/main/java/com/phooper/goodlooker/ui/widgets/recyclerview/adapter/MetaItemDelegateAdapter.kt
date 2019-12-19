package com.phooper.goodlooker.ui.widgets.recyclerview.adapter

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.ui.widgets.recyclerview.model.H1ItemViewModel
import com.phooper.goodlooker.ui.widgets.recyclerview.model.MetaItemViewModel
import kotlinx.android.synthetic.main.item_h1.*
import kotlinx.android.synthetic.main.item_meta.*

class MetaItemDelegateAdapter : KDelegateAdapter<MetaItemViewModel>() {

    override fun getLayoutId() = R.layout.item_meta

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is MetaItemViewModel

    override fun onBind(item: MetaItemViewModel, viewHolder: KViewHolder) {
        with(viewHolder) {
            date_text.text = item.date
            time_text.text = item.timeToRead
        }
    }
}