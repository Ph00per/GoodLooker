package com.phooper.goodlooker.ui.widgets.recyclerview.adapter

import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.ui.widgets.recyclerview.model.ConnectionRetryItemViewModel
import kotlinx.android.synthetic.main.item_connection_fail.*

class ConnectionRetryItemDelegateAdapter(private val someFun: () -> Unit) :
    KDelegateAdapter<ConnectionRetryItemViewModel>() {

    override fun getLayoutId() = R.layout.item_connection_fail

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is ConnectionRetryItemViewModel

    override fun onBind(item: ConnectionRetryItemViewModel, viewHolder: KViewHolder) {
        viewHolder.reconnect_layout.setOnClickListener { someFun() }
    }
}