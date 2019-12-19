package com.phooper.goodlooker.ui.widgets.recyclerview.adapter

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.ui.widgets.recyclerview.model.OLItemViewModel
import kotlinx.android.synthetic.main.item_h1.*
import kotlinx.android.synthetic.main.item_h1.text
import kotlinx.android.synthetic.main.item_ol_li.*

class OLItemDelegateAdapter : KDelegateAdapter<OLItemViewModel>() {

    override fun getLayoutId() = R.layout.item_ol_li

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is OLItemViewModel

    override fun onBind(item: OLItemViewModel, viewHolder: KViewHolder) {
        with(viewHolder) {
            number.text = item.num
            text.text = item.text
        }
    }
}