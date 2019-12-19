package com.phooper.goodlooker.ui.widgets.recyclerview.adapter

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.ui.widgets.recyclerview.model.H3ItemViewModel
import com.phooper.goodlooker.ui.widgets.recyclerview.model.PItemViewModel
import kotlinx.android.synthetic.main.item_h1.*

class PItemDelegateAdapter : KDelegateAdapter<PItemViewModel>() {

    override fun getLayoutId() = R.layout.item_p

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is PItemViewModel

    override fun onBind(item: PItemViewModel, viewHolder: KViewHolder) {
        with(viewHolder) {
            text.text = item.text
        }
    }
}