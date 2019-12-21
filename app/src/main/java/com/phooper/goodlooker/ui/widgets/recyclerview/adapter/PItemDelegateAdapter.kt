package com.phooper.goodlooker.ui.widgets.recyclerview.adapter

import android.util.Log
import android.view.View
import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.ui.widgets.recyclerview.model.PItemViewModel
import com.phooper.goodlooker.util.makeLinks
import kotlinx.android.synthetic.main.item_h1.*

class PItemDelegateAdapter(private val onLinkClick: ((String) -> Unit)) : KDelegateAdapter<PItemViewModel>() {

    override fun getLayoutId() = R.layout.item_p

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is PItemViewModel

    override fun onBind(item: PItemViewModel, viewHolder: KViewHolder) {
        with(viewHolder) {
            text.text = item.text
            item.hyperlinks?.forEach { hyperlink ->
                text.makeLinks(Pair(hyperlink.text, View.OnClickListener {
                    onLinkClick(hyperlink.link)
                }))
            }
        }
    }
}